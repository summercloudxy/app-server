package com.zgiot.app.server.module.sfstart.pojo;

import java.util.Date;

public class StartSignal {

    private int id;
    private String deviceId;
    private int typeId;
    private int name;
    private String dataLabel;
    private int type;
    private int alarmTerm;
    private int boolReal;
    private int enableCondition;
    private String term;
    private String deviceCode;
    private String create_user;
    private Date create_date;
    private String update_user;
    private Date update_date;
    private int state;
    private String groupCode;
    private String unit;
    private String channel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public String getDataLabel() {
        return dataLabel;
    }

    public void setDataLabel(String dataLabel) {
        this.dataLabel = dataLabel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAlarmTerm() {
        return alarmTerm;
    }

    public void setAlarmTerm(int alarmTerm) {
        this.alarmTerm = alarmTerm;
    }

    public int getBoolReal() {
        return boolReal;
    }

    public void setBoolReal(int boolReal) {
        this.boolReal = boolReal;
    }

    public int getEnableCondition() {
        return enableCondition;
    }

    public void setEnableCondition(int enableCondition) {
        this.enableCondition = enableCondition;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getUpdate_user() {
        return update_user;
    }

    public void setUpdate_user(String update_user) {
        this.update_user = update_user;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
