package com.zgiot.app.server.module.sfmonitor.pojo;

import com.zgiot.common.pojo.DataModel;

import java.util.Date;

public class SFMonEquipMonitorInfo {
    private int id;
    private String thingCode;
    private String thingName;
    private String configProgress;
    private String editor;
    private Date createDate;
    private String comment;

    public int getId() {
        return id;
    }

    public String getThingCode() {
        return thingCode;
    }

    public String getThingName() {
        return thingName;
    }

    public String getConfigProgress() {
        return configProgress;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getEditor() {
        return editor;
    }

    public String getComment() {
        return comment;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public void setThingName(String thingName) {
        this.thingName = thingName;
    }

    public void setConfigProgress(String configProgress) {
        this.configProgress = configProgress;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
