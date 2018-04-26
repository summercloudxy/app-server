package com.zgiot.app.server.module.userfavor.controller;

import java.util.List;

public class UserFavorReq {
    private String userUuid;
    private List<Long> moduleIds;

    public String getUserUuid() {
        return userUuid;
    }

    public List<Long> getModuleIds() {
        return moduleIds;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public void setModuleIds(List<Long> moduleIds) {
        this.moduleIds = moduleIds;
    }
}
