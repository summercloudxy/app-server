package com.zgiot.app.server.module.historydata.enums;

/**
 * 精确度枚举
 */
public enum AccuracyEnum {

    SECOND("SECOND"),
    MINUTE("MINUTE"),
    HOUR("HOUR"),
    DAY("DAY");

    private String accuracyType;

    AccuracyEnum(String str) {
        accuracyType = str;
    }

    public String getAccuracyType() {
        return accuracyType;
    }

}
