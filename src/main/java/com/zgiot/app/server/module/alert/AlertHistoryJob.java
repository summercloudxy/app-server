package com.zgiot.app.server.module.alert;

import com.zgiot.app.server.config.ApplicationContextListener;
import com.zgiot.app.server.module.alert.mapper.AlertMapper;
import com.zgiot.common.constants.AlertConstants;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationContext;

import java.util.Date;

public class AlertHistoryJob implements Job {
    private Long oneMonthPeriod = 30 * 24 * 60 * 60 * 1000L;

    @Override
    public void execute(JobExecutionContext jobExecutionContext)  {
        ApplicationContext context = ApplicationContextListener.getApplicationContext();
        AlertMapper alertMapper = (AlertMapper) context.getBean("alertMapper");
        alertMapper.clearAlertDateHistory(new Date(System.currentTimeMillis() - oneMonthPeriod), AlertConstants.STAGE_RELEASE);
    }
}
