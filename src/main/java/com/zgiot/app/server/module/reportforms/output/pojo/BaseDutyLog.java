package com.zgiot.app.server.module.reportforms.output.pojo;


import java.util.Date;


public class BaseDutyLog{

    protected Integer teamId;
    protected Integer isDay;
    protected Date startTime;
    protected Date endTime;
    protected String createUserUuid;
    protected String createUserInfo;
    protected Date createDt;
    protected String updateUserUuid;
    protected String updateUserInfo;
    protected Date updateDt;
    protected Integer isDelete;

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getIsDay() {
        return isDay;
    }

    public void setIsDay(Integer isDay) {
        this.isDay = isDay;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public String getCreateUserInfo() {
        return createUserInfo;
    }

    public void setCreateUserInfo(String createUserInfo) {
        this.createUserInfo = createUserInfo;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public String getUpdateUserInfo() {
        return updateUserInfo;
    }

    public void setUpdateUserInfo(String updateUserInfo) {
        this.updateUserInfo = updateUserInfo;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}
