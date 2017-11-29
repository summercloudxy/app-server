package com.zgiot.app.server.module.filterpress.impl;

import com.zgiot.app.server.module.filterpress.FilterPressLogBean;
import com.zgiot.app.server.module.filterpress.FilterPressLogUtil;
import com.zgiot.app.server.module.filterpress.FilterPressManager;
import com.zgiot.app.server.module.filterpress.dao.FilterPressLogMapper;
import com.zgiot.app.server.module.filterpress.filterPressService.FilterPressLogService;
import com.zgiot.app.server.module.filterpress.pojo.*;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.constants.FilterPressLogConstants;
import com.zgiot.common.constants.FilterPressMetricConstants;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class FilterPressLogServiceImpl implements FilterPressLogService {
    private static final Logger logger = LoggerFactory.getLogger(FilterPressLogServiceImpl.class);
    @Autowired
    private FilterPressLogMapper filterPressLogMapper;
    @Autowired
    private DataService dataService;
    @Autowired
    private FilterPressManager filterPressManager;

    @Override
    public void saveFilterPressLog(FilterPressLogBean filterPressLogBean) {
        filterPressLogMapper.insertFilterPressLog(filterPressLogBean);
    }

    @Override
    public List<FilterPressLogBean> getLogByDate(String queryDate) {
        String startTime = queryDate + FilterPressLogConstants.NIGHT_SHIFT_ZERO_LINE;
        String endTime = queryDate + FilterPressLogConstants.NIGHT_SHIFT_MIDDLE_LINE;
        return filterPressLogMapper.queryLogByDate(startTime, endTime);
    }

    @Override
    public FilterPressPlateCountWrapper getPlateInfos() {
        logger.trace("开始获取压板信息");
        int priorShiftTeam = getPriorShiftTeam();
        int team = getTeamFromCache();
        logger.trace("上一队组：" + priorShiftTeam);
        logger.trace("缓存中队组：" + team);
        FilterPressPlateCountWrapper filterPressPlateCountWrapper = new FilterPressPlateCountWrapper();
        Map<String,Object> mapInfo = getTimeScopeAndDayShiftInfo(priorShiftTeam,team);
        String startTime = String.valueOf(mapInfo.get(FilterPressLogConstants.START_TIME));
        String endTime = String.valueOf(mapInfo.get(FilterPressLogConstants.END_TIME));
        boolean isDayShift = ((Boolean)mapInfo.get(FilterPressLogConstants.DAY_SHIFT)).booleanValue();
        int currentOrPrior = Integer.valueOf(String.valueOf(mapInfo.get(FilterPressLogConstants.CURRENT_OR_PRIOR)));
        filterPressPlateCountWrapper.setIsDayShift(isDayShift);
        logger.trace("查询压板信息开始时间：" + startTime + "   查询压板信息结束时间：" + endTime);
        logger.trace("查询压板信息是白班？" + isDayShift);
        List<FilterPressSinglePlateCountBean> filterPressSinglePlateCountBeans = filterPressLogMapper.queryPlateInfos(isDayShift, startTime, endTime);
        List<FilterPressPlateCountBean> filterPressPlateCountBeanList = new ArrayList<>();
        FilterPressPlateCountBean filterPressPlateCountBean = null;
        FilterPressPlateAndTimeBean filterPressPlateAndTimeBean = null;
        filterPressPlateCountBean = getFirstFilterPressPlateCountBean(filterPressSinglePlateCountBeans, isDayShift, currentOrPrior);
        if(filterPressPlateCountBean != null){
            filterPressPlateCountBeanList.add(filterPressPlateCountBean);
        }
        for (FilterPressSinglePlateCountBean bean : filterPressSinglePlateCountBeans) {
            boolean isPresent = true;
            for (FilterPressPlateCountBean filterPressPlateCount : filterPressPlateCountBeanList) {
                if (bean.getThingCode().equals(filterPressPlateCount.getThingCode())) {
                    filterPressPlateAndTimeBean = bean.getFilterPressPlateAndTimeBean();
                    filterPressPlateCount.getTimeLineMap().put(String.valueOf(filterPressPlateAndTimeBean.getPlateCount()), filterPressPlateAndTimeBean.getTime());
                    isPresent = true;
                    break;
                } else {
                    isPresent = false;
                }
            }
            if (!isPresent) {
                filterPressPlateCountBean = getFilterPressPlateCountBean(bean, isDayShift, currentOrPrior);
                filterPressPlateCountBeanList.add(filterPressPlateCountBean);
            }
        }
        filterPressPlateCountBeanList = createAllFilterPressPlateInfo(filterPressPlateCountBeanList, isDayShift, currentOrPrior);
        filterPressPlateCountWrapper.setFilterPressPlateCountBeanList(filterPressPlateCountBeanList);
        filterPressPlateCountWrapper.setPeriod(FilterPressLogConstants.PERIOD_TWO);
        filterPressPlateCountWrapper.setRatedPlateCount(FilterPressLogConstants.RATE_PLATE_COUNT);

        return filterPressPlateCountWrapper;
    }

    private Map<String,Object> getTimeScopeAndDayShiftInfo(int priorShiftTeam,int team){
        Map<String, String> currentAndPriorDay = FilterPressLogUtil.getCurrentDayAndNextOrPriorDay(FilterPressLogConstants.CURRENT_DAY_OFFSET, FilterPressLogConstants.DAY_DEC_ONE);
        String currentDay = currentAndPriorDay.get(FilterPressLogConstants.CURRENT_DAY);
        String priorDay = currentAndPriorDay.get(FilterPressLogConstants.NEXT_OR_PRIOR_DAY);
        boolean isDayShift = FilterPressLogUtil.isDayShift(FilterPressLogConstants.DAY_SHIFT_START_TIME_SCOPE, FilterPressLogConstants.DAY_SHIFT_END_TIME_SCOPE);
        Map<String,Object> mapInfo = new HashMap<>();
        String startTime = null;
        String endTime = null;
        int currentOrPrior = -1;
        if (isDayShift) {
            if ((team == priorShiftTeam)) {
                startTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_END_LINE;
                endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                isDayShift = (!isDayShift);
                currentOrPrior = 1;//0:当天，1:前一天
            } else {
                startTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_END_LINE;
                currentOrPrior = 0;
            }
        } else {
            if (team == priorShiftTeam && FilterPressLogUtil.isPriorPartNightShift()) {
                startTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_END_LINE;
                isDayShift = (!isDayShift);
                currentOrPrior = 0;
            } else if (FilterPressLogUtil.isPriorPartNightShift()) {
                startTime = currentDay + FilterPressLogConstants.DAY_SHIFT_END_LINE;
                endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                currentOrPrior = 0;
            } else {
                startTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_END_LINE;
                endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                currentOrPrior = 0;
            }
        }
        mapInfo.put(FilterPressLogConstants.START_TIME,startTime);
        mapInfo.put(FilterPressLogConstants.END_TIME,endTime);
        mapInfo.put(FilterPressLogConstants.DAY_SHIFT,isDayShift);
        mapInfo.put(FilterPressLogConstants.CURRENT_OR_PRIOR,currentOrPrior);
        return mapInfo;
    }

    private FilterPressPlateCountBean getFirstFilterPressPlateCountBean(List<FilterPressSinglePlateCountBean> filterPressPlateCountBeans,boolean isDayShift,int currentOrPrior){
        if(filterPressPlateCountBeans == null || (filterPressPlateCountBeans.size() == 0)){
            return null;
        }
        return getFilterPressPlateCountBean(filterPressPlateCountBeans.remove(0), isDayShift, currentOrPrior);
    }

    private List<FilterPressPlateCountBean> createAllFilterPressPlateInfo(List<FilterPressPlateCountBean> filterPressPlateCountBeans, boolean isDayShift, int currentOrPrior) {
        List<FilterPressPlateCountBean> filterPressPlateCountBeanList = new ArrayList<>();
        Set<String> thingCodes = filterPressManager.getAllFilterPressCode();
        for (String thingCode : thingCodes) {
            boolean isFound = false;
            if(filterPressPlateCountBeans.size() > 0 && filterPressPlateCountBeans.get(0) != null){
                for (FilterPressPlateCountBean filterPressPlateCountBean : filterPressPlateCountBeans) {
                    if (thingCode.equals(filterPressPlateCountBean.getThingCode())) {
                        isFound = true;
                        break;
                    }
                }
            }
            if (!isFound) {
                FilterPressPlateCountBean filterPressPlateCountBean = new FilterPressPlateCountBean();
                FilterPressSinglePlateCountBean filterPressSinglePlateCountBean = new FilterPressSinglePlateCountBean();
                filterPressSinglePlateCountBean.setThingCode(thingCode);
                filterPressPlateCountBean = getFilterPressPlateCountBean(filterPressSinglePlateCountBean, isDayShift, currentOrPrior);
                filterPressPlateCountBeanList.add(filterPressPlateCountBean);
            }
        }
        filterPressPlateCountBeanList.addAll(filterPressPlateCountBeans);
        return filterPressPlateCountBeanList;
    }

    private int getTeamFromCache() {
        int team = 0;
        Set<String> thingCodes = filterPressManager.getAllFilterPressCode();
        for (String thingCode : thingCodes) {
            team = getTeam(thingCode);
            if (team > 0) {
                break;
            }
        }
        return team;
    }

    private int getTeam(String filterPressNum) {
        Optional<DataModelWrapper> team1Wrapper = null;
        Optional<DataModelWrapper> team2Wrapper = null;
        Optional<DataModelWrapper> team3Wrapper = null;

        team1Wrapper = dataService.getData(filterPressNum, FilterPressMetricConstants.T1_RCD);
        team2Wrapper = dataService.getData(filterPressNum, FilterPressMetricConstants.T2_RCD);
        team3Wrapper = dataService.getData(filterPressNum, FilterPressMetricConstants.T3_RCD);
        if (team1Wrapper.isPresent() && (Boolean.TRUE.toString().equals(team1Wrapper.get().getValue()))) {
            return FilterPressLogConstants.TEAM1;
        }
        if (team2Wrapper.isPresent() && (Boolean.TRUE.toString().equals(team2Wrapper.get().getValue()))) {
            return FilterPressLogConstants.TEAM2;
        }
        if (team3Wrapper.isPresent() && (Boolean.TRUE.toString().equals(team3Wrapper.get().getValue()))) {
            return FilterPressLogConstants.TEAM3;
        }
        return 0;
    }

    public FilterPressPlateCountBean getFilterPressPlateCountBean(FilterPressSinglePlateCountBean bean, boolean isDayShift, int currentOrPrior) {
        FilterPressPlateCountBean filterPressPlateCountBean = new FilterPressPlateCountBean();
        filterPressPlateCountBean.setThingCode(bean.getThingCode());
        String dayRatedStartTimeKey = isDayShift ? FilterPressLogConstants.RATED_START_TIME_DAY_OFFSET : FilterPressLogConstants.RATED_START_TIME_NIGHT_OFFSET;
        Date date = getRatedStartTimeByThingCode(bean.getThingCode(), dayRatedStartTimeKey, currentOrPrior);
        if (date != null) {
            filterPressPlateCountBean.setStartTime(date);
        }
        FilterPressPlateAndTimeBean filterPressPlateAndTimeBean = bean.getFilterPressPlateAndTimeBean();
        if (filterPressPlateAndTimeBean == null) {
            filterPressPlateAndTimeBean = new FilterPressPlateAndTimeBean();
        }
        filterPressPlateCountBean.getTimeLineMap().put(String.valueOf(filterPressPlateAndTimeBean.getPlateCount()), filterPressPlateAndTimeBean.getTime());
        return filterPressPlateCountBean;
    }

    @Override
    public FilterPressTotalPlateCountBean getTotalPlateInfos() {
        logger.trace("开始获取压板总数信息");
        int priorShiftTeam = getPriorShiftTeam();
        int team = getTeamFromCache();
        logger.trace("上一队组：" + priorShiftTeam);
        logger.trace("缓存中队组：" + team);
        FilterPressTotalPlateCountBean filterPressTotalPlateCountBean = new FilterPressTotalPlateCountBean();
        List<FilterPressPlateAndTimeBean> filterPressPlateAndTimeBeans = null;

        Map<String,Object> mapInfo = getTimeScopeAndDayShiftInfo(priorShiftTeam,team);
        String startTime = String.valueOf(mapInfo.get(FilterPressLogConstants.START_TIME));
        String endTime = String.valueOf(mapInfo.get(FilterPressLogConstants.END_TIME));
        boolean isDayShift = ((Boolean)mapInfo.get(FilterPressLogConstants.DAY_SHIFT)).booleanValue();

        filterPressTotalPlateCountBean.setIsDayShift(isDayShift);
        logger.trace("查询压板总数开始时间：" + startTime + "   查询压板总数结束时间：" + endTime);
        logger.trace("查询压板总数是白班？" + isDayShift);
        filterPressPlateAndTimeBeans = filterPressLogMapper.queryTotalPlateInfos(isDayShift, startTime, endTime);
        filterPressPlateAndTimeBeans = resetTotalPlateCount(filterPressPlateAndTimeBeans);
        filterPressTotalPlateCountBean.setPeriod(FilterPressLogConstants.PERIOD_TWO);
        filterPressTotalPlateCountBean.setRatedTotalPlateCount(FilterPressLogConstants.RATE_PLATE_COUNT * FilterPressLogConstants.FILTER_PRESS_TOTAL_COUNT);
        FilterPressPlateAndTimeBean timeLine = null;
        timeLine = getMaxTotalPlateCount(filterPressPlateAndTimeBeans);
        Map<String, Date> totalPlateCountMap = new HashMap<>();
        int maxTotalPlateCount = timeLine.getPlateCount();
        totalPlateCountMap.put(String.valueOf(timeLine.getPlateCount()), timeLine.getTime());//最大总压板数和最大压板数对应的时间
        for (int i = FilterPressLogConstants.FILTER_PRESS_TOTAL_COUNT, j = 1; i < maxTotalPlateCount; i += FilterPressLogConstants.FILTER_PRESS_TOTAL_COUNT, j++)
            for (FilterPressPlateAndTimeBean filterPressPlateAndTimeBean : filterPressPlateAndTimeBeans) {
                int totalPlateCount = filterPressPlateAndTimeBean.getPlateCount();
                if ((totalPlateCount / i == j) && (totalPlateCount % i == 0)) {
                    totalPlateCountMap.put(String.valueOf(totalPlateCount), filterPressPlateAndTimeBean.getTime());
                    break;
                }
            }
        filterPressTotalPlateCountBean.setTimeLineMap(totalPlateCountMap);
        Date startDate = FilterPressLogUtil.getDateByString(startTime);
        filterPressTotalPlateCountBean.setStartTime(startDate);
        return filterPressTotalPlateCountBean;
    }


    private List<FilterPressPlateAndTimeBean> resetTotalPlateCount(List<FilterPressPlateAndTimeBean> filterPressPlateAndTimeBeans) {
        int i = 0;
        for (FilterPressPlateAndTimeBean filterPressPlateAndTimeBean : filterPressPlateAndTimeBeans) {
            i++;
            filterPressPlateAndTimeBean.setPlateCount(i);
        }
        return filterPressPlateAndTimeBeans;
    }

    @Override
    public List<FilterPressHisPlateCountWrapper> getHisPlateInfos() {
        logger.trace("开始获取历史压板信息");
        List<FilterPressHisPlateCountWrapper> filterPressHisPlateCountWrapperList = new ArrayList<>();
        FilterPressHisPlateCountWrapper filterPressHisPlateCountWrapper = null;
        List<FilterPressHisPlateCountBean> filterPressHisPlateCountBeans = null;
        int priorShiftTeam = getPriorShiftTeam();
        int team = getTeamFromCache();
        logger.trace("上一队组：" + priorShiftTeam);
        logger.trace("缓存中队组：" + team);
        Map<String, String> dateMap = null;
        String currentDay = null;
        String priorDay = null;
        String startTime = null;
        String endTime = null;
        boolean isDayShift = FilterPressLogUtil.isDayShift(FilterPressLogConstants.DAY_SHIFT_START_TIME_SCOPE, FilterPressLogConstants.DAY_SHIFT_END_TIME_SCOPE);
        if (isDayShift) {
            for (int i = FilterPressLogConstants.HIS_PLATE_COUNT_DAY - 1; i > 0; i--) {
                dateMap = FilterPressLogUtil.getCurrentDayAndNextOrPriorDay(FilterPressLogConstants.DAY_DEC_SIX + i - 1, FilterPressLogConstants.DAY_DEC_SEVEN + (i - 1));
                priorDay = dateMap.get(FilterPressLogConstants.NEXT_OR_PRIOR_DAY);
                currentDay = dateMap.get(FilterPressLogConstants.CURRENT_DAY);
                startTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                filterPressHisPlateCountBeans = filterPressLogMapper.queryHisPlateCount(startTime, endTime);
                filterPressHisPlateCountWrapper = new FilterPressHisPlateCountWrapper();
                filterPressHisPlateCountWrapper.setFilterPressHisPlateCountBeanList(filterPressHisPlateCountBeans);
                filterPressHisPlateCountWrapper.setHisDate(priorDay);
                filterPressHisPlateCountWrapperList.add(filterPressHisPlateCountWrapper);
            }
            dateMap = FilterPressLogUtil.getCurrentDayAndNextOrPriorDay(FilterPressLogConstants.CURRENT_DAY_OFFSET, FilterPressLogConstants.DAY_DEC_ONE);
            priorDay = dateMap.get(FilterPressLogConstants.NEXT_OR_PRIOR_DAY);
            currentDay = dateMap.get(FilterPressLogConstants.CURRENT_DAY);
            if ((team == priorShiftTeam)) {
                startTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                endTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_END_LINE;
            } else {
                startTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
            }
            filterPressHisPlateCountBeans = filterPressLogMapper.queryHisPlateCount(startTime, endTime);
            filterPressHisPlateCountWrapper = new FilterPressHisPlateCountWrapper();
            filterPressHisPlateCountWrapper.setFilterPressHisPlateCountBeanList(filterPressHisPlateCountBeans);
            filterPressHisPlateCountWrapper.setHisDate(priorDay);
            filterPressHisPlateCountWrapperList.add(filterPressHisPlateCountWrapper);
        } else {
            if (team == priorShiftTeam) {
                for (int i = FilterPressLogConstants.HIS_PLATE_COUNT_DAY; i > 0; i--) {
                    dateMap = FilterPressLogUtil.getCurrentDayAndNextOrPriorDay(FilterPressLogConstants.DAY_DEC_SEVEN + i, FilterPressLogConstants.DAY_DEC_SEVEN + (i - 1));
                    priorDay = dateMap.get(FilterPressLogConstants.NEXT_OR_PRIOR_DAY);
                    currentDay = dateMap.get(FilterPressLogConstants.CURRENT_DAY);
                    startTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                    endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                    filterPressHisPlateCountBeans = filterPressLogMapper.queryHisPlateCount(startTime, endTime);
                    filterPressHisPlateCountWrapper = new FilterPressHisPlateCountWrapper();
                    filterPressHisPlateCountWrapper.setFilterPressHisPlateCountBeanList(filterPressHisPlateCountBeans);
                    filterPressHisPlateCountWrapper.setHisDate(priorDay);
                    filterPressHisPlateCountWrapperList.add(filterPressHisPlateCountWrapper);
                }
            } else {
                for (int i = FilterPressLogConstants.HIS_PLATE_COUNT_DAY - 1; i > 0; --i) {
                    dateMap = FilterPressLogUtil.getCurrentDayAndNextOrPriorDay(FilterPressLogConstants.DAY_DEC_SIX + i -1, FilterPressLogConstants.DAY_DEC_SIX + (i - 2));
                    priorDay = dateMap.get(FilterPressLogConstants.NEXT_OR_PRIOR_DAY);
                    currentDay = dateMap.get(FilterPressLogConstants.CURRENT_DAY);
                    startTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                    endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                    filterPressHisPlateCountBeans = filterPressLogMapper.queryHisPlateCount(startTime, endTime);
                    filterPressHisPlateCountWrapper = new FilterPressHisPlateCountWrapper();
                    filterPressHisPlateCountWrapper.setFilterPressHisPlateCountBeanList(filterPressHisPlateCountBeans);
                    filterPressHisPlateCountWrapper.setHisDate(priorDay);
                    filterPressHisPlateCountWrapperList.add(filterPressHisPlateCountWrapper);
                }
                filterPressHisPlateCountWrapper = new FilterPressHisPlateCountWrapper();
                if (FilterPressLogUtil.isPriorPartNightShift()) {
                    dateMap = FilterPressLogUtil.getCurrentDayAndNextOrPriorDay(FilterPressLogConstants.CURRENT_DAY_OFFSET, FilterPressLogConstants.DAY_DEC_ONE);
                    currentDay = dateMap.get(FilterPressLogConstants.CURRENT_DAY);
                    priorDay = dateMap.get(FilterPressLogConstants.NEXT_OR_PRIOR_DAY);
                    startTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                    endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                    filterPressHisPlateCountBeans = filterPressLogMapper.queryHisPlateCount(startTime, endTime);
                    filterPressHisPlateCountWrapper.setFilterPressHisPlateCountBeanList(filterPressHisPlateCountBeans);
                    filterPressHisPlateCountWrapper.setHisDate(priorDay);
                    filterPressHisPlateCountWrapperList.add(filterPressHisPlateCountWrapper);
                    startTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                    endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_END_LINE;
                } else {
                    dateMap = FilterPressLogUtil.getCurrentDayAndNextOrPriorDay(FilterPressLogConstants.CURRENT_DAY_OFFSET, FilterPressLogConstants.DAY_DEC_ONE);
                    currentDay = dateMap.get(FilterPressLogConstants.CURRENT_DAY);
                    priorDay = dateMap.get(FilterPressLogConstants.NEXT_OR_PRIOR_DAY);
                    startTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                    endTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_END_LINE;
                    currentDay = priorDay;
                }
                filterPressHisPlateCountBeans = filterPressLogMapper.queryHisPlateCount(startTime, endTime);
                filterPressHisPlateCountWrapper = new FilterPressHisPlateCountWrapper();
                filterPressHisPlateCountWrapper.setFilterPressHisPlateCountBeanList(filterPressHisPlateCountBeans);
                filterPressHisPlateCountWrapper.setHisDate(currentDay);
                filterPressHisPlateCountWrapperList.add(filterPressHisPlateCountWrapper);
            }

        }
        logger.trace("查询历史压板信息开始时间：" + startTime + "   查询历史压板信息结束时间：" + endTime);
        logger.trace("查询历史压板信息是白班？" + isDayShift);
        return addToatalCount(filterPressHisPlateCountWrapperList);
    }

    public List<FilterPressHisPlateCountWrapper> addToatalCount(List<FilterPressHisPlateCountWrapper> filterPressHisPlateCountWrappers) {
        List<FilterPressHisPlateCountWrapper> filterPressHisPlateCountWrapperList = null;
        if (filterPressHisPlateCountWrappers == null) {
            return null;
        }
        int totalPlateCount = 0;
        for (FilterPressHisPlateCountWrapper filterPressHisPlateCountWrapper : filterPressHisPlateCountWrappers) {
            List<FilterPressHisPlateCountBean> filterPressHisPlateCountBeans = filterPressHisPlateCountWrapper.getFilterPressHisPlateCountBeanList();
            if (filterPressHisPlateCountBeans.size() == 0) {
                continue;
            }
            for (FilterPressHisPlateCountBean filterPressHisPlateCountBean : filterPressHisPlateCountBeans) {
                List<FilterPressTcAndMaxPlateCount> filterPressTcAndMaxPlateCountList = filterPressHisPlateCountBean.getFilterPressTcAndMaxPlateCountList();
                totalPlateCount = 0;
                for (FilterPressTcAndMaxPlateCount filterPressTcAndMaxPlateCount : filterPressTcAndMaxPlateCountList) {
                    totalPlateCount += filterPressTcAndMaxPlateCount.getMaxPlateCount();
                }
                filterPressHisPlateCountBean.setUnloadTotalCount(totalPlateCount);
            }
        }
        filterPressHisPlateCountWrapperList = filterPressHisPlateCountWrappers;
        return filterPressHisPlateCountWrapperList;
    }

    @Override
    public Integer getPriorShiftTeam() {
        logger.trace("获取上一班队组信息");
        boolean isDayShift = FilterPressLogUtil.isDayShift(FilterPressLogConstants.DAY_SHIFT_START_TIME_SCOPE, FilterPressLogConstants.DAY_SHIFT_END_TIME_SCOPE);
        Map<String, String> currentAndPrior = FilterPressLogUtil.getCurrentDayAndNextOrPriorDay(FilterPressLogConstants.CURRENT_DAY_OFFSET, FilterPressLogConstants.DAY_DEC_ONE);
        String priorDay = currentAndPrior.get(FilterPressLogConstants.NEXT_OR_PRIOR_DAY);
        String currentDay = currentAndPrior.get(FilterPressLogConstants.CURRENT_DAY);
        Integer team = null;
        String startTime = null;
        String endTime = null;
        if (isDayShift) {
            startTime = priorDay + FilterPressLogConstants.DAY_SHIFT_END_LINE;
            endTime = currentDay + FilterPressLogConstants.DAY_SHIFT_START_LINE;
        } else {
            if (FilterPressLogUtil.isPriorPartNightShift()) {
                startTime = currentDay + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                endTime = currentDay + FilterPressLogConstants.DAY_SHIFT_END_LINE;
            } else {
                startTime = priorDay + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                endTime = priorDay + FilterPressLogConstants.DAY_SHIFT_END_LINE;
            }
        }
        logger.trace("查询上一班队组开始时间：" + startTime + "   查询上一班队组结束时间：" + endTime);
        logger.trace("查询上一班队组是白班？" + isDayShift);
        team = filterPressLogMapper.getPriorTeam(!isDayShift, startTime, endTime);
        return team == null ? 0 : team;
    }

    public List<DataModel> getDataModelAllFilterPressByMetricCode(String metricCode) {
        List<DataModel> dataModelList = new ArrayList<>();
        Set<String> thingCodes = filterPressManager.getAllFilterPressCode();
        for (String thingCode : thingCodes) {
            DataModel dataModel = new DataModel();
            dataModel.setThingCode(thingCode);
            dataModel.setMetricCode(metricCode);
            dataModel.setDataTimeStamp(new Date());
            dataModel.setValue(Boolean.TRUE.toString());
            dataModelList.add(dataModel);
        }
        return dataModelList;
    }

    public FilterPressPlateAndTimeBean getMaxTotalPlateCount(List<FilterPressPlateAndTimeBean> filterPressPlateAndTimeBeans) {
        FilterPressPlateAndTimeBean filterPressPlateAndTimeBean = new FilterPressPlateAndTimeBean();
        int maxTotalPlateCount = 0;
        Date date = null;
        if(filterPressPlateAndTimeBeans != null && filterPressPlateAndTimeBeans.size() > 0){
            for (FilterPressPlateAndTimeBean bean : filterPressPlateAndTimeBeans) {
                int totalPlateCount = bean.getPlateCount();
                if (totalPlateCount > maxTotalPlateCount) {
                    maxTotalPlateCount = totalPlateCount;
                    date = bean.getTime();
                }
            }
        }
        filterPressPlateAndTimeBean.setPlateCount(maxTotalPlateCount);
        filterPressPlateAndTimeBean.setTime(date);
        return filterPressPlateAndTimeBean;
    }

    private Date getRatedStartTimeByThingCode(String thingCode, String dayOrNightRatedTime, int currentOrPrior) {
        FilterPressRatedStartTimeBean ratedStartTime = filterPressLogMapper.getFilterPressRatedStartTime(thingCode, dayOrNightRatedTime);
        String offsetTime = ratedStartTime.getStartTimeOffset();
        Date date = FilterPressLogUtil.getDayOrNightShiftRateStartTime(offsetTime, currentOrPrior);
        return date;
    }
}
