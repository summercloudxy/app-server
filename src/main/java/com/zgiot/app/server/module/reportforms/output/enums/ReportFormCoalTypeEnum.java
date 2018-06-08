package com.zgiot.app.server.module.reportforms.output.enums;

import java.util.HashSet;
import java.util.Set;

public enum  ReportFormCoalTypeEnum {
    CLENED_COAL(2,"气精煤"),
    WASHED_COAL(1,"洗混煤"),
    SLIME(3,"煤泥"),
    WASHERY_REJECTS(4,"洗矸"),
    RAW_COAL(5,"入洗原煤"),
    MEDIUM(6,"介质");

    private Integer code;

    private String name;

    ReportFormCoalTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 获取influenceTypeCode Set
     * @return
     */
    public static Set<Integer> getCodes() {
        Set<Integer> result = new HashSet<>(ReportFormCoalTypeEnum.values().length);
        for (ReportFormCoalTypeEnum e : ReportFormCoalTypeEnum.values()) {
            result.add(e.getCode());
        }
        return result;
    }

    public static ReportFormCoalTypeEnum getReportFormCoalType(Integer code) {
        for (ReportFormCoalTypeEnum e : ReportFormCoalTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
