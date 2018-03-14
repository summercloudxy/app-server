package com.zgiot.app.server.module.sfmonitor.controller;

import java.util.List;

public class SignalWrapperStyleReq {
    private int id;
    private String styleName;
    private List<String> signalWrapperNames;
    private String userName;
    private String comment;

    public String getStyleName() {
        return styleName;
    }

    public List<String> getSignalWrapperNames() {
        return signalWrapperNames;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getComment() {
        return comment;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public void setSignalWrapperNames(List<String> signalWrapperNames) {
        this.signalWrapperNames = signalWrapperNames;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
