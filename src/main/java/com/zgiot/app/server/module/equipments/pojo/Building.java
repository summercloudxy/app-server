package com.zgiot.app.server.module.equipments.pojo;

import java.util.Date;

public class Building {

    /**
     *主键
     */
    private Long id;

    /**
     *车间名
     */
    private String buildingName;

    /**
     *
     */
    private Integer seqNo;

    /**
     *更新时间
     */
    private Date updateDt;

    /**
     *总层数
     */
    private Integer maxFloor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Integer getMaxFloor() {
        return maxFloor;
    }

    public void setMaxFloor(Integer maxFloor) {
        this.maxFloor = maxFloor;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }
}
