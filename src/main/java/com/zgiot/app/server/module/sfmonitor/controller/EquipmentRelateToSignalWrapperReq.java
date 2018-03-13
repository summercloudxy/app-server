package com.zgiot.app.server.module.sfmonitor.controller;

import com.zgiot.app.server.module.sfmonitor.pojo.StateControlAreaInfo;

import java.util.List;

public class EquipmentRelateToSignalWrapperReq {
    private List<String> keyChannels;
    private List<String> fromEquipments;
    private List<String> toEquipments;
    private List<String> similarEquipments;
    private List<String> selectedparameters;
    private List<EquipmentRelateToSignalWrapper> equipmentRelateToSignalWrappers;
    private StateControlAreaInfo stateControlAreaInfo;
    private String thingName;
    private String thingCode;
    private String editor;

    public List<String> getKeyChannels() {
        return keyChannels;
    }

    public List<String> getFromEquipments() {
        return fromEquipments;
    }

    public List<String> getToEquipments() {
        return toEquipments;
    }

    public List<String> getSimilarEquipments() {
        return similarEquipments;
    }

    public List<String> getSelectedparameters() {
        return selectedparameters;
    }

    public List<EquipmentRelateToSignalWrapper> getEquipmentRelateToSignalWrappers() {
        return equipmentRelateToSignalWrappers;
    }

    public StateControlAreaInfo getStateControlAreaInfo() {
        return stateControlAreaInfo;
    }

    public String getThingName() {
        return thingName;
    }

    public String getThingCode() {
        return thingCode;
    }

    public String getEditor() {
        return editor;
    }

    public void setKeyChannels(List<String> keyChannels) {
        this.keyChannels = keyChannels;
    }

    public void setFromEquipments(List<String> fromEquipments) {
        this.fromEquipments = fromEquipments;
    }

    public void setToEquipments(List<String> toEquipments) {
        this.toEquipments = toEquipments;
    }

    public void setSimilarEquipments(List<String> similarEquipments) {
        this.similarEquipments = similarEquipments;
    }

    public void setSelectedparameters(List<String> selectedparameters) {
        this.selectedparameters = selectedparameters;
    }

    public void setEquipmentRelateToSignalWrappers(List<EquipmentRelateToSignalWrapper> equipmentRelateToSignalWrappers) {
        this.equipmentRelateToSignalWrappers = equipmentRelateToSignalWrappers;
    }

    public void setStateControlAreaInfo(StateControlAreaInfo stateControlAreaInfo) {
        this.stateControlAreaInfo = stateControlAreaInfo;
    }

    public void setThingName(String thingName) {
        this.thingName = thingName;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }
}
