package com.zgiot.app.server.module.equipments.pojo;

import java.util.Date;

public class RelThingSystem {

    /**
     * 主键
     */
    private Long id;

    /**
     *设备编号
     */
    private String thingCode;

    /**
     * system主键
     */
    private Long systemId;

    /**
     * 更新时间
     */
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
