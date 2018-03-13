package com.zgiot.app.server.module.sfmonitor.controller;

import java.util.Date;

public class FindSignalWrapperRes {
    private int id;
    private String tagName;
    private String metricName;
    private String editor;
    private Date createDate;
    private String comment;

    public int getId() {
        return id;
    }

    public String getTagName() {
        return tagName;
    }

    public String getMetricName() {
        return metricName;
    }

    public String getEditor() {
        return editor;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getComment() {
        return comment;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
