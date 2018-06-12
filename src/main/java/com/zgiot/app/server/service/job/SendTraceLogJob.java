package com.zgiot.app.server.service.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.zgiot.app.server.service.SendTraceLogService;
import com.zgiot.app.server.service.pojo.SendInfo;
import com.zgiot.app.server.service.pojo.SendTraceLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@JobHandler(value = "SendTraceLogHandler")
public class SendTraceLogJob extends IJobHandler {
    @Autowired
    private SendTraceLogService sendTraceLogService;
    public static final Logger logger = LoggerFactory.getLogger(SendTraceLogJob.class);

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        Map<String, SendTraceLog> sendInfoWithoutFeedback = sendTraceLogService.getSendInfoWithoutFeedback();
        Set<String> sendKeys = sendInfoWithoutFeedback.keySet();
        Iterator<String> iterator = sendKeys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            SendTraceLog sendTraceLog = sendInfoWithoutFeedback.get(key);
            if (new Date().getTime() - sendTraceLog.getSendTime().getTime() > 5000) {
                sendInfoWithoutFeedback.remove(key);
                logger.debug("下发的信号thingCode:{},metricCode:{},user:{},platform:{}在5s内没有收到返回值，摒弃该条记录"
                        , sendTraceLog.getSendThingCode(), sendTraceLog.getSendMetricCode(), sendTraceLog.getUserName(), sendTraceLog.getPlatform());
            }
        }
        return ReturnT.SUCCESS;
    }
}
