package com.zgiot.app.server.module.sfmedium.entity.po;

/**
 * 配介配置
 */
public class MediumCompoundingConfigDO {

    private int id;
    private int mediumPoolCode;
    private String mediumCompoundingLevel;
    private String lowLevel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMediumPoolCode() {
        return mediumPoolCode;
    }

    public void setMediumPoolCode(int mediumPoolCode) {
        this.mediumPoolCode = mediumPoolCode;
    }

    public String getMediumCompoundingLevel() {
        return mediumCompoundingLevel;
    }

    public void setMediumCompoundingLevel(String mediumCompoundingLevel) {
        this.mediumCompoundingLevel = mediumCompoundingLevel;
    }

    public String getLowLevel() {
        return lowLevel;
    }

    public void setLowLevel(String lowLevel) {
        this.lowLevel = lowLevel;
    }
}
