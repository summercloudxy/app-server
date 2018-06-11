package com.zgiot.app.server.module.sfstop.entity.vo;

public class StopExamineResult {

    private String lineId;

    private String lineName;

    private String thingCode;

    private String thingName;

    private String examineType;

    private String typeName;

    private String examineInfo;


    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public String getThingName() {
        return thingName;
    }

    public void setThingName(String thingName) {
        this.thingName = thingName;
    }

    public String getExamineType() {
        return examineType;
    }

    public void setExamineType(String examineType) {
        this.examineType = examineType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getExamineInfo() {
        return examineInfo;
    }

    public void setExamineInfo(String examineInfo) {
        this.examineInfo = examineInfo;
    }
}
