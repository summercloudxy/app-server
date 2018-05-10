package com.zgiot.app.server.module.equipments.pojo;

public class ThingProperties {

    /**
     * 主键
     */
    private Long id;
    /**
     *设备编号
     */
    private String thingCode;

    /**
     *属性名
     */
    private String propKey;

    /**
     *属性值
     */
    private String propValue;

    /**
     *属性类型
     */
    private String propType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public String getPropKey() {
        return propKey;
    }

    public void setPropKey(String propKey) {
        this.propKey = propKey;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public String getPropType() {
        return propType;
    }

    public void setPropType(String propType) {
        this.propType = propType;
    }
}
