package com.zgiot.app.server.dataprocessor;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.config.prop.RocketMqProperties;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RocketMqDataProcessor implements DataProcessor {
    private static final Logger logger = LoggerFactory.getLogger(RocketMqDataProcessor.class);

    private RocketMqProperties p;
    private List<DataListener> listeners = new ArrayList<>();

    private ExecutorService executor = new ThreadPoolExecutor(40, 320,
            5000L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(100000), new ThreadFactory() {
        private AtomicInteger counter = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "RocketMq Processor Pool-" + counter.getAndIncrement());
        }
    });

    @Autowired
    DefaultMQPushConsumer consumer;
    @Autowired
    DataService dataService;

    public RocketMqDataProcessor(RocketMqProperties p) {
        this.p = p;
    }

    @Override
    public CompletableFuture<Void> connect() {
        CompletableFuture<Void> rtn = null;
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            if (msgs == null || msgs.size() == 0) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } else {
                String bodyJson = new String(msgs.get(0).getBody(), Charset.forName("UTF-8"));
                DataModel dataModel = JSON.parseObject(bodyJson, DataModel.class);

                if (logger.isTraceEnabled()) {
                    logger.trace("Got message from bus, body is `{}`", bodyJson);
                }

                if (StringUtils.isBlank(dataModel.getThingCode())) {
                    logger.warn("Bad data from bus. `{}`", dataModel);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }

                // ignore dup msg
                Optional<DataModelWrapper> dataW = this.dataService.
                        getData(dataModel.getThingCode(), dataModel.getMetricCode());
                if (dataW.isPresent() &&
                        dataW.get().getDataTimeStamp().getTime() == dataModel.getDataTimeStamp().getTime()) {
                    logger.warn("Dup data from bus. msg is `{}`", bodyJson);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; // mark it done
                }

                // exec listeners
                ProcessorUtil.handleMessage(dataModel, executor, this.listeners, logger);

            }

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        startConsumer();

        return null;
    }

    private boolean startConsumer() {
        try {
            consumer.start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> consumer.shutdown()));
        } catch (MQClientException e) {
            logger.error("RocketMq consumer failed to start. ", e);
            return true;
        }

        try {
            consumer.subscribe(p.getDatabusTopic(), "*");
        } catch (MQClientException e) {
            logger.error("Failed to subscribe topic . ", e);
        }
        return false;
    }

    @Override
    public CompletableFuture<Void> disconnect() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isConnected() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addListener(DataListener listener) {
        logger.info("DataListener added: `{}`", listener.getClass().getName());
        listeners.add(listener);
    }

    @Override
    public void removeListener(DataListener listener) {
        logger.info("DataListener removed: `{}`", listener.getClass().getName());
        listeners.remove(listener);
    }

}
