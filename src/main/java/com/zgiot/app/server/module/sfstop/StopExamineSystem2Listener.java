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
 * 二期设备停车自检
 */
@Transactional
@Component
public class StopExamineSystem2Listener implements DataListener {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(StopExamineSystem2Listener.class);

    @Autowired
    private StopHandler stopHandler;
    /**
     * 停车自检的设备
     */
    private List<StopExamineThing> system2StopExamineThingList;


    public List<StopExamineThing> getSystem2StopExamineThingList() {
        return system2StopExamineThingList;
    }

    public void setSystem2StopExamineThingList(List<StopExamineThing> system2StopExamineThingList) {
        this.system2StopExamineThingList = system2StopExamineThingList;
    }

    @Override
    public void onDataChange(DataModel dataModel) {

        if (StopHandler.startExamineListenerFlag != null && StopHandler.startExamineListenerFlag) {
            for (StopExamineThing stopExamineThing : system2StopExamineThingList) {
                if (dataModel.getThingCode().equals(stopExamineThing.getThingCode()) && dataModel.getMetricCode().equals(stopExamineThing.getMetricCode())) {
                    logger.info("二期停车检查ThingCode：{},MetricCode{},的值{}收到", dataModel.getThingCode(), dataModel.getMetricCode(), dataModel.getValue());
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
