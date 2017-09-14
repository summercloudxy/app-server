package com.zgiot.app.server.module.filterpress.manager;

import com.zgiot.app.server.module.filterpress.WebSocketDemo;
import com.zgiot.app.server.module.filterpress.dao.FilterPressMapper;
import com.zgiot.app.server.module.filterpress.pojo.FeedOverParam;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressElectricity;
import com.zgiot.app.server.module.filterpress.FilterPress;
import com.zgiot.app.server.service.impl.CmdControlServiceImpl;
import com.zgiot.app.server.util.RequestIdUtil;
import com.zgiot.common.constants.FilterPressConstants;
import com.zgiot.common.pojo.DataModel;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by xiayun on 2017/9/12.
 */
@Component
public class FeedOverManager {
    // 自动/手动状态 key:deviceCode value: 1-auto 0-manu
    private Map<String, Integer> autoManuConfirmStateMap = new ConcurrentHashMap<>();
    // 智能/手动状态 key:deviceCode value: 1-intelligent 0-manu
    private Map<String, Integer> intelligentManuStateMap = new ConcurrentHashMap<>();
    private Map<String, FilterPress> deviceHolder = new ConcurrentHashMap<>();
    // 未确认设备列表
    private Set<String> unConfirmedSet = new CopyOnWriteArraySet<>();

    private static final Integer STATE_AUTO_CONFIRM = 1;
    private static final Integer STATE_MANU_CONFIRM = 0;
    private static final Integer STATE_INTELLIGENT = 1;
    private static final Integer STATE_MANU = 0;
    private static final String FEED_OVER_NOTICE_URI = "/topic/filterPress/feedOver";
    private static final String FEED_OVER_CONFIRMED_NOTICE_URI = "/topic/filterPress/feedOver/confirm";
    private static final Integer FEED_OVER_VALUE = 1;
    private static final Integer CURRENT_COUNT_DURATION = -3;
    // @Value("filterpress.feedover.timeout")
    // private int cacheTimeout;
    @Autowired
    private FilterPressMapper filterPressMapper;
    @Autowired
    private CmdControlServiceImpl cmdControlService;
    private WebSocketDemo webSocketDemo = (uri, object) -> System.out.println("notice test" + uri);

    /**
     * 进料结束时操作
     * 
     * @param thingCode
     */
    public void onfeedOver(String thingCode) {
        Integer intelligentManuState = getIntelligentManuState(thingCode);
        Integer autoManuConfirmState = getAutoManuConfirmState(thingCode);
        FilterPress filterPress = getFilterPressDevice(thingCode);
        Long feedOverTime = new Date().getTime();
        Long feedStartTime = filterPress.getFeedStartTime();
        Long feedDuration = feedOverTime - feedStartTime;
        filterPress.setFeedOverTime(new Date().getTime());
        filterPress.setFeedDuration(feedDuration);
        if (STATE_INTELLIGENT.equals(intelligentManuState)) {
            if (STATE_AUTO_CONFIRM.equals(autoManuConfirmState)) {
                DataModel dataModel = new DataModel();
                dataModel.setMetricCode(FilterPressConstants.FEED_OVER);
                dataModel.setThingCode(thingCode);
                dataModel.setValue(FEED_OVER_VALUE);
                cmdControlService.sendCmd(dataModel, RequestIdUtil.generateRequestId());
            } else {
                webSocketDemo.notice(FEED_OVER_NOTICE_URI, filterPress);
                unConfirmedSet.add(thingCode);
            }
        }
    }

    /**
     * 获取自动手动确认状态
     * 
     * @param thingCode
     * @return
     */
    private Integer getAutoManuConfirmState(String thingCode) {
        if (autoManuConfirmStateMap == null || autoManuConfirmStateMap.size() == 0) {
            initFeedOverSettingStates();
        }
        return autoManuConfirmStateMap.get(thingCode);
    }

    /**
     * 获取智能手动状态
     * 
     * @param thingCode
     * @return
     */
    private Integer getIntelligentManuState(String thingCode) {
        if (intelligentManuStateMap == null || intelligentManuStateMap.size() == 0) {
            initFeedOverSettingStates();
        }
        return intelligentManuStateMap.get(thingCode);
    }

