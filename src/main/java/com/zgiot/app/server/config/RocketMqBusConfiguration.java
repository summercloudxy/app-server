package com.zgiot.app.server.config;

import com.zgiot.app.server.config.prop.DataEngineProperties;
import com.zgiot.app.server.config.prop.RocketMqProperties;
import com.zgiot.app.server.dataprocessor.RocketMqDataProcessor;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({DataEngineProperties.class,
        RocketMqProperties.class})
public class RocketMqBusConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(RocketMqBusConfiguration.class);
    @Autowired
    private DataEngineProperties dae;
    @Autowired
    private RocketMqProperties rocketMqProperties;

    @Bean(name = "rocketMqProcessor")
    public RocketMqDataProcessor rktmqProcessor() {
        if (DataEngineProperties.CONNECTION_MODE_ROCKETMQ.equals(dae.getConnectionMode())) {
            return new RocketMqDataProcessor(this.rocketMqProperties);
        } else {
            return null;
        }
    }

    @Bean
    public DefaultMQPushConsumer newDefaultMQPushConsumer() {
        if (!rocketMqProperties.isEnabled()) {
            return null;
        }

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(rocketMqProperties.getConsumerGroup());
        consumer.setNamesrvAddr(rocketMqProperties.getNameservers());
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        return consumer;
    }

}

