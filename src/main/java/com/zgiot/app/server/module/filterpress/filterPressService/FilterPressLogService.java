package com.zgiot.app.server.module.filterpress.filterPressService;

import com.zgiot.app.server.module.filterpress.FilterPressLogBean;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressHisPlateCountWrapper;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressPlateCountWrapper;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressTotalPlateCountBean;
import com.zgiot.app.server.module.filterpress.pojo.ManualResetBean;
import com.zgiot.common.pojo.DataModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FilterPressLogService {
    public void saveFilterPressLog(FilterPressLogBean filterPressLogBean);
    public List<FilterPressLogBean> getLogByDate(String date);
    public FilterPressPlateCountWrapper getPlateInfos();
    public FilterPressTotalPlateCountBean getTotalPlateInfos();
    public List<FilterPressHisPlateCountWrapper> getHisPlateInfos();
    public Integer getPriorShiftTeam();
    public List<DataModel> getDataModelAllFilterPressByMetricCode(String metricCode);
    public List<ManualResetBean> getResetInfo();
}
