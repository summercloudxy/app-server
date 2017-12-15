package com.zgiot.app.server.module.filterpress;

import com.zgiot.app.server.module.filterpress.dao.FilterPressMapper;
import com.zgiot.app.server.module.filterpress.pojo.FeedAsumConfirmBean;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressElectricity;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.module.filterpress.filterPressService.FilterPressLogService;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.app.server.util.RequestIdUtil;
import com.zgiot.common.constants.*;
import com.zgiot.common.enums.MetricDataTypeEnum;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.pojo.MetricModel;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class FilterPressManager {
    private static final Logger logger = LoggerFactory.getLogger(FilterPressManager.class);
    private static final String FEED_OVER_NOTICE_URI = "/topic/filterPress/feedOver";
    private static final String FEED_OVER_CONFIRMED_NOTICE_URI = "/topic/filterPress/feedOver/confirm";
    private static final String UNLOAD_NOTICE_URI = "/topic/filterPress/unload";
    private static final String UNLOAD_CONFIRMED_NOTICE_URI = "/topic/filterPress/unload/confirm";
    private static final String PARAM_NAME_SYS = "sys";

    private static final int POSITION_FEED_OVER = 5;
    private static final int POSITION_RUN = 1;
    private static final int CLAEN_PERIOD = 0;
    private static final boolean IS_HOLDING_FEED_OVER = false;
    private static final boolean IS_HOLDING_RUN = false;
    private static final Map<String,String> filterPressStage = new HashMap<>();

    @Autowired
    DataService dataService;
    @Autowired
    HistoryDataService historyDataService;
    @Autowired
    private CmdControlService cmdControlService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private FilterPressMapper filterPressMapper;

    @Autowired
    FilterPressLogService filterPressLogService;

    public UnloadManager getUnloadManager() {
        return unloadManager;
    }

    private static final int INIT_CAPACITY = 6;

    private static final Integer CURRENT_COUNT_DURATION = -3;

    private Map<String, FilterPress> deviceHolder = new ConcurrentHashMap<>();

    private Map<String, String> filterPressPumpMapping = new HashMap<>();

    private Set<String> unconfirmedFeed = new ConcurrentSkipListSet<>();

    //private Set<String> unConfirmedUnload = new ConcurrentSkipListSet<>();
    private List<String> unConfirmedUnload = Collections.synchronizedList(new ArrayList<>());

    private UnloadManager unloadManager = new UnloadManager();

    Map<String,FilterPressLogBean> statisticLogs = new ConcurrentHashMap<>();

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
    void initFilterPress() {
        deviceHolder.put("2492", new FilterPress("2492", this));
        deviceHolder.put("2493", new FilterPress("2493", this));
        deviceHolder.put("2494", new FilterPress("2494", this));
        deviceHolder.put("2495", new FilterPress("2495", this));
        deviceHolder.put("2496", new FilterPress("2496", this));
        deviceHolder.put("2496A", new FilterPress("2496A", this));
        filterPressPumpMapping.put("2487", "2492");
        filterPressPumpMapping.put("2488", "2493");
        filterPressPumpMapping.put("2489", "2494");
        filterPressPumpMapping.put("2490", "2495");
        filterPressPumpMapping.put("2491", "2496");
        filterPressPumpMapping.put("2491A", "2496A");
        setMaxUnloadParallel(filterPressMapper
                .selectParamValue(PARAM_NAME_SYS, FilterPress.PARAM_NAME_MAXUNLOADPARALLEL).intValue());
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

    /**
     * call back when data changed
     *
     * @param data
     */
    public void onDataSourceChange(DataModel data) {
        String thingCode = data.getThingCode();
        FilterPress filterPress = null;
        if(deviceHolder.containsKey(thingCode)){
            filterPress = deviceHolder.get(thingCode);
        }else if(filterPressPumpMapping.containsKey(thingCode)){
            filterPress = deviceHolder.get(filterPressPumpMapping.get(thingCode));
        }else{
            return;
            //throw new SysException("filterPress is null",SysException.EC_UNKOWN);
        }

        if(!statisticLogs.containsKey(thingCode)){
            statisticLogs.put(thingCode,new FilterPressLogBean());
        }
        String metricCode = data.getMetricCode();
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
    }

    public void calculatePlateAndSave() {
        int total = 0;
        for (Map.Entry<String, FilterPress> entry : deviceHolder.entrySet()) {
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

        for (Map.Entry<String, FilterPress> entry : deviceHolder.entrySet()) {
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

    public void confirmFeedOver(String code) {
        if (unconfirmedFeed.remove(code)) {
            doFeedOver(getFilterPress(code));
            messagingTemplate.convertAndSend(FEED_OVER_CONFIRMED_NOTICE_URI, code);
        }
    }

    public void confirmUnload(String code) {
        if (unConfirmedUnload.remove(code)) {
            unloadManager.doUnload(getFilterPress(code));
            messagingTemplate.convertAndSend(UNLOAD_CONFIRMED_NOTICE_URI, code);
        }
    }

    private void processFeedAssumption(DataModel data) {
        if (String.valueOf(FilterPressConstants.FEED_OVER_CURRENT).equals(data.getValue())
                || String.valueOf(FilterPressConstants.FEED_OVER_TIME).equals(data.getValue())) {
            String pumpCode = data.getThingCode();
            getFilterPress(filterPressPumpMapping.get(pumpCode)).onAssumeFeedOver();
        }
    }

    /**
     * process the stage data and call the specific method of filter press
     *
     * @param data
     */
    private void processStage(DataModel data) {
        String thingCode = data.getThingCode();
        String metricCodeValue = data.getValue();
        String metricCode = data.getMetricCode();
        FilterPress filterPress = getFilterPress(thingCode);
        Boolean isRunning = Boolean.FALSE;
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
                    isRunning = Boolean.TRUE;
                }else{
                    filterPress.offLoosen();
                }
                break;
            case FilterPressMetricConstants.RO_TAKE:
                if(Boolean.parseBoolean(metricCodeValue)){
                    filterPress.onTaken();
                    isRunning = Boolean.TRUE;
                }else{
                    filterPress.offTaken();
                }
                break;
            case FilterPressMetricConstants.RO_PULL:
                if(Boolean.parseBoolean(metricCodeValue)){
                    filterPress.onPull();
                    isRunning = Boolean.TRUE;
                }else{
                    filterPress.offPull();
                }
                break;
            case FilterPressMetricConstants.RO_PRESS:
                if(Boolean.parseBoolean(metricCodeValue)){
                    filterPress.onPress();
                    isRunning = Boolean.TRUE;
                }
                break;
            case FilterPressMetricConstants.RO_FEEDING:
                if(Boolean.parseBoolean(metricCodeValue)){
                    filterPress.onFeed();
                    isRunning = Boolean.TRUE;
                }else{
                    filterPress.offFeed();
                }
                break;
            case FilterPressMetricConstants.RO_FEED_OVER:
                if(Boolean.parseBoolean(metricCodeValue)){
                    filterPress.onFeedOver();
                    isRunning = Boolean.TRUE;
                }
                break;
            case FilterPressMetricConstants.RO_BLOW:
                if(Boolean.parseBoolean(metricCodeValue)){
                    filterPress.onBlow();
                    isRunning = Boolean.TRUE;
                }
                break;
            case FilterPressMetricConstants.RO_HOLD_PRESS:
                if(Boolean.parseBoolean(metricCodeValue)){
                    isRunning = Boolean.TRUE;
                }
                break;
            case FilterPressMetricConstants.RO_SQUEEZE_OVER:
                if(Boolean.parseBoolean(metricCodeValue)){
                    isRunning = Boolean.TRUE;
                }
                break;
            case FilterPressMetricConstants.RO_SQUEEZE:
                if(Boolean.parseBoolean(metricCodeValue)){
                    isRunning = Boolean.TRUE;
                }
                break;
            case FilterPressMetricConstants.RO_EMPTYING:
                if(Boolean.parseBoolean(metricCodeValue)){
                    isRunning = Boolean.TRUE;
                }
                break;
            case FilterPressMetricConstants.RO_CYCLE:
                if(Boolean.parseBoolean(metricCodeValue)){
                    filterPress.onCycle();
                    isRunning = Boolean.TRUE;
                }
                break;
            default:
        }
        // calculate the state value and call the specific method of filter press
        short stateValue = calculateState(thingCode, isRunning);
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
     * @param isRunning
     * @return
     */
    private short calculateState(String thingCode, Boolean isRunning) {
        short state;
        DataModelWrapper fault = dataService.getData(thingCode, FilterPressMetricConstants.FAULT)
                .orElse(new DataModelWrapper(new DataModel(null, thingCode, null, FilterPressMetricConstants.FAULT, Boolean.FALSE.toString(), new Date())));
        Boolean isRunningFromCache = isRunningFromCache(thingCode);
        if (Boolean.valueOf(fault.getValue())) {
            state = GlobalConstants.STATE_FAULT;
        } else if (Boolean.FALSE.toString().equals(isRunning) && (!isRunningFromCache)) {
            state = GlobalConstants.STATE_STOPPED;
        } else {
            state = GlobalConstants.STATE_RUNNING;
        }
        return state;
    }

    private boolean isRunningFromCache(String thingCode){
        Boolean isRunning = Boolean.FALSE;
        Optional<DataModelWrapper> data = null;
        for(String value:filterPressStage.values()){
            data = dataService.getData(thingCode,value);
            if(data != null && data.isPresent() && Boolean.parseBoolean(data.get().getValue())){
                isRunning = Boolean.TRUE;
                break;
            }
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

    void unloadNext() {
        unloadManager.unloadNext();
    }

    public int getMaxUnloadParallel() {
        return unloadManager.maxUnloadParallel;
    }

    public void setMaxUnloadParallel(int maxUnloadParallel) {
        this.unloadManager.maxUnloadParallel = maxUnloadParallel;
    }

    public void removeQueue(String thingCode,Boolean state){
        if(!state){
            int position = getUnloadSequence().get(thingCode);
            unloadManager.queue.remove(deviceHolder.get(thingCode));
            unloadManager.queuePosition.remove(thingCode);
            try{
                unConfirmedUnload.remove(thingCode);
            }catch (NullPointerException e){
                throw new SysException("未确定卸料set中不存在这台压滤机thingCode",SysException.EC_UNKNOWN);
            }
            if(position > 0){
                unloadManager.reSort(position);
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
            messagingTemplate.convertAndSend(FEED_OVER_NOTICE_URI, feedAsumConfirmBean);
            unconfirmedFeed.add(filterPress.getCode());
        }
    }

    public List<String> getKeyByValueFromMap(Map<String,String> map,String value){
        List<String> keys = new ArrayList<>();
        for(String key:map.keySet()){
            if(map.get(key).equals(value)){
                keys.add(key);
            }
        }
        return keys;
    }

    private void doFeedOver(FilterPress filterPress) {
        DataModel dataModel = new DataModel();
        dataModel.setMetricCode(FilterPressMetricConstants.FEED_OVER);
        dataModel.setThingCode(filterPress.getCode());
        dataModel.setValue(Boolean.TRUE.toString());
        cmdControlService.sendPulseCmdBoolByShort(dataModel,null,null,RequestIdUtil.generateRequestId(),POSITION_FEED_OVER,CLAEN_PERIOD,IS_HOLDING_FEED_OVER);

        //filterPress.setFeedDuration(System.currentTimeMillis() - filterPress.getFeedStartTime());
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
    public void feedAutoManuConfirmChange(String thingCode, boolean state) {
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
            for (Map.Entry<String, FilterPress> entry : deviceHolder.entrySet()) {
                FilterPress filterPress = entry.getValue();
                preState = filterPress.isFeedConfirmNeed();
                if (preState != state) {
                    filterPress.setFeedConfirmNeed(state);
                    filterPressMapper.updateFilterParamValue(entry.getKey(), FilterPress.PARAM_NAME_FEEDCONFIRMNEED,
                            state ? 1.0 : 0.0);
                }
            }
        }
    }

    /**
     * 更新缓存及数据库中进料的智能手动状态
     *
     * @param thingCode
     * @param state
     */
    public void feedIntelligentManuChange(String thingCode, boolean state) {
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
            for (Map.Entry<String, FilterPress> entry : deviceHolder.entrySet()) {
                FilterPress filterPress = entry.getValue();
                preState = filterPress.isFeedIntelligent();
                if (preState != state) {
                    filterPress.setFeedIntelligent(state);
                    filterPressMapper.updateFilterParamValue(entry.getKey(), FilterPress.PARAM_NAME_FEEDINTELLIGENT,
                            state ? 1.0 : 0.0);
                }
            }
        }
    }

    /**
     * 获取进料系统自动/手动确认状态
     *
     * @return
     */
    public boolean getFeedAutoManuConfirmState() {
        boolean state = false;
        for (Map.Entry<String, FilterPress> entry : deviceHolder.entrySet()) {
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
    public Map<String, Boolean> getFeedIntelligentManuStateMap() {
        Map<String, Boolean> intelligentManuStateMap = new HashMap<>();
        for (Map.Entry<String, FilterPress> entry : deviceHolder.entrySet()) {
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
    public void unloadAutoManuConfirmChange(String thingCode, boolean state) {
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
            for (Map.Entry<String, FilterPress> entry : deviceHolder.entrySet()) {
                FilterPress filterPress = entry.getValue();
                preState = filterPress.isUnloadConfirmNeed();
                if (preState != state) {
                    filterPress.setUnloadConfirmNeed(state);
                    filterPressMapper.updateFilterParamValue(entry.getKey(), FilterPress.PARAM_NAME_UNLOADCONFIRMNEED,
                            state ? 1.0 : 0.0);
                }
            }
        }
    }

    /**
     * 更新缓存及数据库中卸料的智能手动状态
     *
     * @param thingCode
     * @param state
     */
    public void unloadIntelligentManuChange(String thingCode, boolean state) {
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
            for (Map.Entry<String, FilterPress> entry : deviceHolder.entrySet()) {
                FilterPress filterPress = entry.getValue();
                preState = filterPress.isUnloadIntelligent();
                if (preState != state) {
                    filterPress.setUnloadIntelligent(state);
                    filterPressMapper.updateFilterParamValue(entry.getKey(), FilterPress.PARAM_NAME_UNLOADINTELLIGENT,
                            state ? 1.0 : 0.0);
                }
            }
        }
    }

    /**
     * 获取卸料系统自动/手动确认状态
     *
     * @return
     */
    public boolean getUnloadAutoManuConfirmState() {
        boolean state = false;
        for (Map.Entry<String, FilterPress> entry : deviceHolder.entrySet()) {
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
    public Map<String, Boolean> getUnloadIntelligentManuStateMap() {
        Map<String, Boolean> intelligentManuStateMap = new HashMap<>();
        for (Map.Entry<String, FilterPress> entry : deviceHolder.entrySet()) {
            FilterPress filterPress = entry.getValue();
            boolean state = filterPress.isUnloadIntelligent();
            intelligentManuStateMap.put(entry.getKey(), state);
        }
        return intelligentManuStateMap;
    }

    public void updateMaxUnloadParallel(int num) {
        setMaxUnloadParallel(num);
        filterPressMapper.updateFilterParamValue(PARAM_NAME_SYS, FilterPress.PARAM_NAME_MAXUNLOADPARALLEL,
                (double) num);
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
    public Map<String, Integer> getUnloadSequence() {
        return unloadManager.getQueuePosition();
    }

    public List<String> getUnConfirmedUnload() {
        return unConfirmedUnload;
    }

    public String getFirstUnConfirmedUnload() {
        String thingCode = null;
        if(unConfirmedUnload.size() > 0){
            thingCode = unConfirmedUnload.get(0);
        }
        return thingCode;
    }

    public Map<String, FilterPressLogBean> getStatisticLogs() {
        return statisticLogs;
    }

    public Set<String> getAllFilterPressCode(){
        return deviceHolder.keySet();
    }

    public Map<String, String> getFilterPressPumpMapping() {
        return filterPressPumpMapping;
    }

    // @Scheduled(cron="cnmt.FilterPressDeviceManager.clear")
    // /**
    // * 手动弹出模式下，超过一段时间不操作后自动进行确认
    // */
    // public void clear() {
    // for (String thingCode : unconfirmedFeed) {
    // if (System.currentTimeMillis() -
    // deviceHolder.get(thingCode).getFeedOverTime() > cacheTimeout) {
    // messagingTemplate.convertAndSend(FEED_OVER_CONFIRMED_NOTICE_URI, thingCode);
    // unconfirmedFeed.remove(thingCode);
    // }
    // }
    // }

    class UnloadManager {
        private AtomicInteger unloading = new AtomicInteger(0);
        private volatile int maxUnloadParallel = 1;
        private volatile int unloadingCount = 0;
        BlockingQueue<FilterPress> queue = new PriorityBlockingQueue<>(INIT_CAPACITY, (f1, f2) -> {
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

        BlockingQueue<FilterPress> getQueue() {
            return queue;
        }


        private Map<String, Integer> queuePosition = new ConcurrentHashMap<>();

        public Map<String, Integer> getQueuePosition() {
            return queuePosition;
        }

        /**
         * 加入卸料排队
         *
         * @param filterPress
         */
        synchronized  void enqueue(FilterPress filterPress) {
            queuePosition.put(filterPress.getCode(), queuePosition.size() + 1);
            queue.add(filterPress);
            unloadNextIfPossible();
        }

        private synchronized void unloadNext() {
            unloading.getAndDecrement();
            unloadNextIfPossible();
        }

        /**
         * 若存在可以卸料的压滤机，则按照最大同时卸料数量进行卸料调度
         */
        private synchronized void unloadNextIfPossible() {
            for (int i = unloading.get(); i < maxUnloadParallel; i++) {
                FilterPress candidate = queue.peek();
                if (candidate == null) {
                    break;
                }
                execUnload(candidate);
                unloading.getAndIncrement();
            }
        }

        private synchronized void execUnload(FilterPress filterPress) {
            if (!filterPress.isUnloadConfirmNeed()) {
                logger.debug("{} unload, send cmd; confirmNeed: {}", filterPress, filterPress.isUnloadConfirmNeed());
                doUnload(filterPress);
            } else {
                logger.debug("{} unload, notifying user; confirmNeed: {}", filterPress,
                        filterPress.isUnloadConfirmNeed());
                messagingTemplate.convertAndSend(UNLOAD_NOTICE_URI, filterPress.getCode());
                if(!unConfirmedUnload.contains(filterPress.getCode())){
                    unConfirmedUnload.add(filterPress.getCode());
                }
            }
        }

        /**
         * 执行卸料
         *
         * @param filterPress
         */
        private synchronized void doUnload(FilterPress filterPress) {
//            filterPress.startUnload();
//            queuePosition.remove(filterPress.getCode());
//            reSort();
            DataModel cmd = new DataModel();
            cmd.setThingCode(filterPress.getCode());
            cmd.setMetricCode(FilterPressMetricConstants.RUN);
            cmd.setValue(Boolean.TRUE.toString());
            cmdControlService.sendPulseCmdBoolByShort(cmd,null,null,RequestIdUtil.generateRequestId(),POSITION_RUN,CLAEN_PERIOD,IS_HOLDING_RUN);
        }

        /**
         * 卸料次序递减
         */
        private void countDownPosition() {
            queuePosition.forEach((code, seq) -> queuePosition.replace(code, seq - 1));
        }

        public synchronized void reSort(int position){
            for(String thingCode:queuePosition.keySet()){
                if(queuePosition.get(thingCode) > position)
                    queuePosition.put(thingCode, queuePosition.get(thingCode) - 1);
            }
        }

        /**
         * 获取所有正在卸料压滤机数量，正在卸料指压滤机处于松开状态或者取板拉板次数小于16次,
         * 排除参数中的压滤机，因为在调用这个接口时是本台压滤机状态处于压紧状态或取板拉板次数大于16次
         */

        public synchronized int getUnloadingCount(String thingCode){
            int unloadingCount = 0;
            for(FilterPress filterPress:deviceHolder.values()){
                if(filterPress.isFilterPressUnloading() && (!filterPress.getCode().equals(thingCode))){
                    unloadingCount++;
                }
            }
            return unloadingCount;
        }
    }
}
