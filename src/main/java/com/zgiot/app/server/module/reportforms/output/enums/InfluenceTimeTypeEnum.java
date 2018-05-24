package com.zgiot.app.server.module.reportforms.output.enums;

import java.util.HashSet;
import java.util.Set;

public enum InfluenceTimeTypeEnum {


    PRODUCTION_TIME(Short.parseShort("1"),2),
    OVERHAUL_TIME(Short.parseShort("2"),1),
    OWE_COAL_TIME(Short.parseShort("3"),3),
    OWE_CAR_TIME(Short.parseShort("4"),4),
    OWE_WATER_TIME(Short.parseShort("5"),5);

    private Short productionDesc;
    private Integer influenceType;

    InfluenceTimeTypeEnum(Short productionDesc, Integer influenceType){
        this.productionDesc=productionDesc;
        this.influenceType=influenceType;
    }

    /**
     * 获取influenceTypeCode Set
     * @return
     */
    public static Set<Short> influenceTimeTypeEnumDescs() {
        Set<Short> result = new HashSet<>(InfluenceTimeTypeEnum.values().length);
        for (InfluenceTimeTypeEnum e : InfluenceTimeTypeEnum.values()) {
            result.add(e.getProductionDesc());
        }
        return result;
    }

    public static InfluenceTimeTypeEnum getInfluenceTimeTypeByDesc(Short productionDesc) {
        for (InfluenceTimeTypeEnum e : InfluenceTimeTypeEnum.values()) {
            if (e.getProductionDesc().equals(productionDesc)) {
                return e;
            }
        }
        return null;
    }

    public Short getProductionDesc() {
        return productionDesc;
    }

    public void setProductionDesc(Short productionDesc) {
        this.productionDesc = productionDesc;
    }

    public Integer getInfluenceType() {
        return influenceType;
    }

    public void setInfluenceType(Integer influenceType) {
        this.influenceType = influenceType;
    }
}
