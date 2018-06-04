package com.zgiot.app.server.module.reportforms.output.productionmonitor.listener;

import com.zgiot.app.server.dataprocessor.DataListener;
import com.zgiot.app.server.module.reportforms.output.productionmonitor.constant.ReportFormSystemStartConstant;
import com.zgiot.app.server.module.reportforms.output.productionmonitor.dao.ReportFormSystemStartMapper;
import com.zgiot.app.server.module.reportforms.output.productionmonitor.pojo.ReportFormSystemStartRecord;
import com.zgiot.app.server.module.reportforms.output.productionmonitor.pojo.StateThreshold;
import com.zgiot.app.server.module.reportforms.output.productionmonitor.util.BitOperationUtil;
import com.zgiot.app.server.module.reportforms.output.utils.ReportFormDateUtil;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ReportFormSystemStartListener implements DataListener {
    @Autowired
    private DataService dataService;
    @Autowired
    private ReportFormSystemStartMapper systemStartMapper;
    public static final Logger logger = LoggerFactory.getLogger(ReportFormSystemStartListener.class);

    /**
     * 判断一个系统启动的条件map
     * outer key: thingcode   inner key: metriccode  value: condition
     */
    private Map<String, Map<String, StateThreshold>> moduleStartupStateJudgeCondition = new HashMap<>();
    /**
     * outer key: module  inner key: term  value: state -- 该系统判断的设备按位存储
     * eg：浅槽系统需要判断1307/1308两个设备，如果1307满足条件1308不满足，state为01；都满足条件时，state为11
     * 按位存储tcs三个桶的状态， eg：1857-1：第1位  1857-2：第2位  1857-3：第3位  某位为1证明该位的桶处于开启状态
     */
    private Map<String, Map<Integer, Integer>> moduleStartupState = new HashMap<>();

    /**
     * 判断一个给煤机启动的条件map
     * outer key: thingcode   inner key: metriccode  value: condition
     */
    private Map<String, Map<String, StateThreshold>> coalFeederStartupStateJudgeCondition = new HashMap<>();
    /**
     * key: term
     * value: 按位存储每个给煤机的状态  eg: 1211 :第1位   1212：第2位  1213：第3位  1214：第4位   某位为1证明该位给煤机处于开启状态
     */
    private Map<Integer, Integer> coalFeederStartupState = new HashMap<>();

    /**
     * key : term
     * value: 系统开始停车时间，即全部给煤机停止的时间
     */
    private Map<Integer, Date> systemStopDateMap = new HashMap<>(2);

    /**
     * 记录整个系统当前的状态
     * key：term   value： 1 start  2 stop
     */
    private Map<Integer, Integer> systemStartupState = new HashMap<>(2);

    /**
     * 有几个系统启动以后认为整个系统开始启车
     */
    private static final int SYSTEM_START_MODULE_COUNT = 2;

    /**
     * 缓存一份当班的记录
     */
    private Map<Integer, List<ReportFormSystemStartRecord>> dutyRecords = new HashMap<>(2);


    public void init() {
        initStateJudgeCondition();
        initRecords(1);
        initRecords(2);
    }

    public Map<Integer, List<ReportFormSystemStartRecord>> getDutyRecords() {
        return dutyRecords;
    }

    /**
     * 初始化当班记录到缓存，并根据当班最后一条记录计算当前系统状态
     *
     * @param term
     */
    private void initRecords(Integer term) {
        Date nowDutyStartTime = ReportFormDateUtil.getNowDutyStartTime(new Date());
        List<ReportFormSystemStartRecord> recordsOnDutyTermOne = systemStartMapper.getRecordsOnDuty(nowDutyStartTime, term);
        dutyRecords.put(term, recordsOnDutyTermOne);
        if (CollectionUtils.isNotEmpty(recordsOnDutyTermOne)) {
            ReportFormSystemStartRecord theLastRecord = recordsOnDutyTermOne.get(recordsOnDutyTermOne.size() - 1);
            if ("/".equals(theLastRecord.getBlendingWashingType())) {
                systemStartupState.put(term, ReportFormSystemStartConstant.SYSTEM_STARTUP_STATE_STOP);
            } else {
                systemStartupState.put(term, ReportFormSystemStartConstant.SYSTEM_STARTUP_STATE_START);
            }
        }
    }

    /**
     * 初始化判断各个模块开启条件
     */
    private void initStateJudgeCondition() {
        List<StateThreshold> allModuleStateThreshold = systemStartMapper.getAllModuleStateThreshold();
        for (StateThreshold stateThreshold : allModuleStateThreshold) {
            Map<String, Map<String, StateThreshold>> startupStateJudgeConditionMap;
            if (ReportFormSystemStartConstant.COAL_FEEDER.equals(stateThreshold.getModule())) {
                startupStateJudgeConditionMap = coalFeederStartupStateJudgeCondition;
            } else {
                startupStateJudgeConditionMap = moduleStartupStateJudgeCondition;
            }
            Map<String, StateThreshold> metricStateThresholdMap;
            if (startupStateJudgeConditionMap.containsKey(stateThreshold.getThingCode())) {
                metricStateThresholdMap = startupStateJudgeConditionMap.get(stateThreshold.getThingCode());
            } else {
                metricStateThresholdMap = new HashMap<>();
                startupStateJudgeConditionMap.put(stateThreshold.getThingCode(), metricStateThresholdMap);
            }
            metricStateThresholdMap.put(stateThreshold.getMetricCode(), stateThreshold);
        }
    }

    @Override
    public void onDataChange(DataModel dataModel) {
        if (moduleStartupStateJudgeCondition.containsKey(dataModel.getThingCode())) {
            Map<String, StateThreshold> stateThresholdMetricMap = moduleStartupStateJudgeCondition.get(dataModel.getThingCode());
            if (stateThresholdMetricMap != null && stateThresholdMetricMap.containsKey(dataModel.getMetricCode())) {
                updateState(dataModel, stateThresholdMetricMap.get(dataModel.getMetricCode()), moduleStartupState);
                return;
            }
        }
        if (coalFeederStartupStateJudgeCondition.containsKey(dataModel.getThingCode())) {
            Map<String, StateThreshold> stateThresholdMetricMap = coalFeederStartupStateJudgeCondition.get(dataModel.getThingCode());
            if (stateThresholdMetricMap != null && stateThresholdMetricMap.containsKey(dataModel.getMetricCode())) {
                updateCoalFeederState(dataModel, stateThresholdMetricMap.get(dataModel.getMetricCode()));
            }
        }
    }


    private void updateState(DataModel dataModel, StateThreshold stateThreshold, Map<String, Map<Integer, Integer>> startupStateMap) {
        Double threshold = stateThreshold.getThresholdValue();
        double value = Double.parseDouble(dataModel.getValue());
        int preState = 0;
        Map<Integer, Integer> moduleStartupStateMap = getModuleStartupStateMap(startupStateMap, stateThreshold);
        if (moduleStartupStateMap.containsKey(stateThreshold.getTerm())) {
            preState = moduleStartupStateMap.get(stateThreshold.getTerm());
        }
        if (value > threshold) {
            int currentState = BitOperationUtil.do1(preState, stateThreshold.getIndex());
            moduleStartupStateMap.put(stateThreshold.getTerm(), currentState);
        } else {
            int currentState = BitOperationUtil.do0(preState, stateThreshold.getIndex());
            moduleStartupStateMap.put(stateThreshold.getTerm(), currentState);
        }
    }

    public Map<Integer, Integer> getModuleStartupStateMap(Map<String, Map<Integer, Integer>> startupStateMap, StateThreshold stateThreshold) {
        Map<Integer, Integer> moduleStartupStateMap;
        if (startupStateMap.containsKey(stateThreshold.getModule())) {
            moduleStartupStateMap = startupStateMap.get(stateThreshold.getModule());
        } else {
            moduleStartupStateMap = new HashMap<>();
            startupStateMap.put(stateThreshold.getModule(), moduleStartupStateMap);
        }
        return moduleStartupStateMap;
    }


    public void updateCoalFeederState(DataModel dataModel, StateThreshold stateThreshold) {
        Double threshold = stateThreshold.getThresholdValue();
        double value = Double.parseDouble(dataModel.getValue());
        Integer term = stateThreshold.getTerm();
        int preState = 0;
        if (coalFeederStartupState.containsKey(term)) {
            preState = coalFeederStartupState.get(term);
        }
        //有给煤机开启
        if (value == threshold) {
            int currentState = BitOperationUtil.do1(preState, stateThreshold.getIndex());
            coalFeederStartupState.put(term, currentState);
            //判断停机延迟过程中给煤机开启，取消停机状态
            if (systemStopDateMap.containsKey(term)) {
                systemStopDateMap.remove(term);
            }
            //当前有几个系统开启
            int moduleStartCount = 0;
            for (Map.Entry<String, Map<Integer, Integer>> entry : moduleStartupState.entrySet()) {
                String module = entry.getKey();
                if (ReportFormSystemStartConstant.MODULE_SHALLOWGROOVE.equals(module) || ReportFormSystemStartConstant.MODULE_CYCLONE_RE.equals(module) || ReportFormSystemStartConstant.MODULE_CYCLONE_MAIN.equals(module)) {
                    Map<Integer, Integer> moduleState = entry.getValue();
                    Integer moduleTermState = moduleState.get(term);
                    if (moduleTermState != null && moduleTermState > 0) {
                        moduleStartCount++;
                    }
                }
            }
            //系统开始启车并且给煤机开启，开始生产
            if (moduleStartCount >= SYSTEM_START_MODULE_COUNT) {
                //当前已处于开启状态
                if (ReportFormSystemStartConstant.SYSTEM_STARTUP_STATE_START.equals(systemStartupState.get(term))) {
                    return;
                }
                Date startupTime = new Date();
                ReportFormSystemStartRecord currentRecord = createStartRecord(term, startupTime);
                //注意 更新上一条记录必须放在新增新纪录之前
                updateLastRecordTime(currentRecord.getStartTime(), term);
                systemStartMapper.insertRecord(currentRecord);
                putNewRecordInCache(currentRecord);
                //更新当前系统状态
                systemStartupState.put(term, ReportFormSystemStartConstant.SYSTEM_STARTUP_STATE_START);
            }
        } else {
            int currentState = BitOperationUtil.do0(preState, stateThreshold.getIndex());
            coalFeederStartupState.put(stateThreshold.getTerm(), currentState);
            //所有的给煤机都关闭
            if (currentState == 0) {
                systemStopDateMap.put(term, new Date());
            }
        }
    }

    /**
     * 新增记录放到缓存
     *
     * @param record
     */
    public void putNewRecordInCache(ReportFormSystemStartRecord record) {
        Integer term = record.getTerm();
        List<ReportFormSystemStartRecord> reportFormSystemStartRecords;
        if (dutyRecords.containsKey(term)) {
            reportFormSystemStartRecords = dutyRecords.get(term);
        } else {
            reportFormSystemStartRecords = new ArrayList<>();
            dutyRecords.put(term, reportFormSystemStartRecords);
        }
        reportFormSystemStartRecords.add(record);
    }

    /**
     * 检查给煤机全部关闭到指定时间，认为是系统停止，生成新记录
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void checkStop() {
        Iterator<Map.Entry<Integer, Date>> it = systemStopDateMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Date> entry = it.next();
            Integer term = entry.getKey();
            Date stopDate = entry.getValue();
            if (System.currentTimeMillis() - stopDate.getTime() > 10 * 60 * 1000) {
                ReportFormSystemStartRecord currentRecord = createStopRecord(term, stopDate);
                updateLastRecordTime(stopDate, entry.getKey());
                systemStartMapper.insertRecord(currentRecord);
                putNewRecordInCache(currentRecord);
                it.remove();
                systemStartupState.put(term, ReportFormSystemStartConstant.SYSTEM_STARTUP_STATE_STOP);
            }
        }
    }


    @Scheduled(cron = "0 0 8,20 * * ? ")
    public void checkDutyEndTime() {
        Date dutyStartTime = ReportFormDateUtil.getNowDutyStartTime(new Date());
        updateLastRecordTime(dutyStartTime, 1);
        updateLastRecordTime(dutyStartTime, 2);
        dutyRecords.clear();
        logger.info("执行定时任务");
        createDutyFirstRecord(dutyStartTime, 1);
        createDutyFirstRecord(dutyStartTime, 2);
    }

    /**
     * 延续上一个班最后一条记录的所有状态，创建当班第一条记录
     *
     * @param dutyStartTime
     */
    public void createDutyFirstRecord(Date dutyStartTime, Integer term) {
        /**
         * 换班的时候正在给煤机停止的延迟时间内，重新计算延迟
         */
        ReportFormSystemStartRecord dutyFirstRecord;
        if (ReportFormSystemStartConstant.SYSTEM_STARTUP_STATE_START.equals(systemStartupState.get(term))) {
            dutyFirstRecord = createStartRecord(term, dutyStartTime);
        } else {
            dutyFirstRecord = createStopRecord(term, dutyStartTime);
        }
        if (systemStopDateMap.containsKey(dutyFirstRecord.getTerm())) {
            systemStopDateMap.put(dutyFirstRecord.getTerm(), dutyStartTime);
        }
        systemStartMapper.insertRecord(dutyFirstRecord);
        putNewRecordInCache(dutyFirstRecord);
    }


    /**
     * 更新上一条记录的结束时间为当前记录的开始时间
     */
    public ReportFormSystemStartRecord updateLastRecordTime(Date currentTime, Integer term) {
//        ReportFormSystemStartRecord preRecord = systemStartMapper.getTheLastOneRecord(currentTime, term);
//        preRecord.setEndTime(currentTime);
//        preRecord.setDuration((currentTime.getTime() - preRecord.getStartTime().getTime()) / 60000);
        List<ReportFormSystemStartRecord> reportFormSystemStartRecords = dutyRecords.get(term);
        ReportFormSystemStartRecord preRecordInCache = null;
        if (CollectionUtils.isNotEmpty(reportFormSystemStartRecords)) {
            preRecordInCache = reportFormSystemStartRecords.get(reportFormSystemStartRecords.size() - 1);
            //换班的时候存在停车延迟的情况，这时第一条数据为脏数据
            if (currentTime.equals(preRecordInCache.getStartTime())) {
                reportFormSystemStartRecords.remove(preRecordInCache);
                systemStartMapper.deleteRecord(preRecordInCache);
            } else {
                preRecordInCache.setEndTime(currentTime);
                preRecordInCache.setDuration((currentTime.getTime() - preRecordInCache.getStartTime().getTime()) / 60000);
                systemStartMapper.updateRecord(preRecordInCache);
            }
        }
//        systemStartMapper.updateRecord(preRecord);
        return preRecordInCache;
    }

    /**
     * 生成一条新的记录（生产时）
     *
     * @return
     */
    public ReportFormSystemStartRecord createStartRecord(Integer term, Date startupTime) {
        ReportFormSystemStartRecord reportFormSystemStartRecord = new ReportFormSystemStartRecord();
        reportFormSystemStartRecord.setTerm(term);
        reportFormSystemStartRecord.setStartTime(startupTime);
        reportFormSystemStartRecord.setDutyStartTime(ReportFormDateUtil.getNowDutyStartTime(startupTime));
        String coal8ThingCode = getCoalThingCode("Quit_SYS_1", "COAL_8_DEVICE", reportFormSystemStartRecord);
        reportFormSystemStartRecord.setCoal8ThingCode(coal8ThingCode);
        String coal13ThingCode = getCoalThingCode("Quit_SYS_1", "COAL_13_DEVICE", reportFormSystemStartRecord);
        reportFormSystemStartRecord.setCoal13ThingCode(coal13ThingCode);
        getWashingType(reportFormSystemStartRecord);
        getHeavyMediumLumpCoalEquipment(reportFormSystemStartRecord);
        getHeavyMediumSlackCoalEquipment(reportFormSystemStartRecord);
        getTcsStartEquipment(reportFormSystemStartRecord);
        return reportFormSystemStartRecord;
    }

    /**
     * 生成一条新的记录（停车时）
     *
     * @param term
     * @param stopDate
     * @return
     */
    public ReportFormSystemStartRecord createStopRecord(Integer term, Date stopDate) {
        ReportFormSystemStartRecord currentRecord = new ReportFormSystemStartRecord();
        currentRecord.setTerm(term);
        currentRecord.setStartTime(stopDate);
        currentRecord.setDutyStartTime(ReportFormDateUtil.getNowDutyStartTime(stopDate));
        currentRecord.setCoal8ThingCode("/");
        currentRecord.setCoal13ThingCode("/");
        currentRecord.setBlendingWashingType("/");
        currentRecord.setHeavyMediumLump("/");
        currentRecord.setHeavyMediumSlack("/");
        currentRecord.setTcs857("/");
        currentRecord.setTcs858("/");
        currentRecord.setTcs859("/");
        return currentRecord;
    }

    private void getWashingType(ReportFormSystemStartRecord reportFormSystemStartRecord) {
        int washingCoalEquipmentCount = reportFormSystemStartRecord.getWashingCoalEquipmentCount();
        String washingCoalCount = "";
        Integer term = reportFormSystemStartRecord.getTerm();
        if (washingCoalEquipmentCount == ReportFormSystemStartConstant.WASHING_COAL_TYPE_SINGLE_EQUIPMENT_COUNT) {
            washingCoalCount = ReportFormSystemStartConstant.WASHING_COAL_COUNT_SINGLE;
        } else if (washingCoalEquipmentCount == ReportFormSystemStartConstant.WASHING_COAL_TYPE_MIX_EQUIPMENT_COUNT) {
            washingCoalCount = ReportFormSystemStartConstant.WASHING_COAL_COUNT_MIX;
        }
        String washingCoalObject = "";
        if (moduleStartupState.get(ReportFormSystemStartConstant.MODULE_CYCLONE_RE) != null &&
                moduleStartupState.get(ReportFormSystemStartConstant.MODULE_CYCLONE_RE).get(term) != null &&
                moduleStartupState.get(ReportFormSystemStartConstant.MODULE_CYCLONE_RE).get(term) > 0) {
            washingCoalObject = ReportFormSystemStartConstant.WASHING_COAL_OBJECT_CLENEDCOAL;
        } else if (moduleStartupState.get(ReportFormSystemStartConstant.MODULE_CYCLONE_RE) != null &&
                moduleStartupState.get(ReportFormSystemStartConstant.MODULE_CYCLONE_RE).get(term) != null &&
                moduleStartupState.get(ReportFormSystemStartConstant.MODULE_CYCLONE_RE).get(term) == 0 &&
                moduleStartupState.get(ReportFormSystemStartConstant.MODULE_CYCLONE_MAIN) != null &&
                moduleStartupState.get(ReportFormSystemStartConstant.MODULE_CYCLONE_MAIN).get(term) != null &&
                moduleStartupState.get(ReportFormSystemStartConstant.MODULE_CYCLONE_MAIN).get(term) > 0) {
            washingCoalObject = ReportFormSystemStartConstant.WASHING_COAL_OBJECT_MIXEDCOAL;
        }
        String washingType = washingCoalCount + washingCoalObject;
        if ("".equals(washingType)) {
            washingType = "/";
        }
        reportFormSystemStartRecord.setBlendingWashingType(washingType);
    }


    public String getCoalThingCode(String thingCode, String metricCode, ReportFormSystemStartRecord reportFormSystemStartRecord) {
        String coalThingCode = ReportFormSystemStartConstant.DEFAULT_SHOW_VALUE;
        DataModelWrapper data = dataService.getData(thingCode, metricCode).orElse(null);
        if (data != null) {
            String value = data.getValue();
            if (!ReportFormSystemStartConstant.INVALID_COAL_THING_CODE.equals(value)) {
                coalThingCode = value;
                int washingCoalEquipmentCount = reportFormSystemStartRecord.getWashingCoalEquipmentCount();
                reportFormSystemStartRecord.setWashingCoalEquipmentCount(washingCoalEquipmentCount + 1);
            }
        }
        return coalThingCode;
    }


    /**
     * 获取重介洗煤系统块煤状态
     *
     * @param reportFormSystemStartRecord
     * @return
     */
    public void getHeavyMediumLumpCoalEquipment(ReportFormSystemStartRecord reportFormSystemStartRecord) {
        String coalThingCode = "/";
        boolean alreadyExistEquipment = false;
        DataModelWrapper dataA = dataService.getData("SYSTEM", "RUNS").orElse(null);
        if (dataA != null) {
            String value = dataA.getValue();
            if (Boolean.TRUE.toString().equals(value)) {
                coalThingCode = "块A";
                alreadyExistEquipment = true;
            }
        }
        DataModelWrapper dataB = dataService.getData("SYSTEM", "RUNS").orElse(null);
        if (dataB != null) {
            String value = dataB.getValue();
            if (Boolean.TRUE.toString().equals(value)) {
                if (alreadyExistEquipment) {
                    coalThingCode += "/B";
                } else {
                    coalThingCode = "块B";
                }
            }
        }
        reportFormSystemStartRecord.setHeavyMediumLump(coalThingCode);
    }


    /**
     * 获取重介洗煤系统末煤
     *
     * @param reportFormSystemStartRecord
     * @return
     */
    public void getHeavyMediumSlackCoalEquipment(ReportFormSystemStartRecord reportFormSystemStartRecord) {
        Integer term = reportFormSystemStartRecord.getTerm();
        String coalThingCode = "末";
        int cycloneReState = getModuleState(term, ReportFormSystemStartConstant.MODULE_CYCLONE_RE);
        int cycloneMainState = getModuleState(term, ReportFormSystemStartConstant.MODULE_CYCLONE_MAIN);
        //洗精
        if (cycloneReState > 0) {
            coalThingCode = "主";
            coalThingCode = getCoalThingCodeOnState(coalThingCode, cycloneReState);
        }
        //洗混
        else if (cycloneReState == 0 && cycloneMainState > 0) {
            coalThingCode = getCoalThingCodeOnState(coalThingCode, cycloneMainState);
        }
        coalThingCode = coalThingCode.substring(0, coalThingCode.length() - 1);
        if ("".equals(coalThingCode)) {
            coalThingCode = "/";
        }
        reportFormSystemStartRecord.setHeavyMediumSlack(coalThingCode);
    }

    /**
     * 通过模块的状态位判断展示的内容
     *
     * @param coalThingCode
     * @param moduleState
     * @return
     */
    public String getCoalThingCodeOnState(String coalThingCode, int moduleState) {
        if ((moduleState & 1) == 1) {
            coalThingCode += "1/";
        }
        if ((moduleState & 2) == 2) {
            coalThingCode += "2/";
        }
        if ((moduleState & 4) == 4) {
            coalThingCode += "3/";
        }
        return coalThingCode;
    }

    public int getModuleState(Integer term, String module) {
        int moduleState = 0;
        if (moduleStartupState.get(module) != null &&
                moduleStartupState.get(module).get(term) != null) {
            moduleState = moduleStartupState.get(module).get(term);
        }
        return moduleState;
    }


    /**
     * 获取目前tcs开启的设备
     *
     * @param reportFormSystemStartRecord
     */
    public void getTcsStartEquipment(ReportFormSystemStartRecord reportFormSystemStartRecord) {
        Integer term = reportFormSystemStartRecord.getTerm();
        if (moduleStartupState.get(ReportFormSystemStartConstant.MODULE_TCS_857) != null &&
                moduleStartupState.get(ReportFormSystemStartConstant.MODULE_TCS_857).get(term) != null &&
                moduleStartupState.get(ReportFormSystemStartConstant.MODULE_TCS_857).get(term) > 0) {
            reportFormSystemStartRecord.setTcs857(reportFormSystemStartRecord.getTerm() + "857");
        } else {
            reportFormSystemStartRecord.setTcs857("/");
        }
        if (moduleStartupState.get(ReportFormSystemStartConstant.MODULE_TCS_858) != null &&
                moduleStartupState.get(ReportFormSystemStartConstant.MODULE_TCS_858).get(term) != null &&
                moduleStartupState.get(ReportFormSystemStartConstant.MODULE_TCS_858).get(term) > 0) {
            reportFormSystemStartRecord.setTcs858(reportFormSystemStartRecord.getTerm() + "858");
        } else {
            reportFormSystemStartRecord.setTcs858("/");
        }
        if (moduleStartupState.get(ReportFormSystemStartConstant.MODULE_TCS_859) != null &&
                moduleStartupState.get(ReportFormSystemStartConstant.MODULE_TCS_859).get(term) != null &&
                moduleStartupState.get(ReportFormSystemStartConstant.MODULE_TCS_859).get(term) > 0) {
            reportFormSystemStartRecord.setTcs859(reportFormSystemStartRecord.getTerm() + "859");
        } else {
            reportFormSystemStartRecord.setTcs859("/");
        }
    }


    @Override
    public void onError(Throwable error) {
        logger.error("data invalid", error);
    }



}
