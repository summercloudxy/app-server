package com.zgiot.app.server.module.sfmonitor.mapper;

import com.zgiot.app.server.module.sfmonitor.pojo.SFMonSignalWrapperRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SFMonSignalWrapperRuleMapper {
    SFMonSignalWrapperRule getSignalWrapperRule(SFMonSignalWrapperRule sfMonSignalWrapperRule);

    void updateSignalWrapperRule(@Param("signalWrapperName") String signalWrapperName,@Param("zoneCode") String zoneCode,@Param("id") int id );

    void addSignalWrapperRule(@Param("signalWrapperName") String signalWrapperName,@Param("zoneCode") String zoneCode );
}
