package com.zgiot.app.server.module.historydata.job;

import com.zgiot.app.server.module.historydata.enums.AccuracyEnum;
import com.zgiot.app.server.module.historydata.enums.SummaryTypeEnum;
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
public class HistoryDayDataManager {
    private static final Logger logger = LoggerFactory.getLogger(HistoryDayDataManager.class);
    @Autowired
    private TMLMapper tmlMapper;
    @Autowired
    private SummaryHelper summaryHelper;

    public void collectHistoryData() {

        // get previous date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);
        calendar.add(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date preDate = calendar.getTime();

        calendar.add(Calendar.DATE, 1);
        calendar.add(Calendar.SECOND, -1);
        Date newDate = calendar.getTime();

        List<HistdataWhitelistModel> allHistdataWhitelist = tmlMapper.findAllHistdataWhitelist();
        logger.info("{}信号小时数据汇总", allHistdataWhitelist.toArray());

        for (HistdataWhitelistModel whiteModel : allHistdataWhitelist) {
           if (1 == whiteModel.getDayAvg()) {
               logger.debug("Sum for day avg. (thingCode=`{}`,metricCode=`{}`)",
                       whiteModel.getThingCode(), whiteModel.getMetricCode());
                summaryHelper.sumAvg(whiteModel, preDate, newDate, AccuracyEnum.HOUR,
                        AccuracyEnum.DAY,SummaryTypeEnum.AVG);
            }
            if (1 == whiteModel.getDayAccu()) { // 值累加
               logger.debug("Sum for day accu in accu mode. (thingCode=`{}`,metricCode=`{}`)",
                       whiteModel.getThingCode(), whiteModel.getMetricCode());
                summaryHelper.sumIt(whiteModel, 1, preDate, newDate, AccuracyEnum.HOUR,
                        AccuracyEnum.DAY,SummaryTypeEnum.SUM_BY_ACCU);
            } else if (2 == whiteModel.getDayAccu()) {
               logger.debug("Sum for hour accu in diff mode. (thingCode=`{}`,metricCode=`{}`)",
                       whiteModel.getThingCode(), whiteModel.getMetricCode());
                summaryHelper.sumIt(whiteModel, 2, preDate, newDate,AccuracyEnum.SECOND,
                        AccuracyEnum.DAY,SummaryTypeEnum.SUM_BY_DIFF);
            }
        }
    }
}
