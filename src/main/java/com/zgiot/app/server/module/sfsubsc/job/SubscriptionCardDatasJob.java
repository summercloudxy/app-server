package com.zgiot.app.server.module.sfsubsc.job;

import com.zgiot.app.server.config.ApplicationContextListener;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

/**
 * @author jys
 */
public class SubscriptionCardDatasJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ApplicationContext context = ApplicationContextListener.getApplicationContext();
        CardDataManager cardDataManager = (CardDataManager) context.getBean("cardDataManager");
        cardDataManager.getAllCardDatas();
    }
}
