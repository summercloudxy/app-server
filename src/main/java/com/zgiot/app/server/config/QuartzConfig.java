package com.zgiot.app.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {

    @Bean(name = "quartzScheduler")
    public SchedulerFactoryBean schedulerFactory(){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean ();
        return schedulerFactoryBean;
    }
}
