package com.zgiot.app.server.module.sfmonitor.pojo;

import com.zgiot.common.pojo.DataModel;

import java.util.Date;

public class SFMonEquipMonitorInfo {
    private int id;
    private String thingCode;
    private String thingName;
    private String configProgress;
    private String userUuid;
    private Date createDate;
    private Date updateDate;
    private String comments;

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
