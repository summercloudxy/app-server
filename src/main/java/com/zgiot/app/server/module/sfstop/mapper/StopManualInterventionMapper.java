package com.zgiot.app.server.module.sfstop.mapper;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopManualIntervention;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 停车人工干预
 */
@Mapper
public interface StopManualInterventionMapper {

    /**
     * 根据thingcode查询人工干预记录
     *
     * @param thingCode
     * @return
     */
    @Select("SELECT * from tb_stop_manual_intervention where thing_code =#{thingCode}")
    StopManualIntervention getStopManualInterventionByThingCode(@Param("thingCode") String thingCode);

    /**
     * 更新人工干预的状态
     *
     * @param state
     * @param thingCode
     */
    @Update("UPDATE tb_stop_manual_intervention set state=#{state} where thing_code =#{thingCode}")
    void updateStopManualInterventionState(@Param("state") Integer state, @Param("thingCode") String thingCode);

    @Update("update tb_stop_manual_intervention set state=#{state},update_user=#{updateUser},update_time=#{updateTime} where thing_code=#{thingCode}")
    void updateStopManualInterventionByTC(StopManualIntervention stopManualIntervention);

}
                                                  