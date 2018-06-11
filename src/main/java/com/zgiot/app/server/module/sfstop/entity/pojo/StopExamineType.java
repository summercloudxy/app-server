package com.zgiot.app.server.module.sfstop.entity.pojo;

import java.util.Date;

/**
 * 停车自检类型
 */
public class StopExamineType {

    private Integer id;

    private String typeName;

    private String isStopNecessary;

    private String handleName;

    private String createUser;

    private Date createTime;

    private Integer isDelete;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getIsStopNecessary() {
        return isStopNecessary;
    }

    public void setIsStopNecessary(String isStopNecessary) {
        this.isStopNecessary = isStopNecessary;
    }

    public String getHandleName() {
        return handleName;
    }

    public void setHandleName(String handleName) {
        this.handleName = handleName;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}
