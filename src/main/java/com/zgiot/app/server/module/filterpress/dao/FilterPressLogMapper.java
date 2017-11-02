package com.zgiot.app.server.module.filterpress.dao;

import com.zgiot.app.server.module.filterpress.FilterPressLogBean;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressPlateCountBean;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressPlateCountWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * Created by lulansong on 2017/10/31.
 */
@Mapper
public interface FilterPressLogMapper {
    void insertFilterPressLog(FilterPressLogBean filterPressLogBean);

    List<FilterPressLogBean> queryLogByDate(String queryDate);

    List<FilterPressPlateCountBean> queryPlateInfos(boolean isDayShift);
}
