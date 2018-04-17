package com.zgiot.app.server.module.auth.pojo;

import java.util.Date;

public class AuthorityGroup {
    private int id;
    private String code;
    private String name;
    private int platformClientId;
    private int moduleId;
    private Date updateDate;

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getPlatformClientId() {
        return platformClientId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlatformClientId(int platformClientId) {
        this.platformClientId = platformClientId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
