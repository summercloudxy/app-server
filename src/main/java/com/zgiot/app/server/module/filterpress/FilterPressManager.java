package com.zgiot.app.server.module.filterpress;

import com.zgiot.app.server.module.filterpress.dao.FilterPressMapper;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressElectricity;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.util.RequestIdUtil;
import com.zgiot.common.constants.FilterPressConstants;
import com.zgiot.common.constants.FilterPressMetricConstants;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class FilterPressManager {
    private static final String FEED_OVER_NOTICE_URI = "/topic/filterPress/feedOver";
    private static final String FEED_OVER_CONFIRMED_NOTICE_URI = "/topic/filterPress/feedOver/confirm";
    private static final String UNLOAD_NOTICE_URI = "/topic/filterPress/unload";
    private static final String UNLOAD_CONFIRMED_NOTICE_URI = "/topic/filterPress/unload/confirm";
    private static final String PARAM_NAME_SYS = "sys";
    @Autowired
    private DataService dataService;
    @Autowired
    private CmdControlService cmdControlService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private FilterPressMapper filterPressMapper;

    private static final int INIT_CAPACITY = 6;

    private static final Integer CURRENT_COUNT_DURATION = -3;

    private Map<String, FilterPress> deviceHolder = new ConcurrentHashMap<>();

    private Set<String> unconfirmedFeed = new ConcurrentSkipListSet<>();

    private Set<String> unConfirmedUnload = new ConcurrentSkipListSet<>();

    private UnloadManager unloadManager = new UnloadManager();

    @PostConstruct
    void initFilterPress() {
        deviceHolder.put("2492", new FilterPress("2492", this));
        deviceHolder.put("2493", new FilterPress("2493", this));
        deviceHolder.put("2494", new FilterPress("2494", this));
        deviceHolder.put("2495", new FilterPress("2495", this));
        deviceHolder.put("2496", new FilterPress("2496", this));
        deviceHolder.put("2496A", new FilterPress("2496A", this));
        setMaxUnloadParallel(filterPressMapper.selectParamValue(PARAM_NAME_SYS, FilterPress.PARAM_NAME_MAXUNLOADPARALLEL).intValue());
        deviceHolder.forEach((code, filterPress) -> {
            boolean feedIntelligent = filterPressMapper.selectParamValue(code, FilterPress.PARAM_NAME_FEEDINTELLIGENT).intValue()==1;
            filterPress.setFeedIntelligent(feedIntelligent);
            boolean feedConfirmNeed = filterPressMapper.selectParamValue(code, FilterPress.PARAM_NAME_FEEDCONFIRMNEED).intValue()==1;
            filterPress.setFeedConfirmNeed(feedConfirmNeed);
            boolean unloadIntelligent = filterPressMapper.selectParamValue(code, FilterPress.PARAM_NAME_UNLOADINTELLIGENT).intValue() == 1;
            filterPress.setUnloadIntelligent(unloadIntelligent);
            boolean unloadConfirmNeed = filterPressMapper.selectParamValue(code, FilterPress.PARAM_NAME_UNLOADCONFIRMNEED).intValue() == 1;
            filterPress.setUnloadConfirmNeed(unloadConfirmNeed);
        });
    }

    /**
     * call back when data changed
     *
     * @param data
     */
    public void onDataSourceChange(DataModel data) {
        FilterPress filterPress = deviceHolder.get(data.getThingCode());
        String metricCode = data.getMetricCode();
        filterPress.onDataSourceChange(metricCode, data.getValue());
        if (FilterPressMetricConstants.STAGE.equals(metricCode)) {
            processStage(data);
        }
        if (FilterPressMetricConstants.FEED_ASUM.equals(metricCode)) {
            processFeedAssumption(data);
        }
    }

    public void confirmFeedOver(String code) {
        if (unconfirmedFeed.remove(code)) {
            doFeedOver(getFilterPress(code));
        }
    }

    public void confirmUnload(String code) {
        if (unConfirmedUnload.remove(code)) {
            unloadManager.execUnload(getFilterPress(code));
        }
    }

    private void processFeedAssumption(DataModel data) {
        if (String.valueOf(FilterPressConstants.FEED_OVER_CURRENT).equals(data.getValue())
                || String.valueOf(FilterPressConstants.FEED_OVER_TIME).equals(data.getValue())) {
            getFilterPress(data.getThingCode()).onAssumeFeedOver();
        }
    }

    /**
     * process the stage data and call the specific method of filter press
     *
     * @param data
     */
    private void processStage(DataModel data) {
        String thingCode = data.getThingCode();
        short stageValue = Short.valueOf(data.getValue());
        FilterPress filterPress = getFilterPress(thingCode);
        switch (stageValue) { // 回调各阶段
            case FilterPressConstants.STAGE_LOOSEN:
                filterPress.onLoosen();
                break;
            case FilterPressConstants.STAGE_TAKEN:
                filterPress.onTaken();
                break;
            case FilterPressConstants.STAGE_PULL:
                filterPress.onPull();
                break;
            case FilterPressConstants.STAGE_PRESS:
                filterPress.onPress();
                break;
            case FilterPressConstants.STAGE_FEEDING:
                filterPress.onFeed();
                break;
            case FilterPressConstants.STAGE_FEED_OVER:
                filterPress.onFeedOver();
                break;
            case FilterPressConstants.STAGE_BLOW:
                filterPress.onBlow();
                break;
            case FilterPressConstants.STAGE_CYCLE:
                filterPress.onCycle();
                break;
            default:
        }
        // calculate the state value and call the specific method of filter press
        short stateValue = calculateState(thingCode, stageValue);
        if (!Objects.equals(dataService.getData(thingCode, FilterPressMetricConstants.STATE).getValue(),
                String.valueOf(stateValue))) {// 若值变化，保存并回调
            DataModel stateModel = new DataModel();
            stateModel.setThingCode(thingCode);
            stateModel.setThingCategoryCode(data.getThingCategoryCode());
            stateModel.setMetricCode(FilterPressMetricConstants.STATE);
            stateModel.setThingCategoryCode(data.getMetricCategoryCode());
            stateModel.setValue(String.valueOf(stateValue));
            stateModel.setDataTimeStamp(new Date());
            dataService.updateCache(stateModel);
            dataService.persist2NoSQL(stateModel);
            if (stateValue == GlobalConstants.STATE_STOPPED) {
                filterPress.onStop();
            } else if (stateValue == GlobalConstants.STATE_RUNNING) {
                filterPress.onRun();
            } else if (stateValue == GlobalConstants.STATE_FAULT) {
                filterPress.onFault();
            }
        }
    }

    /**
     * calculate the state(running/stopped/fault) of specific thing
     *
     * @param thingCode
     * @param stageValue
     * @return
     */
    private short calculateState(String thingCode, short stageValue) {
        short state;
        DataModelWrapper fault = dataService.getData(thingCode, FilterPressMetricConstants.FAULT);
        if (Boolean.valueOf(fault.getValue())) {
            state = GlobalConstants.STATE_FAULT;
        } else if (stageValue == 0) {
            state = GlobalConstants.STATE_STOPPED;
        } else {
            state = GlobalConstants.STATE_RUNNING;
        }
        return state;
    }

    /**
     * get the filter press by thing code from the deviceHolder
     *
     * @param thingCode
     * @return
     */
    public FilterPress getFilterPress(String thingCode) {
        FilterPress filterPress = deviceHolder.get(thingCode);
        if (filterPress == null) {
            throw new NoSuchElementException("no such filter press:" + thingCode);
        }
        return filterPress;
    }

    void enqueueUnload(FilterPress filterPress) {
        unloadManager.enqueue(filterPress);
    }

    void unloadNext() {
        unloadManager.unloadNextIfPossible();
    }

    public int getMaxUnloadParallel() {
        return unloadManager.maxUnloadParallel;
    }

    public void setMaxUnloadParallel(int maxUnloadParallel) {
        this.unloadManager.maxUnloadParallel = maxUnloadParallel;
    }

    void execFeedOver(FilterPress filterPress) {
        if (!filterPress.isFeedConfirmNeed()) {
            doFeedOver(filterPress);
        } else {
            messagingTemplate.convertAndSend(FEED_OVER_NOTICE_URI, filterPress.getCode());
            unconfirmedFeed.add(filterPress.getCode());
        }
    }

    private void doFeedOver(FilterPress filterPress) {
        DataModel dataModel = new DataModel();
        dataModel.setMetricCode(FilterPressMetricConstants.FEED_OVER);
        dataModel.setThingCode(filterPress.getCode());
        dataModel.setValue(Boolean.TRUE.toString());
        cmdControlService.sendPulseCmd(dataModel, null, null, RequestIdUtil.generateRequestId());
    }

    /**
     * 更新缓存及数据库中的自动手动确认状态
     *
     * @param thingCode
     * @param state
     */
    public void autoManuConfirmChange(String thingCode, boolean state) {
        boolean preState;
        if (thingCode != null) {
            FilterPress filterPress = deviceHolder.get(thingCode);
            preState = filterPress.isFeedConfirmNeed();
            if (preState != state) {
                filterPress.setFeedConfirmNeed(state);
                filterPressMapper.updateFilterParamValue(thingCode, FilterPress.PARAM_NAME_FEEDCONFIRMNEED,
                        state ? 1.0 : 0.0);
            }
        } else {
            for (Map.Entry<String, FilterPress> entry : deviceHolder.entrySet()) {
                FilterPress filterPress = entry.getValue();
                preState = filterPress.isFeedConfirmNeed();
                if (preState != state) {
                    filterPress.setFeedConfirmNeed(state);
                    filterPressMapper.updateFilterParamValue(entry.getKey(), FilterPress.PARAM_NAME_FEEDCONFIRMNEED, state ? 1.0 : 0.0);
                }
            }
        }
    }

    /**
     * 更新缓存及数据库中的智能手动状态
     *
     * @param thingCode
     * @param state
     */
    public void intelligentManuChange(String thingCode, boolean state) {
        boolean preState;
        if (thingCode != null) {
            FilterPress filterPress = deviceHolder.get(thingCode);
            preState = filterPress.isFeedIntelligent();
            if (preState != state) {
                filterPress.setFeedIntelligent(state);
                filterPressMapper.updateFilterParamValue(thingCode, FilterPress.PARAM_NAME_FEEDINTELLIGENT, state ? 1.0 : 0.0);
            }
        } else {
            for (Map.Entry<String, FilterPress> entry : deviceHolder.entrySet()) {
                FilterPress filterPress = entry.getValue();
                preState = filterPress.isFeedIntelligent();
                if (preState != state) {
                    filterPress.setFeedIntelligent(state);
                    filterPressMapper.updateFilterParamValue(entry.getKey(), FilterPress.PARAM_NAME_FEEDINTELLIGENT, state ? 1.0 : 0.0);
                }
            }
        }
    }

    /**
     * 弹窗确认
     *
     * @param code
     */
    public void feedOverPopupConfirm(String code) {
        doFeedOver(getFilterPress(code));
        messagingTemplate.convertAndSend(FEED_OVER_CONFIRMED_NOTICE_URI, code);
        unconfirmedFeed.remove(code);
    }

    /**
     * 获取系统自动/手动确认状态
     *
     * @return
     */
    public boolean getAutoManuConfirmState() {
        boolean state = false;
        for (Map.Entry<String, FilterPress> entry : deviceHolder.entrySet()) {
            state = entry.getValue().isFeedConfirmNeed();
            break;
        }
        return state;
    }

    /**
     * 获取智能/手动状态
     *
     * @return
     */
    public Map<String, Boolean> getIntelligentManuStateMap() {
        Map<String, Boolean> intelligentManuStateMap = new HashMap<>();
        for (Map.Entry<String, FilterPress> entry : deviceHolder.entrySet()) {
            FilterPress filterPress = entry.getValue();
            boolean state = filterPress.isFeedIntelligent();
            intelligentManuStateMap.put(entry.getKey(), state);
        }
        return intelligentManuStateMap;
    }

    /**
     * 获取一段时间内的电流最大值、最小值及平均值
     *
     * @return
     */
    public Map<String, FilterPressElectricity> getCurrentInfoInDuration() {
        Date endTime = DateUtils.truncate(new Date(), Calendar.HOUR_OF_DAY);
        Date startTime = DateUtils.addDays(endTime, CURRENT_COUNT_DURATION);
//        return filterPressMapper.getCurrentInfoInDuration(startTime, endTime);
        //TODO get real data
        return new HashMap<>();
    }

    // @Scheduled(cron="cnmt.FilterPressDeviceManager.clear")
    // /**
    // * 手动弹出模式下，超过一段时间不操作后自动进行确认
    // */
    // public void clear() {
    // for (String thingCode : unconfirmedFeed) {
    // if (System.currentTimeMillis() -
    // deviceHolder.get(thingCode).getFeedOverTime() > cacheTimeout) {
    // messagingTemplate.convertAndSend(FEED_OVER_CONFIRMED_NOTICE_URI, thingCode);
    // unconfirmedFeed.remove(thingCode);
    // }
    // }
    // }

    private class UnloadManager {
        private AtomicInteger unloading = new AtomicInteger(0);
        private volatile int maxUnloadParallel = 1;
        private BlockingQueue<FilterPress> queue = new PriorityBlockingQueue<>(INIT_CAPACITY, (f1, f2) -> {
            int result;
            if (f1.getOnCycleTime() < f2.getOnCycleTime()) {
                result = -1;
            } else if (f1.getOnCycleTime() > f2.getOnCycleTime()) {
                result = 1;
            } else {
                result = 0;
            }
            return result;
        });

        private Map<String, Integer> queuePosition = new ConcurrentHashMap<>();

        /**
         * 加入卸料排队
         *
         * @param filterPress
         */
        void enqueue(FilterPress filterPress) {
            queuePosition.put(filterPress.getCode(), queue.size());
            queue.add(filterPress);
            unloadNextIfPossible();
        }

        /**
         * 若存在可以卸料的压滤机，则按照最大同时卸料数量进行卸料调度
         */
        private void unloadNextIfPossible() {
            for (int i = unloading.get(); i <= maxUnloadParallel; i++) {
                FilterPress candidate = queue.poll();
                if (candidate == null) {
                    break;
                }
                execUnload(candidate);
            }
        }

        private void execUnload(FilterPress filterPress) {
            if (!filterPress.isUnloadConfirmNeed()) {
                doUnload(filterPress);
            } else {
                messagingTemplate.convertAndSend(UNLOAD_NOTICE_URI, filterPress.getCode());
                unConfirmedUnload.add(filterPress.getCode());
            }
        }

        /**
         * 执行卸料
         *
         * @param filterPress
         */
        private void doUnload(FilterPress filterPress) {
            filterPress.startUnload();
            queuePosition.remove(filterPress.getCode());
            countDownPosition();
            DataModel cmd = new DataModel();
            cmd.setThingCode(filterPress.getCode());
            cmd.setMetricCode(FilterPressMetricConstants.LOOSE);
            cmd.setValue(Boolean.TRUE.toString());
            cmdControlService.sendPulseCmd(cmd, null, null, RequestIdUtil.generateRequestId());
        }

        /**
         * 卸料次序递减
         */
        private void countDownPosition() {
            queuePosition.forEach((code, seq) -> queuePosition.replace(code, seq - 1));
        }

    }
}
