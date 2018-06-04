package com.zgiot.app.server.module.sfstop.mapper;


import com.zgiot.app.server.module.alert.pojo.AlertData;
import com.zgiot.app.server.module.sfstop.entity.pojo.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StopMapper {

    /**
     * 查询最新的一条
     *
     * @param operateState
     * @return
     */
    @Select("select * from tb_start_operation_record where operate_state =#{operateState} ORDER BY created_time DESC LIMIT 1")
    StartOperationRecord getStartOperationRecord(@Param("operateState") Integer operateState);

    /**
     * 查询某个设备级别最高的故障告警信息
     *
     * @param thingCode
     * @return
     */
    @Select("SELECT * from tb_alert_data where thing_code =#{thingCode} and alert_type =0 and alert_stage!='RELEASE' ORDER BY alert_datetime DESC,alert_level DESC LIMIT 1")
    AlertData getMaxLevelAlertData(@Param("thingCode") String thingCode);

    /**
     * 保存停车方案设置
     *
     * @param stopChoiceSet
     */
    @Insert("INSERT INTO tb_stop_choice_set (raw_stop_set,tcs_stop_set,filterpress_stop_set,belt_route,create_user,create_time,update_user,update_time) VALUES(#{rawStopSet},#{tcsStopSet},#{filterpressStopSet},#{beltRoute}，#{createUser}，#{createTime},#{updateUser},#{updateTime} )")
    void saveStopChoiceSet(StopChoiceSet stopChoiceSet);


    /**
     * 根据设备查询规则
     *
     * @param deviceIds
     * @return
     */
    List<StopExamineRule> selectStartExamineRuleByDeviceId(@Param("deviceIds") List<String> deviceIds);

    /**
     * 查询未完成的有效启车任务
     *
     * @param operateState 查询状态开始
     * @param finishState  查询状态结尾
     * @return
     */
    List<StopOperationRecord> selectOperateRecordWithoutOperateState(@Param("system") Integer system, @Param("operateState") Integer operateState, @Param("finishState") Integer finishState);


    /**
     * 保存新的停车记录
     *
     * @param stopOperationRecord
     */
    void saveStopOperationRecord(StopOperationRecord stopOperationRecord);

    /**
     * 根据操作状态查询停车记录
     *
     * @param operateState
     * @return
     */
    @Select("select * from tb_stop_operation_record where is_delete=0 and operate_state =#{operateState}")
    StopOperationRecord getStopOperationRecordByOperateState(@Param("operateState") int operateState);

    /**
     * 保存停车检查记录
     *
     * @param stopExamineRecord
     */
    void saveStartExamineRecord(StopExamineRecord stopExamineRecord);

    /**
     * 查询启车的人工干预解除记录
     *
     * @param operateId
     * @param thingCode
     * @return
     */
    @Select("select * from tb_stop_manual_intervention_record WHERE operate_id =#{operateId} and thing_code=#{thingCode}")
    StopManualInterventionRecord getStopManualInterventionRecord(@Param("operateId") int operateId, @Param("thingCode") String thingCode);

    /**
     * 查询某条启车线下的人工干预解除记录
     *
     * @param lineId
     * @return
     */
    @Select("select * from tb_stop_manual_intervention_record smir LEFT JOIN (select thing_code from tb_stop_information WHERE bag_id in (select id from  tb_stop_line WHERE id =#{lineId} )" +
            ") t on  smir.thing_code=t.thing_code ")
    List<StopManualInterventionRecord> getStopManualInterventionRecordByLineId(@Param("lineId") Integer lineId);

    /**
     * 根据设备查询规则
     *
     * @param deviceId
     * @return
     */
    List<StopExamineRule> selectStopExamineRuleByDeviceId(@Param("deviceId") String deviceId);
}
