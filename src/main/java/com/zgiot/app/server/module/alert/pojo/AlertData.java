package com.zgiot.app.server.module.alert.pojo;

import com.zgiot.common.constants.AlertConstants;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.SystemModel;
import com.zgiot.common.pojo.ThingModel;

import java.util.Date;
import java.util.List;

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
    private Double paramLower;
    private Double paramUpper;
    private Date lastUpdateTime;
    private Date verifyTime;
    private String postWorker;
    private String dispatcher;
    private Date releaseTime;
    private Long alertDuration;
    private Long repairDuration;
    private List<AlertMessage> alertMessageList;
    private int messageUnreadCount;
    private List<String> feedBackImageList;
    private ThingModel thingModel;
    private SystemModel systemModel;


    public List<AlertMessage> getAlertMessageList() {
        return alertMessageList;
    }

    public void setAlertMessageList(List<AlertMessage> alertMessageList) {
        this.alertMessageList = alertMessageList;
    }



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

    public Boolean isRepair() {
        return repair;
    }

    public void setRepair(Boolean isRepair) {
        this.repair = isRepair;
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

    public Boolean isManualIntervention() {
        return manualIntervention;
    }

    public void setManualIntervention(Boolean isManualIntervention) {
        this.manualIntervention = isManualIntervention;
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

    public Boolean isRecovery() {
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

    public String getPostWorker() {
        return postWorker;
    }

    public void setPostWorker(String postWorker) {
        this.postWorker = postWorker;
    }

    public String getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(String dispatcher) {
        this.dispatcher = dispatcher;
    }

    public Double getParamLower() {
        return paramLower;
    }

    public void setParamLower(Double paramLower) {
        this.paramLower = paramLower;
    }

    public Double getParamUpper() {
        return paramUpper;
    }

    public void setParamUpper(Double paramUpper) {
        this.paramUpper = paramUpper;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Long getAlertDuration() {
        return alertDuration;
    }

    public void setAlertDuration(Long alertDuration) {
        this.alertDuration = alertDuration;
    }

    public int getMessageUnreadCount() {
        return messageUnreadCount;
    }

    public void setMessageUnreadCount(int messageUnreadCount) {
        this.messageUnreadCount = messageUnreadCount;
    }

    public List<String> getFeedBackImageList() {
        return feedBackImageList;
    }

    public void setFeedBackImageList(List<String> feedBackImageList) {
        this.feedBackImageList = feedBackImageList;
    }

    public Long getRepairDuration() {
        return repairDuration;
    }

    public void setRepairDuration(Long repairDuration) {
        this.repairDuration = repairDuration;
    }

    public ThingModel getThingModel() {
        return thingModel;
    }

    public void setThingModel(ThingModel thingModel) {
        this.thingModel = thingModel;
    }

    public SystemModel getSystemModel() {
        return systemModel;
    }

    public void setSystemModel(SystemModel systemModel) {
        this.systemModel = systemModel;
    }
}
