package com.zgiot.app.server.module.reportforms.output.enums;

import java.util.HashSet;
import java.util.Set;

public enum InfluenceTimeTypeEnum {


    PRODUCTION_TIME(1,"生产时间"),
    OVERHAUL_TIME(2,"检修时间"),
    OWE_COAL_TIME(3,"欠煤时间"),
    OWE_CAR_TIME(4,"欠车时间"),
    OWE_WATER_TIME(5,"欠水时间");
    private Integer influenceTypeCode;

    private String influenceTypeName;

    InfluenceTimeTypeEnum(Integer influenceTypeCode, String influenceTypeName){
        this.influenceTypeCode=influenceTypeCode;
        this.influenceTypeName=influenceTypeName;
    }

    /**
     * 获取influenceTypeCode Set
     * @return
     */
    public static Set<Integer> influenceTypeCodes() {
        Set<Integer> result = new HashSet<>(InfluenceTimeTypeEnum.values().length);
        for (InfluenceTimeTypeEnum e : InfluenceTimeTypeEnum.values()) {
            result.add(e.getinfluenceTypeCode());
        }
        return result;
    }

    public static InfluenceTimeTypeEnum getByInfluenceTypeCode(Integer influenceTypeCode) {
        for (InfluenceTimeTypeEnum e : InfluenceTimeTypeEnum.values()) {
            if (e.getinfluenceTypeCode().equals(influenceTypeCode)) {
                return e;
            }
        }
        return null;
    }

    public Integer getinfluenceTypeCode() {
        return influenceTypeCode;
    }

    public void setinfluenceTypeCode(Integer influenceTypeCode) {
        this.influenceTypeCode = influenceTypeCode;
    }

    public String getinfluenceTypeName() {
        return influenceTypeName;
    }

    public void setinfluenceTypeName(String influenceTypeName) {
        this.influenceTypeName = influenceTypeName;
    }
}
