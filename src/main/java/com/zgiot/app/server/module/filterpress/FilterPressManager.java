package com.zgiot.app.server.module.filterpress;

import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.util.RequestIdUtil;
import com.zgiot.common.constants.FilterPressConstants;
import com.zgiot.common.constants.FilterPressMetricConstants;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class FilterPressManager {
    @Autowired
    private DataService dataService;
    @Autowired
    private CmdControlService cmdControlService;

    private static final int INIT_CAPACITY = 6;

    private Map<String, FilterPress> manager = new ConcurrentHashMap<>();

    private UnloadManager unloadManager = new UnloadManager();

    /**
     * call back when data changed
     *
     * @param data
     */
    public void onDataSourceChange(DataModel data) {
        FilterPress filterPress = manager.get(data.getThingCode());
        filterPress.onDataSourceChange(data.getMetricCode(), data.getValue());
        if (FilterPressMetricConstants.STAGE.equals(data.getMetricCode())) {
            processStage(data);
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
        switch (stageValue) { //回调各阶段
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
        //calculate the state value and call the specific method of filter press
        short stateValue = calculateState(thingCode, stageValue);
        if (!Objects.equals(dataService.getData(thingCode, FilterPressMetricConstants.STATE).getValue(), String.valueOf(stateValue))) {//若值变化，保存并回调
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
     * get the filter press by thing code from the manager
     *
     * @param thingCode
     * @return
     */
    public FilterPress getFilterPress(String thingCode) {
        FilterPress filterPress = manager.get(thingCode);
        if (filterPress == null) {
            throw new NoSuchElementException("no such filter press:" + thingCode);
        }
        return filterPress;
    }

    /**
     * send the command
     *
     * @param filterPress
     * @param metricCode
     * @param value
     */
    void sendCmd(FilterPress filterPress, String metricCode, Object value) {
        DataModel data = new DataModel();
        data.setThingCode(filterPress.getCode());
        data.setMetricCode(metricCode);
        data.setValue(value.toString());
        cmdControlService.sendCmd(data, RequestIdUtil.generateRequestId());
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
                doUnload(candidate);
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
            cmdControlService.sendCmd(cmd, RequestIdUtil.generateRequestId());
        }

        /**
         * 卸料次序递减
         */
        private void countDownPosition() {
            queuePosition.forEach((code, seq) -> queuePosition.replace(code, seq - 1));
        }

    }
}

