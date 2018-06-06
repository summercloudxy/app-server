package com.zgiot.app.server.module.historydata.enums;

/**
 * 汇总类型
 */
public enum SummaryTypeEnum {
    AVG("AVG"), // 算术平均值
    SUM_BY_ACCU("SUM_ACCU"), // 时间断内值累加
    SUM_BY_DIFF("SUM_DIFF"); // 时间断内最新减最旧

    private String sumType;


    SummaryTypeEnum(String sumType) {
        this.sumType = sumType;
    }

    public String getSumType() {
        return sumType;
    }

}
