package com.zgiot.app.server.module.bellows.valve;


import com.zgiot.app.server.config.ApplicationContextListener;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

public class ValveIntelligentJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ApplicationContext context = ApplicationContextListener.getApplicationContext();
        ValveManager valveManager = (ValveManager) context.getBean("valveManager");
        valveManager.checkLoop();
    }
}
