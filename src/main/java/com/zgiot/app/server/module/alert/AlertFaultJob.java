package com.zgiot.app.server.module.alert;

import com.zgiot.app.server.module.alert.handler.AlertFaultHandler;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlertFaultJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(AlertFaultJob.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        AlertFaultHandler alertFaultHandler = (AlertFaultHandler) jobDataMap.get("handler");
        alertFaultHandler.updateCache();
        logger.debug("执行故障类报警检查定时任务");
    }
}
