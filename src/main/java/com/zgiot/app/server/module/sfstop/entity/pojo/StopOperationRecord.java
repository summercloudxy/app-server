package com.zgiot.app.server.module.sfstop.entity.pojo;

import java.util.Date;

/**
 * 停车操作记录
 */
public class StopOperationRecord {

    // 操作记录id
    private Integer operateId;

    // 创建启车任务人id(检查)
    private String createUser;

    // 确认启车人id
    private String operateUser;

    private Integer operateState;

    //选择的系统id
    private String operateSystem;

    // 创建时间
    private Date createdTime;

    private String updateUser;

    private Date updateTime;

    // 是否删除
    private Integer isDelete;

    private int system;

    private Long stopElapsedTime;

    private Long stopPauseTime;


    public Long getStopElapsedTime() {
        return stopElapsedTime;
    }

    public void setStopElapsedTime(Long stopElapsedTime) {
        this.stopElapsedTime = stopElapsedTime;
    }

    public Long getStopPauseTime() {
        return stopPauseTime;
    }

    public void setStopPauseTime(Long stopPauseTime) {
        this.stopPauseTime = stopPauseTime;
    }

    public int getSystem() {
        return system;
    }

    public void setSystem(int system) {
        this.system = system;
    }

    public Integer getOperateId() {
        return operateId;
    }

    public void setOperateId(Integer operateId) {
        this.operateId = operateId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getOperateUser() {
        return operateUser;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }

    public Integer getOperateState() {
        return operateState;
    }

    public void setOperateState(Integer operateState) {
        this.operateState = operateState;
    }

    public String getOperateSystem() {
        return operateSystem;
    }

    public void setOperateSystem(String operateSystem) {
        this.operateSystem = operateSystem;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}
