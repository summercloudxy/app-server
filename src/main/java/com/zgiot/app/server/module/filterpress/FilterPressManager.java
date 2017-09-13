package com.zgiot.app.server.module.filterpress;

import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.constants.FilterPressConstants;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FilterPressManager {
    @Autowired
    private DataService dataService;
    @Autowired
    private CmdControlService cmdControlService;

    private Map<String, FilterPress> manager = new ConcurrentHashMap<>();

    public void onDataSourceChange(DataModel data) {
        if (data.getMetricCode().equals(FilterPressConstants.STAGE)) {
            processStage(data);
        }
    }

    private void processStage(DataModel data) {
        String thingCode = data.getThingCode();
        short stageValue = (short) data.getValue();
        FilterPress filterPress = getFilterPress(thingCode);
        switch (stageValue) {//回调各阶段
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

        short stateValue = calculateState(thingCode, stageValue);
        if (!Objects.equals(dataService.getData(thingCode, FilterPressConstants.STATE).getValue(), stateValue)) {//若值变化，保存并回调
            DataModel stateModel = new DataModel();
            stateModel.setThingCode(thingCode);
            stateModel.setThingCategoryCode(data.getThingCategoryCode());
            stateModel.setMetricCode(FilterPressConstants.STATE);
            stateModel.setThingCategoryCode(data.getMetricCategoryCode());
            stateModel.setValue(stateValue);
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
        DataModelWrapper fault = dataService.getData(thingCode, FilterPressConstants.FAULT);
        if ((boolean) fault.getValue()) {
            state = GlobalConstants.STATE_FAULT;
        } else if (stageValue == 0) {
            state = GlobalConstants.STATE_STOPPED;
        } else {
            state = GlobalConstants.STATE_RUNNING;
        }
        return state;
    }

    private FilterPress getFilterPress(String thingCode) {
        FilterPress filterPress = manager.get(thingCode);
        if (filterPress == null) {
            throw new NullPointerException("no such filter press:" + thingCode);
        }
        return filterPress;
    }
}
