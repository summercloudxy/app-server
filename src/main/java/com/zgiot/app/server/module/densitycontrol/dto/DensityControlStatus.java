package com.zgiot.app.server.module.densitycontrol.dto;

public class DensityControlStatus {

    private Integer systemStatus;// 系统状态:0没运行;1单系统;2双系统
    private String systemThingCode1;// 系统设备编号1
    private String systemThingCode2;// 系统设备编号2
    private String densityThingCode;// 主控密度计设备编号
    private Double density;// 主控密度计当前密度
    private Double level;// 当前液位

    public Integer getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(Integer systemStatus) {
        this.systemStatus = systemStatus;
    }

    public String getSystemThingCode1() {
        return systemThingCode1;
    }

    public void setSystemThingCode1(String systemThingCode1) {
        this.systemThingCode1 = systemThingCode1;
    }

    public String getSystemThingCode2() {
        return systemThingCode2;
    }

    public void setSystemThingCode2(String systemThingCode2) {
        this.systemThingCode2 = systemThingCode2;
    }

    public String getDensityThingCode() {
        return densityThingCode;
    }

    public void setDensityThingCode(String densityThingCode) {
        this.densityThingCode = densityThingCode;
    }

    public Double getDensity() {
        return density;
    }

    public void setDensity(Double density) {
        this.density = density;
    }

    public Double getLevel() {
        return level;
    }

    public void setLevel(Double level) {
        this.level = level;
    }
}
