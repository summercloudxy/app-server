package com.zgiot.app.server.module.filterpress;


import com.zgiot.common.constants.FilterPressMetricConstants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FilterPress {
    public static final String PARAM_NAME_FEEDINTELLIGENT = "feedIntelligent";
    public static final String PARAM_NAME_UNLOADINTELLIGENT = "unloadIntelligent";
    public static final String PARAM_NAME_FEEDCONFIRMNEED = "feedConfirmNeed";
    public static final String PARAM_NAME_UNLOADCONFIRMNEED = "unloadConfirmNeed";
    public static final String PARAM_NAME_MAXUNLOADPARALLEL = "maxUnloadParallel";

    private static final Logger logger = LoggerFactory.getLogger(FilterPress.class);
    private static final long UNLOAD_WAIT_DURATION = Duration.ofMinutes(3).plusSeconds(45).toMillis();

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
    private volatile double feedPumpCurrent;
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

    public FilterPress(String code, FilterPressManager manager) {
        this.code = code;
        this.manager = manager;
    }

    void onDataSourceChange(String metricCode, String value) {
        if (FilterPressMetricConstants.PAUSE.equals(metricCode) && unloadManager.isUnloading) {
            if (Boolean.TRUE.toString().equals(value)) {
                unloadManager.pauseTimer();
            } else {
                unloadManager.scheduleTimer();
            }
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

    public void onLoosen() {
        logger.trace("{} on loosen", code);
    }

    public void onTaken() {
        logger.trace("{} on taken", code);
        unloadManager.countTaken();
    }

    public void onPull() {
        logger.trace("{} on pull", code);
        unloadManager.countPulled();
    }

    public void onPress() {
        logger.trace("{} on press", code);
        //压紧后通知下一台
        unloadManager.notifyNext();
        unloadManager.stopUnload();
    }

    public void onFeed() {
        logger.trace("{} on feed", code);
        feedStartTime = System.currentTimeMillis();
    }

    public void onFeedOver() {
        logger.trace("{} on feed over", code);
    }

    public void onBlow() {
        logger.trace("{} on blow", code);
    }

    public void onCycle() {
        logger.trace("{} on cycle", code);
        this.onCycleTime = System.currentTimeMillis();
        this.unloadManager.notifiedNext = false;
        if (unloadIntelligent) {
            manager.enqueueUnload(this);
            logger.debug("{} enqueue unload, intelligent:{}", code, unloadIntelligent);
        }
    }

    public void onAssumeFeedOver() {
        logger.trace("{} assume to be feed over", code);
        this.feedOverTime = System.currentTimeMillis();
//        feedDuration = feedOverTime - feedStartTime;
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

    public void setFeedOverTime(Long feedOverTime) {
        this.feedOverTime = feedOverTime;
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

    public void setFeedPumpCurrent(double feedPumpCurrent) {
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

    public void setUnloadConfirmNeed(boolean unloadConfirmNeed) {
        this.unloadConfirmNeed = unloadConfirmNeed;
    }

    public int getPlateCount() {
        logger.info("producingTeam:" + producingTeam);
        if(producingTeam == null){
            return 0;
        }
        logger.info("plateCount:" + plateCount);
        return plateCount.get(producingTeam);
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
        private AtomicInteger takeAndPullCount = new AtomicInteger(0);
        /**
         * 已通知下一台
         */
        volatile boolean notifiedNext = false;

        /**
         * 开始卸料
         */
        private void startUnload() {
            isUnloading = true;
            scheduleTimer();
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
                logger.debug("{} unload finished, notifying next", code);
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
            if (takeAndPullCount.incrementAndGet() >= UNLOAD_EXCHANGE_COUNT && isUnloading) {
                logger.debug("{} take and pull enough", code);
                cancelTimer();
                notifyNext();
                takeAndPullCount.set(0);
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
