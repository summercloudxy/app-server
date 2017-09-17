package com.zgiot.app.server.module.filterpress;


import com.zgiot.common.constants.FilterPressMetricConstants;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class FilterPress {
    private static final long UNLOAD_WAIT_DURATION = Duration.ofMinutes(3).plusSeconds(45).toMillis();
    private static final int UNLOAD_EXCHANGE_COUNT = 16;
    private final String code;
    private FilterPressManager manager;
    /**
     * 入料开始时间，若不处于入料状态，值为空
     */
    private Long feedStartTime;
    /**
     * 入料结束时间
     */
    private Long feedOverTime;
    /**
     * 进料时长
     */
    private Long feedDuration;
    /**
     * 入料泵电流
     */
    private double feedPumpCurrent;
    /**
     * 进入循环等待的时间
     */
    private Long onCycleTime;
    /**
     * 卸料条件判断管理器
     */
    private UnloadManager unloadManager = new UnloadManager();
    /**
     * 进料智能
     */
    private volatile boolean feedIntelligent;
    /**
     * 卸料智能
     */
    private volatile boolean unloadIntelligent;

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
    }

    public void onStop() {
    }

    public void onFault() {
    }

    public void onLoosen() {
    }

    public void onTaken() {
        unloadManager.countTaken();
    }

    public void onPull() {
        unloadManager.countPulled();
    }

    public void onPress() {
        //压紧后通知下一台
        unloadManager.notifyNext();
        unloadManager.stopUnload();
    }

    public void onFeed() {
    }

    public void onFeedOver() {
    }

    public void onBlow() {
    }

    public void onCycle() {
        this.onCycleTime = System.currentTimeMillis();
        manager.enqueueUnload(this);
    }

    /**
     * 开始卸料
     */
    void startUnload() {
        unloadManager.startUnload();
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

    private class UnloadManager {
        /**
         * 卸料计时
         */
        private Timer unloadTimer;
        /**
         * 卸料暂停时已过计时
         */
        private volatile long unloadInterruptedDuration = 0;
        /**
         * 是否正在卸料
         */
        private volatile boolean isUnloading = false;
        /**
         * 上一次暂停的时间
         */
        private Long latestPauseTime;
        /**
         * 拉板和取板的交替次数
         */
        private AtomicInteger takeAndPullCount = new AtomicInteger(0);
        /**
         * 已通知下一台
         */
        private volatile boolean notifiedNext = false;

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
            if (latestPauseTime != null) {
                unloadInterruptedDuration += System.currentTimeMillis() - latestPauseTime;
            }
            unloadTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    notifyNext();
                    unloadInterruptedDuration = 0;
                    latestPauseTime = null;
                }
            }, UNLOAD_WAIT_DURATION - unloadInterruptedDuration);
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
            latestPauseTime = System.currentTimeMillis();
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
                manager.unloadNext();
                notifiedNext = true;
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
                cancelTimer();
                notifyNext();
                takeAndPullCount.set(0);
            }
        }
    }

}
