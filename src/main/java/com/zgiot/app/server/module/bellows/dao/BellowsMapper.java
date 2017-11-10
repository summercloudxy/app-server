package com.zgiot.app.server.module.bellows.dao;

import com.zgiot.app.server.module.bellows.compressor.pojo.CompressorLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
