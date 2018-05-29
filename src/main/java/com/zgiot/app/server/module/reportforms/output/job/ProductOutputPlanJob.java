package com.zgiot.app.server.module.reportforms.output.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.zgiot.app.server.module.reportforms.output.constant.ReportFormCoalTypeConstant;
import com.zgiot.app.server.module.reportforms.output.constant.TaskConstant;
import com.zgiot.app.server.module.reportforms.output.dao.ReportFormProductionMapper;
import com.zgiot.app.server.module.reportforms.output.dao.ReportFormTargetMapper;
import com.zgiot.app.server.module.reportforms.output.pojo.ReportFormProductOutput;
import com.zgiot.app.server.module.reportforms.output.pojo.TaskFeedbackInfo;
import com.zgiot.app.server.module.reportforms.output.utils.ReportFormDateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


@Component
@JobHandler(value = "ProductOutputPlanHandler")
public class ProductOutputPlanJob  extends IJobHandler {
    @Autowired
    private ReportFormTargetMapper reportFormTargetMapper;
    @Autowired
    private ReportFormProductionMapper reportFormProductionMapper;


    public ReturnT<String> execute(String s) throws Exception {
        createMonthPlan(new Date());
        return ReturnT.SUCCESS;
    }

    /**
     * 生成一个月的生产计划,每月最后一天4点执行
     * @param date
     */
    public void createMonthPlan(Date date) {
        Date currentMonthFirstDay = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        Date lastMonthFirstDay = DateUtils.addMonths(currentMonthFirstDay, -1);
        TaskFeedbackInfo clenedCoalPlan = reportFormTargetMapper.getLastTaskFeedbackInfoInDuration(lastMonthFirstDay, currentMonthFirstDay, TaskConstant.FEEDBACK_INFO_ID_CLENED_COAL);
        TaskFeedbackInfo washedCoalPlan = reportFormTargetMapper.getLastTaskFeedbackInfoInDuration(lastMonthFirstDay, currentMonthFirstDay, TaskConstant.FEEDBACK_INFO_ID_WASHED_COAL);
        insertDaysProductOutputPlan(date, clenedCoalPlan,ReportFormCoalTypeConstant.clenedCoal);
        insertDaysProductOutputPlan(date, washedCoalPlan,ReportFormCoalTypeConstant.washedCoal);
    }

    /**
     * 生成一个月每一天的计划数据
     * @param date  生产开始时间，4点
     * @param coalPlan
     * @param coalType
     */
    private void insertDaysProductOutputPlan(Date date, TaskFeedbackInfo coalPlan,int coalType) {
        if (coalPlan != null) {
            List<ReportFormProductOutput> coalPlanOutputList = countAvgPlan(ReportFormDateUtil.getProductStartTime(date), coalPlan,coalType);
            if (CollectionUtils.isNotEmpty(coalPlanOutputList)) {
                reportFormProductionMapper.insertReportFormProductOutputPlans(coalPlanOutputList);
            }
        }
    }

    /**
     *
     * @param date  生产日报开始时间，上个月最后一天16点
     * @param coalMonthPlan
     * @param coalType
     * @return
     */
    private List<ReportFormProductOutput> countAvgPlan(Date date, TaskFeedbackInfo coalMonthPlan, int coalType) {
        int daysOfMonth = ReportFormDateUtil.getDaysOfMonth(DateUtils.addDays(date,1));
        double monthTaskValue = coalMonthPlan.getFeedbackValue();
        BigDecimal avgDayPlan = BigDecimal.valueOf(monthTaskValue).divide(new BigDecimal(daysOfMonth), 0, RoundingMode.UP);
        int dayPlan = avgDayPlan.intValue();
        int monthPlan = 0;
        int lastDayPlan = (int) monthTaskValue - dayPlan * (daysOfMonth - 1);
        List<ReportFormProductOutput> reportFormProductOutputList = new ArrayList<>();
        for (int i = 0; i < daysOfMonth; i++) {
            //最后一天
            if (i == daysOfMonth - 1) {
                dayPlan = lastDayPlan;
            }
            monthPlan += dayPlan;
            ReportFormProductOutput reportFormProductOutput = new ReportFormProductOutput();
            reportFormProductOutput.setProductStartTime(DateUtils.addDays(date, i));
            reportFormProductOutput.setPlanDay(dayPlan);
            reportFormProductOutput.setPlanMonth(monthPlan);
            reportFormProductOutput.setPlanYear(monthPlan);
            reportFormProductOutput.setCoalType(coalType);
            reportFormProductOutputList.add(reportFormProductOutput);
        }
        //获取上个月最后一天数据
        Map<Integer, ReportFormProductOutput> lastReportFormOutPutPlanMap = reportFormProductionMapper.getReportFormProductOutputPlan(DateUtils.addDays(date, -1));
        if (lastReportFormOutPutPlanMap.containsKey(coalType)) {
            ReportFormProductOutput lastReportFormOutPutPlan = lastReportFormOutPutPlanMap.get(coalType);
            if (!ReportFormDateUtil.isYearFirstDay(date)) {
                for (ReportFormProductOutput reportFormProductOutput : reportFormProductOutputList) {
                    reportFormProductOutput.setPlanYear(reportFormProductOutput.getPlanYear() + lastReportFormOutPutPlan.getPlanYear());
                }
            }
        }
        return reportFormProductOutputList;
    }
}
