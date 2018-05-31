package com.zgiot.app.server.module.sfstop.mapper;

import com.zgiot.app.server.module.alert.pojo.AlertData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StopAlertDataMapper {
    /**
     * 查询某个设备级别最高的告警信息
     *
     * @param thingCode
     * @return
     */
    @Select("SELECT * from tb_alert_data where thing_code =#{thingCode} and alert_stage!='RELEASE' ORDER BY alert_datetime DESC,alert_level DESC LIMIT 1")
    AlertData getMaxLevelAlertData(@Param("thingCode") String thingCode);

}
                                                  