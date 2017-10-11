package com.zgiot.app.server.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.zgiot.app.server.config.prop.MongoDBProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableConfigurationProperties(MongoDBProperties.class)
@EnableAutoConfiguration
public class MongoConfiguration {
    @Autowired
    private MongoDBProperties properties;

    @Bean
    public MongoClient mongoClient() {
        String username = properties.getUsername();
        String url = properties.getUrl();
        Integer port = properties.getPort();
        if (username != null) {
            MongoCredential credential = MongoCredential.createCredential(username, properties.getDbName(), properties.getPassword());
            return new MongoClient(new ServerAddress(url, port), Collections.singletonList(credential));
        } else {
            return new MongoClient(url, port);
        }

    }

    @Bean
    public MongoDatabase database() {
        return mongoClient().getDatabase(properties.getDbName());
    }
}
