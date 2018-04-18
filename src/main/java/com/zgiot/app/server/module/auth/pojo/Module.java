package com.zgiot.app.server.module.auth.pojo;

public class Module {
    private int id;
    private String moduleCode;
    private String moduleName;
    private int platformClientId;

    public int getId() {
        return id;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public int getPlatformClientId() {
        return platformClientId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setPlatformClientId(int platformClientId) {
        this.platformClientId = platformClientId;
    }
}
