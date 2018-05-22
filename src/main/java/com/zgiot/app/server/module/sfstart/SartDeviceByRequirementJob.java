package com.zgiot.app.server.module.sfstart;

import com.zgiot.app.server.config.ApplicationContextListener;
import com.zgiot.app.server.module.alert.AlertFaultJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class SartDeviceByRequirementJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(AlertFaultJob.class);


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ApplicationContext context = ApplicationContextListener.getApplicationContext();
        SfStartManager sfStartManager = (SfStartManager) context.getBean("sfStartManager");
        sfStartManager.startDeviceByRequirement();

    }
}
