package com.zgiot.app.server.module.sfstart;

import com.zgiot.app.server.dataprocessor.DataListener;
import com.zgiot.app.server.module.sfstart.constants.StartStopConstants;
import com.zgiot.app.server.module.sfstart.controller.StartHandler;
import com.zgiot.app.server.module.sfstart.service.StartService;
import com.zgiot.app.server.service.impl.mapper.TMLMapper;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.ThingMetricLabel;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Component
public class StartExamineListener implements DataListener {
    @Autowired
    private StartService startService;
    @Autowired
    private TMLMapper tmlMapper;
    @Autowired
    private StartHandler startHandler;

    public List<String> getStartExamineLabels() {
        return startExamineLabels;
    }

    public void setStartExamineLabels(List<String> startExamineLabels) {
        this.startExamineLabels = startExamineLabels;
    }


    /**
     * 需要订阅的标签
     */
    private List<String> startExamineLabels;


    private static org.slf4j.Logger logger = LoggerFactory.getLogger(StartExamineListener.class);

    @Override
    public void onDataChange(DataModel dataModel) {

        ThingMetricLabel thingMetricLabel = new ThingMetricLabel();
        List<ThingMetricLabel> thingMetricLabels = tmlMapper.findThingMetricLabel(dataModel.getThingCode(), dataModel.getMetricCode());
        if (CollectionUtils.isNotEmpty(thingMetricLabels)) {
            thingMetricLabel = thingMetricLabels.get(0);
        }


        //启车检查
        for (String label : startExamineLabels) {
            if (thingMetricLabel.getLabelPath().equals(label)) {
                if (!startService.judgeStartingState(StartStopConstants.START_PREPARE_STATE, StartStopConstants.START_STARTING_STATE)) {
                    logger.error("抛弃虚假启车检查标签数据{}的值{}", label, dataModel.getValue());
                    return;
                }
                logger.info("启车检查标签{}的值{}收到", label, dataModel.getValue());
                String metricValue=null;
                if("false".equals(dataModel.getValue())){
                    metricValue="0.0";
                }else if("true".equals(dataModel.getValue())){
                    metricValue="1.0";
                }else {
                    metricValue=dataModel.getValue();
                }

                startHandler.updateExamineRecordByRule(label, Double.valueOf(metricValue));
            }
        }
    }

    @Override
    public void onError(Throwable error) {
        logger.error("data invalid", error);
    }
}
