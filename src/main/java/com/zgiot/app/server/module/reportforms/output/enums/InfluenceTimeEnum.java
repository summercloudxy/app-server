package com.zgiot.app.server.module.reportforms.output.enums;

import java.util.HashSet;
import java.util.Set;

public enum InfluenceTimeEnum {

    PRODUCTION_TIME(1,"生产时间"),
    OVERHAUL_TIME(2,"检修时间"),
    OWE_COAL_TIME(3,"欠煤时间"),
    OWE_CAR_TIME(4,"欠车时间"),
    OWE_WATER_TIME(5,"欠水时间"),
    RESTRICTED_ELECTRICITY_TIME(6,"限电时间"),
    PREPARE_PLANT(7,"准备时间"),
    COAL_WASHING_PLANT(8,"洗煤车间"),
    TRANSPORT_SALE_PLANT(9,"运销车间"),
    COAL_QUALITY_PLANT(10,"煤质车间"),
    GANGUE_TRANSPORT_PLANT(11,"矸运车间"),
    FIN_RAY_COMPANY(12,"芬雷公司"),
    TENGEN_COMPANY(13,"美腾公司"),
    BIG_GANGUE_INFLUENCE(14,"大矸影响"),
    RESTS(15,"其他");

    private Integer influenceTypeCode;

    private String influenceTypeName;

    InfluenceTimeEnum(Integer influenceTypeCode, String influenceTypeName){
        this.influenceTypeCode=influenceTypeCode;
        this.influenceTypeName=influenceTypeName;
    }

    /**
     * 获取influenceTypeCode Set
     * @return
     */
    public static Set<Integer> influenceTypeCodes() {
        Set<Integer> result = new HashSet<>(InfluenceTimeEnum.values().length);
        for (InfluenceTimeEnum e : InfluenceTimeEnum.values()) {
            result.add(e.getinfluenceTypeCode());
        }
        return result;
    }

    public static InfluenceTimeEnum getByInfluenceTypeCode(Integer influenceTypeCode) {
        for (InfluenceTimeEnum e : InfluenceTimeEnum.values()) {
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
