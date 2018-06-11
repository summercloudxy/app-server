package com.zgiot.app.server.module.sfstop.mapper;


import com.zgiot.app.server.module.alert.pojo.AlertData;
import com.zgiot.app.server.module.sfstop.entity.pojo.*;
import com.zgiot.app.server.module.sfstop.entity.vo.StopExamineResult;
import org.apache.ibatis.annotations.*;

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
     * @param thingCode
     * @return
     */
    List<StopExamineRule> selectStopExamineRuleByThingCode(@Param("thingCode") String thingCode);

    /**
     * 结束停车
     *
     * @param isDelete
     * @param operateId
     */
    void closeOperateStopByOperateId(@Param("isDelete") int isDelete, @Param("operateId") Integer operateId);

    /**
     * 修改启车中人工干预记录
     *
     * @param deviceId          人工干预设备
     * @param operateId         启车操作id
     * @param interventionState 修改状态
     * @param relievePersonId   干预解除人
     */
    void updateStopManualInterventionRecord(@Param("thingCode") String deviceId, @Param("operateId") Integer operateId, @Param("interventionState") Integer interventionState, @Param("relievePersonId") String relievePersonId);

    /**
     * 修改停车记录状态
     *
     * @param operateState
     * @param operateId
     */
    void updateOperateStateByOperateId(@Param("operateState") Integer operateState, @Param("operateId") Integer operateId);


    /**
     * 根据规则查询本次停车检查记录
     *
     * @param operateId
     * @return
     */
    @Select("SELECT t.id as line_id,t.line_name,t.thing_name,t.thing_code,ser.examine_type,se.type_name,ser.examine_information as examine_info from " +
            " tb_stop_examine_record  ser left join (select si.thing_code,si.thing_name,sl.id,sl.line_name from tb_stop_information si LEFT JOIN " +
            "tb_stop_device_bag sdb on si.bag_id=sdb.id LEFT JOIN tb_stop_line sl on sl.id =sdb.stop_line_id) t on t.thing_code=ser.stop_thing_code " +
            " left join tb_stop_examine_type se on se.id=ser.examine_type WHERE ser.is_delete =0 and ser.examine_result =2 and ser.operate_id =#{operateId}")
    List<StopExamineResult> getStartExaminRecordByOperateId(@Param("operateId") Integer operateId);

    /**
     * 查询停车自检的记录
     *
     * @param operateId
     * @param examineThingCode
     * @return
     */
    @Select("select * from tb_stop_examine_record where operate_id =#{operateId} and examine_thing_code =#{examineThingCode}")
    StopExamineRecord getStopExamineRecordByThingCode(@Param("operateId") Integer operateId, @Param("examineThingCode") String examineThingCode);

    /**
     * 查询停车自检规则
     *
     * @param ruleId
     * @return
     */
    @Select("select * from tb_stop_examine_rule WHERE rule_id =#{ruleId}")
    StopExamineRule getStopExamineRuleByRuleId(@Param("ruleId") int ruleId);

    /**
     * 更新自检的记录（液位确认的结果）
     *
     * @param examineResult
     * @param examineId
     */
    @Update("update tb_stop_examine_record set examine_result =#{examineResult} where examine_id =#{examineId} ")
    void updateStopExamineResult(@Param("examineResult") int examineResult, @Param("examineId") int examineId);


    /**
     * 根据规则查询本次停车检查记录
     *
     * @param ruleId
     * @param operateId
     * @return
     */
    @Select("SELECT * from tb_stop_examine_record WHERE rule_id = #{ruleId} and  operate_id =#{operateId}")
    List<StopExamineRecord> getStopExaminRecordByRuleAndOperateId(@Param("ruleId") Integer ruleId, @Param("operateId") Integer operateId);

    /**
     * 修改停车检查记录
     *
     * @param ruleId
     * @param stopOperateId
     * @param examineResult
     * @param examineInformation
     */
    @Update(" UPDATE tb_start_examine_record  SET examine_result = #{examineResult},  examine_information = #{examineInformation}   WHERE " +
            "  rule_id = #{ruleId}  AND operate_id = #{stopOperateId}")
    void updateStopExamineRecord(@Param("ruleId") Integer ruleId, @Param("stopOperateId") Integer stopOperateId, @Param("examineResult") Integer examineResult, @Param("examineInformation") String examineInformation);

    /**
     * 根据设备号查询人工干预设置
     *
     * @param thingCode
     * @return
     */
    @Select("select * from  tb_stop_manual_intervention where thingCode=#{thingCode}")
    StopManualIntervention getStopManualInterventionByThingCode(@Param("thingCode") String thingCode);


    /**
     * 保存人工自检记录
     *
     * @param stopManualInterventionRecord
     */
    @Insert(" INSERT INTO tb_stop_manual_intervention_record (operate_id, thing_code, intervention_state, intervention_person_id, create_time, is_delete) " +
            "        VALUES( #{operateId}, #{thingCode}, #{interventionState}, #{manualInterventionPerson}, now(), '0'  )")
    void saveManualInterventionRecord(StopManualInterventionRecord stopManualInterventionRecord);

    /**
     * 保存停车设备状态记录
     *
     * @param stopDeviceStateRecord
     */
    @Insert("INSERT tb_stop_device_state_record ( operate_id, thing_code, state, create_time, update_time, is_delete )  VALUES (#{operateId}, #{thing_code}, #{state},#{createTime}, #{updateTime}, '0')")
    void saveStopDeviceStateRecord(StopDeviceStateRecord stopDeviceStateRecord);
}
