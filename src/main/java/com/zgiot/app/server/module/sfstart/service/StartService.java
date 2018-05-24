package com.zgiot.app.server.module.sfstart.service;

import com.zgiot.app.server.module.sfstart.pojo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface StartService {


    /**
     * 查询未完成启车任务
     *
     * @param startState
     * @param finishState
     * @return
     */
    List<StartOperationRecord> findUnfinishStartOperate(Integer startState, Integer finishState);


    /**
     * 根据期数和类型和系统所属页面查询系统
     *
     * @param productionLine
     * @param type
     * @return
     */
    List<StartSystem> getStartSystem(Integer productionLine, Integer type, int systemCategory);

    /**
     * 查询启车中系统
     *
     * @param systemCategory
     * @return
     */
    List<StartSystem> getStartSystemWithStarting(int systemCategory);

    /**
     * 增加启车中系统与本次启车设备的关系
     *
     * @param startSystems
     */
    void getStartinSystemWithDeviceInformation(List<StartSystem> startSystems);

    /**
     * 获取operateId
     *
     * @return
     */
    Integer findOperateIdWhenNull();

    /**
     * 获取启车中设备状态
     *
     * @return
     */
    List<StartDeviceStateRecord> getStartDeviceState();

    /**
     * 根据deviceId获取异常原因信息
     *
     * @param deviceId
     * @return
     */
    String getFaultByDeviceId(String deviceId);

    /**
     * 关闭启车操作
     */
    void closeStartOperate();

    /**
     * 判断当前启车状态
     *
     * @param startState  查询状态开始
     * @param finishState 查询状态结尾
     * @return
     */
    boolean judgeStartingState(Integer startState, Integer finishState);

    /**
     * 判断启车检查是否所有必要检查项都合格
     *
     * @return
     */
    boolean isfullyExamine();

    /**
     * 根据系统类型，返回设备信息列表
     *
     * @param systemIds 系统信息
     * @return
     */
    List<StartDevice> getDeviceBySystenIds(List<String> systemIds);

    /**
     * 查询公用系统
     *
     * @param systemIds 现选中系统
     * @return
     */
    List<String> findSynergicSystems(List<String> systemIds);

    /**
     * 返回所有的label信息
     *
     * @param deviceIds
     * @param name
     * @return
     */
    List<String> getLabelBydeviceIdsAndName(Set<String> deviceIds, String name);

    /**
     * 修改启车状态(10:启车检查15:清除信号发送成功,20:信号发送中，25:启车信号发送成功，30:正在启车，40:完成启车)
     *
     * @param state
     */
    void updateStartOperate(Integer state);

    /**
     * 获取系统下设备的操作顺序
     *
     * @param operateId 本次启车操作id
     * @return 返回设备信息
     */
    List<StartDeviceInformation> selectDeviceInformationBySystemId(Integer operateId, Integer ruleId);

    /**
     * 查询设备启动规则
     *
     * @param deviceId 设备id
     * @return
     */
    List<StartRequirement> getRequirementByDeviceId(String deviceId);


    /**
     * 根据设备查询检查规则
     *
     * @param deviceIds
     * @return
     */
    List<StartExamineRule> getStartExamineRuleByDeviceIds(Set<String> deviceIds);

    /**
     * 获取系统设备对应信息标签
     *
     * @param deviceId 设备id
     * @param name     查询name
     * @return
     */
    List<String> selectLabelByDeviceIdAndName(String deviceId, String name);

    /**
     * 根据设备信息查询人工干预状态
     *
     * @param deviceId
     * @return
     */
    List<StartManualInterventionDevice> selectManualInterventionStateByDeviceId(String deviceId);

    /**
     * 保存人工自检记录
     *
     * @param startManualInterventionRecord
     */
    void saveManualInterventionRecord(StartManualInterventionRecord startManualInterventionRecord);

    /**
     * 修改人工干预设置
     *
     * @param deviceId
     * @param userId
     * @param state
     * @param beforeState
     */
    void updateZgkwStartManualIntervention(String deviceId, String userId, Integer state, Integer beforeState);

    /**
     * 启车区域检查任务建立
     *
     * @param operateId
     */
    void setUpAreaExamine(Integer operateId);

    /**
     * 增加启车中设备状态记录
     *
     * @param operateId      对应启车操作id
     * @param startDeviceIds 启车设备id
     * @return
     */
    void saveDeviceStateRecord(Set<String> startDeviceIds, Integer operateId);

    /**
     * 获得总览页面启车系统信息根据类型
     *
     * @return
     * @throws Exception
     */
    List<StartSystem> getStartBrowseSystem();

    /**
     * 获得总览页面启车系统信息根据类型
     *
     * @return
     * @throws Exception
     */
    List<StartSystem> getStartBrowseDeprotSystem();

    /**
     * 测试用启车信息获取
     *
     * @return
     */
    List<StartDevice> testGetDevice();

    /**
     * 保存新的启车操作记录检查开始
     *
     * @param systemIds 操作选择系统
     * @param userId    操作人id
     */
    StartOperationRecord saveStartOperationRecord(List<String> systemIds, String userId);

    /**
     * 建立启车自检
     *
     * @param deviceIds
     */
    void setUpAutoExamine(Set<String> deviceIds, Integer operateId);

    /**
     * 本次启车自检结果反馈
     *
     * @return
     */
    List<StartExamineRecord> getAutoExamineRecord();

    /**
     * 根据返回信号获取对应规则
     *
     * @param label
     * @return
     */
    List<StartExamineRule> getStartExaminRuleByRuleIdAndLabel(Integer ruleId, String label);

    /**
     * 获得启车系统信息根据类型
     *
     * @return
     * @throws Exception
     */
    List<StartSystem> getAutoExamineSystem();


    /**
     * 增加自检中中系统与本次启车设备的关系
     *
     * @param startSystems
     */
    void getAutoExamineSystemWithDeviceInformation(List<StartSystem> startSystems);

    /**
     * 获取启车可设置范围
     *
     * @param deviceCode
     */
    List<StartManualInterventionRecord> getManualInterventionScopeStart(String deviceCode);


    /**
     * 修改启车中人工干预记录
     *
     * @param deviceId             人工干预设备
     * @param operateId            启车操作id
     * @param state                修改状态
     * @param interventionPersonId 干预设置人
     * @param relievePersonId      干预解除人
     */
    void updateStartManualInterventionRecord(String deviceId, Integer operateId, Integer state, String interventionPersonId, String relievePersonId);

    /**
     * 获取设备人工干预中显示信息
     *
     * @param deviceId
     * @return
     */
    StartManualInterventionRecord getManualInterventionDeviceInformation(String deviceId);

    /**
     * 反馈当前设置的干预项
     *
     * @return
     */
    List<StartManualInterventionRecord> getManualInterventionRecord();

    /**
     * 获取启车包的信息
     *
     * @return
     */
    List<StartPackage> getStartingPackage();

    /**
     * 获取包中参与本次启车的设备
     *
     * @param startPackages
     */
    void getStartinPackageWithDeviceInformation(List<StartPackage> startPackages);


    /**
     * 根据packageId和deviceId获取设备对应关系
     *
     * @param packageId
     * @return
     */
    List<StartDeviceRelation> selectStartDeviceRelationByPackageIdAndDeviceId(String packageId, String deviceId);

    /**
     * 启车中人工干预信息反馈
     *
     * @param deviceId
     * @param operateId
     * @param state
     * @return
     */
    List<StartManualInterventionRecord> selectStartingManualInterventionRecord(String deviceId, Integer operateId, Integer state);

    /**
     * 根据系统类型，返回设备信息列表
     *
     * @param systemIds 系统信息
     * @return
     */
    Set<String> getAllDeviceIdBySystenIds(List<String> systemIds);


    /**
     * 查询启车设备id根据系统类型
     *
     * @param systemCategory
     * @return
     */
    List<String> selectStartDeviceIdBySystemCategory(Integer systemCategory, Integer systemType);

    /**
     * 根据设备提取StartSignal
     *
     * @param deviceId
     * @return
     */
    StartSignal getStartSignalByDeviceId(@Param("deviceId") String deviceId);


    /**
     * 根据设备id获取设备信息
     *
     * @param deviceId
     * @return
     */
    StartDevice selectStartDeviceByDeviceId(String deviceId);

    /**
     * 获取变压器信息
     *
     * @return
     */
    List<StartSingleLabelAndValue> selectTransformerInformation(String labelName);

    /**
     * 根据规则查询本次启车检查记录
     *
     * @param ruleId
     * @param operateId
     * @return
     */
    List<StartExamineRecord> getStartExaminRecordByRuleAndOperateId(Integer ruleId, Integer operateId);

    /**
     * 修改启车检查记录
     *
     * @param ruleId
     * @param operateId
     * @param examineResult
     * @param examineInformation
     */
    void updateStartExamineRecord(Integer ruleId, Integer operateId, Integer examineResult, String examineInformation);

    /**
     * @param id
     * @return
     */
    StartDeviceSignal getStartDeviceSignalById(int id);


    /**
     * 获取包延时信息
     *
     * @return
     */
    List<StartSingleLabelAndValue> selectPackageWaitTime(String deviceCodePart, String name);


    /**
     * 获取包所属区域
     *
     * @return
     */
    List<StartSingleLabelAndValue> selectPackageBelongArea(String deviceCodePart, String name);

    /**
     * 获取包所属大区
     *
     * @return
     */
    List<StartSingleLabelAndValue> selectPackageBelongRegion(String deviceCodePart, String name);


    /**
     * 获取区域所属大区
     *
     * @return
     */
    List<StartSingleLabelAndValue> selectAreaBelongRegion(String deviceCodePart, String name);

    /**
     * 根据信号查询deviceId
     *
     * @param datalabel
     * @return
     */
    List<String> selectDeviceIdByDatelabel(String datalabel, String labelName);

    /**
     * 修改启车状态
     *
     * @param operateId 本次操作记录
     * @param deviceId  修改设备id
     * @param value     修改值
     */
    void updateStartDeviceState(Integer operateId, String deviceId, Integer value);

    /**
     * 获取频率发送label
     *
     * @return
     */
    StartSingleLabelAndValue selectFrequency(String deviceId, String name, String startType, Integer type);


    /**
     * 获取启车区域启动规则
     *
     * @param parentState
     * @param areaFirstId
     * @return
     */
    List<StartAreaRule> selectAreaRuleByParentStateAndAreaFirstId(Integer parentState, Integer areaFirstId);


    /**
     * 修改区域检查记录
     *
     * @param operateId
     * @param state
     * @param areaRuleId
     */
    void updateStartAreaRecord(Integer operateId, Integer state, Integer areaRuleId);

    /**
     * 获取启车区域启动规则实现记录
     *
     * @param state
     * @param areaSecondId
     * @return
     */
    List<Integer> selectAreaRuleRecordByStateAndAreaSecondId(Integer operateId, Integer state, Integer areaSecondId);


    /**
     * 获取区域启动label
     *
     * @return
     */
    StartSingleLabelAndValue selectAreaStartLabel(String deviceCodePart, Integer number, String name);

    /**
     * @param dataLabel
     * @return
     */
    ThingMetricCode getStartSignalByDataLabel(String dataLabel);


}
