package com.zgiot.app.server.module.filterpress.dao;

import com.zgiot.app.server.module.filterpress.pojo.FeedOverParam;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressElectricity;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xiayun on 2017/9/12.
 */
@Mapper
public interface FilterPressMapper {


    void updateFilterParamValue(@Param("thingCode") String thingCode, @Param("paramName") String paramName,
            @Param("paramValue") Boolean paramValue);

    @MapKey("deviceCode")
    Map<String, FilterPressElectricity> getCurrentInfoInDuration(Date startTime, Date endTime);
}
