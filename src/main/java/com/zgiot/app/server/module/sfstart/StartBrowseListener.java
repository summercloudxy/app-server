package com.zgiot.app.server.module.sfstart;

import com.zgiot.app.server.dataprocessor.DataListener;
import com.zgiot.app.server.module.sfstart.constants.StartConstants;
import com.zgiot.app.server.module.sfstart.controller.StartHandler;
import com.zgiot.app.server.module.sfstart.pojo.StartDevice;
import com.zgiot.app.server.module.sfstart.service.StartService;
import com.zgiot.app.server.service.impl.mapper.TMLMapper;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.ThingMetricLabel;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class StartBrowseListener implements DataListener {
    @Autowired
    private  StartService startService;
    @Autowired
    private TMLMapper tmlMapper;
    @Autowired
    private StartHandler startHandler;
    @Autowired
    private SfStartManager startStopManager;


    private static Logger logger = LoggerFactory.getLogger(StartBrowseListener.class);

    @Override
    public void onDataChange(DataModel dataModel) {
        ThingMetricLabel thingMetricLabel = new ThingMetricLabel();
        List<ThingMetricLabel> thingMetricLabels = tmlMapper.findThingMetricLabel(dataModel.getThingCode(), dataModel.getMetricCode());
        if (CollectionUtils.isNotEmpty(thingMetricLabels)) {
            thingMetricLabel = thingMetricLabels.get(0);
        }
        //启车总览
        for (String label : startStopManager.getLabelBydevices()) {
            if (label.equals(thingMetricLabel.getLabelPath())) {
                if (logger.isDebugEnabled()) {
                    logger.debug("启车总览标签{}的值{}收到", label, dataModel.getValue());
                }
                String deviceId = startService.selectDeviceIdByDatelabel(label, StartConstants.DEVICE_STATE).get(0);
                StartDevice startDevice = startService.selectStartDeviceByDeviceId(deviceId);
                startDevice.setDeviceState(Integer.valueOf(dataModel.getValue()));
                startHandler.sendMessagingTemplate(StartConstants.URI_START_BROWSE_STATE, startDevice);
            }
        }
    }


    @Override
    public void onError(Throwable error) {
        logger.error("data invalid", error);
    }
}
