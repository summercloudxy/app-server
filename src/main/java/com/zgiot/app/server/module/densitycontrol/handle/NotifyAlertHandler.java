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
public class NotifyAlertHandler implements DensityControlHandler {
    @Autowired
    private DensityControlMapper densityControlMapper;
    private Map<String, NotifyInterval> notifyInterval;
    private static final Logger logger = LoggerFactory.getLogger(NotifyAlertHandler.class);

    @PostConstruct
    private void init() {
        notifyInterval = densityControlMapper.getNotifyIntervalByNotifyType(DensityControlManager.NOTIFY_TYPE_ALERT);
    }

    @Override
    public void dispose(MonitoringParam monitoringParam) {
        // logger.debug(
        // "设备{}应该开启高密度报警弹窗通知，关闭其他弹窗通知，当前的密度智能控制各项参数值为：当前处于停车前状态{}，当前比例阀开度为{}，设定高开度为{}，设定低开度为{}，停车前设定低开度为{}，当前密度{}，设定密度{}，密度波动值{}，当前液位{}，设定高液位{}，设定低液位{}，停车前设定高液位{}",
        // monitoringParam.getThingCode(), monitoringParam.getPreStopState(),
        // monitoringParam.getCurrentValveOpening(),
        // monitoringParam.getSettedHighValveOpening(),
        // monitoringParam.getSettedLowValveOpening(),
        // monitoringParam.getPreStopSettedLowValveOpening(),
        // monitoringParam.getCurrentDensity(), monitoringParam.getSettedDensity(),
        // monitoringParam.getFluctuantDensity(), monitoringParam.getCurrentFuelLevel(),
        // monitoringParam.getSettedHighLevel(), monitoringParam.getSettedLowLevel(),
        // monitoringParam.getPreStopSettedHighLevel());
        logger.debug("设备{}应该开启高密度报警弹窗通知，关闭其他弹窗通知", monitoringParam.getThingCode());
        String thingCode = monitoringParam.getThingCode();
        int interval = notifyInterval.get(thingCode).getInterval();
        monitoringParam.setCurrentStage(DensityControlManager.NOTIFY_TYPE_ALERT);
        // if
        // (QuartzManager.checkExists(DensityControlManager.NOTIFY_TYPE_ADDING_MEDIUM,
        // monitoringParam.getThingCode())) {
        // QuartzManager.removeJob(DensityControlManager.NOTIFY_TYPE_ADDING_MEDIUM,
        // monitoringParam.getThingCode(),
        // DensityControlManager.NOTIFY_TYPE_ADDING_MEDIUM,
        // monitoringParam.getThingCode());
        //
        // logger.debug("设备{}目前存在加介弹窗通知，关闭该推送", monitoringParam.getThingCode());
        // }
        // if (QuartzManager.checkExists(DensityControlManager.NOTIFY_TYPE_BACK_FLOW,
        // monitoringParam.getThingCode())) {
        // QuartzManager.removeJob(DensityControlManager.NOTIFY_TYPE_BACK_FLOW,
        // monitoringParam.getThingCode(),
        // DensityControlManager.NOTIFY_TYPE_BACK_FLOW, monitoringParam.getThingCode());
        // logger.debug("设备{}目前存在回流弹窗通知，关闭该推送", monitoringParam.getThingCode());
        // }
        if (!QuartzManager.checkExists(DensityControlManager.NOTIFY_TYPE_ALERT, monitoringParam.getThingCode())) {
            // JobDataMap jobDataMap = new JobDataMap() {
            // {
            // put("param", monitoringParam);
            // }
            // };
            QuartzManager.addJobWithMinutes(DensityControlManager.NOTIFY_TYPE_ALERT, monitoringParam.getThingCode(),
                    DensityControlManager.NOTIFY_TYPE_ALERT, monitoringParam.getThingCode(), NotifyJob.class, interval,
                    null);
            logger.debug("设备{}目前不存在高密度弹窗通知，开启该推送", monitoringParam.getThingCode());
        }
    }

    public void cancel(String thingCode) {
        if (QuartzManager.checkExists(DensityControlManager.NOTIFY_TYPE_ALERT, thingCode)) {
            QuartzManager.removeJob(DensityControlManager.NOTIFY_TYPE_ALERT, thingCode,
                    DensityControlManager.NOTIFY_TYPE_ALERT, thingCode);
            logger.debug("设备{}目前存在高密度弹窗通知，关闭该推送", thingCode);
        }
    }
}
