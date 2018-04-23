package com.zgiot.app.server.module.alert.pojo;

/**
 * 非动力设备
 */
public class NoPowerThing {

    private int id;
    private String thingCode;
    private String thingName;
    private String system;
    private String subThingCode;
    private String subThingName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getSubThingCode() {
        return subThingCode;
    }

    public void setSubThingCode(String subThingCode) {
        this.subThingCode = subThingCode;
    }

    public String getSubThingName() {
        return subThingName;
    }

    public void setSubThingName(String subThingName) {
        this.subThingName = subThingName;
    }
}
