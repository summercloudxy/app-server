package com.zgiot.app.server.module.historydata.enums;

/**
 * 精确度枚举
 */
public enum AccuracyEnum {

    SECOND("SECOND"),

    MINUTE("MINUTE");


    private String accuracyType;


    AccuracyEnum(String state) {
        accuracyType = accuracyType;
    }

    public String getAccuracyType() {
        return accuracyType;
    }

}
