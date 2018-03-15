package com.zgiot.app.server.module.sfsubsc.job;

import com.zgiot.app.server.config.ApplicationContextListener;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

/**
 * 卡片定时任务启动
 *
 * @author jys
 */
public class UploadHistorySubscCardDatas implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ApplicationContext context = ApplicationContextListener.getApplicationContext();
        CardDataManager cardDataManager = (CardDataManager) context.getBean("cardDataManager");
        cardDataManager.getHistoryCardDatas();
    }
}
