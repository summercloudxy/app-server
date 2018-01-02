package com.zgiot.app.server.module.densitycontrol.job;

import com.zgiot.app.server.config.ApplicationContextListener;
import com.zgiot.app.server.module.densitycontrol.DensityControlManager;
import com.zgiot.app.server.module.densitycontrol.pojo.MonitoringParam;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class NotifyJob implements Job {
    private static Logger logger = LoggerFactory.getLogger(NotifyJob.class);
    // private ApplicationContext context =
    // ContextLoader.getCurrentWebApplicationContext();
    // private SimpMessagingTemplate messagingTemplate =
    // (SimpMessagingTemplate) context.getBean("brokerMessagingTemplate");
    private String noticeUri = "/topic/densityControl/notify";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ApplicationContext context = ApplicationContextListener.getApplicationContext();
        SimpMessagingTemplate messagingTemplate = (SimpMessagingTemplate) context.getBean("brokerMessagingTemplate");
        JobKey key = jobExecutionContext.getJobDetail().getKey();
        DensityControlManager densityControlManager = (DensityControlManager) context.getBean("densityControlManager");
        MonitoringParam monitoringParam = densityControlManager.getMonitoringParam(key.getGroup());
        monitoringParam.setCurrentStage(key.getName());
        messagingTemplate.convertAndSend(noticeUri, monitoringParam);
        // logger.debug(
        // "设备{}进行{}弹窗推送，推送参数值为：当前处于停车前状态{}，当前比例阀开度为{}，设定高开度为{}，设定低开度为{}，停车前设定低开度为{}，当前密度{}，设定密度{}，密度波动值{}，当前液位{}，设定高液位{}，设定低液位{}，停车前设定高液位{}",
        // monitoringParam.getThingCode(), monitoringParam.getCurrentStage(),
        // monitoringParam.getPreStopState(), monitoringParam.getCurrentValveOpening(),
        // monitoringParam.getSettedHighValveOpening(),
        // monitoringParam.getSettedLowValveOpening(),
        // monitoringParam.getPreStopSettedLowValveOpening(),
        // monitoringParam.getCurrentDensity(), monitoringParam.getSettedDensity(),
        // monitoringParam.getFluctuantDensity(), monitoringParam.getCurrentFuelLevel(),
        // monitoringParam.getSettedHighLevel(),
        // monitoringParam.getSettedLowLevel(),
        // monitoringParam.getPreStopSettedHighLevel());
        logger.debug("设备{}进行{}弹窗推送，推送参数值为：当前比例阀开度为{},当前密度{}，当前液位{}", monitoringParam.getThingCode(),
                monitoringParam.getCurrentStage(), monitoringParam.getCurrentValveOpening(),
                monitoringParam.getCurrentDensity(), monitoringParam.getCurrentFuelLevel());

    }
}
