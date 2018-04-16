package com.zgiot.app.server.module.auth.pojo;

import java.util.Date;

public class UserLogin {
    private String userUuid;
    private int platClientId;
    private String token;
    private Date updateDate;
    private String ipAddress;
    private String macAddress;

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public int getPlatClientId() {
        return platClientId;
    }

    public void setPlatClientId(int platClientId) {
        this.platClientId = platClientId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
