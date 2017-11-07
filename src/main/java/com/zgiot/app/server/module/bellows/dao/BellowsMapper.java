package com.zgiot.app.server.module.bellows.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
