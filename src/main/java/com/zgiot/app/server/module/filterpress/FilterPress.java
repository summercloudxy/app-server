package com.zgiot.app.server.module.filterpress;


import com.zgiot.common.constants.FilterPressLogConstants;
import com.zgiot.common.constants.FilterPressMetricConstants;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FilterPress {
    public static final String PARAM_NAME_FEEDINTELLIGENT = "feedIntelligent";
    public static final String PARAM_NAME_UNLOADINTELLIGENT = "unloadIntelligent";
    public static final String PARAM_NAME_FEEDCONFIRMNEED = "feedConfirmNeed";
    public static final String PARAM_NAME_UNLOADCONFIRMNEED = "unloadConfirmNeed";
    public static final String PARAM_NAME_MAXUNLOADPARALLEL = "maxUnloadParallel";

    private static final Logger logger = LoggerFactory.getLogger(FilterPress.class);
    private static final long UNLOAD_WAIT_DURATION = Duration.ofMinutes(30).plusSeconds(45).toMillis();

    private static final int UNLOAD_EXCHANGE_COUNT = 16;


    private final String code;
    private FilterPressManager manager;
    /**
     * 板数计数器
     */
    private Map<Integer, Integer> plateCount = new ConcurrentHashMap<>();
    /**
     * 正在生产的队伍
     */
    private volatile Integer producingTeam;
    /**
     * 入料开始时间，若不处于入料状态，值为空
     */
    private volatile long feedStartTime;
    /**
     * 入料结束时间
     */
    private volatile long feedOverTime;
    /**
     * 进料时长
     */
    private volatile long feedDuration;
    /**
     * 入料泵电流
     */
    private volatile float feedPumpCurrent;
    /**
     * 进入循环等待的时间
     */
    private volatile long onCycleTime;
    /**
     * 卸料条件判断管理器
     */
    private UnloadManager unloadManager = new UnloadManager();
    /**
     * 进料智能
     */
    private volatile boolean feedIntelligent;
    /**
     * 是否需要人工确认进料
     */
    private volatile boolean feedConfirmNeed;
    /**
     * 卸料智能
     */
    private volatile boolean unloadIntelligent;
    /**
     * 是否需要人工确认卸料
     */
    private volatile boolean unloadConfirmNeed;

    /**
     * T_COUNT变化时间，用于说明压板数变化，如本台压滤机本队压板数增加1
     */
    private volatile long plateCountChangeTime = 0;
    /**
     * 卸料时长
     */
    private volatile long unloadDuration;
    /**
     * 卸料等待时长
     */
    private volatile long waitDuration;
    /**
     * 过程计时
     */
    private volatile long proceedingDuration;
    /**
     * 松开时长
     */
    private volatile long looseDuration;
    /**
     * 松开开始时间
     */
    private volatile long looseStartTime;
    /**
     * 松开结束时间
     */
    private volatile long looseEndTime;
    /**
     * 取板时长
     */
    private volatile long takenDuration;
    /**
     * 取板开始时间
     */
    private volatile long takenStartTime;
    /**
     * 取板结束时间
     */
    private volatile long takenEndTime;
    /**
     * 拉板时长
     */
    private volatile long pullDuration;
    /**
     * 拉板开始时间
     */
    private volatile long pullStartTime;
    /**
     * 拉板结束时间
     */
    private volatile long pullEndTime;
    /**
     * 统计每台压滤机第几板，用于日志保存和查询
     * 每天0点置0
     */
    private volatile int statisticLogplateCount;

    private volatile  boolean isFilterPressUnloading = false;

    private volatile  AtomicInteger filterPressTakeAndPullCount = new AtomicInteger(0);

    public boolean isFilterPressUnloading() {
        return isFilterPressUnloading;
    }

    public void setFilterPressUnloading(boolean filterPressUnloading) {
        isFilterPressUnloading = filterPressUnloading;
    }

    public FilterPress(String code, FilterPressManager manager) {
        this.code = code;
        this.manager = manager;
    }

    void onDataSourceChange(String metricCode, String value) {
        if (FilterPressMetricConstants.PAUSE.equals(metricCode) && unloadManager.isUnloading) {
            if (Boolean.TRUE.toString().equals(value)) {
                unloadManager.pauseTimer();
            }
//            else {
//                unloadManager.scheduleTimer();
//            }
        }
    }

    public void onRun() {
        logger.trace("{} starts running", code);
    }

    public void onStop() {
        logger.trace("{} stopped", code);
    }

    public void onFault() {
        logger.trace("{} got faults", code);
    }

    public void teamCount(String metricCode, String metricCodeValue) {
        if(Integer.valueOf(metricCodeValue) == 0){
            savePlateInfo(metricCode,metricCodeValue);
            return;
        }
        savePlateInfo(metricCode,metricCodeValue);
        /**
         * 日志操作
         * 此时认为是松开时刻
         */

        if (manager.getStatisticLogs().containsKey(this.code)) {
            FilterPressLogBean filterPressLogBean = manager.getStatisticLogs().get(this.code);
            filterPressLogBean.clear();
            filterPressLogBean.setThingCode(this.code);
            if (looseDuration > 0 && takenDuration > 0 && pullDuration > 0) {
                unloadDuration = looseDuration + takenDuration + pullDuration;
                filterPressLogBean.setUnloadDuration(unloadDuration);
                logger.info("unloadDuration:" + unloadDuration);
                looseDuration = 0;
                takenDuration = 0;
                pullDuration = 0;
                unloadDuration = 0;
            }

            filterPressLogBean.setFeedStartTime(parseDate(feedStartTime));
            filterPressLogBean.setFeedDuration(feedDuration);
            logger.info("feedStartTime:" + feedStartTime);
            logger.info("feedDuration:" + feedDuration);

            if (feedPumpCurrent > 0) {
                filterPressLogBean.setFeedCurrent(feedPumpCurrent);
                logger.info("feedPumpCurrent:" + feedPumpCurrent);
            }

            //保存上一次压板计数变化开始时间
            if(plateCountChangeTime == 0){
                plateCountChangeTime = System.currentTimeMillis();
            }
            long proceedingTime = System.currentTimeMillis() - plateCountChangeTime;
            long waitingTime = 0;
            if(looseStartTime > 0){
                filterPressLogBean.setUnloadTime(parseDate(looseStartTime));
                waitingTime = looseStartTime - waitDuration;
            }else{
                filterPressLogBean.setUnloadTime(parseDate(System.currentTimeMillis()));
                waitingTime = System.currentTimeMillis() - waitDuration;
            }
            logger.info("unloadTime:" + looseStartTime);

            if (producingTeam != null && producingTeam > 0) {
                filterPressLogBean.setTeam(producingTeam);
                int plateCount = this.plateCount.get(producingTeam);
                if (plateCount > 0) {
                    filterPressLogBean.setPlateCount(plateCount);
                }
                int totalPlateCount = getAllFilterPressTotalPlateCount(this.getCode());
                filterPressLogBean.setTotalPlateCount(totalPlateCount);
                logger.info("plateCount:" + plateCount);
                logger.info("totalPlateCount:" + totalPlateCount);
            }
            filterPressLogBean.setSaveTime(parseDate(System.currentTimeMillis()));

            if(proceedingTime > 0){
                filterPressLogBean.setProceedingDuration(proceedingTime);
                logger.info("proceedingDuration:" + proceedingTime);
            }
            if (waitingTime > 0) {
                filterPressLogBean.setWaitDuration(waitingTime);
                logger.info("waitingTime:" + waitingTime);
                waitDuration = 0;
            }

            filterPressLogBean.setFeedState(feedIntelligent ? FilterPressLogConstants.FEED_INTELLIGENT : FilterPressLogConstants.FEED_AUTO);
            filterPressLogBean.setUnloadState(unloadIntelligent ? FilterPressLogConstants.UNLOAD_INTELLIGENT : FilterPressLogConstants.UNLOAD_AUTO);

            filterPressLogBean.setPlateStartTime(parseDate(plateCountChangeTime));
            plateCountChangeTime = System.currentTimeMillis();

            if (FilterPressLogUtil.isDayShift(FilterPressLogConstants.DAY_SHIFT_START_TIME_SCOPE, FilterPressLogConstants.DAY_SHIFT_END_TIME_SCOPE)) {
                filterPressLogBean.setDayShift(FilterPressLogConstants.IS_DAY_SHIFT_OK);
            } else {
                filterPressLogBean.setDayShift(FilterPressLogConstants.IS_DAY_SHIFT_NO);
            }

            filterPressLogBean.setPeriod(FilterPressLogConstants.PERIOD_TWO);
            if((feedDuration == 0) && (unloadDuration == 0) && (proceedingTime == 0)){
                return;
            }
            manager.filterPressLogService.saveFilterPressLog(filterPressLogBean);
        }
    }

    public void onLocal() {
        logger.trace("{} on local", code);
        int position = -1;
        if (manager != null && (!manager.getUnloadSequence().isEmpty()) && (!StringUtils.isBlank(this.getCode()))) {
            position = manager.getUnloadSequence().get(this.getCode());
        }
        //manager.getUnloadManager().getQueue().remove(this);
        removeFilterPress(manager.getUnloadManager().getQueue(),this);
        manager.getUnloadSequence().remove(this.getCode());
        logger.debug("local remove unloadSequence,filterpress:" + this.getCode());
        try {
            manager.getUnConfirmedUnload().remove(this.getCode());
        } catch (NullPointerException e) {
            throw new SysException("未确定卸料set中不存在这台压滤机thingCode", SysException.EC_UNKNOWN);
        }
        if (position > 0) {
            manager.getUnloadManager().reSort(position);
            logger.debug("local resort");
        }
    }

    public void onLoosen() {
        logger.debug("{} on loosen", code);
        isFilterPressUnloading = true;
        looseStartTime = System.currentTimeMillis();
        this.startUnload();
        int position = -1;
        if (manager != null && (!manager.getUnloadSequence().isEmpty())
                && (!StringUtils.isBlank(this.getCode()))
                && manager.getUnloadSequence().containsKey(this.getCode())) {
            position = manager.getUnloadSequence().get(this.getCode());
        }
        //manager.getUnloadManager().getQueue().remove(this);
        removeFilterPress(manager.getUnloadManager().getQueue(),this);
        manager.printQueueData(manager.getUnloadManager().getQueue());
        manager.getUnloadSequence().remove(this.getCode());
        logger.debug("loose remove unloadSequence,filterpress:" + this.getCode());
        try {
            HashSet unConfirm = new HashSet(manager.getUnConfirmedUnload());
            manager.getUnConfirmedUnload().clear();
            manager.getUnConfirmedUnload().addAll(unConfirm);
            manager.getUnConfirmedUnload().remove(this.getCode());
        } catch (NullPointerException e) {
            throw new SysException("未确定卸料set中不存在这台压滤机thingCode", SysException.EC_UNKNOWN);
        }
        if (position > 0) {
            manager.getUnloadManager().reSort(position);
            logger.debug("loose resort");
        }
    }

    private void deleteFilterPressInQueue(){
        int position = -1;
        if (manager != null && (!manager.getUnloadSequence().isEmpty())
                && (!StringUtils.isBlank(this.getCode()))
                && manager.getUnloadSequence().containsKey(this.getCode())) {
            position = manager.getUnloadSequence().get(this.getCode());
        }

        if (position > 0) {
            manager.getUnloadManager().reSort(position);
            logger.debug("resort");
        }
    }

    public void offLoosen() {
        logger.trace("{} off loosen", code);
        looseEndTime = System.currentTimeMillis();
        logger.info("looseStartTime", looseStartTime);
        if(looseStartTime == 0){
            looseStartTime = System.currentTimeMillis();
        }
        looseDuration = 0;
        looseDuration = looseEndTime - looseStartTime;
    }

    public void offTaken() {
        takenEndTime = System.currentTimeMillis();
        logger.info("takenEndTime", takenEndTime);
        if(takenStartTime == 0){
            takenStartTime = System.currentTimeMillis();
        }
        takenDuration += takenEndTime - takenStartTime;
    }

    public void offPull() {
        pullEndTime = System.currentTimeMillis();
        logger.info("pullStartTime", pullStartTime);
        if(pullStartTime == 0){
            pullStartTime = System.currentTimeMillis();
        }
        pullDuration += pullEndTime - pullStartTime;
    }

    public void offFeed(){
        this.feedOverTime = System.currentTimeMillis();
        if(feedStartTime == 0){
            this.feedStartTime = System.currentTimeMillis();
        }
        feedDuration = feedOverTime - feedStartTime;
        List<String> feedPumpCodes = manager.getKeyByValueFromMap(manager.getFilterPressPumpMapping(),this.getCode());
        String feedPumpCode = feedPumpCodes.get(0);
        if(feedPumpCodes.size() == 0){
            throw new SysException("feedPump thingCode is null",SysException.EC_UNKNOWN);
        }
        Optional<DataModelWrapper> currentWrapper = manager.dataService.getData(feedPumpCode,FilterPressMetricConstants.FEED_PUMP_CURRENT);
        Float current = new Float(0);
        if(currentWrapper.isPresent()){
            current = Float.parseFloat(currentWrapper.get().getValue());
        }
        this.setFeedPumpCurrent(current);
    }

    public void onTaken() {
        takenStartTime = System.currentTimeMillis();
        logger.trace("{} on taken", code);
        unloadManager.countTaken();
    }

    public void onPull() {
        logger.trace("{} on pull", code);
        pullStartTime = System.currentTimeMillis();
        unloadManager.countPulled();
    }

    public void onPress() {
        logger.debug("{} on press", code);
        logger.debug("take and pull count:", filterPressTakeAndPullCount.get());
        isFilterPressUnloading = false;
        //压紧后通知下一台
        if((manager.getUnloadManager().getUnloadingCount(code) < manager.getMaxUnloadParallel()) && (filterPressTakeAndPullCount.get() < 16)){
            unloadManager.notifyNext();
            if(logger.isDebugEnabled()){
                logger.debug("notify next filterpress unload cause by press!");
                logger.debug("press state unloading filterpress count:" + manager.getUnloadManager().getUnloadingCount(code));
            }
        }
        filterPressTakeAndPullCount.set(0);
        unloadManager.stopUnload();
        this.deleteFilterPressInQueue();
    }

    public void onFeed() {
        logger.trace("{} on feed", code);
        feedStartTime = System.currentTimeMillis();
        this.deleteFilterPressInQueue();
    }

    public void onFeedOver() {
        logger.trace("{} on feed over", code);
    }

    public void onBlow() {
        logger.trace("{} on blow", code);
        waitDuration = System.currentTimeMillis();
        this.deleteFilterPressInQueue();
    }

    public void onCycle() {
        logger.trace("{} on cycle", code);
        unloadManager.takeAndPullCount.set(0);
        this.onCycleTime = System.currentTimeMillis();
        this.unloadManager.notifiedNext = false;
        if (unloadIntelligent) {
            manager.enqueueUnload(this);
            logger.debug("{} enqueue unload, intelligent:{}", code, unloadIntelligent);
        }
    }

    public void onAssumeFeedOver() {
        logger.trace("{} assume to be feed over", code);
        if (feedIntelligent) {
            manager.execFeedOver(this);
            logger.debug("{} executed feed over, intelligent:{}", code, feedIntelligent);
        }
    }

    /**
     * 开始卸料
     */
    void startUnload() {
        logger.debug("{} starts unload counting", code);
        unloadManager.startUnload();
    }

    void updatePlateCount(int team, int count) {
        plateCount.put(team, count);
    }

    void setProducingTeam(int team) {
        this.producingTeam = team;
    }

    int getProducingTeam() {
        return producingTeam;
    }


    public String getCode() {
        return code;
    }

    public Long getFeedDuration() {
        return feedDuration;
    }

    public void setFeedDuration(Long feedDuration) {
        this.feedDuration = feedDuration;
    }

    public Long getFeedStartTime() {
        return feedStartTime;
    }

    public void setFeedStartTime(Long feedStartTime) {
        this.feedStartTime = feedStartTime;
    }

    public Long getFeedOverTime() {
        return feedOverTime;
    }

    public double getFeedPumpCurrent() {
        return feedPumpCurrent;
    }

    public void setFeedPumpCurrent(float feedPumpCurrent) {
        this.feedPumpCurrent = feedPumpCurrent;
    }

    public Long getOnCycleTime() {
        return onCycleTime;
    }

    public boolean isFeedIntelligent() {
        return feedIntelligent;
    }

    public void setFeedIntelligent(boolean feedIntelligent) {
        this.feedIntelligent = feedIntelligent;
    }

    public boolean isFeedConfirmNeed() {
        return feedConfirmNeed;
    }

    public void setFeedConfirmNeed(boolean feedConfirmNeed) {
        this.feedConfirmNeed = feedConfirmNeed;
    }

    public boolean isUnloadIntelligent() {
        return unloadIntelligent;
    }

    public void setUnloadIntelligent(boolean unloadIntelligent) {
        this.unloadIntelligent = unloadIntelligent;
    }

    public boolean isUnloadConfirmNeed() {
        return unloadConfirmNeed;
    }


    public long getUnloadDuration() {
        return unloadDuration;
    }

    public long getWaitDuration() {
        return waitDuration;
    }

    public long getProceedingDuration() {
        return proceedingDuration;
    }

    public long getLooseDuration() {
        return looseDuration;
    }

    public long getTakenDuration() {
        return takenDuration;
    }

    public long getPullDuration() {
        return pullDuration;
    }

    public long getPlateCountChangeTime() {
        return plateCountChangeTime;
    }

    public void setPlateCountChangeTime(long plateCountChangeTime) {
        this.plateCountChangeTime = plateCountChangeTime;
    }

    public void setUnloadDuration(long unloadDuration) {
        this.unloadDuration = unloadDuration;
    }

    public void setWaitDuration(long waitDuration) {
        this.waitDuration = waitDuration;
    }

    public void setProceedingDuration(long proceedingDuration) {
        this.proceedingDuration = proceedingDuration;
    }

    public void setLooseDuration(long looseDuration) {
        this.looseDuration = looseDuration;
    }

    public void setTakenDuration(long takenDuration) {
        this.takenDuration = takenDuration;
    }

    public void setPullDuration(long pullDuration) {
        this.pullDuration = pullDuration;
    }

    public long getLooseStartTime() {
        return looseStartTime;
    }

    public long getLooseEndTime() {
        return looseEndTime;
    }

    public long getTakenStartTime() {
        return takenStartTime;
    }

    public long getTakenEndTime() {
        return takenEndTime;
    }

    public long getPullStartTime() {
        return pullStartTime;
    }

    public long getPullEndTime() {
        return pullEndTime;
    }

    public int getStatisticLogplateCount() {
        return statisticLogplateCount;
    }

    public void setFeedOverTime(long feedOverTime) {
        this.feedOverTime = feedOverTime;
    }

    public void setOnCycleTime(long onCycleTime) {
        this.onCycleTime = onCycleTime;
    }

    public void setLooseStartTime(long looseStartTime) {
        this.looseStartTime = looseStartTime;
    }

    public void setLooseEndTime(long looseEndTime) {
        this.looseEndTime = looseEndTime;
    }

    public void setTakenStartTime(long takenStartTime) {
        this.takenStartTime = takenStartTime;
    }

    public void setTakenEndTime(long takenEndTime) {
        this.takenEndTime = takenEndTime;
    }

    public void setPullStartTime(long pullStartTime) {
        this.pullStartTime = pullStartTime;
    }

    public void setPullEndTime(long pullEndTime) {
        this.pullEndTime = pullEndTime;
    }

    public void setStatisticLogplateCount(int statisticLogplateCount) {
        this.statisticLogplateCount = statisticLogplateCount;
    }

    public void setUnloadConfirmNeed(boolean unloadConfirmNeed) {
        this.unloadConfirmNeed = unloadConfirmNeed;
    }

    public int getPlateCount() {
        logger.info("producingTeam:" + producingTeam);
        if (producingTeam == null) {
            return 0;
        }
        logger.info("plateCount:" + plateCount);
        return plateCount.get(producingTeam);
    }

    public Date parseDate(long timeStamp) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateformat.format(timeStamp);
        Date date = null;
        try {
            date = dateformat.parse(dateStr);
        } catch (ParseException e) {
            logger.debug("date parse exception in filterPress.parseDate()");
        }
        return date;
    }

    public String getPRCTimeFromCacheByMetric(String metricCode) {
        Optional<DataModelWrapper> wrapper = manager.dataService.getData(this.getCode(), metricCode);
        if (wrapper.isPresent()) {
            String preValue = wrapper.get().getPreValue();
            String value = wrapper.get().getValue();
            if(Long.parseLong(value) < Long.parseLong(preValue)){
                return preValue;
            }else{
                return value;
            }
        }
        return null;
    }

    private int getAllFilterPressTotalPlateCount(String thingCode) {
        String totalPlateCount = null;
        Optional<DataModelWrapper> wrapper = manager.dataService.getData(thingCode, FilterPressMetricConstants.PLATE_TTL);
        if (wrapper.isPresent()) {
            totalPlateCount = wrapper.get().getValue();
        }
        if(!StringUtils.isBlank(totalPlateCount)){
            return Integer.valueOf(totalPlateCount);
        }else{
            return 0;
        }
    }

    private void savePlateInfo(String metricCode, String metricCodeValue){
        if (FilterPressMetricConstants.T1_COUNT.equals(metricCode)
                || FilterPressMetricConstants.T2_COUNT.equals(metricCode)
                || FilterPressMetricConstants.T3_COUNT.equals(metricCode)) {
            switch (metricCode) {
                case FilterPressMetricConstants.T1_COUNT:
                    this.updatePlateCount(FilterPressLogConstants.TEAM1, Integer.valueOf(metricCodeValue));
                    break;
                case FilterPressMetricConstants.T2_COUNT:
                    this.updatePlateCount(FilterPressLogConstants.TEAM2, Integer.valueOf(metricCodeValue));
                    break;
                case FilterPressMetricConstants.T3_COUNT:
                    this.updatePlateCount(FilterPressLogConstants.TEAM3, Integer.valueOf(metricCodeValue));
                    break;
                default:
            }
            manager.calculatePlateAndSave();
        }
    }

    private void removeFilterPress(BlockingQueue<FilterPress> queue,FilterPress filterPress){
        Iterator<FilterPress> iterator = queue.iterator();
        while(iterator.hasNext()){
            if(filterPress.getCode().equals(iterator.next().code)){
                queue.remove(filterPress);
                if(logger.isDebugEnabled()){
                    logger.debug("delete filterpress {}",filterPress.getCode());
                }
            }
        }
    }

    class UnloadManager {
        /**
         * 卸料计时
         */
        private Timer unloadTimer;
        /**
         * 卸料暂停时已过计时
         */
        private volatile long unloadTimedDuration = 0;
        /**
         * 是否正在卸料
         */
        private volatile boolean isUnloading = false;
        /**
         * 上一次暂停的时间
         */
        private Long latestStartTime;
        /**
         * 拉板和取板的交替次数
         */
        AtomicInteger takeAndPullCount = new AtomicInteger(0);
        /**
         * 已通知下一台
         */
        volatile boolean notifiedNext = false;

        /**
         * 开始卸料
         */
        private void startUnload() {
            isUnloading = true;
            //scheduleTimer();
        }

        /**
         * 卸料过程计时
         */
        private void scheduleTimer() {
            unloadTimer = new Timer("unload timer");
            latestStartTime = System.currentTimeMillis();
            unloadTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    logger.debug("{} unload time up", code);
                    notifyNext();
                    unloadTimedDuration = 0;
                    latestStartTime = null;
                }
            }, UNLOAD_WAIT_DURATION - unloadTimedDuration);
        }

        /**
         * 停止卸料判断
         */
        private void stopUnload() {
            isUnloading = false;
            cancelTimer();
        }

        /**
         * 暂停计时器
         */
        private void pauseTimer() {
            cancelTimer();
            if (latestStartTime != null) {
                unloadTimedDuration += System.currentTimeMillis() - latestStartTime;
            }
        }

        /**
         * 取消计时器
         */
        private void cancelTimer() {
            if (unloadTimer != null) {
                unloadTimer.cancel();
                unloadTimer = null;
            }
        }

        /**
         * 通知下一台卸料
         */
        private void notifyNext() {
            if (!notifiedNext) {
                notifiedNext = true;
                manager.unloadNext();
                logger.debug("{} unload enough to notify next", code);
            }
        }

        /**
         * 记录取板
         */
        private void countTaken() {
            checkUnloadExchange();
        }

        /**
         * 记录拉板
         */
        private void countPulled() {
            checkUnloadExchange();
        }

        private void checkUnloadExchange() {
            long startGetValueTime = System.currentTimeMillis();
            DataModel dataModel = new DataModel();
            dataModel.setThingCode(code);
            dataModel.setMetricCode(FilterPressMetricConstants.RO_PRESS);
            String readValue = manager.cmdControlService.getDataSync(dataModel);
            if(logger.isTraceEnabled()){
                logger.trace("read press state  durationTime:{} ms", (System.currentTimeMillis() - startGetValueTime));
            }
            int pullAndTakeCount = filterPressTakeAndPullCount.incrementAndGet();
            if ((!Boolean.valueOf(readValue)) && (pullAndTakeCount < UNLOAD_EXCHANGE_COUNT)){
                isFilterPressUnloading = true;
            }

            if (pullAndTakeCount == UNLOAD_EXCHANGE_COUNT ) {
                    cancelTimer();
                    if(logger.isDebugEnabled()){
                        logger.debug("{} take and pull enough", code);
                        logger.debug("take and pull state unloading filterpress count:" + manager.getUnloadManager().getUnloadingCount(code));
                    }
                    isFilterPressUnloading = false;
                    if(manager.getUnloadManager().getUnloadingCount(code) < manager.getMaxUnloadParallel()){
                        notifyNext();
                    }
                    takeAndPullCount.set(0);
                    //filterPressTakeAndPullCount.set(0);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilterPress that = (FilterPress) o;

        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "FilterPress{" +
                "code='" + code + '\'' +
                '}';
    }
}
