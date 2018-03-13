package com.zgiot.app.server.module.sfmonitor.pojo;

import java.util.Date;

public class RelSFMonMetricTagStyle {
    private int id;
    private String metricTagCode;
    private String styleCode;
    private Date createDate;
    private String comment;
    private String editor;

    public String getMetricTagCode() {
        return metricTagCode;
    }

    public String getStyleCode() {
        return styleCode;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getComment() {
        return comment;
    }

    public void setMetricTagCode(String metricTagCode) {
        this.metricTagCode = metricTagCode;
    }

    public void setStyleCode(String styleCode) {
        this.styleCode = styleCode;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
