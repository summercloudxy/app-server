package com.zgiot.app.server.module.filterpress.impl;

import com.zgiot.app.server.module.filterpress.FilterPressLogBean;
import com.zgiot.app.server.module.filterpress.FilterPressLogUtil;
import com.zgiot.app.server.module.filterpress.dao.FilterPressLogMapper;
import com.zgiot.app.server.module.filterpress.filterPressService.FilterPressLogService;
import com.zgiot.app.server.module.filterpress.pojo.*;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.constants.FilterPressLogConstants;
import com.zgiot.common.constants.FilterPressMetricConstants;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FilterPressLogServiceImpl implements FilterPressLogService {
    @Autowired
    private FilterPressLogMapper filterPressLogMapper;
    @Autowired
    private DataService dataService;
    @Override
    public void saveFilterPressLog(FilterPressLogBean filterPressLogBean) {
        filterPressLogMapper.insertFilterPressLog(filterPressLogBean);
    }

    @Override
    public List<FilterPressLogBean> getLogByDate(String queryDate) {
        String startTime = queryDate + FilterPressLogConstants.NIGHT_SHIFT_ZERO_LINE;
        String endTime = queryDate + FilterPressLogConstants.NIGHT_SHIFT_MIDDLE_LINE;
        return filterPressLogMapper.queryLogByDate(startTime,endTime);
    }

    @Override
    public FilterPressPlateCountWrapper getPlateInfos() {
        int priorShiftTeam = getPriorShiftTeam();
        int team = getTeamFromCache();
        FilterPressPlateCountWrapper filterPressPlateCountWrapper = new FilterPressPlateCountWrapper();
        Map<String,String> currentAndPriorDay =  FilterPressLogUtil.getCurrentDayAndNextOrPriorDay(FilterPressLogConstants.CURRENT_DAY_OFFSET,FilterPressLogConstants.DAY_DEC_ONE);
        String currentDay = currentAndPriorDay.get(FilterPressLogConstants.CURRENT_DAY);
        String priorDay = currentAndPriorDay.get(FilterPressLogConstants.NEXT_OR_PRIOR_DAY);
        String startTime = null;
        String endTime = null;
        boolean isDayShift = FilterPressLogUtil.isDayShift(FilterPressLogConstants.DAY_SHIFT_START_TIME_SCOPE,FilterPressLogConstants.DAY_SHIFT_END_TIME_SCOPE);
        int currentOrPrior = -1;
        if(isDayShift){
            if((team == priorShiftTeam)){
                startTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_END_LINE;
                endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                filterPressPlateCountWrapper.setIsDayShift(FilterPressLogConstants.IS_DAY_SHIFT_NO);
                isDayShift = (!isDayShift);
                currentOrPrior = 1;
            }else{
                startTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_END_LINE;
                filterPressPlateCountWrapper.setIsDayShift(FilterPressLogConstants.IS_DAY_SHIFT_OK);
                currentOrPrior = 0;
            }
        }else{
            if(team == priorShiftTeam){
                startTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_END_LINE;
                filterPressPlateCountWrapper.setIsDayShift(FilterPressLogConstants.IS_DAY_SHIFT_OK);
                isDayShift = (!isDayShift);
                currentOrPrior = 1;
            }else{
                startTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_END_LINE;
                endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                filterPressPlateCountWrapper.setIsDayShift(FilterPressLogConstants.IS_DAY_SHIFT_NO);
                currentOrPrior = 0;
            }
        }
        List<FilterPressSinglePlateCountBean> filterPressSinglePlateCountBeans =filterPressLogMapper.queryPlateInfos(isDayShift,startTime,endTime);
        List<FilterPressPlateCountBean> filterPressPlateCountBeanList = new ArrayList<>();
        FilterPressPlateCountBean filterPressPlateCountBean = null;
        FilterPressPlateAndTimeBean filterPressPlateAndTimeBean = null;
        for(FilterPressSinglePlateCountBean bean:filterPressSinglePlateCountBeans){
            if(filterPressPlateCountBeanList.size() == 0 && filterPressPlateCountBeanList.isEmpty()){
                filterPressPlateCountBean = getFilterPressPlateCountBean(bean,isDayShift,currentOrPrior);
                filterPressPlateCountBeanList.add(filterPressPlateCountBean);
                continue;
            }else{
                boolean isPresent = true;
                for(FilterPressPlateCountBean filterPressPlateCount:filterPressPlateCountBeanList){
                    if(bean.getThingCode().equals(filterPressPlateCount.getThingCode())){
                        filterPressPlateAndTimeBean = bean.getFilterPressPlateAndTimeBean();
                        filterPressPlateCount.getTimeLineMap().put(filterPressPlateAndTimeBean.getPlateCount(),filterPressPlateAndTimeBean.getTime());
                        isPresent = true;
                        break;
                    }else{
                        isPresent = false;
                    }
                }
                if(!isPresent){
                    filterPressPlateCountBean = getFilterPressPlateCountBean(bean,isDayShift,currentOrPrior);
                    filterPressPlateCountBeanList.add(filterPressPlateCountBean);
                }
            }
        }
        filterPressPlateCountWrapper.setFilterPressPlateCountBeanList(filterPressPlateCountBeanList);
        filterPressPlateCountWrapper.setPeriod(FilterPressLogConstants.PERIOD_TWO);
        filterPressPlateCountWrapper.setRatedPlateCount(FilterPressLogConstants.RATE_PLATE_COUNT);

        return filterPressPlateCountWrapper;
    }

    private int getTeamFromCache(){
        Optional<DataModelWrapper> dataModelWrapper = null;
        int team = 0;
        boolean isFound = Boolean.FALSE;
        for(int i = 0; i < FilterPressLogConstants.FILTER_PRESS_TOTAL_COUNT && (!isFound);i++)
            for(int j = 0;j < FilterPressLogConstants.THREE_TEAM_RESET;j++){
                int filterPressNum = Integer.valueOf(FilterPressLogConstants.FILTERPRESS_2492) + i;
                String chooseTeam = null;
                switch (j){
                    case FilterPressLogConstants.T1_CHOOSE:
                        dataModelWrapper = dataService.getData(String.valueOf(filterPressNum), FilterPressMetricConstants.T1_CHOOSE);
                        break;
                    case FilterPressLogConstants.T2_CHOOSE:
                        dataModelWrapper = dataService.getData(String.valueOf(filterPressNum), FilterPressMetricConstants.T2_CHOOSE);
                        break;
                    case FilterPressLogConstants.T3_CHOOSE:
                        dataModelWrapper = dataService.getData(String.valueOf(filterPressNum), FilterPressMetricConstants.T3_CHOOSE);
                        break;
                }
                if(dataModelWrapper.isPresent()){
                    chooseTeam = dataModelWrapper.get().getValue();
                    if(Boolean.parseBoolean(chooseTeam)){
                        team = j + 1;
                        isFound = Boolean.TRUE;
                    }
                }
            }
            return team;
    }

    public FilterPressPlateCountBean getFilterPressPlateCountBean(FilterPressSinglePlateCountBean bean,boolean isDayShift,int currentOrPrior){
        FilterPressPlateCountBean filterPressPlateCountBean = new FilterPressPlateCountBean();
        filterPressPlateCountBean.setThingCode(bean.getThingCode());
        String dayRatedStartTimeKey = isDayShift ? FilterPressLogConstants.RATED_START_TIME_DAY_OFFSET : FilterPressLogConstants.RATED_START_TIME_NIGHT_OFFSET;
        Date date = getRatedStartTimeByThingCode(bean.getThingCode(),dayRatedStartTimeKey,currentOrPrior);
        if(date != null){
            filterPressPlateCountBean.setStartTime(date);
        }
        FilterPressPlateAndTimeBean filterPressPlateAndTimeBean = bean.getFilterPressPlateAndTimeBean();
        filterPressPlateCountBean.getTimeLineMap().put(filterPressPlateAndTimeBean.getPlateCount(),filterPressPlateAndTimeBean.getTime());
        return filterPressPlateCountBean;
    }

    @Override
    public FilterPressTotalPlateCountBean getTotalPlateInfos() {
        int priorShiftTeam = getPriorShiftTeam();
        int team = getTeamFromCache();
        Map<String,String> dateMap = FilterPressLogUtil.getCurrentDayAndNextOrPriorDay(FilterPressLogConstants.CURRENT_DAY_OFFSET,FilterPressLogConstants.DAY_DEC_ONE);
        String currentDay = dateMap.get(FilterPressLogConstants.CURRENT_DAY);
        String priorDay = dateMap.get(FilterPressLogConstants.NEXT_OR_PRIOR_DAY);
        String startTime = null;
        String endTime = null;
        boolean isDayShift = FilterPressLogUtil.isDayShift(FilterPressLogConstants.DAY_SHIFT_START_TIME_SCOPE,FilterPressLogConstants.DAY_SHIFT_END_TIME_SCOPE);
        FilterPressTotalPlateCountBean filterPressTotalPlateCountBean = new FilterPressTotalPlateCountBean();
        List<FilterPressPlateAndTimeBean> filterPressPlateAndTimeBeans = null;
        if(isDayShift){
            if((team == priorShiftTeam)){
                startTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_END_LINE;
                endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                filterPressTotalPlateCountBean.setDayShift(FilterPressLogConstants.IS_DAY_SHIFT_NO);
                isDayShift = (!isDayShift);
            }else{
                startTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_END_LINE;
                filterPressTotalPlateCountBean.setDayShift(FilterPressLogConstants.IS_DAY_SHIFT_OK);
            }
        }else{
            if(team == priorShiftTeam){
                startTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_END_LINE;
                filterPressTotalPlateCountBean.setDayShift(FilterPressLogConstants.IS_DAY_SHIFT_OK);
                isDayShift = (!isDayShift);
            }else{
                startTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_END_LINE;
                endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                filterPressTotalPlateCountBean.setDayShift(FilterPressLogConstants.IS_DAY_SHIFT_NO);
            }
        }
        filterPressPlateAndTimeBeans = filterPressLogMapper.queryTotalPlateInfos(isDayShift,startTime,endTime);
        filterPressPlateAndTimeBeans = resetTotalPlateCount(filterPressPlateAndTimeBeans);
        filterPressTotalPlateCountBean.setPeriod(FilterPressLogConstants.PERIOD_TWO);
        filterPressTotalPlateCountBean.setRatedTotalPlateCount(FilterPressLogConstants.RATE_PLATE_COUNT * FilterPressLogConstants.FILTER_PRESS_TOTAL_COUNT);
        FilterPressPlateAndTimeBean timeLine = null;
        timeLine = (filterPressPlateAndTimeBeans.size() == 0) ? new FilterPressPlateAndTimeBean():getMaxTotalPlateCount(filterPressPlateAndTimeBeans);
        Map<Integer,Date> totalPlateCountMap = new HashMap<>();
        int maxTotalPlateCount = timeLine.getPlateCount();
        if(timeLine.getPlateCount() % FilterPressLogConstants.FILTER_PRESS_TOTAL_COUNT != 0){
            totalPlateCountMap.put(timeLine.getPlateCount(),timeLine.getTime());//最大总压板数和最大压板数对应的时间
        }
        for(int i = FilterPressLogConstants.FILTER_PRESS_TOTAL_COUNT,j = 1;i <= maxTotalPlateCount;i+=FilterPressLogConstants.FILTER_PRESS_TOTAL_COUNT,j++)
            for(FilterPressPlateAndTimeBean filterPressPlateAndTimeBean:filterPressPlateAndTimeBeans)
            {
                int totalPlateCount = filterPressPlateAndTimeBean.getPlateCount();
                if((totalPlateCount / i == j) && (totalPlateCount % i == 0)){
                    totalPlateCountMap.put(totalPlateCount,filterPressPlateAndTimeBean.getTime());
                    break;
                }
            }
        filterPressTotalPlateCountBean.setTimeLineMap(totalPlateCountMap);
        filterPressTotalPlateCountBean.setStartTime(new Date());
        return filterPressTotalPlateCountBean;
    }

    private List<FilterPressPlateAndTimeBean> resetTotalPlateCount(List<FilterPressPlateAndTimeBean> filterPressPlateAndTimeBeans){
        int i = 0;
        for(FilterPressPlateAndTimeBean filterPressPlateAndTimeBean:filterPressPlateAndTimeBeans){
            i++;
            filterPressPlateAndTimeBean.setPlateCount(i);
        }
        return filterPressPlateAndTimeBeans;
    }

    @Override
    public List<FilterPressHisPlateCountWrapper> getHisPlateInfos() {
        List<FilterPressHisPlateCountWrapper> filterPressHisPlateCountWrapperList = new ArrayList<>();
        FilterPressHisPlateCountWrapper filterPressHisPlateCountWrapper = null;
        List<FilterPressHisPlateCountBean> filterPressHisPlateCountBeans = null;
        int priorShiftTeam = getPriorShiftTeam();
        int team = getTeamFromCache();
        Map<String,String> dateMap = null;
        String currentDay = null;
        String priorDay = null;
        String startTime = null;
        String endTime = null;
        boolean isDayShift = FilterPressLogUtil.isDayShift(FilterPressLogConstants.DAY_SHIFT_START_TIME_SCOPE,FilterPressLogConstants.DAY_SHIFT_END_TIME_SCOPE);
        if(isDayShift){
            for(int i = FilterPressLogConstants.HIS_PLATE_COUNT_DAY -1;i > 0;i--){
                dateMap = FilterPressLogUtil.getCurrentDayAndNextOrPriorDay(FilterPressLogConstants.DAY_DEC_SIX +i-1,FilterPressLogConstants.DAY_DEC_SEVEN +(i-1));
                priorDay = dateMap.get(FilterPressLogConstants.NEXT_OR_PRIOR_DAY);
                currentDay = dateMap.get(FilterPressLogConstants.CURRENT_DAY);
                startTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                filterPressHisPlateCountBeans = filterPressLogMapper.queryHisPlateCount(startTime,endTime);
                filterPressHisPlateCountWrapper = new FilterPressHisPlateCountWrapper();
                filterPressHisPlateCountWrapper.setFilterPressHisPlateCountBeanList(filterPressHisPlateCountBeans);
                filterPressHisPlateCountWrapper.setHisDate(priorDay);
                filterPressHisPlateCountWrapperList.add(filterPressHisPlateCountWrapper);
            }
            dateMap = FilterPressLogUtil.getCurrentDayAndNextOrPriorDay(FilterPressLogConstants.CURRENT_DAY_OFFSET,FilterPressLogConstants.DAY_DEC_ONE );
            priorDay = dateMap.get(FilterPressLogConstants.NEXT_OR_PRIOR_DAY);
            currentDay = dateMap.get(FilterPressLogConstants.CURRENT_DAY);
            if((team == priorShiftTeam)){
                startTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                endTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_END_LINE;
            }else{
                startTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
            }
            filterPressHisPlateCountBeans = filterPressLogMapper.queryHisPlateCount(startTime,endTime);
            filterPressHisPlateCountWrapper = new FilterPressHisPlateCountWrapper();
            filterPressHisPlateCountWrapper.setFilterPressHisPlateCountBeanList(filterPressHisPlateCountBeans);
            filterPressHisPlateCountWrapper.setHisDate(priorDay);
            filterPressHisPlateCountWrapperList.add(filterPressHisPlateCountWrapper);
        }else{
            if(team == priorShiftTeam){
                for(int i = FilterPressLogConstants.HIS_PLATE_COUNT_DAY;i > 0;i--){
                    dateMap = FilterPressLogUtil.getCurrentDayAndNextOrPriorDay(FilterPressLogConstants.DAY_DEC_SEVEN +i,FilterPressLogConstants.DAY_DEC_SEVEN +(i-1));
                    priorDay = dateMap.get(FilterPressLogConstants.NEXT_OR_PRIOR_DAY);
                    currentDay = dateMap.get(FilterPressLogConstants.CURRENT_DAY);
                    startTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                    endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                    filterPressHisPlateCountBeans = filterPressLogMapper.queryHisPlateCount(startTime,endTime);
                    filterPressHisPlateCountWrapper = new FilterPressHisPlateCountWrapper();
                    filterPressHisPlateCountWrapper.setFilterPressHisPlateCountBeanList(filterPressHisPlateCountBeans);
                    filterPressHisPlateCountWrapper.setHisDate(priorDay);
                    filterPressHisPlateCountWrapperList.add(filterPressHisPlateCountWrapper);
                }
            }else{
                for(int i = FilterPressLogConstants.HIS_PLATE_COUNT_DAY - 1;i > 0;--i){
                    dateMap = FilterPressLogUtil.getCurrentDayAndNextOrPriorDay(FilterPressLogConstants.DAY_DEC_SIX +i,FilterPressLogConstants.DAY_DEC_SIX +(i-1));
                    priorDay = dateMap.get(FilterPressLogConstants.NEXT_OR_PRIOR_DAY);
                    currentDay = dateMap.get(FilterPressLogConstants.CURRENT_DAY);
                    startTime = priorDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                    endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                    filterPressHisPlateCountBeans = filterPressLogMapper.queryHisPlateCount(startTime,endTime);
                    filterPressHisPlateCountWrapper = new FilterPressHisPlateCountWrapper();
                    filterPressHisPlateCountWrapper.setFilterPressHisPlateCountBeanList(filterPressHisPlateCountBeans);
                    filterPressHisPlateCountWrapper.setHisDate(priorDay);
                    filterPressHisPlateCountWrapperList.add(filterPressHisPlateCountWrapper);
                }
                dateMap = FilterPressLogUtil.getCurrentDayAndNextOrPriorDay(FilterPressLogConstants.CURRENT_DAY_OFFSET,FilterPressLogConstants.DAY_DEC_ONE);
                currentDay = dateMap.get(FilterPressLogConstants.CURRENT_DAY);
                startTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                endTime = currentDay.trim() + FilterPressLogConstants.DAY_SHIFT_END_LINE;
                filterPressHisPlateCountBeans = filterPressLogMapper.queryHisPlateCount(startTime,endTime);
                filterPressHisPlateCountWrapper = new FilterPressHisPlateCountWrapper();
                filterPressHisPlateCountWrapper.setFilterPressHisPlateCountBeanList(filterPressHisPlateCountBeans);
                filterPressHisPlateCountWrapper.setHisDate(currentDay);
                filterPressHisPlateCountWrapperList.add(filterPressHisPlateCountWrapper);
            }
        }
        return addToatalCount(filterPressHisPlateCountWrapperList);
    }

    public List<FilterPressHisPlateCountWrapper> addToatalCount(List<FilterPressHisPlateCountWrapper> filterPressHisPlateCountWrappers){
        List<FilterPressHisPlateCountWrapper> filterPressHisPlateCountWrapperList = null;
        if(filterPressHisPlateCountWrappers == null){
            return null;
        }
        int totalPlateCount = 0;
        for(FilterPressHisPlateCountWrapper filterPressHisPlateCountWrapper:filterPressHisPlateCountWrappers){
            List<FilterPressHisPlateCountBean> filterPressHisPlateCountBeans = filterPressHisPlateCountWrapper.getFilterPressHisPlateCountBeanList();
            if(filterPressHisPlateCountBeans.size() == 0){
                continue;
            }
            for(FilterPressHisPlateCountBean filterPressHisPlateCountBean:filterPressHisPlateCountBeans){
                List<FilterPressTcAndMaxPlateCount> filterPressTcAndMaxPlateCountList = filterPressHisPlateCountBean.getFilterPressTcAndMaxPlateCountList();
                totalPlateCount = 0;
                for(FilterPressTcAndMaxPlateCount filterPressTcAndMaxPlateCount:filterPressTcAndMaxPlateCountList){
                    totalPlateCount += filterPressTcAndMaxPlateCount.getMaxPlateCount();
                }
                filterPressHisPlateCountBean.setUnloadTotalCount(totalPlateCount);
            }
        }
        filterPressHisPlateCountWrapperList = filterPressHisPlateCountWrappers;
        return filterPressHisPlateCountWrapperList;
    }

    @Override
    public int getPriorShiftTeam() {
        boolean isDayShift = FilterPressLogUtil.isDayShift(FilterPressLogConstants.DAY_SHIFT_START_TIME_SCOPE,FilterPressLogConstants.DAY_SHIFT_END_TIME_SCOPE);
        Map<String,String> currentAndPrior = FilterPressLogUtil.getCurrentDayAndNextOrPriorDay(FilterPressLogConstants.CURRENT_DAY_OFFSET,FilterPressLogConstants.DAY_DEC_ONE);
        String priorDay = currentAndPrior.get(FilterPressLogConstants.NEXT_OR_PRIOR_DAY);
        String currentDay = currentAndPrior.get(FilterPressLogConstants.CURRENT_DAY);
        int team;
        String startTime = null;
        String endTime = null;
        if(isDayShift){
            startTime = priorDay + FilterPressLogConstants.DAY_SHIFT_END_LINE;
            endTime = currentDay + FilterPressLogConstants.DAY_SHIFT_START_LINE;
        }else{
            if(FilterPressLogUtil.isPriorPartNightShift()){
                startTime = currentDay + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                endTime = currentDay + FilterPressLogConstants.DAY_SHIFT_END_LINE;
            }else{
                startTime = priorDay + FilterPressLogConstants.DAY_SHIFT_START_LINE;
                endTime = priorDay + FilterPressLogConstants.DAY_SHIFT_END_LINE;
            }
        }
        team = filterPressLogMapper.getPriorTeam(!isDayShift,startTime,endTime);
        return team;
    }

    public List<DataModel> getDataModelAllFilterPressByMetricCode(String metricCode){
        List<DataModel> dataModelList = new ArrayList<>();
        for(int i = 0;i < FilterPressLogConstants.FILTER_PRESS_TOTAL_COUNT ;i++){
            DataModel dataModel = new DataModel();
            dataModel.setThingCode(String.valueOf(Integer.valueOf(FilterPressLogConstants.FILTERPRESS_2492) + i));
            dataModel.setMetricCode(metricCode);
            dataModel.setDataTimeStamp(new Date());
            dataModel.setMetricCategoryCode(FilterPressLogConstants.SIG);
            dataModel.setValue(Boolean.TRUE.toString());
            dataModelList.add(dataModel);
        }
        return dataModelList;
    }

    public FilterPressPlateAndTimeBean getMaxTotalPlateCount(List<FilterPressPlateAndTimeBean> filterPressPlateAndTimeBeans){
        FilterPressPlateAndTimeBean filterPressPlateAndTimeBean = new FilterPressPlateAndTimeBean();
        int maxTotalPlateCount = 0;
        Date date = null;
        for(FilterPressPlateAndTimeBean bean:filterPressPlateAndTimeBeans){
            int totalPlateCount = bean.getPlateCount();
            if(totalPlateCount > maxTotalPlateCount){
                maxTotalPlateCount = totalPlateCount;
                date = bean.getTime();
            }
        }
        filterPressPlateAndTimeBean.setPlateCount(maxTotalPlateCount);
        filterPressPlateAndTimeBean.setTime(date);
        return filterPressPlateAndTimeBean;
    }

    private Date getRatedStartTimeByThingCode(String thingCode,String dayOrNightRatedTime,int currentOrPrior){
        FilterPressRatedStartTimeBean ratedStartTime = filterPressLogMapper.getFilterPressRatedStartTime(thingCode,dayOrNightRatedTime);
        String offsetTime = ratedStartTime.getStartTimeOffset();
        Date date = FilterPressLogUtil.getDayOrNightShiftRateStartTime(offsetTime,currentOrPrior);
        return date;
    }
}
