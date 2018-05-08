package com.zgiot.app.server.module.sfstart.pojo;

import java.util.List;


public class StartDeviceControlInformation {

    // 设备条件
    List<StartDeviceRelation> startDeviceRelationList;

    // 参数条件
    List<StartRequirement> startRequirements;

    //人工干预(0:未干预，1:干预，2::解除干预)
    private StartManualInterventionRecord startManualInterventionRecord;

    public List<StartDeviceRelation> getStartDeviceRelationList() {
        return startDeviceRelationList;
    }

    public void setStartDeviceRelationList(List<StartDeviceRelation> startDeviceRelationList) {
        this.startDeviceRelationList = startDeviceRelationList;
    }

    public List<StartRequirement> getStartRequirements() {
        return startRequirements;
    }

    public void setStartRequirements(List<StartRequirement> startRequirements) {
        this.startRequirements = startRequirements;
    }

    public StartManualInterventionRecord getStartManualInterventionRecord() {
        return startManualInterventionRecord;
    }

    public void setStartManualInterventionRecord(StartManualInterventionRecord startManualInterventionRecord) {
        this.startManualInterventionRecord = startManualInterventionRecord;
    }
}
