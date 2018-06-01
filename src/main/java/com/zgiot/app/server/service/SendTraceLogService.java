package com.zgiot.app.server.service;

import com.zgiot.app.server.dataprocessor.DataListener;
import com.zgiot.app.server.service.pojo.SendInfo;
import com.zgiot.app.server.service.pojo.SendTraceLog;
import com.zgiot.app.server.service.pojo.SendType;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SendTraceLogService extends DataListener{
    void recordSendTraceLog(SendInfo sendInfo);
    Map<String,SendTraceLog> getSendInfoWithoutFeedback();
    List<SendTraceLog> getSendTraceLogList(SendTraceLog condition, Date startTime, Date endTime);
    List<SendType> getSendTypeList();
    void init();
}
