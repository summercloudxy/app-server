package com.zgiot.app.server.module.sfstart.pojo;

import java.util.Date;


public class StartDeviceStateRecord {

    private int id;

    // 操作记录号
    private Integer operateId;
    // 设备号
    private String deviceId;
    // 设备启动状态(0:未启动，2:已启动，1:异常)
    private Integer state;
    // 创建时间
    private Date createTime;
    // 启动修改时间
    private Date updateTime;
    // 异常原因(预留字段)
    private String exceptionCause;

    public Integer getOperateId() {
        return operateId;
    }

    public void setOperateId(Integer operateId) {
        this.operateId = operateId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getExceptionCause() {
        return exceptionCause;
    }

    public void setExceptionCause(String exceptionCause) {
        this.exceptionCause = exceptionCause;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
