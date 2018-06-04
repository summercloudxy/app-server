package com.zgiot.app.server.module.sfstop.mapper;


import com.zgiot.app.server.module.sfstop.entity.pojo.StopManualInterventionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StopManualInterventionRecordMapper {

    /**
     * 查询启车的人工干预解除记录
     *
     * @param operateId
     * @param thingCode
     * @return
     */
    @Select("select * from tb_stop_manual_intervention_record WHERE operate_id =#{operateId} and thing_code=#{thingCode}")
    StopManualInterventionRecord getStopManualInterventionRecord(@Param("operateId") int operateId, @Param("thingCode") String thingCode);


}
