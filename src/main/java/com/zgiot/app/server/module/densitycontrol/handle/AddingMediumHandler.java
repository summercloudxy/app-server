package com.zgiot.app.server.module.densitycontrol.handle;

import com.zgiot.app.server.module.densitycontrol.DensityControlManager;
import com.zgiot.app.server.module.densitycontrol.job.NotifyJob;
import com.zgiot.app.server.module.densitycontrol.mapper.DensityControlMapper;
import com.zgiot.app.server.module.densitycontrol.pojo.MonitoringParam;
import com.zgiot.app.server.module.densitycontrol.pojo.NotifyInterval;
import com.zgiot.app.server.service.impl.QuartzManager;
import org.quartz.JobDataMap;
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
    private static final Logger logger = LoggerFactory.getLogger(AddingMediumHandler.class);

    @PostConstruct
    public void init() {
        notifyIntervalMap =
                densityControlMapper.getNotifyIntervalByNotifyType(DensityControlManager.NOTIFY_TYPE_ADDING_MEDIUM);
    }

    @Override
    public void dispose(MonitoringParam monitoringParam) {
        // logger.debug(
        // "设备{}应该开启加介弹窗通知，关闭其他弹窗通知，当前的密度智能控制各项参数值为：当前处于停车前状态{}，当前比例阀开度为{}，设定高开度为{}，设定低开度为{}，停车前设定低开度为{}，当前密度{}，设定密度{}，密度波动值{}，当前液位{}，设定高液位{}，设定低液位{}，停车前设定高液位{}",
        // monitoringParam.getThingCode(), monitoringParam.getPreStopState(),
        // monitoringParam.getCurrentValveOpening(),
        // monitoringParam.getSettedHighValveOpening(),
        // monitoringParam.getSettedLowValveOpening(),
        // monitoringParam.getPreStopSettedLowValveOpening(),
        // monitoringParam.getCurrentDensity(), monitoringParam.getSettedDensity(),
        // monitoringParam.getFluctuantDensity(), monitoringParam.getCurrentFuelLevel(),
        // monitoringParam.getSettedHighLevel(), monitoringParam.getSettedLowLevel(),
        // monitoringParam.getPreStopSettedHighLevel());
        logger.debug("设备{}应该开启加介弹窗通知，关闭其他弹窗通知", monitoringParam.getThingCode());
        int interval = notifyIntervalMap.get(monitoringParam.getThingCode()).getInterval();
        monitoringParam.setCurrentStage(DensityControlManager.NOTIFY_TYPE_ADDING_MEDIUM);
        // if (QuartzManager.checkExists(DensityControlManager.NOTIFY_TYPE_ALERT,
        // monitoringParam.getThingCode())) {
        // QuartzManager.removeJob(DensityControlManager.NOTIFY_TYPE_ALERT,
        // monitoringParam.getThingCode(),
        // DensityControlManager.NOTIFY_TYPE_ALERT, monitoringParam.getThingCode());
        // logger.debug("设备{}目前存在高密度报警弹窗通知，关闭该推送", monitoringParam.getThingCode());
        // }
        // if (QuartzManager.checkExists(DensityControlManager.NOTIFY_TYPE_BACK_FLOW,
        // monitoringParam.getThingCode())) {
        // QuartzManager.removeJob(DensityControlManager.NOTIFY_TYPE_BACK_FLOW,
        // monitoringParam.getThingCode(),
        // DensityControlManager.NOTIFY_TYPE_BACK_FLOW, monitoringParam.getThingCode());
        // logger.debug("设备{}目前存在回流弹窗通知，关闭该推送", monitoringParam.getThingCode());
        // }
        if (!QuartzManager.checkExists(DensityControlManager.NOTIFY_TYPE_ADDING_MEDIUM,
                monitoringParam.getThingCode())) {
            // JobDataMap jobDataMap = new JobDataMap() {
            // {
            // put(DensityControlManager.JOB_DATA_MAP_KEY, monitoringParam);
            // }
            // };
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
