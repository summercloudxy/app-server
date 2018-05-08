package com.zgiot.app.server.module.sfstart.enums;

/**
 * Created by wangfan on 2017/9/1.
 */
public enum EnumDeviceCode {

    // 启停包类信号标识开头
    PACKAGE("package", "Pack_"),
    // 启停区域类信号标识开头
    AREA("area", "Area_");

    private String code;
    private String info;

    EnumDeviceCode(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static String getInfoByCode(String code) {
        for (EnumDeviceCode e : EnumDeviceCode.values()) {
            if (e.getCode().equals(code)) {
                return e.getInfo();
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
