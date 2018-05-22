package com.zgiot.app.server.module.sfstart.pojo;

import java.util.List;


public class StartSystem {

    // 系统编号
    private Long systemId;

    //系统期数 例如：一期
    private Integer productionLine;

    //系统总系统 例如：块煤A的总系统为块煤系统
    private String systemName;

    //系统名称 例如：块煤A
    private String subSystem;

    //系统类型 1：系统；2：设备；0：公用系统，不展示给用户看，3：独立设备系统，总览页面使用，独立设备系统需要，提供带煤量，4：双信号仓库，5:单信号仓库
    private String type;

    //系统状态 0:停止 1:开启 2:异常
    private String systemState;

    // 系统对应设备
    private List<StartDevice> startDevices;

    // 系统对应仓储系统
    private List<StartBrowseCoalDevice> startCoalDevice;

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Integer getProductionLine() {
        return productionLine;
    }

    public void setProductionLine(Integer productionLine) {
        this.productionLine = productionLine;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSystemState() {
        return systemState;
    }

    public void setSystemState(String systemState) {
        this.systemState = systemState;
    }

    public List<StartDevice> getStartDevices() {
        return startDevices;
    }

    public void setStartDevices(List<StartDevice> startDevices) {
        this.startDevices = startDevices;
    }

    public List<StartBrowseCoalDevice> getStartCoalDevice() {
        return startCoalDevice;
    }

    public void setStartCoalDevice(List<StartBrowseCoalDevice> startCoalDevice) {
        this.startCoalDevice = startCoalDevice;
    }
}
