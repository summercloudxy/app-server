package com.zgiot.app.server.module.reportforms.output.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.zgiot.app.server.module.reportforms.output.pojo.ProductCoalStatistics;
import com.zgiot.app.server.module.reportforms.output.service.CoalAnalysisService;
import com.zgiot.app.server.module.reportforms.output.utils.ReportFormDateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@JobHandler(value = "ProductCoalStatisticHandler")
public class ProductCoalStatisticJob extends IJobHandler {
    @Autowired
    private CoalAnalysisService coalAnalysisService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        Date dutyStartTime = ReportFormDateUtil.getNowDutyStartTime(new Date());
        Date dutyEndTime = DateUtils.addHours(dutyStartTime, 8);
        coalAnalysisService.clearAllProductCoalStatisticsInDuration(dutyStartTime, dutyEndTime);
        List<ProductCoalStatistics> productCoalStatisticsList = coalAnalysisService.getProductCoalStatisticsListFromOtherModule(ReportFormDateUtil.getNowDutyStartTime(new Date()));
        if (CollectionUtils.isNotEmpty(productCoalStatisticsList)) {
            coalAnalysisService.insertProductCoalStatisticsRecords(productCoalStatisticsList);
        }
        return ReturnT.SUCCESS;
    }
}
