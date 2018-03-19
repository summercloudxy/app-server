package com.zgiot.app.server.module.sfmonitor.controller;

import java.util.List;

public class SignalWrapperStyleRes {
    private int id;
    private String styleName;
    private String signalWrapperName;
    private String styleImageName;
    private String comment;

    public int getId() {
        return id;
    }

    public String getStyleName() {
        return styleName;
    }

    public String getSignalWrapperName() {
        return signalWrapperName;
    }

    public String getStyleImageName() {
        return styleImageName;
    }

    public String getComment() {
        return comment;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public void setSignalWrapperName(String signalWrapperName) {
        this.signalWrapperName = signalWrapperName;
    }

    public void setStyleImageName(String styleImageName) {
        this.styleImageName = styleImageName;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
