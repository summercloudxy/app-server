package com.zgiot.app.server.module.sfstart.mapper;

import com.zgiot.app.server.module.sfstart.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StartMapper {

    /**
     * 根据期数和类型查询数据状态
     *
     * @param productionLine
     * @param type
     * @return
     */
    List<StartSystem> selectSystem(@Param("productionLine") Integer productionLine, @Param("type") Integer type, @Param("systemCategory") Integer systemCategory);

    /**
     * 查询未完成的有效启车任务
     *
     * @param operateState 查询状态开始
     * @param finishState  查询状态结尾
     * @return
     */
    List<StartOperationRecord> selectOperateRecordWithoutOperateState(@Param("operateState") Integer operateState, @Param("finishState") Integer finishState);

    /**
     * 保存新的启车记录
     *
     * @param startOperationRecord
     */
    void saveStartOperationRecord(StartOperationRecord startOperationRecord);

    /**
     * 修改启车记录状态
     *
     * @param operateState
     * @param operateId
     */
    void updateOperateStateByOperateId(@Param("operateState") Integer operateState, @Param("operateId") Integer operateId);

    /**
     * 查询未完成的有效启车任务
     *
     * @param systemIds
     * @return
     */
    List<String> selectSynergicSystem(@Param("systemIds") List<String> systemIds);

    /**
     * 获取系统下的设备信息
     *
     * @param systemIds 设备id
     * @return 返回设备信息
     */
    List<StartDevice> selectDeviceBySystemId(@Param("systemIds") List<String> systemIds);

    /**
     * 保存设备启动记录
     *
     * @param startDeviceStateRecord
     */
    void saveStartDeviceStateRecord(StartDeviceStateRecord startDeviceStateRecord);

    /**
     * 获取系统下设备的操作顺序
     *
     * @param operateId 本次启车操作id
     * @return 返回设备信息
     */
    List<StartDeviceInformation> selectDeviceInformationBySystemId(@Param("operateId") Integer operateId, @Param("ruleId") Integer ruleId);

    /**
     * 获取系统设备对应信息标签
     *
     * @param deviceId 设备id
     * @param name     查询name
     * @return
     */
    List<String> selectLabelByDeviceIdAndName(@Param("deviceId") String deviceId, @Param("name") String name);

    /**
     * 获取启车前规则根据设备id
     *
     * @return
     */
    List<StartRequirement> selectRequirementByDeviceId(@Param("deviceId") String deviceId);

    /**
     * 获取变压器信息
     *
     * @return
     */
    List<StartSingleLabelAndValue> selectTransformerInformation(@Param("labelName") String labelName);

    /**
     * 根据信号查询deviceId
     *
     * @param datalabel
     * @return
     */
    List<String> selectDeviceIdByDatelabel(@Param("datalabel") String datalabel, @Param("labelName") String labelName);

    /**
     * 修改启车状态
     *
     * @param operateId 本次操作记录
     * @param deviceId  修改设备id
     * @param value     修改值
     */
    void updateStartDeviceState(@Param("operateId") Integer operateId, @Param("deviceId") String deviceId, @Param("value") Integer value);

    /**
     * 查询启车状态
     *
     * @param operateId 操作号
     * @param deviceId  设备id
     * @return
     */
    List<StartDeviceStateRecord> selectStartDeviceState(@Param("operateId") Integer operateId, @Param("deviceId") String deviceId);

    /**
     * 根据操作记录和系统查询启动设备信息
     *
     * @param operateId
     * @param systemId
     * @return
     */
    List<StartDevice> selectStartingDeviceBySystemIdAndOperateId(@Param("operateId") Integer operateId, @Param("systemId") Long systemId);

    /**
     * 获取启车最大序号
     *
     * @param operateId
     * @return
     */
    List<String> selectMaxSequence(@Param("operateId") Integer operateId);

    /**
     * 结束启车
     *
     * @param isDelete
     * @param operateId
     */
    void closeOperateStateByOperateId(@Param("isDelete") Integer isDelete, @Param("operateId") Integer operateId);

    /**
     * 查询启车中系统类型
     *
     * @param systemCategory
     * @return
     */
    List<StartSystem> selectSystemWithStarting(@Param("systemCategory") Integer systemCategory);

    /**
     * 测试用查询启车信息
     *
     * @return
     */
    List<StartDevice> selectTestDevice();

    /**
     * 查询启车设备id根据系统类型
     *
     * @param systemCategory
     * @return
     */
    List<String> selectStartDeviceIdBySystemCategory(@Param("systemCategory") Integer systemCategory, @Param("systemType") Integer systemType);

    /**
     * 根据设备id获取设备信息
     *
     * @param deviceId
     * @return
     */
    StartDevice selectStartDeviceByDeviceId(String deviceId);

    /**
     * 根据系统查询仓库信息
     *
     * @param systemId
     * @return
     */
    List<StartBrowseCoalDevice> selectStartBrowseCoalDeviceBySystemId(@Param("systemId") Long systemId);

    /**
     * 根据设备查询规则
     *
     * @param deviceId
     * @return
     */
    List<StartExamineRule> selectStartExamineRuleByDeviceId(@Param("deviceId") String deviceId);

    /**
     * 保存启车检查记录
     *
     * @param startExamineRecord
     */
    void saveStartExamineRecord(StartExamineRecord startExamineRecord);

    /**
     * 根据返回信号获取对应规则
     *
     * @param label
     * @return
     */
    List<StartExamineRule> getStartExaminRuleByRuleIdAndLabel(@Param("ruleId") Integer ruleId, @Param("label") String label);

    /**
     * 修改启车检查记录
     *
     * @param ruleId
     * @param operateId
     * @param examineResult
     * @param examineInformation
     */
    void updateStartExamineRecord(@Param("ruleId") Integer ruleId, @Param("operateId") Integer operateId, @Param("examineResult") Integer examineResult, @Param("examineInformation") String examineInformation);

    /**
     * 根据规则查询本次启车检查记录
     *
     * @param ruleId
     * @param operateId
     * @return
     */
    List<StartExamineRecord> getStartExaminRecordByRuleAndOperateId(@Param("ruleId") Integer ruleId, @Param("operateId") Integer operateId);

    /**
     * 根据自检设备号查询故障信息
     *
     * @param deviceId
     * @return
     */
    List<StartFaultInformation> selectFaultByExamineDeviceId(@Param("deviceId") String deviceId);

    /**
     * 查询本次启车必要错误
     *
     * @param operateId
     * @return
     */
    List<StartExamineRecord> selectFullyExamineErrorByOperateId(@Param("operateId") Integer operateId, @Param("examineType") Integer examineType);

    /**
     * 根据操作记录和系统查询自检设备信息
     *
     * @param operateId
     * @param systemId
     * @return
     */
    List<StartDevice> selectAutoExamineDeviceBySystemIdAndOperateId(@Param("operateId") Integer operateId, @Param("systemId") Long systemId);

    /**
     * 根据设备信息查询人工干预状态
     *
     * @param deviceId
     * @return
     */
    List<StartManualInterventionDevice> selectManualInterventionStateByDeviceId(@Param("deviceId") String deviceId);

    /**
     * 保存人工自检记录
     *
     * @param startManualInterventionRecord
     */
    void saveManualInterventionRecord(StartManualInterventionRecord startManualInterventionRecord);

    /**
     * 根据干预状态和模糊的设备id查询设启车前设定范围
     *
     * @param state
     * @param deviceCode
     * @return
     */
    List<StartManualInterventionRecord> selectManualInterventionScopeByLikeDeviceCodeAndState(@Param("state") Integer state, @Param("deviceCode") String deviceCode);

    /**
     * 修改人工干预设置
     *
     * @param deviceId
     * @param userId
     * @param state
     * @param beforeState
     */
    void updateZgkwStartManualIntervention(@Param("deviceId") String deviceId, @Param("userId") String userId, @Param("state") Integer state, @Param("beforeState") Integer beforeState);

    /**
     * 修改启车中人工干预记录
     *
     * @param deviceId             人工干预设备
     * @param operateId            启车操作id
     * @param state                修改状态
     * @param interventionPersonId 干预设置人
     * @param relievePersonId      干预解除人
     */
    void updateStartManualInterventionRecord(@Param("deviceId") String deviceId, @Param("operateId") Integer operateId, @Param("state") Integer state, @Param("interventionPersonId") String interventionPersonId, @Param("relievePersonId") String relievePersonId);

    /**
     * 根据干预状态和模糊的设备id查询启车中设定范围
     *
     * @param deviceCode
     * @return
     */
    List<StartManualInterventionRecord> selectStartingManualInterventionScopeByLikeDeviceCode(@Param("deviceCode") String deviceCode, @Param("operateId") Integer operateId);

    /**
     * 查询启车人工干预信息
     *
     * @param deviceId
     * @return
     */
    StartManualInterventionRecord selectManualInterventionInformation(@Param("deviceId") String deviceId);

    /**
     * 根据设备id获取设备信息
     *
     * @param deviceId 设备id
     * @return 返回设备信息
     */
    StartDeviceInformation selectDeviceInformationByDeviceId(@Param("deviceId") String deviceId);

    /**
     * 获取区域信息
     *
     * @param areaId
     * @return
     */
    StartAreaInformation selectAreaInformationByAreaId(@Param("areaId") String areaId);

    /**
     * 启车中人工干预信息反馈
     *
     * @param deviceId
     * @param operateId
     * @param state
     * @return
     */
    List<StartManualInterventionRecord> selectStartingManualInterventionRecord(@Param("deviceId") String deviceId, @Param("operateId") Integer operateId, @Param("state") Integer state);

    /**
     * 启车前获取人工干预设定信息
     *
     * @return
     */
    List<StartManualInterventionRecord> selectManualInterventionRecordByBefore();

    /**
     * 获取包延时信息
     *
     * @return
     */
    List<StartSingleLabelAndValue> selectPackageWaitTime(@Param("deviceCodePart") String deviceCodePart, @Param("name") String name);

    /**
     * 获取启车区域启动规则
     *
     * @param parentState
     * @param areaFirstId
     * @return
     */
    List<StartAreaRule> selectAreaRuleByParentStateAndAreaFirstId(@Param("parentState") Integer parentState, @Param("areaFirstId") Integer areaFirstId);

    /**
     * 保存区域检查记录
     *
     * @param areaRuleId
     * @param areaSecondId
     * @param operateId
     */
    void saveStartAreaRecord(@Param("areaRuleId") Integer areaRuleId, @Param("areaSecondId") Integer areaSecondId, @Param("operateId") Integer operateId);

    /**
     * 修改区域检查记录
     *
     * @param operateId
     * @param state
     * @param areaRuleId
     */
    void updateStartAreaRecord(@Param("operateId") Integer operateId, @Param("state") Integer state, @Param("areaRuleId") Integer areaRuleId);

    /**
     * 获取启车区域启动规则实现记录
     *
     * @param state
     * @param areaSecondId
     * @return
     */
    List<Integer> selectAreaRuleRecordByStateAndAreaSecondId(@Param("operateId") Integer operateId, @Param("state") Integer state, @Param("areaSecondId") Integer areaSecondId);

    /**
     * 获取包所属区域
     *
     * @return
     */
    List<StartSingleLabelAndValue> selectPackageBelongArea(@Param("deviceCodePart") String deviceCodePart, @Param("name") String name);

    /**
     * 获取包所属大区
     *
     * @return
     */
    List<StartSingleLabelAndValue> selectPackageBelongRegion(@Param("deviceCodePart") String deviceCodePart, @Param("name") String name);

    /**
     * 获取区域所属大区
     *
     * @return
     */
    List<StartSingleLabelAndValue> selectAreaBelongRegion(@Param("deviceCodePart") String deviceCodePart, @Param("name") String name);

    /**
     * 获取区域启动label
     *
     * @return
     */
    StartSingleLabelAndValue selectAreaStartLabel(@Param("deviceCodePart") String deviceCodePart, @Param("number") Integer number, @Param("name") String name);

    /**
     * 获取频率发送label
     *
     * @return
     */
    StartSingleLabelAndValue selectFrequency(@Param("deviceId") String deviceId, @Param("name") String name, @Param("startType") String startType, @Param("type") Integer type);

    /**
     * 获取包信息
     *
     * @return
     */
    List<StartPackage> selectStartingPackage();

    /**
     * 根据操作记录和包id查询启动设备信息
     *
     * @param operateId
     * @param packageId
     * @return
     */
    List<StartDevice> selectStartingDeviceByPackageIdAndOperateId(@Param("operateId") Integer operateId, @Param("packageId") String packageId);

    /**
     * 根据packageId和deviceId获取设备对应关系
     *
     * @param packageId
     * @return
     */
    List<StartDeviceRelation> selectStartDeviceRelationByPackageIdAndDeviceId(@Param("packageId") String packageId, @Param("deviceId") String deviceId);

    /**
     * 根据设备提取StartSignal
     *
     * @param deviceId
     * @return
     */
    @Select("select * from tb_start_signal where deviceId=#{deviceId}")
    List<StartSignal> getStartSignalByDeviceId(@Param("deviceId") String deviceId);

    /**
     * 查询老设备信号关系
     *
     * @param id
     * @return
     */
    @Select("select * from tb_start_device_signal where id=#{id}")
    StartDeviceSignal getStartDeviceSignalById(@Param("id") int id);

    /**
     * @param dataLabel
     * @return
     */
    @Select("select * from tb_start_signal where dataLabel=#{dataLabel}")
    StartSignal getStartSignalByDataLabel(@Param("dataLabel") String dataLabel);
}
