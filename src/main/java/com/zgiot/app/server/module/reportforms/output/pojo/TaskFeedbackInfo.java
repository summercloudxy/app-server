package com.zgiot.app.server.module.reportforms.output.pojo;

import java.util.Date;

public class TaskFeedbackInfo {
    private Integer feedbackInfoId;
    private Double feedbackValue;
    private Date feedbackTime;

    public Integer getFeedbackInfoId() {
        return feedbackInfoId;
    }

    public void setFeedbackInfoId(Integer feedbackInfoId) {
        this.feedbackInfoId = feedbackInfoId;
    }

    public Double getFeedbackValue() {
        return feedbackValue;
    }

    public void setFeedbackValue(Double feedbackValue) {
        this.feedbackValue = feedbackValue;
    }

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
    }
}
