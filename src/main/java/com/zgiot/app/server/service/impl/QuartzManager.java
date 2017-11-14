package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.config.ApplicationContextListener;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.context.ApplicationContext;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Quartz调度管理器
 *
 */
public class QuartzManager {


    /**
     * 添加一个定时任务
     * @param jobName
     * @param jobGroup
     * @param triggerName
     * @param triggerGroup
     * @param cls
     * @param cronExpression
     */
    public static void addJob(String jobName, String jobGroup, String triggerName, String triggerGroup,
            @SuppressWarnings("rawtypes") Class cls, String cronExpression) {
        try {
            ApplicationContext context = ApplicationContextListener.getApplicationContext();
            StdScheduler scheduler = (StdScheduler) context.getBean("quartzScheduler");
            JobDetail jobDetail = newJob().withIdentity(JobKey.jobKey(jobName, jobGroup)).ofType(cls).build();
            // 触发器
            CronTrigger trigger = newTrigger().withIdentity(TriggerKey.triggerKey(triggerName, triggerGroup))
                    .withSchedule(cronSchedule(cronExpression)).build();
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void addJob(String jobName, String jobGroup, String triggerName, String triggerGroup,
                              @SuppressWarnings("rawtypes") Class cls, String cronExpression,JobDataMap jobDataMap) {
        try {
            ApplicationContext context = ApplicationContextListener.getApplicationContext();
            StdScheduler scheduler = (StdScheduler) context.getBean("quartzScheduler");
            JobDetail jobDetail = newJob().setJobData(jobDataMap).withIdentity(JobKey.jobKey(jobName, jobGroup)).ofType(cls).build();
            // 触发器
            CronTrigger trigger = newTrigger().withIdentity(TriggerKey.triggerKey(triggerName, triggerGroup))
                    .withSchedule(cronSchedule(cronExpression)).build();
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * 添加定时任务
     * 
     * @param jobName
     * @param triggerName
     * @param jobClass
     * @param time
     */
    public static void addJob(String jobName, String triggerName, @SuppressWarnings("rawtypes") Class jobClass,
            String time) {
        addJob(jobName, null, triggerName, null, jobClass, time);
    }

    public static void addJob(String jobName, @SuppressWarnings("rawtypes") Class jobClass, String time) {
        addJob(jobName, null, jobName, null, jobClass, time);
    }


    public static void addJob(String jobName, @SuppressWarnings("rawtypes") Class jobClass, String time,JobDataMap jobDataMap) {
        addJob(jobName, null, jobName, null, jobClass, time,jobDataMap);
    }

    /**
     * 修改一个任务的触发时间
     * @param jobName
     * @param time
     */
    @SuppressWarnings("rawtypes")
    public static void modifyJobTime(String jobName, String time) {
        try {
            ApplicationContext context = ApplicationContextListener.getApplicationContext();
            StdScheduler scheduler = (StdScheduler) context.getBean("quartzScheduler");
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(TriggerKey.triggerKey(jobName));
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName));
                Class objJobClass = jobDetail.getJobClass();
                removeJob(jobName);
                addJob(jobName, objJobClass, time);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改一个任务的触发时间
     * @param triggerName
     * @param triggerGroupName
     * @param time
     */
    public static void modifyJobTime(String triggerName, String triggerGroupName, String time) {
        try {
            ApplicationContext context = ApplicationContextListener.getApplicationContext();
            StdScheduler scheduler = (StdScheduler) context.getBean("quartzScheduler");
            CronTriggerImpl trigger =
                    (CronTriggerImpl) scheduler.getTrigger(TriggerKey.triggerKey(triggerName, triggerGroupName));
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                // 修改时间
                trigger.setCronExpression(time);
                // 重启触发器
                scheduler.resumeTrigger(TriggerKey.triggerKey(triggerName, triggerGroupName));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 移除一个任务
     * @param jobName
     */
    public static void removeJob(String jobName) {
        try {
            ApplicationContext context = ApplicationContextListener.getApplicationContext();
            StdScheduler scheduler = (StdScheduler) context.getBean("quartzScheduler");
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobName));// 停止触发器
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobName));// 移除触发器
            scheduler.deleteJob(JobKey.jobKey(jobName));// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 移除一个任务
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     */
    public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            ApplicationContext context = ApplicationContextListener.getApplicationContext();
            StdScheduler scheduler = (StdScheduler) context.getBean("quartzScheduler");
            scheduler.pauseTrigger(TriggerKey.triggerKey(triggerName, triggerGroupName));// 停止触发器
            scheduler.unscheduleJob(TriggerKey.triggerKey(triggerName, triggerGroupName));// 移除触发器
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 启动所有定时任务
     * @param sched
     */
    public static void startJobs(Scheduler sched) {
        try {
            sched.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭所有定时任务
     * @param sched
     */
    public static void shutdownJobs(Scheduler sched) {
        try {
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
