package com.zgiot.app.server.module.bellows.enumeration;

/**
 * @author wangwei
 */
public enum EnumValveState {

    OPEN("OPEN"),

    CLOSE("CLOSE"),

    UNKNOWN("UNKNOWN");

    private String state;

    EnumValveState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
