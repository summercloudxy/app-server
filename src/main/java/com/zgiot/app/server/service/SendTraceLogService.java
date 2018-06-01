package com.zgiot.app.server.service;

import com.github.pagehelper.PageInfo;
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
    PageInfo<SendTraceLog> getSendTraceLogList(SendTraceLog condition, Date startTime, Date endTime, Integer page, Integer count);
    List<SendType> getSendTypeList();
    void init();
}
