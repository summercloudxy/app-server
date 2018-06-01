package com.zgiot.app.server.module.historydata.job;

import com.zgiot.app.server.module.historydata.enums.AccuracyEnum;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.app.server.service.impl.mapper.TMLMapper;
import com.zgiot.app.server.service.pojo.HistdataWhitelistModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
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
    @Autowired
    private SummaryHelper summaryHelper;

    public void collectHistoryMinData() {

        // get previous min date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, -1);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date preMin = calendar.getTime();
        calendar.add(Calendar.MINUTE, 1);
        calendar.add(Calendar.SECOND, -1);
        Date newMin = calendar.getTime();

        List<HistdataWhitelistModel> allHistdataWhitelist = tmlMapper.findAllHistdataWhitelist();
        logger.info("信号分钟数据汇总, size={}", allHistdataWhitelist.size());

        allHistdataWhitelist.parallelStream().forEach(whiteModel -> {
            if (1 == whiteModel.getMinuteAvg()) {
                logger.debug("Sum for min avg. (thingCode=`{}`,metricCode=`{}`)",
                        whiteModel.getThingCode(), whiteModel.getMetricCode());
                this.summaryHelper.sumAvg(whiteModel, preMin, newMin, AccuracyEnum.SECOND,
                        AccuracyEnum.MINUTE,null);
            }

            if (1 == whiteModel.getMinuteAccu()) { // 值累加
                logger.debug("Sum for min accu in accu mode. (thingCode=`{}`,metricCode=`{}`)",
                        whiteModel.getThingCode(), whiteModel.getMetricCode());
                summaryHelper.sumIt(whiteModel, 1, preMin, newMin, AccuracyEnum.SECOND,
                        AccuracyEnum.MINUTE,null);
            } else if (2 == whiteModel.getMinuteAccu()) {
                logger.debug("Sum for min accu in diff mode. (thingCode=`{}`,metricCode=`{}`)",
                        whiteModel.getThingCode(), whiteModel.getMetricCode());
                summaryHelper.sumIt(whiteModel, 2, preMin, newMin,AccuracyEnum.SECOND,
                        AccuracyEnum.MINUTE,null);
            }
        });

    }

}
