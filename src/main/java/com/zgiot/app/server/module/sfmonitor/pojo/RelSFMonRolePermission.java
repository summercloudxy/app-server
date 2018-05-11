package com.zgiot.app.server.module.sfmonitor.pojo;

import java.util.Date;

public class RelSFMonRolePermission {

    private Long id;
    private Long roleId;
    private String thingCode;
    private boolean opView;
    private boolean opStartstop;
    private boolean opControl;
    private Date createDt;
    private Date updateDt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public boolean isOpView() {
        return opView;
    }

    public void setOpView(boolean opView) {
        this.opView = opView;
    }

    public boolean isOpStartstop() {
        return opStartstop;
    }

    public void setOpStartstop(boolean opStartstop) {
        this.opStartstop = opStartstop;
    }

    public boolean isOpControl() {
        return opControl;
    }

    public void setOpControl(boolean opControl) {
        this.opControl = opControl;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }
}
