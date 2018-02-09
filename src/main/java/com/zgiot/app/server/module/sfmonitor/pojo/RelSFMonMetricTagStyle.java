package com.zgiot.app.server.module.sfmonitor.pojo;

import java.util.Date;

public class RelSFMonMetricTagStyle {
    private String metricTagCode;
    private String styleCode;
    private String userUuid;
    private Date createDate;
    private Date updateDate;
    private String comments;

    public String getMetricTagCode() {
        return metricTagCode;
    }

    public String getStyleCode() {
        return styleCode;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getComments() {
        return comments;
    }

    public void setMetricTagCode(String metricTagCode) {
        this.metricTagCode = metricTagCode;
    }

    public void setStyleCode(String styleCode) {
        this.styleCode = styleCode;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
