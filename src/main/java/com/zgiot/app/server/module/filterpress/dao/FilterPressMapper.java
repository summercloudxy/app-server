package com.zgiot.app.server.module.filterpress.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by xiayun on 2017/9/12.
 */
@Mapper
public interface FilterPressMapper {
    Double selectParamValue(@Param("thingCode") String thingCode, @Param("paramName") String paramName);

    void updateFilterParamValue(@Param("thingCode") String thingCode, @Param("paramName") String paramName,
                                @Param("paramValue") Double paramValue);

//    @MapKey("deviceCode")
//    Map<String, FilterPressElectricity> getCurrentInfoInDuration(Date startTime, Date endTime);
}
