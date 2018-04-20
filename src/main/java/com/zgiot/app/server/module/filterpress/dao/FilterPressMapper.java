package com.zgiot.app.server.module.filterpress.dao;

import com.zgiot.app.server.module.filterpress.pojo.FilterPressConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xiayun on 2017/9/12.
 */
@Mapper
public interface FilterPressMapper {
    Double selectParamValue(@Param("thingCode") String thingCode, @Param("paramName") String paramName);

    void updateFilterParamValue(@Param("thingCode") String thingCode, @Param("paramName") String paramName,
                                @Param("paramValue") Double paramValue);

    List<FilterPressConfig> findFilterInfo(FilterPressConfig filterPressConfig);

//    @MapKey("deviceCode")
//    Map<String, FilterPressElectricity> getCurrentInfoInDuration(Date startTime, Date endTime);
}
