package com.zgiot.app.server.module.equipments.pojo;

public class Thing {

    /**
     * 主键
     */
    private Long id;

    /**
     * 设备父id
     */
    private Long parentThingId;

    /**
     *设备编号
     */
    private String thingCode;

    /**
     *设备名称
     */
    private String thingName;

    /**
     *
     */
    private String thingType1Code;

    /**
     *
     */
    private String thingType2Code;

    /**
     *
     */
    private String thingType3Code;

    /**
     *设备简称
     */
    private String thingShortName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentThingId() {
        return parentThingId;
    }

    public void setParentThingId(Long parentThingId) {
        this.parentThingId = parentThingId;
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

    public String getThingType1Code() {
        return thingType1Code;
    }

    public void setThingType1Code(String thingType1Code) {
        this.thingType1Code = thingType1Code;
    }

    public String getThingType2Code() {
        return thingType2Code;
    }

    public void setThingType2Code(String thingType2Code) {
        this.thingType2Code = thingType2Code;
    }

    public String getThingType3Code() {
        return thingType3Code;
    }

    public void setThingType3Code(String thingType3Code) {
        this.thingType3Code = thingType3Code;
    }

    public String getThingShortName() {
        return thingShortName;
    }

    public void setThingShortName(String thingShortName) {
        this.thingShortName = thingShortName;
    }
}
