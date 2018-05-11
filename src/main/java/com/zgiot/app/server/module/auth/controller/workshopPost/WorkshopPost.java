package com.zgiot.app.server.module.auth.controller.workshopPost;

import java.util.Date;
import java.util.List;

public class WorkshopPost {

    private Integer id;


    private String workshopPostId;

    private String workshopPostName;

    private Integer departmentId;

    private Integer floorAreaId;

    private List<String> thingCodeList;

    private transient String thingCodes;

    public List<String> getThingCodeList() {
        return thingCodeList;
    }

    public void setThingCodeList(List<String> thingCodeList) {
        this.thingCodeList = thingCodeList;
    }

    private String other;


    private String createUserUuid;


    private String createUserInfo;


    private Date createDt;

    private String updateUserUuid;


    private String updateUserInfo;


    private Date updateDt;


    private Boolean isDelete;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getWorkshopPostId() {
        return workshopPostId;
    }


    public void setWorkshopPostId(String workshopPostId) {
        this.workshopPostId = workshopPostId == null ? null : workshopPostId.trim();
    }


    public String getWorkshopPostName() {
        return workshopPostName;
    }


    public void setWorkshopPostName(String workshopPostName) {
        this.workshopPostName = workshopPostName == null ? null : workshopPostName.trim();
    }

    public Integer getDepartmentId() {
        return departmentId;
    }


    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getFloorAreaId() {
        return floorAreaId;
    }


    public void setFloorAreaId(Integer floorAreaId) {
        this.floorAreaId = floorAreaId;
    }


    public String getThingCodes() {
        return thingCodes;
    }

    public void setThingCodes(String thingCodes) {
        this.thingCodes = thingCodes == null ? null : thingCodes.trim();
    }


    public String getOther() {
        return other;
    }


    public void setOther(String other) {
        this.other = other == null ? null : other.trim();
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

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }
}