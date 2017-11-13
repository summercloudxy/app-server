package com.zgiot.app.server.module.bellows.dao;

import com.zgiot.app.server.module.bellows.compressor.pojo.CompressorLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author wangwei
 */
@Mapper
public interface BellowsMapper {

    /**
     * 查询bellows配置
     * @param thingCode
     * @param paramName
     * @return
     */
    Double selectParamValue(@Param("thingCode") String thingCode, @Param("paramName") String paramName);

    /**
     * 修改bellows配置
     * @param thingCode
     * @param paramName
     * @param paramValue
     */
    void updateParamValue(@Param("thingCode") String thingCode, @Param("paramName") String paramName,
                                @Param("paramValue") Double paramValue);


    /**
     * 保存空压机日志
     * @param compressorLog
     */
    void saveCompressorLog(@Param("log")CompressorLog compressorLog);

    /**
     * 更新空压机日志，确认状态
     * @param compressorLog
     */
    void updateCompressorLog(@Param("log") CompressorLog compressorLog);

    /**
     * 查询空压机操作日志
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param offset    偏移量
     * @param count 个数
     * @return
     */
    List<CompressorLog> getCompressorLog(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("offset") Integer offset, @Param("count") Integer count);
}
