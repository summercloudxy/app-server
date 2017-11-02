package com.zgiot.app.server.service;

import com.zgiot.app.server.module.filterpress.FilterPressLogBean;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressPlateCountBean;
import com.zgiot.app.server.module.filterpress.pojo.FilterPressPlateCountWrapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface FilterPressLogService {
    public void saveFilterPressLog(FilterPressLogBean filterPressLogBean);
    public List<FilterPressLogBean> getLogByDate(String date);
    public FilterPressPlateCountWrapper getPlateInfos(boolean isDatShift);
}
