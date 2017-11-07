package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.module.filterpress.FilterPressLogBean;
import com.zgiot.app.server.module.filterpress.FilterPressLogUtil;
import com.zgiot.app.server.module.filterpress.dao.FilterPressLogMapper;
import com.zgiot.app.server.module.filterpress.pojo.*;
import com.zgiot.app.server.service.FilterPressLogService;
import com.zgiot.common.constants.FilterPressLogConstants;
import com.zgiot.common.pojo.DataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class FilterPressLogServiceImpl implements FilterPressLogService {
    @Autowired
    FilterPressLogMapper filterPressLogMapper;
    @Override
    public void saveFilterPressLog(FilterPressLogBean filterPressLogBean) {
        filterPressLogMapper.insertFilterPressLog(filterPressLogBean);
    }

    @Override
    public List<FilterPressLogBean> getLogByDate(String queryDate) {
        return filterPressLogMapper.queryLogByDate(queryDate);
    }

    @Override
    public FilterPressPlateCountWrapper getPlateInfos() {
        FilterPressPlateCountWrapper filterPressPlateCountWrapper = new FilterPressPlateCountWrapper();
        Map<String,String> currentAndNextDay =  FilterPressLogUtil.getCurrentDayAndNextOrPriorDay(FilterPressLogConstants.DAY_ADD_ONE);
        String currentDay = currentAndNextDay.get(FilterPressLogConstants.CURRENT_DAY);
        String nextDay = currentAndNextDay.get(FilterPressLogConstants.NEXT_OR_PRIOR_DAY);
        boolean isDayShift = FilterPressLogUtil.isDayShift(FilterPressLogConstants.DAY_SHIFT_START_TIME_SCOPE,FilterPressLogConstants.DAY_SHIFT_END_TIME_SCOPE);
        if(isDayShift){
            nextDay = null;
            filterPressPlateCountWrapper.setIsDayShift(FilterPressLogConstants.IS_DAY_SHIFT_OK);
        }else{
            filterPressPlateCountWrapper.setIsDayShift(FilterPressLogConstants.IS_DAY_SHIFT_NO);
        }
        List<FilterPressSinglePlateCountBean> filterPressSinglePlateCountBeans =filterPressLogMapper.queryPlateInfos(isDayShift,currentDay,nextDay);
        List<FilterPressPlateCountBean> filterPressPlateCountBeanList = new ArrayList<>();
        FilterPressPlateCountBean filterPressPlateCountBean = null;
        FilterPressPlateAndTimeBean filterPressPlateAndTimeBean = null;
        for(FilterPressSinglePlateCountBean bean:filterPressSinglePlateCountBeans){
            if(filterPressPlateCountBeanList.size() == 0 && filterPressPlateCountBeanList.isEmpty()){
                filterPressPlateCountBean = getFilterPressPlateCountBean(bean);
                filterPressPlateCountBeanList.add(filterPressPlateCountBean);
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
                    filterPressPlateCountBean = getFilterPressPlateCountBean(bean);
                    filterPressPlateCountBeanList.add(filterPressPlateCountBean);
                }
            }
        }
        filterPressPlateCountWrapper.setFilterPressPlateCountBeanList(filterPressPlateCountBeanList);
        filterPressPlateCountWrapper.setPeriod(FilterPressLogConstants.PERIOD_TWO);
        filterPressPlateCountWrapper.setRatedPlateCount(FilterPressLogConstants.RATE_PLATE_COUNT);

        return filterPressPlateCountWrapper;
    }

    public FilterPressPlateCountBean getFilterPressPlateCountBean(FilterPressSinglePlateCountBean bean){
        FilterPressPlateCountBean filterPressPlateCountBean = new FilterPressPlateCountBean();
        filterPressPlateCountBean.setThingCode(bean.getThingCode());
        Date date = getRatedStartTimeByThingCode(bean.getThingCode());
        if(date != null){
            filterPressPlateCountBean.setStartTime(date);
        }
        FilterPressPlateAndTimeBean filterPressPlateAndTimeBean = bean.getFilterPressPlateAndTimeBean();
        filterPressPlateCountBean.getTimeLineMap().put(filterPressPlateAndTimeBean.getPlateCount(),filterPressPlateAndTimeBean.getTime());
        return filterPressPlateCountBean;
    }

    @Override
    public FilterPressTotalPlateCountBean getTotalPlateInfos() {
        Map<String,String> dateMap = FilterPressLogUtil.getCurrentDayAndNextOrPriorDay(FilterPressLogConstants.DAY_ADD_ONE);
        String currentDay = dateMap.get(FilterPressLogConstants.CURRENT_DAY);
        String nextDay = dateMap.get(FilterPressLogConstants.NEXT_OR_PRIOR_DAY);
        boolean isDayShift = FilterPressLogUtil.isDayShift(FilterPressLogConstants.DAY_SHIFT_START_TIME_SCOPE,FilterPressLogConstants.DAY_SHIFT_END_TIME_SCOPE);
        FilterPressTotalPlateCountBean filterPressTotalPlateCountBean = new FilterPressTotalPlateCountBean();
        List<FilterPressPlateAndTimeBean> filterPressPlateAndTimeBeans = null;
        if(isDayShift){
            nextDay = null;
            filterPressTotalPlateCountBean.setDayShift(Boolean.TRUE);
        }else{
            filterPressTotalPlateCountBean.setDayShift(Boolean.FALSE);
        }
        filterPressPlateAndTimeBeans = filterPressLogMapper.queryTotalPlateInfos(isDayShift,currentDay,nextDay);
        filterPressTotalPlateCountBean.setPeriod(FilterPressLogConstants.PERIOD_TWO);
        filterPressTotalPlateCountBean.setRatedTotalPlateCount(FilterPressLogConstants.RATE_PLATE_COUNT * FilterPressLogConstants.FILTER_PRESS_TOTAL_COUNT);
        int maxTotalPlateCount = getMaxTotalPlateCount(filterPressPlateAndTimeBeans);
        Map<Integer,Date> totalPlateCountMap = new HashMap<>();
        for(int i = FilterPressLogConstants.FILTER_PRESS_TOTAL_COUNT,j = 1;i <= maxTotalPlateCount;i+=FilterPressLogConstants.FILTER_PRESS_TOTAL_COUNT,j++)
            for(FilterPressPlateAndTimeBean filterPressPlateAndTimeBean:filterPressPlateAndTimeBeans)
            {
                int totalPlateCount = filterPressPlateAndTimeBean.getPlateCount();
                if((totalPlateCount / i == j) && (totalPlateCount % i == 0)){
                    totalPlateCountMap.put(totalPlateCount,filterPressPlateAndTimeBean.getTime());
                    break;
                }
                if(totalPlateCount == maxTotalPlateCount){
                    totalPlateCountMap.put(totalPlateCount,filterPressPlateAndTimeBean.getTime());
                    break;
                }
            }
        filterPressTotalPlateCountBean.setTimeLineMap(totalPlateCountMap);
        return filterPressTotalPlateCountBean;
    }

    @Override
    public FilterPressHisPlateCountBean getHisPlateInfos() {
        FilterPressHisPlateCountBean filterPressHisPlateCountBean = new FilterPressHisPlateCountBean();
        Map<String,String> currentAndPrior = FilterPressLogUtil.getCurrentDayAndNextOrPriorDay(FilterPressLogConstants.DAY_DEC_ONE);
        String currentDay = currentAndPrior.get(FilterPressLogConstants.CURRENT_DAY);
        String priorDay = currentAndPrior.get(FilterPressLogConstants.NEXT_OR_PRIOR_DAY);
        List<FilterPressTcAndMaxPlateCount> filterPressTcAndMaxPlateCountList = null;
        boolean isDayShift = FilterPressLogUtil.isDayShift(FilterPressLogConstants.DAY_SHIFT_START_TIME_SCOPE,FilterPressLogConstants.DAY_SHIFT_END_TIME_SCOPE);
        if(isDayShift){
            filterPressTcAndMaxPlateCountList = filterPressLogMapper.queryHisPlateCount(!isDayShift,currentDay.trim(),priorDay.trim());

        }else{
            if(FilterPressLogUtil.isPriorPartNightShift()){
                filterPressTcAndMaxPlateCountList = filterPressLogMapper.queryHisPlateCount(!isDayShift,currentDay.trim(),null);
            }else{
                filterPressTcAndMaxPlateCountList = filterPressLogMapper.queryHisPlateCount(!isDayShift,priorDay.trim(),null);
            }
        }
        filterPressHisPlateCountBean.setFilterPressTcAndMaxPlateCountList(filterPressTcAndMaxPlateCountList);
        filterPressHisPlateCountBean.setDayShift(isDayShift);
        int totalPlateCount = 0;
        for(FilterPressTcAndMaxPlateCount bean:filterPressTcAndMaxPlateCountList){
            totalPlateCount += bean.getMaxPlateCount();
        }
        filterPressHisPlateCountBean.setUnloadTotalCount(totalPlateCount);

        return filterPressHisPlateCountBean;
    }

    @Override
    public int getPriorShiftTeam() {
        boolean isDayShift = FilterPressLogUtil.isDayShift(FilterPressLogConstants.DAY_SHIFT_START_TIME_SCOPE,FilterPressLogConstants.DAY_SHIFT_END_TIME_SCOPE);
        Map<String,String> currentAndPrior = FilterPressLogUtil.getCurrentDayAndNextOrPriorDay(FilterPressLogConstants.DAY_DEC_ONE);
        String priorDay = currentAndPrior.get(FilterPressLogConstants.NEXT_OR_PRIOR_DAY);
        String currentDay = currentAndPrior.get(FilterPressLogConstants.CURRENT_DAY);
        int team;
        if(isDayShift){
            team = filterPressLogMapper.getPriorTeam(!isDayShift,priorDay);
        }else{
            if(FilterPressLogUtil.isPriorPartNightShift()){
                team =  filterPressLogMapper.getPriorTeam(!isDayShift,currentDay);
            }else{
                team =  filterPressLogMapper.getPriorTeam(!isDayShift,priorDay);
            }
        }
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

    public int getMaxTotalPlateCount(List<FilterPressPlateAndTimeBean> filterPressPlateAndTimeBeans){
        int maxTotalPlateCount = 0;
        for(FilterPressPlateAndTimeBean bean:filterPressPlateAndTimeBeans){
            int totalPlateCount = bean.getPlateCount();
            if(totalPlateCount > maxTotalPlateCount){
                maxTotalPlateCount = totalPlateCount;
            }
        }
        return maxTotalPlateCount;
    }

    private Date getRatedStartTimeByThingCode(String thingCode){
        Date date = null;
        switch(thingCode){
            case FilterPressLogConstants.FILTERPRESS_2492:
                date = FilterPressLogUtil.getNightShiftRateStartTime(FilterPressLogConstants.FILTERPRESS_2492_RATE_TIME);
                break;
            case FilterPressLogConstants.FILTERPRESS_2493:
                date = FilterPressLogUtil.getNightShiftRateStartTime(FilterPressLogConstants.FILTERPRESS_2493_RATE_TIME);
                break;
            case FilterPressLogConstants.FILTERPRESS_2494:
                date = FilterPressLogUtil.getNightShiftRateStartTime(FilterPressLogConstants.FILTERPRESS_2494_RATE_TIME);
                break;
            case FilterPressLogConstants.FILTERPRESS_2495:
                date = FilterPressLogUtil.getNightShiftRateStartTime(FilterPressLogConstants.FILTERPRESS_2495_RATE_TIME);
                break;
            case FilterPressLogConstants.FILTERPRESS_2496:
                date = FilterPressLogUtil.getNightShiftRateStartTime(FilterPressLogConstants.FILTERPRESS_2496_RATE_TIME);
                break;
            case FilterPressLogConstants.FILTERPRESS_2496A:
                date = FilterPressLogUtil.getNightShiftRateStartTime(FilterPressLogConstants.FILTERPRESS_2496A_RATE_TIME);
                break;
        }
        return date;
    }
}
