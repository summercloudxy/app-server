package com.zgiot.app.server.module.alert.pojo;

import com.zgiot.common.constants.AlertConstants;
import com.zgiot.common.pojo.DataModel;

import java.util.Date;

/**
 * Created by xiayun on 2017/9/25.
 */
public class AlertData {
    private Integer id;
    private String thingCode;
    private String metricCode;
    private Short alertSource;
    private Short alertType;
    private Short alertLevel;
    private Date alertDateTime;
    private String alertInfo;
    private String reporter;
    private String alertStage = AlertConstants.STAGE_NOT_VERIFY;
    private Boolean repair = false;
    private String repairConfirmUser;
    private Date repairStartTime;
    private Date repairEndTime;
    private Boolean manualIntervention = false;
    private String feedBackImage;
    private String feedBackVideo;
    private String sceneConfirmUser;
    private Date sceneConfirmTime;
    private Boolean sceneConfirmState;
    private Boolean recovery = false;
    private Double paramValue;
    private Date lastUpdateTime;
    private Date verifyTime;

    public AlertData() {}

    public AlertData(DataModel dataModel, Short alertType, Short alertLevel, String alertInfo, Short alertSource,
                     String reporter) {
        this.thingCode = dataModel.getThingCode();
        this.metricCode = dataModel.getMetricCode();
        this.alertDateTime = new Date();
        this.alertInfo = alertInfo;
        this.alertLevel = alertLevel;
        this.alertSource = alertSource;
        this.alertType = alertType;
        this.reporter = reporter;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }

    public Short getAlertSource() {
        return alertSource;
    }

    public void setAlertSource(Short alertSource) {
        this.alertSource = alertSource;
    }

    public Short getAlertType() {
        return alertType;
    }

    public void setAlertType(Short alertType) {
        this.alertType = alertType;
    }

    public Short getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(Short alertLevel) {
        this.alertLevel = alertLevel;
    }

    public Date getAlertDateTime() {
        return alertDateTime;
    }

    public void setAlertDateTime(Date alertDateTime) {
        this.alertDateTime = alertDateTime;
    }

    public String getAlertInfo() {
        return alertInfo;
    }

    public void setAlertInfo(String alertInfo) {
        this.alertInfo = alertInfo;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getAlertStage() {
        return alertStage;
    }

    public void setAlertStage(String alertStage) {
        this.alertStage = alertStage;
    }

    public Boolean getRepair() {
        return repair;
    }

    public void setRepair(Boolean repair) {
        this.repair = repair;
    }

    public String getRepairConfirmUser() {
        return repairConfirmUser;
    }

    public void setRepairConfirmUser(String repairConfirmUser) {
        this.repairConfirmUser = repairConfirmUser;
    }

    public Date getRepairStartTime() {
        return repairStartTime;
    }

    public void setRepairStartTime(Date repairStartTime) {
        this.repairStartTime = repairStartTime;
    }

    public Date getRepairEndTime() {
        return repairEndTime;
    }

    public void setRepairEndTime(Date repairEndTime) {
        this.repairEndTime = repairEndTime;
    }

    public Boolean getManualIntervention() {
        return manualIntervention;
    }

    public void setManualIntervention(Boolean manualIntervention) {
        this.manualIntervention = manualIntervention;
    }

    public String getFeedBackImage() {
        return feedBackImage;
    }

    public void setFeedBackImage(String feedBackImage) {
        this.feedBackImage = feedBackImage;
    }

    public String getFeedBackVideo() {
        return feedBackVideo;
    }

    public void setFeedBackVideo(String feedBackVideo) {
        this.feedBackVideo = feedBackVideo;
    }

    public String getSceneConfirmUser() {
        return sceneConfirmUser;
    }

    public void setSceneConfirmUser(String sceneConfirmUser) {
        this.sceneConfirmUser = sceneConfirmUser;
    }

    public Date getSceneConfirmTime() {
        return sceneConfirmTime;
    }

    public void setSceneConfirmTime(Date sceneConfirmTime) {
        this.sceneConfirmTime = sceneConfirmTime;
    }

    public Boolean getSceneConfirmState() {
        return sceneConfirmState;
    }

    public void setSceneConfirmState(Boolean sceneConfirmState) {
        this.sceneConfirmState = sceneConfirmState;
    }

    public Boolean getRecovery() {
        return recovery;
    }

    public void setRecovery(Boolean recovery) {
        this.recovery = recovery;
    }

    public Double getParamValue() {
        return paramValue;
    }

    public void setParamValue(Double paramValue) {
        this.paramValue = paramValue;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    @Override
    public String toString() {
        return "AlertData{" +
                "id=" + id +
                ", thingCode='" + thingCode + '\'' +
                ", metricCode='" + metricCode + '\'' +
                ", alertSource=" + alertSource +
                ", alertType=" + alertType +
                ", alertLevel=" + alertLevel +
                ", alertDateTime=" + alertDateTime +
                ", alertInfo='" + alertInfo + '\'' +
                ", reporter='" + reporter + '\'' +
                ", alertStage='" + alertStage + '\'' +
                ", repair=" + repair +
                ", repairConfirmUser='" + repairConfirmUser + '\'' +
                ", repairStartTime=" + repairStartTime +
                ", repairEndTime=" + repairEndTime +
                ", manualIntervention=" + manualIntervention +
                ", feedBackImage='" + feedBackImage + '\'' +
                ", feedBackVideo='" + feedBackVideo + '\'' +
                ", sceneConfirmUser='" + sceneConfirmUser + '\'' +
                ", sceneConfirmTime=" + sceneConfirmTime +
                ", sceneConfirmState=" + sceneConfirmState +
                ", recovery=" + recovery +
                ", paramValue=" + paramValue +
                ", lastUpdateTime=" + lastUpdateTime +
                ", verifyTime=" + verifyTime +
                '}';
    }
}