    /**
     * 更新缓存及数据库中的自动手动确认状态
     * 
     * @param thingCode
     * @param state
     */
    public void autoManuConfirmChange(String thingCode, Integer state) {
        Integer preState;
        if (autoManuConfirmStateMap == null || autoManuConfirmStateMap.size() == 0) {
            initFeedOverSettingStates();
        }
        if (thingCode != null) {
            preState = autoManuConfirmStateMap.get(thingCode);
            if (!preState.equals(state)) {
                autoManuConfirmStateMap.put(thingCode, state);
                filterPressMapper.setFilterFeedOverAutoMaunState(thingCode, state);
            }
        } else {
            for (Map.Entry<String, Integer> entry : autoManuConfirmStateMap.entrySet()) {
                preState = entry.getValue();
                if (!preState.equals(state)) {
                    autoManuConfirmStateMap.put(entry.getKey(), state);
                    filterPressMapper.setFilterFeedOverAutoMaunState(entry.getKey(), state);
                }
            }
        }
    }

    /**
     * 更新缓存及数据库中的智能手动状态
     * 
     * @param thingCode
     * @param state
     */
    public void intelligentManuChange(String thingCode, Integer state) {
        Integer preState;
        if (intelligentManuStateMap == null || intelligentManuStateMap.size() == 0) {
            initFeedOverSettingStates();
        }
        if (thingCode != null) {
            preState = intelligentManuStateMap.get(thingCode);
            if (!preState.equals(state)) {
                intelligentManuStateMap.put(thingCode, state);
                filterPressMapper.setFilterFeedOverIntelligentMaunState(thingCode, state);
            }
        } else {
            for (Map.Entry<String, Integer> entry : intelligentManuStateMap.entrySet()) {
                preState = entry.getValue();
                if (!preState.equals(state)) {
                    intelligentManuStateMap.put(entry.getKey(), state);
                    filterPressMapper.setFilterFeedOverIntelligentMaunState(entry.getKey(), state);
                }
            }
        }
    }

    /**
     * 弹窗确认
     * 
     * @param thingCode
     */
    public void feedOverPopupConfirm(String thingCode) {
        DataModel dataModel = new DataModel();
        dataModel.setMetricCode(FilterPressConstants.FEED_OVER);
        dataModel.setThingCode(thingCode);
        dataModel.setValue(FEED_OVER_VALUE);
        cmdControlService.sendCmd(dataModel, RequestIdUtil.generateRequestId());
        webSocketDemo.notice(FEED_OVER_CONFIRMED_NOTICE_URI, thingCode);
        unConfirmedSet.remove(thingCode);
    }

    /**
     * 获取自动手动确认状态
     * 
     * @return
     */
    public Integer getAutoManuConfirmState() {
        if (autoManuConfirmStateMap == null || autoManuConfirmStateMap.size() == 0) {
            initFeedOverSettingStates();
        }
        for (Map.Entry<String, Integer> entry : autoManuConfirmStateMap.entrySet()) {
            Integer state = entry.getValue();
            if (state != null) {
                return entry.getValue();
            }
        }
        return STATE_MANU_CONFIRM;
    }

    public Map<String, Integer> getIntelligentManuStateMap() {
        if (autoManuConfirmStateMap == null || autoManuConfirmStateMap.size() == 0) {
            initFeedOverSettingStates();
        }
        return intelligentManuStateMap;
    }

    // @Scheduled(cron="cnmt.FilterPressDeviceManager.clear")
    // /**
    // * 手动弹出模式下，超过一段时间不操作后自动进行确认
    // */
    // public void clear() {
    // for (String thingCode : unConfirmedSet) {
    // if (System.currentTimeMillis() -
    // getFilterPressDevice(thingCode).getFeedOverTime() > cacheTimeout) {
    // notice(FEED_OVER_CONFIRMED_NOTICE_URI, thingCode);
    // unConfirmedSet.remove(thingCode);
    // }
    // }
    // }

    /**
     * 获取压滤设备
     * 
     * @param thingCode
     * @return
     */
    public FilterPress getFilterPressDevice(String thingCode) {
        return deviceHolder.get(thingCode);
    }

    /**
     * 获取一段时间内的电流最大值、最小值及平均值
     * 
     * @return
     */
    public List<FilterPressElectricity> getCurrentInfoInDuration() {
        Date endTime = DateUtils.truncate(new Date(), Calendar.HOUR_OF_DAY);
        Date startTime = DateUtils.addDays(endTime, CURRENT_COUNT_DURATION);
        return filterPressMapper.getCurrentInfoInDuration(startTime, endTime);
    }

    /**
     * 初始化自动/手动、手动/智能状态
     */
    private void initFeedOverSettingStates() {
        List<FeedOverParam> params = filterPressMapper.getFilterFeedOverParams();
        for (FeedOverParam state : params) {
            intelligentManuStateMap.put(state.getThingCode(), state.getIntelligentManuState());
            autoManuConfirmStateMap.put(state.getThingCode(), state.getAutoManuConfirmState());
        }
    }
}
