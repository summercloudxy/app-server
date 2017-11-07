package com.zgiot.app.server.service;

import com.zgiot.app.server.module.filterpress.FilterPressLogBean;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressHisPlateCountBean;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressPlateCountBean;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressPlateCountWrapper;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressTotalPlateCountBean;
import com.zgiot.common.pojo.DataModel;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface FilterPressLogService {
    public void saveFilterPressLog(FilterPressLogBean filterPressLogBean);
    public List<FilterPressLogBean> getLogByDate(String date);
    public FilterPressPlateCountWrapper getPlateInfos();
    public FilterPressTotalPlateCountBean getTotalPlateInfos();
    public FilterPressHisPlateCountBean getHisPlateInfos();
    public int getPriorShiftTeam();
    public List<DataModel> getDataModelAllFilterPressByMetricCode(String metricCode);
}
