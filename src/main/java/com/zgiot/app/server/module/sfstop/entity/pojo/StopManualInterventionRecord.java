package com.zgiot.app.server.module.sfstop.entity.pojo;

import java.util.Date;

/**
 * 停车人工干预记录
 */
public class StopManualInterventionRecord {

    private int manualInterventionId;

    private int operateId;

    private String thingCode;

    private int interventionState;

    private String interventionPersonId;

    private String relievePersonId;

    private Date createTime;

    private int isDelete;

    public int getManualInterventionId() {
        return manualInterventionId;
    }

    public void setManualInterventionId(int manualInterventionId) {
        this.manualInterventionId = manualInterventionId;
    }

    public int getOperateId() {
        return operateId;
    }

    public void setOperateId(int operateId) {
        this.operateId = operateId;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public int getInterventionState() {
        return interventionState;
    }

    public void setInterventionState(int interventionState) {
        this.interventionState = interventionState;
    }

    public String getInterventionPersonId() {
        return interventionPersonId;
    }

    public void setInterventionPersonId(String interventionPersonId) {
        this.interventionPersonId = interventionPersonId;
    }

    public String getRelievePersonId() {
        return relievePersonId;
    }

    public void setRelievePersonId(String relievePersonId) {
        this.relievePersonId = relievePersonId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }
}
