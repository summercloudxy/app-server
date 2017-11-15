package com.zgiot.app.server.module.alert;

import com.zgiot.app.server.config.ApplicationContextListener;
import com.zgiot.app.server.module.alert.mapper.AlertMapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import java.util.Date;

public class AlertHistoryJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ApplicationContext context = ApplicationContextListener.getApplicationContext();
        AlertMapper alertMapper = (AlertMapper) context.getBean("alertMapper");
        alertMapper.clearAlertDateHistory(new Date(System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000L));
    }
}
