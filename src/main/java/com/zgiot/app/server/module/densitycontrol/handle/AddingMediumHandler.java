package com.zgiot.app.server.module.densitycontrol.handle;

import com.zgiot.app.server.module.densitycontrol.DensityControlManager;
import com.zgiot.app.server.module.densitycontrol.job.NotifyJob;
import com.zgiot.app.server.module.densitycontrol.mapper.DensityControlMapper;
import com.zgiot.app.server.module.densitycontrol.pojo.MonitoringParam;
import com.zgiot.app.server.module.densitycontrol.pojo.NotifyInterval;
import com.zgiot.app.server.service.impl.QuartzManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class AddingMediumHandler implements DensityControlHandler {
    @Autowired
    private DensityControlMapper densityControlMapper;
    private Map<String, NotifyInterval> notifyIntervalMap;
    private final Logger logger = LoggerFactory.getLogger(AddingMediumHandler.class);

    @PostConstruct
    public void init() {
        notifyIntervalMap =
                densityControlMapper.getNotifyIntervalByNotifyType(DensityControlManager.NOTIFY_TYPE_ADDING_MEDIUM);
    }

    @Override
    public void dispose(MonitoringParam monitoringParam) {
        logger.debug("设备{}应该开启加介弹窗通知，关闭其他弹窗通知", monitoringParam.getThingCode());
        int interval = notifyIntervalMap.get(monitoringParam.getThingCode()).getInterval();
        monitoringParam.setCurrentStage(DensityControlManager.NOTIFY_TYPE_ADDING_MEDIUM);
        if (!QuartzManager.checkExists(DensityControlManager.NOTIFY_TYPE_ADDING_MEDIUM,
                monitoringParam.getThingCode())) {
            QuartzManager.addJobWithMinutes(DensityControlManager.NOTIFY_TYPE_ADDING_MEDIUM,
                    monitoringParam.getThingCode(), DensityControlManager.NOTIFY_TYPE_ADDING_MEDIUM,
                    monitoringParam.getThingCode(), NotifyJob.class, interval, null);
            logger.debug("设备{}目前不存在加介弹窗通知，开启该推送", monitoringParam.getThingCode());
        }
    }

    public void cancel(String thingCode) {
        if (QuartzManager.checkExists(DensityControlManager.NOTIFY_TYPE_ADDING_MEDIUM, thingCode)) {
            QuartzManager.removeJob(DensityControlManager.NOTIFY_TYPE_ADDING_MEDIUM, thingCode,
                    DensityControlManager.NOTIFY_TYPE_ADDING_MEDIUM, thingCode);
            logger.debug("设备{}目前存在加介弹窗通知，关闭该推送", thingCode);
        }
    }
}
