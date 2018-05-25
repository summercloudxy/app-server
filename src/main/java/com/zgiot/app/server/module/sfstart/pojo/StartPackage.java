package com.zgiot.app.server.module.sfstart.pojo;

import java.util.List;


public class StartPackage {

    // 包编号
    private String packageId;

    // 包名称
    private String packageName;

    // 所属大区id
    private String regionId;

    // 所属大区名称
    private String regionName;

    // 所属区域id
    private String areaId;

    // 所属区域名称
    private String areaName;

    // 包对应设备
    private List<StartDevice> startDevices;

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public List<StartDevice> getStartDevices() {
        return startDevices;
    }

    public void setStartDevices(List<StartDevice> startDevices) {
        this.startDevices = startDevices;
    }
}
