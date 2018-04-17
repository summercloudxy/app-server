package com.zgiot.app.server.module.manufacturer.pojo;

import java.util.Date;

public class Manufacturer {

    private Long id;

    /**
     * SB设备,YB仪表,BJ部件,FM阀门,GD管道,ZB闸板,LC溜槽,BYQ变压器
     */
    private String typeCode;

    /**
     * 厂家编号
     */
    private String manufacturerCode;

    /**
     * 厂家名称
     */
    private String manufacturerName;

    /**
     * 厂家简称
     */
    private String manufacturerShortName;

    /**
     * 厂家地址
     */
    private String manufacturerAddress;

    /**
     * 厂家官方网站
     */
    private String manufacturerWeb;

    /**
     * 厂家联系电话
     */
    private String manufacturerTel;

    /**
     * 更新时间
     */
    private Date updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getManufacturerCode() {
        return manufacturerCode;
    }

    public void setManufacturerCode(String manufacturerCode) {
        this.manufacturerCode = manufacturerCode;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getManufacturerShortName() {
        return manufacturerShortName;
    }

    public void setManufacturerShortName(String manufacturerShortName) {
        this.manufacturerShortName = manufacturerShortName;
    }

    public String getManufacturerAddress() {
        return manufacturerAddress;
    }

    public void setManufacturerAddress(String manufacturerAddress) {
        this.manufacturerAddress = manufacturerAddress;
    }

    public String getManufacturerWeb() {
        return manufacturerWeb;
    }

    public void setManufacturerWeb(String manufacturerWeb) {
        this.manufacturerWeb = manufacturerWeb;
    }

    public String getManufacturerTel() {
        return manufacturerTel;
    }

    public void setManufacturerTel(String manufacturerTel) {
        this.manufacturerTel = manufacturerTel;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
