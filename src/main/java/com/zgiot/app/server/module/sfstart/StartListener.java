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
public class StartListener implements DataListener {
    @Autowired
    private StartService startService;
    @Autowired
    private TMLMapper tmlMapper;
    @Autowired
    private StartHandler startHandler;


    /**
     * 需要订阅的标签
     */
    private List<String> startLabels;

    public List<String> getStartLabels() {
        return startLabels;
    }

    public void setStartLabels(List<String> startLabels) {
        this.startLabels = startLabels;
    }


    private static org.slf4j.Logger logger = LoggerFactory.getLogger(StartListener.class);

    @Override
    public void onDataChange(DataModel dataModel) {

        ThingMetricLabel thingMetricLabel = new ThingMetricLabel();
        List<ThingMetricLabel> thingMetricLabels = tmlMapper.findThingMetricLabel(dataModel.getThingCode(), dataModel.getMetricCode());
        if (CollectionUtils.isNotEmpty(thingMetricLabels)) {
            thingMetricLabel = thingMetricLabels.get(0);
        }


        //启车
        if (!startService.judgeStartingState(StartStopConstants.START_SEND_FINISH_STATE, StartStopConstants.START_FINISH_STATE)) {
            logger.error("抛弃虚假启车标签数据{}的值{}", thingMetricLabel.getLabelPath(), dataModel.getValue());
            return;
        } else {
            logger.info("启车标签{}的值{}收到", thingMetricLabel.getLabelPath(), dataModel.getValue());
            // 返回获取数值
            if (StartStopConstants.FINISH_STARTING_LABEL.equals(thingMetricLabel.getLabelPath())) {
                if (Double.valueOf(dataModel.getValue()) == StartStopConstants.START_PLC_FINISH) {
                    logger.info("本次启车结束，收到" + StartStopConstants.FINISH_STARTING_LABEL + "的反馈消息");
                    startHandler.finishStartState();
                    startHandler.sendMessageTemplateByJson(StartStopConstants.URI_START_STATE, StartStopConstants.URI_START_STATE_MESSAGE_START_FINISH);
                }
            } else if (StartStopConstants.PAUSE_STARTING_LABEL.equals(thingMetricLabel.getLabelPath())) {
                if (Double.valueOf(dataModel.getValue()) == (double) StartStopConstants.VALUE_TRUE) {
                    StartHandler.setPauseState(true);
                } else {
                    StartHandler.setPauseState(false);
                }
            } else {
                startHandler.updateStartDeviceState(thingMetricLabel.getLabelPath(), dataModel.getValue());
            }
        }


    }

    @Override
    public void onError(Throwable error) {
        logger.error("data invalid", error);
    }
}
