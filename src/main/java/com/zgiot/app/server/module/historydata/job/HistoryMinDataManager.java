package com.zgiot.app.server.module.historydata.job;

import com.google.common.collect.Lists;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.app.server.service.impl.mapper.TMLMapper;
import com.zgiot.app.server.service.pojo.HistdataWhitelistModel;
import com.zgiot.common.pojo.DataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 分钟信号数据汇总
 */
@Component
public class HistoryMinDataManager {
    private static final Logger logger = LoggerFactory.getLogger(HistoryMinDataManager.class);
    @Autowired
    private TMLMapper tmlMapper;
    @Autowired
    private HistoryDataService historyDataService;

    public void collectHistoryMinData() {
        List<HistdataWhitelistModel> allHistdataMinWhitelist = tmlMapper.findAllHistdataMinWhitelist();
        logger.debug("{}信号分钟数据汇总", allHistdataMinWhitelist.toArray());
        for (HistdataWhitelistModel histdataWhitelistModel : allHistdataMinWhitelist) {
            long time = System.currentTimeMillis();
            Double avgValueDataInDuration = historyDataService.findAvgValueDataInDuration(histdataWhitelistModel.getThingCode(), histdataWhitelistModel.getMetricCode(), new Date(time - 60000), new Date(time));
            if (avgValueDataInDuration != null) {
                DataModel dataModel = new DataModel();
                dataModel.setThingCode(histdataWhitelistModel.getThingCode());
                dataModel.setMetricCode(histdataWhitelistModel.getMetricCode());
                dataModel.setValue(String.valueOf(avgValueDataInDuration));
                dataModel.setDataTimeStamp(new Date(time));
                historyDataService.insertMinDataBatch(Lists.newArrayList(dataModel));
            }
        }
    }
}
