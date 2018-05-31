package com.zgiot.app.server.module.sfstop.entity.pojo;

import java.util.Date;

/**
 * 停车方案选择
 */
public class StopChoiceSet {

    private Long id;

    private Integer rawStopSet;

    private Integer tcsStopSet;

    private Integer filterpressStopSet;

    private String beltRoute;

    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRawStopSet() {
        return rawStopSet;
    }

    public void setRawStopSet(Integer rawStopSet) {
        this.rawStopSet = rawStopSet;
    }

    public Integer getTcsStopSet() {
        return tcsStopSet;
    }

    public void setTcsStopSet(Integer tcsStopSet) {
        this.tcsStopSet = tcsStopSet;
    }

    public Integer getFilterpressStopSet() {
        return filterpressStopSet;
    }

    public void setFilterpressStopSet(Integer filterpressStopSet) {
        this.filterpressStopSet = filterpressStopSet;
    }

    public String getBeltRoute() {
        return beltRoute;
    }

    public void setBeltRoute(String beltRoute) {
        this.beltRoute = beltRoute;
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
}
