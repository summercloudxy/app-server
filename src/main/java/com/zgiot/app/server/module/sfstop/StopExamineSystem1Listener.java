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
public class StopExamineSystem1Listener implements DataListener {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(StopExamineSystem1Listener.class);

    @Autowired
    private StopHandler stopHandler;
    /**
     * 停车自检的设备
     */
    private List<StopExamineThing> stopExamineThingList;


    public List<StopExamineThing> getStopExamineThingList() {
        return stopExamineThingList;
    }

    public void setStopExamineThingList(List<StopExamineThing> stopExamineThingList) {
        this.stopExamineThingList = stopExamineThingList;
    }

    @Override
    public void onDataChange(DataModel dataModel) {

        if (StopHandler.startExamineListenerFlag != null && StopHandler.startExamineListenerFlag) {
            for (StopExamineThing stopExamineThing : stopExamineThingList) {
                if (dataModel.getThingCode().equals(stopExamineThing.getThingCode()) && dataModel.getMetricCode().equals(stopExamineThing.getMetricCode())) {
                    logger.info("停车检查ThingCode：{},MetricCode{},的值{}收到", dataModel.getThingCode(), dataModel.getMetricCode(), dataModel.getValue());
                    stopHandler.updateExamineRecordByRule(dataModel.getThingCode(), dataModel.getMetricCode(), dataModel.getValue());
                } else {
                    return;
                }

            }
        } else {
            return;
        }


    }

    @Override
    public void onError(Throwable error) {
        logger.error("data invalid", error);
    }
}
