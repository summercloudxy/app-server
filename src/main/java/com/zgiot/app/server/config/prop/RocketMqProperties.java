package com.zgiot.app.server.config.prop;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rocketmq.sf")
public class RocketMqProperties {

    private boolean enabled;
    @Value("${rocketmq.sf.consumergroup}")
    private String consumerGroup;
    @Value("${rocketmq.sf.nameservers}")
    private String nameservers;
    @Value("${rocketmq.sf.databus-topicname}")
    private String databusTopic;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public String getNameservers() {
        return nameservers;
    }

    public void setNameservers(String nameservers) {
        this.nameservers = nameservers;
    }

    public String getDatabusTopic() {
        return databusTopic;
    }

    public void setDatabusTopic(String databusTopic) {
        this.databusTopic = databusTopic;
    }
}
