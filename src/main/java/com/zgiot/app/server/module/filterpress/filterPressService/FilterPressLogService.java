package com.zgiot.app.server.module.filterpress.filterPressService;

import com.zgiot.app.server.module.filterpress.FilterPressLogBean;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressHisPlateCountWrapper;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressPlateCountWrapper;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressTotalPlateCountBean;
import com.zgiot.common.pojo.DataModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FilterPressLogService {
    void saveFilterPressLog(FilterPressLogBean filterPressLogBean);
    List<FilterPressLogBean> getLogByDate(String date);
    FilterPressPlateCountWrapper getPlateInfos();
    FilterPressTotalPlateCountBean getTotalPlateInfos();
    List<FilterPressHisPlateCountWrapper> getHisPlateInfos();
    Integer getPriorShiftTeam();
    List<DataModel> getDataModelAllFilterPressByMetricCode(String metricCode);
}
