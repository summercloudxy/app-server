package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.module.filterpress.FilterPressLogBean;
import com.zgiot.app.server.module.filterpress.dao.FilterPressLogMapper;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressPlateCountBean;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressPlateCountWrapper;
import com.zgiot.app.server.service.FilterPressLogService;
import com.zgiot.common.constants.FilterPressLogConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
    public FilterPressPlateCountWrapper getPlateInfos(boolean isDatShift) {
        List<FilterPressPlateCountBean> filterPressPlateCountBeans = filterPressLogMapper.queryPlateInfos(isDatShift);
        List<FilterPressPlateCountBean> filterPressPlateCountBeansBak = new ArrayList<>();
        for(FilterPressPlateCountBean bean:filterPressPlateCountBeans){
            if(filterPressPlateCountBeansBak.isEmpty()){
                filterPressPlateCountBeansBak.add(bean);
            }
            boolean isPresent = true;
            for(FilterPressPlateCountBean beanBak:filterPressPlateCountBeansBak){
                if(beanBak.getThingCode().equals(bean.getThingCode())){
                    beanBak.getTimeLineMap().putAll(bean.getTimeLineMap());
                    isPresent = true;
                }else{
                    isPresent = false;
                }
            }
            if(!isPresent){
                filterPressPlateCountBeansBak.add(bean);
            }
       }
        FilterPressPlateCountWrapper filterPressPlateCountWrapper = new FilterPressPlateCountWrapper();
        filterPressPlateCountWrapper.setFilterPressPlateCountBeanList(filterPressPlateCountBeansBak);
        filterPressPlateCountWrapper.setPeriod(FilterPressLogConstants.PERIOD_TWO);
        filterPressPlateCountWrapper.setRatedPlateCount(FilterPressLogConstants.RATE_PLATE_COUNT);
        return filterPressPlateCountWrapper;
    }
}
