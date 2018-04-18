package com.zgiot.app.server.module.auth.controller.auth;

import com.zgiot.app.server.module.auth.pojo.Authority;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoginRes {
    private long userId;
    private String uuid;
    private String loginName;
    private String personName;
    private String token;
    private Date loginTimestamp;
    List<Authority> authorityList = new ArrayList<>();

    public long getUserId() {
        return userId;
    }

    public String getUuid() {
        return uuid;
    }

    public String getToken() {
        return token;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Date getLoginTimestamp() {
        return loginTimestamp;
    }

    public void setLoginTimestamp(Date loginTimestamp) {
        this.loginTimestamp = loginTimestamp;
    }

    public List<Authority> getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(List<Authority> authorityList) {
        this.authorityList = authorityList;
    }
}
