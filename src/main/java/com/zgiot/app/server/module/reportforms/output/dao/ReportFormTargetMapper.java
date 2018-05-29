package com.zgiot.app.server.module.reportforms.output.dao;

import com.zgiot.app.server.module.reportforms.output.pojo.FeedbackTargetRelation;
import com.zgiot.app.server.module.reportforms.output.pojo.ReportFormTargetRecord;
import com.zgiot.app.server.module.reportforms.output.pojo.TaskFeedbackInfo;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReportFormTargetMapper {
    @Select(value = "select * from tb_report_form_target where duty_start_time = #{dutyStartTime} and term =#{term} and target_type = #{targetType}")
    ReportFormTargetRecord getTargetRecord(@Param("dutyStartTime") Date dutyStartTime, @Param("term") Integer term, @Param("targetType") Integer targetType);


    List<ReportFormTargetRecord> getTargetRecordList(@Param("dutyStartTime") Date dutyStartTime, @Param("term") Integer term, @Param("targetType") Integer targetType);


    @Insert(value = "insert into tb_report_form_target(duty_start_time,target_type,class_value,month_value,year_value,term) values (#{dutyStartTime},#{targetType},#{classValue},#{monthValue},#{yearValue},#{term})")
    void insertTargetRecord(ReportFormTargetRecord targetRecord);

    void insertTargetRecordList(@Param("list") List<ReportFormTargetRecord> targetRecordList);

    List<TaskFeedbackInfo> getTaskFeedbackInfoList(@Param("dutyStartTime") Date dutyStartTime, @Param("dutyEndTime") Date dutyEndTime);


    TaskFeedbackInfo getLastTaskFeedbackInfoInDuration(@Param("dutyStartTime") Date dutyStartTime, @Param("dutyEndTime") Date dutyEndTime, @Param("feedbackInfoId") Integer feedbackInfoId);


    @Select(value = "select * from rel_reportform_feedback_target")
    @MapKey(value = "feedbackInfoId")
    Map<Integer, FeedbackTargetRelation> getFeedbackTargetRelationMap();


    TaskFeedbackInfo getLastTaskFeedbackInfoBeforeDate(@Param("dutyStartTime") Date dutyStartTime,@Param("feedbackInfoId")Integer feedbackInfoId);

    TaskFeedbackInfo getFirstTaskFeedbackInfoBeforeDate(@Param("dutyStartTime") Date dutyStartTime,@Param("feedbackInfoId")Integer feedbackInfoId);
}
