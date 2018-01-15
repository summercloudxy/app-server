package com.zgiot.app.server.module.alert;

import com.zgiot.app.server.module.alert.handler.AlertParamHandler;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlertParamJob implements Job {
    private final Logger logger = LoggerFactory.getLogger(AlertParamJob.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        logger.debug("执行参数类报警检查定时任务");
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        AlertParamHandler alertHandler = (AlertParamHandler) jobDataMap.get("handler");
        alertHandler.updateAlertLevel();
    }
}
