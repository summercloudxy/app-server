package com.zgiot.app.server.module.filterpress;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.filterpress.dao.FilterPressLogMapper;
import com.zgiot.app.server.module.filterpress.dao.FilterPressMapper;
import com.zgiot.app.server.module.filterpress.pojo.FeedAsumConfirmBean;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressConfig;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressElectricity;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressPlateStatistic;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.module.filterpress.filterPressService.FilterPressLogService;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.app.server.service.impl.DataEngineTemplate;
import com.zgiot.app.server.util.RequestIdUtil;
import com.zgiot.common.constants.*;
import com.zgiot.common.enums.MetricDataTypeEnum;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.pojo.MetricModel;
import com.zgiot.common.restcontroller.ServerResponse;
import javafx.beans.binding.IntegerBinding;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class FilterPressManager {
    private static final Logger logger = LoggerFactory.getLogger(FilterPressManager.class);
    private static final String FEED_OVER_NOTICE_URI_TERM2 = "/topic/filterPress/feedOver/term2";
    private static final String FEED_OVER_CONFIRMED_NOTICE_URI_TERM2 = "/topic/filterPress/feedOver/confirm/term2";
    private static final String UNLOAD_NOTICE_URI_TERM2 = "/topic/filterPress/unload/term2";
    private static final String UNLOAD_CONFIRMED_NOTICE_URI_TERM2 = "/topic/filterPress/unload/confirm/term2";
    private static final String FEED_OVER_NOTICE_URI_TERM1 = "/topic/filterPress/feedOver/term1";
    private static final String FEED_OVER_CONFIRMED_NOTICE_URI_TERM1 = "/topic/filterPress/feedOver/confirm/term1";
    private static final String UNLOAD_NOTICE_URI_TERM1 = "/topic/filterPress/unload/term1";
    private static final String UNLOAD_CONFIRMED_NOTICE_URI_TERM1 = "/topic/filterPress/unload/confirm/term1";
    private static final String PARAM_NAME_SYS = "sys";
    private static final String PARAM_NAME_SYS_TERM1 = "sys_term1";

    private static final int POSITION_FEED_OVER = 5;
    private static final int POSITION_RUN = 1;
    private static final int POSITION_R_BAN_RUN = 9;
    private static final int CLAEN_PERIOD = 500;
    private static final int RETRY_PERIOD = 5000;
    private static final int RETRY_COUNT = 3;
    private static final boolean IS_HOLDING_FEED_OVER = false;
    private static final boolean IS_HOLDING_RUN = false;
    private static final Map<String,String> filterPressStage = new HashMap<>();

    @Autowired
    DataService dataService;
    @Autowired
    HistoryDataService historyDataService;
    @Autowired
    CmdControlService cmdControlService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private FilterPressMapper filterPressMapper;

    @Autowired
    FilterPressLogService filterPressLogService;

    @Autowired
    FilterPressLogMapper filterPressLogMapper;


    public UnloadManager getUnloadManager() {
        return unloadManager;
    }

    private static final int INIT_CAPACITY = 6;

    private static final int TERM1 = 1;

    private static final int TERM2 = 2;

    private static final String TERM = "term";

    private static final String PUMP = "filterPressPump";

    private static final String SYS_1_YL = "SYS_1_YL";

    private static final String SYS_2_YL = "SYS_2_YL";

    private static final Integer CURRENT_COUNT_DURATION = -3;

    private Map<String, FilterPress> deviceHolder = new ConcurrentHashMap<>();

    private Map<String, String> filterPressPumpMapping = new HashMap<>();

    private Map<String, Integer> filterPressTerm = new HashMap<>();

    //禁止启动煤泥刮板数据map,key:对应一期压滤连锁和二期压滤连锁信号点，value：对应1502（一期）或2502（二期）
    private Map<String, String> runMap = new HashMap<>();

    private Set<String> unconfirmedFeed = new ConcurrentSkipListSet<>();

    private Set<String> unconfirmedFeedTerm1 = new ConcurrentSkipListSet<>();

    private List<String> unConfirmedUnloadTerm2 = Collections.synchronizedList(new ArrayList<>());

    private List<String> unConfirmedUnloadTerm1 = Collections.synchronizedList(new ArrayList<>());

    private UnloadManager unloadManager = new UnloadManager();

    Map<String,FilterPressLogBean> statisticLogs = new ConcurrentHashMap<>();

    public Map<String, Integer> getFilterPressTerm() {
        return filterPressTerm;
    }

    static{
        filterPressStage.put(FilterPressMetricConstants.RO_LOOSE,"");
        filterPressStage.put(FilterPressMetricConstants.RO_TAKE,"");
        filterPressStage.put(FilterPressMetricConstants.RO_PULL,"");
        filterPressStage.put(FilterPressMetricConstants.RO_PRESS,"");
        filterPressStage.put(FilterPressMetricConstants.RO_FEEDING,"");
        filterPressStage.put(FilterPressMetricConstants.RO_FEED_OVER,"");
        filterPressStage.put(FilterPressMetricConstants.RO_BLOW,"");
        filterPressStage.put(FilterPressMetricConstants.RO_EMPTYING,"");
        filterPressStage.put(FilterPressMetricConstants.RO_SQUEEZE,"");
        filterPressStage.put(FilterPressMetricConstants.RO_SQUEEZE_OVER,"");
        filterPressStage.put(FilterPressMetricConstants.RO_HOLD_PRESS,"");
        filterPressStage.put(FilterPressMetricConstants.RO_CYCLE,"");
        filterPressStage.put(FilterPressMetricConstants.LOCAL,"");
        filterPressStage.put(FilterPressMetricConstants.T1_COUNT,"");
        filterPressStage.put(FilterPressMetricConstants.T2_COUNT,"");
        filterPressStage.put(FilterPressMetricConstants.T3_COUNT,"");
    }

    @PostConstruct
    public void initFilterPress() {
        mappingFilterPressInfo(deviceHolder,filterPressTerm);
        mappingFilterPressAndPump(filterPressPumpMapping);
        mappingRBanRunData(runMap);
        setMaxUnloadParallel(filterPressMapper
                .selectParamValue(PARAM_NAME_SYS, FilterPress.PARAM_NAME_MAXUNLOADPARALLEL).intValue());
        setMaxUnloadParallelTerm1(filterPressMapper
                .selectParamValue(PARAM_NAME_SYS_TERM1, FilterPress.PARAM_NAME_MAXUNLOADPARALLEL).intValue());
        deviceHolder.forEach((code, filterPress) -> {
            boolean feedIntelligent = filterPressMapper.selectParamValue(code, FilterPress.PARAM_NAME_FEEDINTELLIGENT).intValue() == 1;
            filterPress.setFeedIntelligent(feedIntelligent);
            boolean feedConfirmNeed = filterPressMapper.selectParamValue(code, FilterPress.PARAM_NAME_FEEDCONFIRMNEED).intValue() == 1;
            filterPress.setFeedConfirmNeed(feedConfirmNeed);
            boolean unloadIntelligent = filterPressMapper.selectParamValue(code, FilterPress.PARAM_NAME_UNLOADINTELLIGENT).intValue() == 1;
            filterPress.setUnloadIntelligent(unloadIntelligent);
            boolean unloadConfirmNeed = filterPressMapper.selectParamValue(code, FilterPress.PARAM_NAME_UNLOADCONFIRMNEED).intValue() == 1;
            filterPress.setUnloadConfirmNeed(unloadConfirmNeed);
        });
    }

    private void mappingRBanRunData(Map<String, String> runMap){
        FilterPressConfig filterPressConfig = new FilterPressConfig();
        filterPressConfig.setThingCode(SYS_1_YL);
        List<FilterPressConfig> filterPressList = filterPressMapper.findFilterInfo(filterPressConfig);
        filterPressConfig.setThingCode(SYS_2_YL);
        filterPressList.addAll(filterPressMapper.findFilterInfo(filterPressConfig));
        for(FilterPressConfig data:filterPressList){
            runMap.put(data.getThingCode(),data.getParamValue());
        }
    }

    private void mappingFilterPressInfo(Map<String, FilterPress> deviceHolder,Map<String, Integer> filterPressPeriod){
        FilterPressConfig filterPressConfig = new FilterPressConfig();
        filterPressConfig.setParamName(TERM);
        filterPressConfig.setParamValue(String.valueOf(TERM1));
        List<FilterPressConfig> filterPressList = filterPressMapper.findFilterInfo(filterPressConfig);
        filterPressConfig.setParamValue(String.valueOf(TERM2));
        filterPressList.addAll(filterPressMapper.findFilterInfo(filterPressConfig));
        for(FilterPressConfig data:filterPressList){
            deviceHolder.put(data.getThingCode(), new FilterPress(data.getThingCode(), this));
            filterPressPeriod.put(data.getThingCode(),Integer.valueOf(data.getParamValue()));
        }
    }

    private void mappingFilterPressAndPump(Map<String, String> filterPressPumpMapping){
        FilterPressConfig filterPressConfig = new FilterPressConfig();
        filterPressConfig.setParamName(PUMP);
        List<FilterPressConfig> filterPressList = filterPressMapper.findFilterInfo(filterPressConfig);
        for(FilterPressConfig data:filterPressList){
            filterPressPumpMapping.put(data.getParamValue(),data.getThingCode());
        }
    }

    /**
     * call back when data changed
     *
     * @param data
     */
    public void onDataSourceChange(DataModel data) {
        String thingCode = data.getThingCode();
        FilterPress filterPress = null;
        String metricCode = data.getMetricCode();
        rBanRunOperate(metricCode,thingCode,data.getValue());
        if(deviceHolder.containsKey(thingCode)){
            filterPress = deviceHolder.get(thingCode);
        }else if(filterPressPumpMapping.containsKey(thingCode)){
            filterPress = deviceHolder.get(filterPressPumpMapping.get(thingCode));
        }else{
            return;
        }

        if(!statisticLogs.containsKey(thingCode)){
            statisticLogs.put(thingCode,new FilterPressLogBean());
        }

        filterPress.onDataSourceChange(metricCode, data.getValue());
        if (deviceHolder.containsKey(thingCode) && filterPressStage.containsKey(metricCode)) {
            processStage(data);
        }
        if (FilterPressMetricConstants.FEED_ASUM.equals(metricCode)) {
            processFeedAssumption(data);
        }
        if (FilterPressMetricConstants.T1_RCD.equals(metricCode)
                || FilterPressMetricConstants.T2_RCD.equals(metricCode)
                || FilterPressMetricConstants.T3_RCD.equals(metricCode)) {
            setTeam(thingCode,metricCode,data);
        }

        savePlateStatisticsData(thingCode,metricCode,data);
    }

    /**
     * 远程控制刮板启动逻辑
     * @param metricCode
     * @param metricValueData
     */
    private void rBanRunOperate(String metricCode,String code,String metricValueData){
        int term = 0;
        if(FilterPressMetricConstants.YL_LS.equals(metricCode)){
            String thingCode = runMap.get(code);//thingCode:1502 or 2502
            Optional<DataModelWrapper> value = dataService.getData(thingCode,MetricCodes.STATE);
            if((value.isPresent()) && (!StringUtils.isBlank(value.get().getValue()))){
                String meticValue = value.get().getValue();
                if(code.equals(SYS_1_YL)){
                    term = 1;
                    Set<String> thingCodes = getAllFilterPressCode(term);
                    filterPressLockControlRBanRun(metricValueData,meticValue,thingCodes);

                }else if(code.equals(SYS_2_YL)){
                    term = 2;
                    Set<String> thingCodes = getAllFilterPressCode(term);
                    filterPressLockControlRBanRun(metricValueData,meticValue,thingCodes);
                }
            }
        }else if((runMap.containsValue(code) && FilterPressMetricConstants.STATE.equals(metricCode))) {
            stateChangeOperate(metricCode,code,metricValueData);
        }
    }

    /**
     * 1502或者2502 STATE状态值触发刮板是否启动逻辑
     */
    private void stateChangeOperate(String metricCode,String code,String metricValueData){
        String meticValue = null;
        int term = 0;
        if(code.startsWith(String.valueOf(TERM1))){
            Optional<DataModelWrapper> value = dataService.getData(SYS_1_YL,FilterPressMetricConstants.YL_LS);
            if((value.isPresent()) && (!StringUtils.isBlank(value.get().getValue()))){
                meticValue = value.get().getValue();
            }
            term = 1;
            Set<String> thingCodes = getAllFilterPressCode(term);
            stateContrlRbanRun(meticValue,metricValueData,thingCodes);
        }else if(code.startsWith(String.valueOf(TERM2))){
            Optional<DataModelWrapper> value = dataService.getData(SYS_2_YL,FilterPressMetricConstants.YL_LS);
            if((value.isPresent()) && (!StringUtils.isBlank(value.get().getValue()))){
                meticValue = value.get().getValue();
            }
            term = 2;
            Set<String> thingCodes = getAllFilterPressCode(term);
            stateContrlRbanRun(meticValue,metricValueData,thingCodes);
        }
    }

    private void stateContrlRbanRun(String metricValueData,String meticValue,Set<String> thingCodes){
        if((Boolean.parseBoolean(metricValueData)) && (GlobalConstants.STATE_RUNNING == Short.valueOf(meticValue))){
            //启动刮板，即给刮板启动信号设置为0
            sendRBanRunSignal(thingCodes,Boolean.FALSE.toString());
        }else if((Boolean.parseBoolean(metricValueData)) && (GlobalConstants.STATE_RUNNING != Short.valueOf(meticValue))){
            //禁止刮板启动
            sendRBanRunSignal(thingCodes,Boolean.TRUE.toString());
        }
    }

    private void filterPressLockControlRBanRun(String metricValueData,String meticValue,Set<String> thingCodes){
        if((Boolean.parseBoolean(metricValueData)) && (GlobalConstants.STATE_RUNNING == Short.valueOf(meticValue))){
            //启动刮板，即给刮板启动信号设置为0
            sendRBanRunSignal(thingCodes,Boolean.FALSE.toString());
        }else if(!Boolean.parseBoolean(metricValueData)){//启动刮板，即给刮板启动信号设置为0
            sendRBanRunSignal(thingCodes,Boolean.FALSE.toString());
        }else if((Boolean.parseBoolean(metricValueData)) && (GlobalConstants.STATE_RUNNING != Short.valueOf(meticValue))){
            //禁止刮板启动
            sendRBanRunSignal(thingCodes,Boolean.TRUE.toString());
        }
    }

    private void sendRBanRunSignal(Set<String> thingCodes,String stopFlag){
        for(String code:thingCodes){
            setRBanRun(code,stopFlag);
        }
    }

    public void setRBanRun(String thingCode,String stopFlag){
        DataModel cmd = new DataModel();
        cmd.setThingCode(thingCode);
        cmd.setMetricCode(FilterPressMetricConstants.R_BAN_RUN);
        cmd.setValue(stopFlag);
        cmdControlService.sendPulseCmdBoolByShort(cmd,RETRY_PERIOD,RETRY_COUNT,RequestIdUtil.generateRequestId(),POSITION_R_BAN_RUN,CLAEN_PERIOD,true);
    }

    private void savePlateStatisticsData(String thingCode,String metricCode,DataModel data){
        if (FilterPressMetricConstants.T1_CLR.equals(metricCode)
                || FilterPressMetricConstants.T2_CLR.equals(metricCode)
                || FilterPressMetricConstants.T3_CLR.equals(metricCode)) {
            String metricCodeValue = data.getValue();
            int term = filterPressTerm.get(thingCode);
            switch (metricCode){
                case FilterPressMetricConstants.T1_CLR:
                    if(Boolean.parseBoolean(metricCodeValue)){
                        savePlateStatistics(FilterPressLogConstants.TEAM1,term);
                    }
                    break;
                case FilterPressMetricConstants.T2_CLR:
                    if(Boolean.parseBoolean(metricCodeValue)){
                        savePlateStatistics(FilterPressLogConstants.TEAM2,term);
                    }
                    break;
                case FilterPressMetricConstants.T3_CLR:
                    if(Boolean.parseBoolean(metricCodeValue)){
                        savePlateStatistics(FilterPressLogConstants.TEAM3,term);
                    }
                    break;
            }
        }
    }

    private void setTeam(String thingCode,String metricCode,DataModel data){
        if (Boolean.TRUE.toString().equals(data.getValue())) {
            switch (metricCode) {
                case FilterPressMetricConstants.T1_RCD:
                    getFilterPress(thingCode).setProducingTeam(FilterPressLogConstants.TEAM1);
                    break;
                case FilterPressMetricConstants.T2_RCD:
                    getFilterPress(thingCode).setProducingTeam(FilterPressLogConstants.TEAM2);
                    break;
                case FilterPressMetricConstants.T3_RCD:
                    getFilterPress(thingCode).setProducingTeam(FilterPressLogConstants.TEAM3);
                    break;
                default:
            }
        }
    }

    public void calculatePlateAndSave(int term) {
        int total = 0;
        Map<String,FilterPress> map = getFilterPress(term);
        for (Map.Entry<String, FilterPress> entry : map.entrySet()) {
            String code = entry.getKey();
            FilterPress filterPress = entry.getValue();
            int plateCount = filterPress.getPlateCount();
            DataModel dataModel = new DataModel();
            dataModel.setThingCode(code);
            dataModel.setMetricDataType(MetricDataTypeEnum.METRIC_DATA_TYPE_OK.getName());
            dataModel.setMetricCode(FilterPressMetricConstants.PLATE_CNT);
            dataModel.setMetricCategoryCode(MetricModel.CATEGORY_SIGNAL);
            dataModel.setValue(String.valueOf(plateCount));
            dataModel.setDataTimeStamp(new Date());
            dataService.saveData(dataModel);
            total += plateCount;
        }

        for (Map.Entry<String, FilterPress> entry : map.entrySet()) {
            String code = entry.getKey();
            DataModel dataModel = new DataModel();
            dataModel.setThingCode(code);
            dataModel.setMetricDataType(MetricDataTypeEnum.METRIC_DATA_TYPE_OK.getName());
            dataModel.setMetricCode(FilterPressMetricConstants.PLATE_TTL);
            dataModel.setMetricCategoryCode(MetricModel.CATEGORY_SIGNAL);
            dataModel.setValue(String.valueOf(total));
            dataModel.setDataTimeStamp(new Date());
            dataService.saveData(dataModel);
        }
    }

    public void confirmFeedOver(String code,int term) {
        if((term == TERM1) && (unconfirmedFeedTerm1.remove(code))){
            doFeedOver(getFilterPress(code));
            messagingTemplate.convertAndSend(FEED_OVER_CONFIRMED_NOTICE_URI_TERM1, code);
        } else if((term == TERM2) && (unconfirmedFeed.remove(code))){
            doFeedOver(getFilterPress(code));
            messagingTemplate.convertAndSend(FEED_OVER_CONFIRMED_NOTICE_URI_TERM2, code);
        }
    }

    public void confirmUnload(String code,int term) {
        if((term == TERM1) && (unConfirmedUnloadTerm1.remove(code))){
            unloadManager.doUnload(getFilterPress(code));
            messagingTemplate.convertAndSend(UNLOAD_CONFIRMED_NOTICE_URI_TERM1, code);
        }else if((term == TERM2) && (unConfirmedUnloadTerm2.remove(code))){
            unloadManager.doUnload(getFilterPress(code));
            messagingTemplate.convertAndSend(UNLOAD_CONFIRMED_NOTICE_URI_TERM2, code);
        }

    }

    private void processFeedAssumption(DataModel data) {
        if (String.valueOf(FilterPressConstants.FEED_OVER_CURRENT).equals(data.getValue())
                || String.valueOf(FilterPressConstants.FEED_OVER_TIME).equals(data.getValue())) {
            String pumpCode = data.getThingCode();
            getFilterPress(filterPressPumpMapping.get(pumpCode)).onAssumeFeedOver();
        }
    }

    public void clearAllUnloadQueue(int term){
        getUnloadSequence(term).clear();
        getUnloadManager().getQueue(term).clear();
        getUnConfirmedUnload(term).clear();
    }

    /**
     * process the stage data and call the specific method of filter press
     *
     * @param data
     */
    private void processStage(DataModel data) {
        String thingCode = data.getThingCode();
        int term = filterPressTerm.get(thingCode);
        String metricCodeValue = data.getValue();
        String metricCode = data.getMetricCode();
        FilterPress filterPress = getFilterPress(thingCode);
        switch (metricCode) { // 回调各阶段
            case FilterPressMetricConstants.T1_COUNT:
                filterPress.teamCount(FilterPressMetricConstants.T1_COUNT,metricCodeValue);
                break;
            case FilterPressMetricConstants.T2_COUNT:
                filterPress.teamCount(FilterPressMetricConstants.T2_COUNT,metricCodeValue);
                break;
            case FilterPressMetricConstants.T3_COUNT:
                filterPress.teamCount(FilterPressMetricConstants.T3_COUNT,metricCodeValue);
                break;
            case FilterPressMetricConstants.LOCAL:
                if(Boolean.parseBoolean(metricCodeValue)){
                    filterPress.onLocal();
                }
                break;
            case FilterPressMetricConstants.RO_LOOSE:
                if(Boolean.parseBoolean(metricCodeValue)){
                    filterPress.onLoosen();
                }else{
                    filterPress.offLoosen();
                }
                break;
            case FilterPressMetricConstants.RO_TAKE:
                if(Boolean.parseBoolean(metricCodeValue)){
                    filterPress.onTaken();
                }else{
                    filterPress.offTaken();
                }
                break;
            case FilterPressMetricConstants.RO_PULL:
                if(Boolean.parseBoolean(metricCodeValue)){
                    filterPress.onPull();
                }else{
                    filterPress.offPull();
                }
                break;
            case FilterPressMetricConstants.RO_PRESS:
                if(Boolean.parseBoolean(metricCodeValue)){
                    filterPress.onPress();
                }
                break;
            case FilterPressMetricConstants.RO_FEEDING:
                if(Boolean.parseBoolean(metricCodeValue)){
                    filterPress.onFeed();
                }else{
                    filterPress.offFeed();
                }
                break;
            case FilterPressMetricConstants.RO_FEED_OVER:
                if(Boolean.parseBoolean(metricCodeValue)){
                    filterPress.onFeedOver();
                }
                break;
            case FilterPressMetricConstants.RO_BLOW:
                if(Boolean.parseBoolean(metricCodeValue)){
                    filterPress.onBlow();
                }
                break;
            case FilterPressMetricConstants.RO_HOLD_PRESS:
                break;
            case FilterPressMetricConstants.RO_SQUEEZE_OVER:
                break;
            case FilterPressMetricConstants.RO_SQUEEZE:
                break;
            case FilterPressMetricConstants.RO_EMPTYING:
                break;
            case FilterPressMetricConstants.RO_CYCLE:
                if(Boolean.parseBoolean(metricCodeValue)){
                    filterPress.onCycle();
                }
                break;
            default:
        }
        // calculate the state value and call the specific method of filter press
        saveStateValue(thingCode,data,filterPress);
    }

    private void saveStateValue(String thingCode,DataModel data,FilterPress filterPress){
        short stateValue = calculateState(thingCode,data);
        Optional<DataModelWrapper> stateData = dataService.getData(thingCode, MetricCodes.STATE);
        if (!stateData.isPresent()) {
            saveState(data, thingCode, stateValue);
            return;
        }
        if (!Objects.equals(stateData.get().getValue(), String.valueOf(stateValue))) {// 若值变化，保存并回调
            saveState(data, thingCode, stateValue);
            if (stateValue == GlobalConstants.STATE_STOPPED) {
                filterPress.onStop();
            } else if (stateValue == GlobalConstants.STATE_RUNNING) {
                filterPress.onRun();
            } else if (stateValue == GlobalConstants.STATE_FAULT) {
                filterPress.onFault();
            }
        }
    }

    private synchronized void savePlateStatistics(int team,int term){
        boolean isDayShift = FilterPressLogUtil.isDayShift(FilterPressLogConstants.DAY_SHIFT_START_TIME_SCOPE, FilterPressLogConstants.DAY_SHIFT_END_TIME_SCOPE);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        String currentDay = simpleDateFormat.format(date);
        String startTime = currentDay + FilterPressLogConstants.NIGHT_SHIFT_ZERO_LINE;
        String endTime = currentDay + FilterPressLogConstants.NIGHT_SHIFT_MIDDLE_LINE;
        int totalPlateCount = filterPressLogMapper.selectMaxPlate(isDayShift,startTime,endTime,term,team);
        FilterPressPlateStatistic filterPressPlateStatistic = new FilterPressPlateStatistic();
        filterPressPlateStatistic.setIsDayShift(isDayShift);
        filterPressPlateStatistic.setTeam(team);
        filterPressPlateStatistic.setTotalPlateCount(totalPlateCount);
        filterPressPlateStatistic.setTerm(term);
        filterPressPlateStatistic.setDateTime(new Date());
        Integer plateCount = filterPressLogMapper.selectTotalPlate(isDayShift,startTime,endTime,term,team);
        if(plateCount == null){//每次有清零信号只保存一次压板统计
            filterPressLogMapper.insertPlateStatistic(filterPressPlateStatistic);
        }
    }

    private void saveState(DataModel data, String thingCode, short stateValue) {
        DataModel stateModel = new DataModel();
        stateModel.setThingCode(thingCode);
        stateModel.setMetricDataType(data.getMetricDataType());
        stateModel.setMetricCode(MetricCodes.STATE);
        stateModel.setMetricCategoryCode(data.getMetricCategoryCode());
        stateModel.setValue(String.valueOf(stateValue));
        stateModel.setDataTimeStamp(new Date());
        dataService.saveData(stateModel);
    }

    /**
     * calculate the state(running/stopped/fault) of specific thing
     *
     * @param thingCode
     * @return
     */
    private short calculateState(String thingCode,DataModel data) {
        short state;
        DataModelWrapper fault = dataService.getData(thingCode, FilterPressMetricConstants.FAULT)
                .orElse(new DataModelWrapper(new DataModel(null, thingCode, null, FilterPressMetricConstants.FAULT, Boolean.FALSE.toString(), new Date())));
        DataModel dataModel = new DataModel();
        dataModel.setThingCode(data.getThingCode());
        dataModel.setMetricCode(FilterPressMetricConstants.STAGE);
        String readValue = cmdControlService.getDataSync(dataModel);
        if (Boolean.valueOf(fault.getValue())) {
            state = GlobalConstants.STATE_FAULT;
        } else if (Short.valueOf(readValue) == 0) {
            state = GlobalConstants.STATE_STOPPED;
        } else {
            state = GlobalConstants.STATE_RUNNING;
        }
        return state;
    }

    private synchronized boolean isRunningFromCache(String thingCode){
        Boolean isRunning = Boolean.FALSE;
        Optional<DataModelWrapper> data = dataService.getData(thingCode,FilterPressMetricConstants.STAGE);
        if(data.isPresent() && Integer.valueOf(data.get().getValue()) > 0){
            isRunning = Boolean.TRUE;
        }
        return isRunning;
    }

    /**
     * get the filter press by thing code from the deviceHolder
     *
     * @param thingCode
     * @return
     */
    public FilterPress getFilterPress(String thingCode) {
        FilterPress filterPress = deviceHolder.get(thingCode);
        if (filterPress == null) {
            throw new NoSuchElementException("no such filter press:" + thingCode);
        }
        return filterPress;
    }

    void enqueueUnload(FilterPress filterPress) {
        unloadManager.enqueue(filterPress);
    }

    void unloadNext(int term) {
        unloadManager.unloadNext(term);
    }

    public int getMaxUnloadParallel(int term) {
        if(term == TERM1){
            return unloadManager.maxUnloadParallelTerm1;
        }else if(term == TERM2){
            return unloadManager.maxUnloadParallel;
        }
        return 0;
    }

    public void setMaxUnloadParallel(int maxUnloadParallel) {
        this.unloadManager.maxUnloadParallel = maxUnloadParallel;
    }

    public void setMaxUnloadParallelTerm1(int maxUnloadParallel) {
        this.unloadManager.maxUnloadParallelTerm1 = maxUnloadParallel;
    }

    public void removeQueue(String thingCode,Boolean state,int term){
        if(!state){
            if(term == TERM1){
                unloadManager.queueTerm1.remove(deviceHolder.get(thingCode));
                unloadManager.queuePositionTerm1.remove(thingCode);
                logger.debug("manual model remove filterpress:" + thingCode);
                try{
                    unConfirmedUnloadTerm1.remove(thingCode);
                }catch (NullPointerException e){
                    throw new SysException("未确定卸料set中不存在这台压滤机thingCode",SysException.EC_UNKNOWN);
                }
            }else if(term == TERM2){
                unloadManager.queueTerm2.remove(deviceHolder.get(thingCode));
                unloadManager.queuePositionTerm1.remove(thingCode);
                logger.debug("manual model remove filterpress:" + thingCode);
                try{
                    unConfirmedUnloadTerm2.remove(thingCode);
                }catch (NullPointerException e){
                    throw new SysException("未确定卸料set中不存在这台压滤机thingCode",SysException.EC_UNKNOWN);
                }
            }
        }
    }

    void execFeedOver(FilterPress filterPress) {
        logger.debug("{} feed over", filterPress);
        if (!filterPress.isFeedConfirmNeed()) {
            logger.debug("{} feed over,send cmd; confirmNeed: {}", filterPress, filterPress.isFeedConfirmNeed());
            doFeedOver(filterPress);
        } else {
            logger.debug("{} feed over,notifying user; confirmNeed: {}", filterPress, filterPress.isFeedConfirmNeed());
            FeedAsumConfirmBean feedAsumConfirmBean = new FeedAsumConfirmBean();
            feedAsumConfirmBean.setDeviceCode(filterPress.getCode());
            feedAsumConfirmBean.setFeedOverDuration(System.currentTimeMillis() - filterPress.getFeedStartTime());
            List<String> feedPumpCodes = getKeyByValueFromMap(filterPressPumpMapping,filterPress.getCode());
            String feedPumpCode = feedPumpCodes.get(0);
            if(feedPumpCodes.size() == 0){
                throw new SysException("feedPump thingCode is null",SysException.EC_UNKNOWN);
            }
            Optional<DataModelWrapper> currentWrapper = dataService.getData(feedPumpCode,FilterPressMetricConstants.FEED_PUMP_CURRENT);
            Float current = new Float(0);
            if(currentWrapper.isPresent()){
                current = Float.parseFloat(currentWrapper.get().getValue());
            }
            feedAsumConfirmBean.setFeedOverCurrent(current);
            filterPress.setFeedPumpCurrent(current);
            int term = filterPressTerm.get(filterPress.getCode());
            if(term == TERM1){
                messagingTemplate.convertAndSend(FEED_OVER_NOTICE_URI_TERM1, feedAsumConfirmBean);
            }else if(term == TERM2){
                messagingTemplate.convertAndSend(FEED_OVER_NOTICE_URI_TERM2, feedAsumConfirmBean);
            }
            unconfirmedFeed.add(filterPress.getCode());
        }
    }

    public List<String> getKeyByValueFromMap(Map<String,String> map,String value){
        List<String> keys = new ArrayList<>();
        for(Map.Entry<String,String> entry:map.entrySet()){
            if(map.get(entry.getKey()).equals(value)){
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    private void doFeedOver(FilterPress filterPress) {
        DataModel dataModel = new DataModel();
        dataModel.setMetricCode(FilterPressMetricConstants.FEED_OVER);
        dataModel.setThingCode(filterPress.getCode());
        dataModel.setValue(Boolean.TRUE.toString());
        cmdControlService.sendPulseCmdBoolByShort(dataModel,RETRY_PERIOD,RETRY_COUNT,RequestIdUtil.generateRequestId(),POSITION_FEED_OVER,CLAEN_PERIOD,IS_HOLDING_FEED_OVER);

        List<String> feedPumpCodes = getKeyByValueFromMap(filterPressPumpMapping,filterPress.getCode());
        String feedPumpCode = feedPumpCodes.get(0);
        if(feedPumpCodes.size() == 0){
            throw new SysException("feedPump thingCode is null",SysException.EC_UNKNOWN);
        }
        Optional<DataModelWrapper> currentWrapper = dataService.getData(feedPumpCode,FilterPressMetricConstants.FEED_PUMP_CURRENT);
        Float current = new Float(0);
        if(currentWrapper.isPresent()){
            current = Float.parseFloat(currentWrapper.get().getValue());
        }
        filterPress.setFeedPumpCurrent(current);
    }

    /**
     * 更新缓存及数据库中进料的自动手动确认状态
     *
     * @param thingCode
     * @param state
     */
    public void feedAutoManuConfirmChange(String thingCode, boolean state,int term) {
        boolean preState;
        if (thingCode != null) {
            FilterPress filterPress = deviceHolder.get(thingCode);
            preState = filterPress.isFeedConfirmNeed();
            if (preState != state) {
                filterPress.setFeedConfirmNeed(state);
                filterPressMapper.updateFilterParamValue(thingCode, FilterPress.PARAM_NAME_FEEDCONFIRMNEED,
                        state ? 1.0 : 0.0);
            }
        } else {
            updateFeedConfig(term,state);
        }
    }

    private void updateFeedConfig(int term,boolean state){
        boolean preState;
        Map<String,FilterPress> map = getFilterPress(term);
        for (Map.Entry<String, FilterPress> entry : map.entrySet()) {
            FilterPress filterPress = entry.getValue();
            preState = filterPress.isFeedConfirmNeed();
            if (preState != state) {
                filterPress.setFeedConfirmNeed(state);
                filterPressMapper.updateFilterParamValue(entry.getKey(), FilterPress.PARAM_NAME_FEEDCONFIRMNEED,
                        state ? 1.0 : 0.0);
            }
        }
    }

    private Map<String,FilterPress> getFilterPress(int term){
        Map<String,FilterPress> map = new HashMap<>();
        for(Map.Entry<String, FilterPress> entry : deviceHolder.entrySet()){
            if(filterPressTerm.get(entry.getKey()) == term){
                map.put(entry.getKey(),entry.getValue());
            }
        }
        return map;
    }

    private Set<String> getFilterPressThingCodes(int term){
        Set<String> thingCodes = new HashSet<>();
        for(Map.Entry<String, FilterPress> entry : deviceHolder.entrySet()){
            if(filterPressTerm.get(entry.getKey()) == term){
                thingCodes.add(entry.getKey());
            }
        }
        return thingCodes;
    }
    /**
     * 更新缓存及数据库中进料的智能手动状态
     *
     * @param thingCode
     * @param state
     */
    public void feedIntelligentManuChange(String thingCode, boolean state,int term) {
        boolean preState;
        if (thingCode != null) {
            FilterPress filterPress = deviceHolder.get(thingCode);
            preState = filterPress.isFeedIntelligent();
            if (preState != state) {
                filterPress.setFeedIntelligent(state);
                filterPressMapper.updateFilterParamValue(thingCode, FilterPress.PARAM_NAME_FEEDINTELLIGENT,
                        state ? 1.0 : 0.0);
            }
        } else {
            updateIntelligentManuConfig(term,state);
        }
    }

    private void updateIntelligentManuConfig(int term,boolean state){
        boolean preState;
        Map<String,FilterPress> map = getFilterPress(term);
        for (Map.Entry<String, FilterPress> entry : map.entrySet()) {
            FilterPress filterPress = entry.getValue();
            preState = filterPress.isFeedIntelligent();
            if (preState != state) {
                filterPress.setFeedIntelligent(state);
                filterPressMapper.updateFilterParamValue(entry.getKey(), FilterPress.PARAM_NAME_FEEDINTELLIGENT,
                        state ? 1.0 : 0.0);
            }
        }
    }

    /**
     * 获取进料系统自动/手动确认状态
     *
     * @return
     */
    public boolean getFeedAutoManuConfirmState(int term) {
        boolean state = false;
        Map<String,FilterPress> map = getFilterPress(term);
        for (Map.Entry<String, FilterPress> entry : map.entrySet()) {
            state = entry.getValue().isFeedConfirmNeed();
            break;
        }
        return state;
    }

    /**
     * 获取进料智能/手动状态
     *
     * @return
     */
    public Map<String, Boolean> getFeedIntelligentManuStateMap(int term) {
        Map<String, Boolean> intelligentManuStateMap = new HashMap<>();
        Map<String,FilterPress> map = getFilterPress(term);
        for (Map.Entry<String, FilterPress> entry : map.entrySet()) {
            FilterPress filterPress = entry.getValue();
            boolean state = filterPress.isFeedIntelligent();
            intelligentManuStateMap.put(entry.getKey(), state);
        }
        return intelligentManuStateMap;
    }

    /**
     * 更新缓存及数据库中卸料的自动手动确认状态
     *
     * @param thingCode
     * @param state
     */
    public void unloadAutoManuConfirmChange(String thingCode, boolean state,int term) {
        boolean preState;
        if (thingCode != null) {
            FilterPress filterPress = deviceHolder.get(thingCode);
            preState = filterPress.isUnloadConfirmNeed();
            if (preState != state) {
                filterPress.setUnloadConfirmNeed(state);
                filterPressMapper.updateFilterParamValue(thingCode, FilterPress.PARAM_NAME_UNLOADCONFIRMNEED,
                        state ? 1.0 : 0.0);
            }
        } else {
            updateUnloadConfig(term,state);
        }
    }

    private void updateUnloadConfig(int term,boolean state){
        boolean preState;
        Map<String,FilterPress> map = getFilterPress(term);
        for (Map.Entry<String, FilterPress> entry : map.entrySet()) {
            FilterPress filterPress = entry.getValue();
            preState = filterPress.isUnloadConfirmNeed();
            if (preState != state) {
                filterPress.setUnloadConfirmNeed(state);
                filterPressMapper.updateFilterParamValue(entry.getKey(), FilterPress.PARAM_NAME_UNLOADCONFIRMNEED,
                        state ? 1.0 : 0.0);
            }
        }
    }

    /**
     * 更新缓存及数据库中卸料的智能手动状态
     *
     * @param thingCode
     * @param state
     */
    public void unloadIntelligentManuChange(String thingCode, boolean state,int term) {
        boolean preState;
        if (thingCode != null) {
            FilterPress filterPress = deviceHolder.get(thingCode);
            preState = filterPress.isUnloadIntelligent();
            if (preState != state) {
                filterPress.setUnloadIntelligent(state);
                filterPressMapper.updateFilterParamValue(thingCode, FilterPress.PARAM_NAME_UNLOADINTELLIGENT,
                        state ? 1.0 : 0.0);
            }
        } else {
            updateUnloadIntelligentManuConfig(term,state);
        }
    }

    private void updateUnloadIntelligentManuConfig(int term,boolean state){
        boolean preState;
        Map<String,FilterPress> map = getFilterPress(term);
        for (Map.Entry<String, FilterPress> entry : map.entrySet()) {
            FilterPress filterPress = entry.getValue();
            preState = filterPress.isUnloadIntelligent();
            if (preState != state) {
                filterPress.setUnloadIntelligent(state);
                filterPressMapper.updateFilterParamValue(entry.getKey(), FilterPress.PARAM_NAME_UNLOADINTELLIGENT,
                        state ? 1.0 : 0.0);
            }
        }
    }

    /**
     * 获取卸料系统自动/手动确认状态
     *
     * @return
     */
    public boolean getUnloadAutoManuConfirmState(int term) {
        boolean state = false;
        Map<String,FilterPress> map = getFilterPress(term);
        for (Map.Entry<String, FilterPress> entry : map.entrySet()) {
            state = entry.getValue().isUnloadConfirmNeed();
            break;
        }
        return state;
    }

    /**
     * 获取卸料智能/手动状态
     *
     * @return
     */
    public Map<String, Boolean> getUnloadIntelligentManuStateMap(int term) {
        Map<String, Boolean> intelligentManuStateMap = new HashMap<>();
        Map<String,FilterPress> map = getFilterPress(term);
        for (Map.Entry<String, FilterPress> entry : map.entrySet()) {
            FilterPress filterPress = entry.getValue();
            boolean state = filterPress.isUnloadIntelligent();
            intelligentManuStateMap.put(entry.getKey(), state);
        }
        return intelligentManuStateMap;
    }

    public void updateMaxUnloadParallel(int num,int term) {
        if(term == TERM1){
            setMaxUnloadParallelTerm1(num);
            filterPressMapper.updateFilterParamValue(PARAM_NAME_SYS_TERM1, FilterPress.PARAM_NAME_MAXUNLOADPARALLEL,
                    (double) num);
        }else if(term == TERM2){
            setMaxUnloadParallel(num);
            filterPressMapper.updateFilterParamValue(PARAM_NAME_SYS, FilterPress.PARAM_NAME_MAXUNLOADPARALLEL,
                    (double) num);
        }
    }

    /**
     * 获取一段时间内的电流最大值、最小值及平均值
     *
     * @return
     */
    public Map<String, FilterPressElectricity> getCurrentInfoInDuration() {
        Date endTime = DateUtils.truncate(new Date(), Calendar.HOUR_OF_DAY);
        Date startTime = DateUtils.addDays(endTime, CURRENT_COUNT_DURATION);
        // return filterPressMapper.getCurrentInfoInDuration(startTime, endTime);
        // TODO get real data
        return new HashMap<>();
    }

    /**
     * 获取卸料次序
     *
     * @return
     */
    public Set<String> getUnloadSequence(int term) {
        if(term == TERM1){
            return unloadManager.getQueuePositionTerm1();
        }else if(term == TERM2){
            return unloadManager.getQueuePositionTerm2();
        }
        return null;
    }

    public List<String> getUnConfirmedUnload(int term) {
        if(term == TERM1){
            return unConfirmedUnloadTerm1;
        }else if(term == TERM2){
            return unConfirmedUnloadTerm2;
        }
        return null;
    }

    public String getFirstUnConfirmedUnload() {
        String thingCode = null;
        if(unConfirmedUnloadTerm2.size() > 0){
            thingCode = unConfirmedUnloadTerm2.get(0);
        }
        return thingCode;
    }

    public String getFirstUnConfirmedUnloadTerm1() {
        String thingCode = null;
        if(unConfirmedUnloadTerm1.size() > 0){
            thingCode = unConfirmedUnloadTerm1.get(0);
        }
        return thingCode;
    }

    public Map<String, FilterPressLogBean> getStatisticLogs() {
        return statisticLogs;
    }

    public Set<String> getAllFilterPressCode(int term){
        return getFilterPressThingCodes(term);
    }

    public Map<String, String> getFilterPressPumpMapping() {
        return filterPressPumpMapping;
    }

    public void printQueueData(BlockingQueue<FilterPress> queue){
        for(FilterPress filterPress:queue){
            if(logger.isDebugEnabled()){
                logger.debug("filterpress:{} in queue",filterPress.getCode());
            }
        }
    }

    class UnloadManager {
        private AtomicInteger unloading = new AtomicInteger(0);
        private volatile int maxUnloadParallel = 1;
        private volatile int maxUnloadParallelTerm1 = 1;
        private volatile int unloadingCount = 0;
        /**
         * 二期排队队列
         */
        BlockingQueue<FilterPress> queueTerm2 = new PriorityBlockingQueue<>(INIT_CAPACITY, (f1, f2) -> {
            int result;
            if (f1.getOnCycleTime() < f2.getOnCycleTime()) {
                result = -1;
            } else if (f1.getOnCycleTime() > f2.getOnCycleTime()) {
                result = 1;
            } else {
                result = 0;
            }
            return result;
        });

        /**
         * 一期排队队列
         */
        BlockingQueue<FilterPress> queueTerm1 = new PriorityBlockingQueue<>(INIT_CAPACITY, (f1, f2) -> {
            int result;
            if (f1.getOnCycleTime() < f2.getOnCycleTime()) {
                result = -1;
            } else if (f1.getOnCycleTime() > f2.getOnCycleTime()) {
                result = 1;
            } else {
                result = 0;
            }
            return result;
        });

        BlockingQueue<FilterPress> getQueue(int term) {
            if(term == TERM1){
                return queueTerm1;
            }else if(term == TERM2){
                return queueTerm2;
            }
            return null;
        }


        /**
         * 二期卸料替代排队Set
         */
        private Set<String> queuePositionTerm2 = new ConcurrentSkipListSet<>();


        /**
         * 一期卸料替代排队Set
         */
        private Set<String> queuePositionTerm1 = new ConcurrentSkipListSet<>();

        public BlockingQueue<FilterPress> getQueueTerm1() {
            return queueTerm1;
        }

        public Set<String> getQueuePositionTerm2() {
            return queuePositionTerm2;
        }

        public Set<String> getQueuePositionTerm1() {
            return queuePositionTerm1;
        }

        public void setQueueTerm1(BlockingQueue<FilterPress> queueTerm1) {
            this.queueTerm1 = queueTerm1;
        }

        public void setQueuePositionTerm2(Set<String> queuePositionTerm2) {
            this.queuePositionTerm2 = queuePositionTerm2;
        }

        public void setQueuePositionTerm1(Set<String> queuePositionTerm1) {
            this.queuePositionTerm1 = queuePositionTerm1;
        }


        /**
         * 加入卸料排队
         *
         * @param filterPress
         */
        private void enqueue(FilterPress filterPress) {
            if(filterPressTerm.get(filterPress.getCode())== TERM1){
                fpEnqueue(filterPress,queuePositionTerm1,queueTerm1,TERM1);
            }else if(filterPressTerm.get(filterPress.getCode())== TERM2){
                fpEnqueue(filterPress,queuePositionTerm2,queueTerm2,TERM2);
            }

        }

        private void fpEnqueue(FilterPress filterPress,Set<String> queuePositionTerm, BlockingQueue<FilterPress> queue,int term){
            if(!queuePositionTerm.contains(filterPress.getCode())){
                queuePositionTerm.add(filterPress.getCode());
            }
            if(logger.isDebugEnabled()){
                logger.debug("filterPress:" + filterPress.getCode() + " enqueue,position:" + (queuePositionTerm.size()));
            }
            if(!queue.contains(filterPress)){
                queue.add(filterPress);
                printQueueData(queue);
                unloadNextIfPossible(term);
            }
        }

        private synchronized void unloadNext(int term) {
            unloading.getAndDecrement();
            unloadNextIfPossible(term);
        }

        /**
         * 若存在可以卸料的压滤机，则按照最大同时卸料数量进行卸料调度
         */
        private synchronized void unloadNextIfPossible(int term) {
            int unloadingCount = getUnloadingCount(null,term);
            logger.debug("正在卸料台数：" + unloadingCount);
            if((term == TERM1) && (unloadingCount < maxUnloadParallelTerm1)){
                FilterPress candidate = queueTerm1.peek();
                if (candidate != null) {
                    execUnload(candidate,term);
                    unloading.getAndIncrement(); }
            }else if((term == TERM2) && (unloadingCount < maxUnloadParallel)){
                FilterPress candidate = queueTerm2.peek();
                if (candidate != null) {
                    execUnload(candidate,term);
                    unloading.getAndIncrement(); }
            }

        }

        private synchronized void execUnload(FilterPress filterPress,int term) {
            if (!filterPress.isUnloadConfirmNeed()) {
                logger.debug("{} unload, send cmd; confirmNeed: {}", filterPress, filterPress.isUnloadConfirmNeed());
                doUnload(filterPress);
            } else {
                logger.debug("{} unload, notifying user; confirmNeed: {}", filterPress,
                        filterPress.isUnloadConfirmNeed());
                if(term == TERM1){
                    messagingTemplate.convertAndSend(UNLOAD_NOTICE_URI_TERM1, filterPress.getCode());
                    if(!unConfirmedUnloadTerm1.contains(filterPress.getCode())){
                        unConfirmedUnloadTerm1.add(filterPress.getCode());
                    }
                }else if(term == TERM2){
                    messagingTemplate.convertAndSend(UNLOAD_NOTICE_URI_TERM2, filterPress.getCode());
                    if(!unConfirmedUnloadTerm2.contains(filterPress.getCode())){
                        unConfirmedUnloadTerm2.add(filterPress.getCode());
                    }
                }
            }
        }

        /**
         * 执行卸料
         *
         * @param filterPress
         */
        private synchronized void doUnload(FilterPress filterPress) {
            DataModel cmd = new DataModel();
            cmd.setThingCode(filterPress.getCode());
            cmd.setMetricCode(FilterPressMetricConstants.RUN);
            cmd.setValue(Boolean.TRUE.toString());
            cmdControlService.sendPulseCmdBoolByShort(cmd,RETRY_PERIOD,RETRY_COUNT,RequestIdUtil.generateRequestId(),POSITION_RUN,CLAEN_PERIOD,IS_HOLDING_RUN);
        }


        /**
         * 获取所有正在卸料压滤机数量，正在卸料指压滤机处于松开状态或者取板拉板次数小于16次,
         * 排除参数中的压滤机，因为在调用这个接口时是本台压滤机状态处于压紧状态或取板拉板次数大于16次
         */

        public synchronized int getUnloadingCount(String thingCode,int term){
            int unloadingCount = 0;
            Map<String,FilterPress> map = getFilterPress(term);
            for(FilterPress filterPress:map.values()){
                if(StringUtils.isBlank(thingCode)){
                    if(filterPress.isFilterPressUnloading()) {
                        unloadingCount++;
                    }
                }else{
                    if(filterPress.isFilterPressUnloading() && (!filterPress.getCode().equals(thingCode))){
                        unloadingCount++;
                    }
                }
            }
            return unloadingCount;
        }
    }
}
