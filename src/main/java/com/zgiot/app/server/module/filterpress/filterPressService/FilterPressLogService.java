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
    public List<FilterPressLogBean> getLogByDate(String date, int term);
    public FilterPressPlateCountWrapper getPlateInfos(int term);
    public FilterPressTotalPlateCountBean getTotalPlateInfos(int term);
    public List<FilterPressHisPlateCountWrapper> getHisPlateInfos(int term);
    public Integer getPriorShiftTeam(int term);
    public List<DataModel> getDataModelAllFilterPressByMetricCode(String metricCode, int term);
    public List<ManualResetBean> getResetInfo(int term);
}
