package com.zgiot.app.server.module.sfstart.pojo;

import java.util.Date;


public class StartOperationRecord {

    // 操作记录id
    private Integer operateId;

    // 创建启车任务人id(检查)
    private String createUser;

    // 确认启车人id（正式启车）
    private String operateUser;

    // 启车状态（0:数据库中没有启车任务,1:检查中,2:启车中）
    private Integer operateState;

    //选择的系统id
    private String systemIds;

    // 创建时间
    private Date createdDate;

    // 是否删除
    private Integer isDelete;

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

    public String getSystemIds() {
        return systemIds;
    }

    public void setSystemIds(String systemIds) {
        this.systemIds = systemIds;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}
