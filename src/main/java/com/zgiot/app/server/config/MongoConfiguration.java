package com.zgiot.app.server.config;

import com.mongodb.*;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.CreateViewOptions;
import com.zgiot.app.server.config.prop.MongoDBProperties;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableConfigurationProperties(MongoDBProperties.class)
@EnableAutoConfiguration
public class MongoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(MongoConfiguration.class);
    @Autowired
    private MongoDBProperties properties;

    @Bean
    public MongoClient mongoClient() {
        if (properties.getEnable()) {
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
        logger.info("mongo disabled, didn't initialize");
        return null;
    }

    @Bean
    public MongoDatabase database() {
        if (properties.getEnable()) {
            return mongoClient().getDatabase(properties.getDbName());
        } else {
            return new MongoDatabase() {
                @Override
                public String getName() {
                    logger.warn("mongo disabled");
                    return null;
                }

                @Override
                public CodecRegistry getCodecRegistry() {
                    logger.warn("mongo disabled");
                    return null;
                }

                @Override
                public ReadPreference getReadPreference() {
                    logger.warn("mongo disabled");
                    return null;
                }

                @Override
                public WriteConcern getWriteConcern() {
                    logger.warn("mongo disabled");
                    return null;
                }

                @Override
                public ReadConcern getReadConcern() {
                    logger.warn("mongo disabled");
                    return null;
                }

                @Override
                public MongoDatabase withCodecRegistry(CodecRegistry codecRegistry) {
                    logger.warn("mongo disabled");
                    return null;
                }

                @Override
                public MongoDatabase withReadPreference(ReadPreference readPreference) {
                    logger.warn("mongo disabled");
                    return null;
                }

                @Override
                public MongoDatabase withWriteConcern(WriteConcern writeConcern) {
                    logger.warn("mongo disabled");
                    return null;
                }

                @Override
                public MongoDatabase withReadConcern(ReadConcern readConcern) {
                    logger.warn("mongo disabled");
                    return null;
                }

                @Override
                public MongoCollection<Document> getCollection(String s) {
                    logger.warn("mongo disabled");
                    return null;
                }

                @Override
                public <TDocument> MongoCollection<TDocument> getCollection(String s, Class<TDocument> aClass) {
                    logger.warn("mongo disabled");
                    return null;
                }

                @Override
                public Document runCommand(Bson bson) {
                    logger.warn("mongo disabled");
                    return null;
                }

                @Override
                public Document runCommand(Bson bson, ReadPreference readPreference) {
                    logger.warn("mongo disabled");
                    return null;
                }

                @Override
                public <TResult> TResult runCommand(Bson bson, Class<TResult> aClass) {
                    logger.warn("mongo disabled");
                    return null;
                }

                @Override
                public <TResult> TResult runCommand(Bson bson, ReadPreference readPreference, Class<TResult> aClass) {
                    logger.warn("mongo disabled");
                    return null;
                }

                @Override
                public void drop() {
                    logger.warn("mongo disabled");
                }

                @Override
                public MongoIterable<String> listCollectionNames() {
                    logger.warn("mongo disabled");
                    return null;
                }

                @Override
                public ListCollectionsIterable<Document> listCollections() {
                    logger.warn("mongo disabled");
                    return null;
                }

                @Override
                public <TResult> ListCollectionsIterable<TResult> listCollections(Class<TResult> aClass) {
                    logger.warn("mongo disabled");
                    return null;
                }

                @Override
                public void createCollection(String s) {
                    logger.warn("mongo disabled");
                }

                @Override
                public void createCollection(String s, CreateCollectionOptions createCollectionOptions) {
                    logger.warn("mongo disabled");
                }

                @Override
                public void createView(String s, String s1, List<? extends Bson> list) {
                    logger.warn("mongo disabled");
                }

                @Override
                public void createView(String s, String s1, List<? extends Bson> list, CreateViewOptions createViewOptions) {
                    logger.warn("mongo disabled");
                }
            };
        }
    }
}
