package com.zgiot.app.server.module.auth.pojo;

public class AuthorityType {
    private int id;
    private String authorityTypeName;
    private int platformClientId;
    private int moduleId;
    private int parentId;

    public int getId() {
        return id;
    }

    public String getAuthorityTypeName() {
        return authorityTypeName;
    }

    public int getPlatformClientId() {
        return platformClientId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthorityTypeName(String authorityTypeName) {
        this.authorityTypeName = authorityTypeName;
    }

    public void setPlatformClientId(int platformClientId) {
        this.platformClientId = platformClientId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
