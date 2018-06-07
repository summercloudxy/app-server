package com.zgiot.app.server.module.sfstop;

import com.zgiot.app.server.dataprocessor.DataListener;
import com.zgiot.app.server.module.sfstop.controller.StopHandler;
import com.zgiot.app.server.module.sfstop.entity.vo.StopExamineThing;
import com.zgiot.common.pojo.DataModel;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 一期设备停车自检
 */
@Transactional
@Component
public class StopExamineSystemListener implements DataListener {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(StopExamineSystemListener.class);

    @Autowired
    private StopHandler stopHandler;
    /**
     * 一期停车自检的设备
     */
    private List<StopExamineThing> system1StopExamineThingList;

    /**
     * 二期停车自检的设备
     */
    private List<StopExamineThing> system2StopExamineThingList;

    public List<StopExamineThing> getSystem2StopExamineThingList() {
        return system2StopExamineThingList;
    }

    public void setSystem2StopExamineThingList(List<StopExamineThing> system2StopExamineThingList) {
        this.system2StopExamineThingList = system2StopExamineThingList;
    }

    public List<StopExamineThing> getSystem1StopExamineThingList() {
        return system1StopExamineThingList;
    }

    public void setSystem1StopExamineThingList(List<StopExamineThing> system1StopExamineThingList) {
        this.system1StopExamineThingList = system1StopExamineThingList;
    }

    @Override
    public void onDataChange(DataModel dataModel) {

            for (StopExamineThing stopExamineThing : system1StopExamineThingList) {
                if (dataModel.getThingCode().equals(stopExamineThing.getThingCode()) && dataModel.getMetricCode().equals(stopExamineThing.getMetricCode())) {
                    logger.info("一期停车检查ThingCode：{},MetricCode{},的值{}收到", dataModel.getThingCode(), dataModel.getMetricCode(), dataModel.getValue());
                    stopHandler.updateExamineRecordByRule(dataModel.getThingCode(), dataModel.getMetricCode(), dataModel.getValue());
                } else {
                    return;
                }

            }
        for (StopExamineThing stopExamineThing : system2StopExamineThingList) {
            if (dataModel.getThingCode().equals(stopExamineThing.getThingCode()) && dataModel.getMetricCode().equals(stopExamineThing.getMetricCode())) {
                logger.info("二期停车检查ThingCode：{},MetricCode{},的值{}收到", dataModel.getThingCode(), dataModel.getMetricCode(), dataModel.getValue());
                stopHandler.updateExamineRecordByRule(dataModel.getThingCode(), dataModel.getMetricCode(), dataModel.getValue());
            } else {
                return;
            }

        }


    }

    @Override
    public void onError(Throwable error) {
        logger.error("data invalid", error);
    }
}
