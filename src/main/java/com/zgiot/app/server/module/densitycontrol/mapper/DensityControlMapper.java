package com.zgiot.app.server.module.densitycontrol.mapper;

import com.zgiot.app.server.module.densitycontrol.pojo.MonitoredThing;
import com.zgiot.app.server.module.densitycontrol.pojo.NotifyInterval;
import com.zgiot.app.server.module.densitycontrol.pojo.NotifyThingMetricInfo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface DensityControlMapper {
    @MapKey("thingCode")
    Map<String, NotifyInterval> getNotifyIntervalByNotifyType(String notifyType);
    @MapKey("thingCode")
    Map<String, MonitoredThing> getMonitoredThingMap(String module);
    @MapKey("notifyThingCode")
    Map<String, NotifyThingMetricInfo> getNotifyInfo();

}
