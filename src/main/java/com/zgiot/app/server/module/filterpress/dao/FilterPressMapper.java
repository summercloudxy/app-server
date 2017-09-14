package com.zgiot.app.server.module.filterpress.dao;

import com.zgiot.app.server.module.filterpress.pojo.FeedOverParam;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressElectricity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by xiayun on 2017/9/12.
 */
@Mapper
public interface FilterPressMapper {
    void setFilterFeedOverAutoMaunState(@Param("thingCode") String thingCode, @Param("state") Integer state);

    void setFilterFeedOverIntelligentMaunState(@Param("thingCode") String deviceCode, @Param("state") Integer state);

    List<FeedOverParam> getFilterFeedOverParams();

    List<FilterPressElectricity> getCurrentInfoInDuration(Date startTime, Date endTime);
}
