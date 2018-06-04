package com.zgiot.app.server.service.dao;

import com.zgiot.app.server.service.pojo.RelSendTraceInfo;
import com.zgiot.app.server.service.pojo.SendTraceLog;
import com.zgiot.app.server.service.pojo.SendType;
import com.zgiot.app.server.service.pojo.SendTypeShowContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface SendTraceLogMapper {
    void insertSendTraceLog(SendTraceLog sendInfo);
    List<RelSendTraceInfo> getRelSendTraceInfo();
    List<SendTraceLog> getSendTraceLogList(@Param("condition") SendTraceLog condition,@Param("startTime") Date startTime,@Param("endTime") Date endTime);
    @Select(value = "select * from tb_send_type")
    List<SendType> getSendTypeList();
    @Select(value = "select * from tb_send_type_show_content")
    List<SendTypeShowContent> getSendTypeShowContentList();
}
