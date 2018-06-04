package com.zgiot.app.server.module.historydata.job;

import com.google.common.collect.Lists;
import com.zgiot.app.server.config.ApplicationContextListener;
import com.zgiot.app.server.module.historydata.enums.AccuracyEnum;
import com.zgiot.app.server.module.historydata.enums.SummaryTypeEnum;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.app.server.service.impl.HistoryDataServiceImpl;
import com.zgiot.app.server.service.pojo.HistdataWhitelistModel;
import com.zgiot.common.pojo.DataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class SummaryHelper {
    private static final Logger logger = LoggerFactory.getLogger(SummaryHelper.class);
    @Autowired
    private HistoryDataService historyDataService;

    public void sumIt(HistdataWhitelistModel whiteModel, int mode, Date preDate, Date newDate,
                      AccuracyEnum sourceAccuracy , AccuracyEnum targetAccuracy, SummaryTypeEnum summaryTypeEnum) {
        List<DataModel> dmList = historyDataService
                .findHistoryDataList(Lists.newArrayList(whiteModel.getThingCode()),
                        Lists.newArrayList(whiteModel.getMetricCode()),
                        preDate, newDate, sourceAccuracy,targetAccuracy,summaryTypeEnum);

        Double sum = 0d;
        if (1 == mode) { // 累加
            for (DataModel dm : dmList) {
                Double d = safeParseDoubleValue(dm.getValue());
                sum += d;
            }
        } else if (2 == mode) { // 减法
            if (dmList.size() == 0) {
                // no action
            } else if (dmList.size() == 1) {
                sum = safeParseDoubleValue(dmList.get(0).getValue());
            } else {
                double latest = safeParseDoubleValue(dmList.get(dmList.size() - 1).getValue());
                double older = safeParseDoubleValue(dmList.get(0).getValue());
                sum = latest - older;
            }
        }

        // write sum value
        DataModel dataModel = new DataModel();
        dataModel.setThingCode(whiteModel.getThingCode());
        dataModel.setMetricCode(whiteModel.getMetricCode());
        dataModel.setValue(String.valueOf(sum));
        dataModel.setDataTimeStamp(preDate);
        historyDataService.insertBatchSuite(Lists.newArrayList(dataModel),targetAccuracy,
                SummaryTypeEnum.SUM_BY_ACCU);
    }

    public double safeParseDoubleValue(String doubleStr) {
        try {
            Double d = Double.parseDouble(doubleStr);
            return d;
        } catch (NumberFormatException e) {
            logger.warn("Parse double error. valueStr=`{}`, msg=`{}`", doubleStr, e.getMessage());
        }
        return 0d;
    }

    public void sumAvg(HistdataWhitelistModel whiteModel, Date preDate, Date newDate,
                       AccuracyEnum sourceAccuracy , AccuracyEnum targetAccuracy, SummaryTypeEnum summaryTypeEnum) {
        ApplicationContext context = ApplicationContextListener.getApplicationContext();
        HistoryDataServiceImpl historyDataService = (HistoryDataServiceImpl) context.getBean("historyDataServiceImpl");
        List<DataModel> dmList = historyDataService
                .findHistoryDataList(Lists.newArrayList(whiteModel.getThingCode()),
                        Lists.newArrayList(whiteModel.getMetricCode()),
                        preDate, newDate, sourceAccuracy,targetAccuracy,summaryTypeEnum);

        if (dmList != null && dmList.size()>0) {
            DataModel dataModel = new DataModel();
            dataModel.setThingCode(whiteModel.getThingCode());
            dataModel.setMetricCode(whiteModel.getMetricCode());
            dataModel.setValue(String.valueOf(getAvgValue(dmList)));
            dataModel.setDataTimeStamp(preDate);
            historyDataService.insertBatchSuite(Lists.newArrayList(dataModel),targetAccuracy,SummaryTypeEnum.AVG);
        }
    }

    private double getAvgValue(List<DataModel> resultList) {
        int count = 0;
        double sumValue = 0.0;
        double avgValue = 0.0;
        for (DataModel dataModel : resultList) {
            sumValue += safeParseDoubleValue(dataModel.getValue());
            count++;
        }
        if (count != 0) {
            avgValue = sumValue / count;
        }
        return avgValue;
    }
}
