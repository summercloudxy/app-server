package com.zgiot.app.server.module.alert.pojo;

import java.util.List;

public class AlertStatisticsRsp {
    //一段时间内全部报警数量统计
    private AlertStatisticsNum wholeStatisticsInfo;
    private AlertStatisticsNum unReleaseStatisticsInfo;
    private AlertStatisticsNum releaseStatisticsInfo;
    //每个设备的报警数量统计
    private List<AlertStatisticsNum> detailStatisticsInfo;
    //不同类型的报警数量统计
    private List<Integer> paramStatisticsInfo;
    private List<Integer> protectStatisticsInfo;
    private List<Integer> faultStatisticsInfo;

    public AlertStatisticsNum getWholeStatisticsInfo() {
        return wholeStatisticsInfo;
    }

    public void setWholeStatisticsInfo(AlertStatisticsNum wholeStatisticsInfo) {
        this.wholeStatisticsInfo = wholeStatisticsInfo;
    }

    public AlertStatisticsNum getUnReleaseStatisticsInfo() {
        return unReleaseStatisticsInfo;
    }

    public void setUnReleaseStatisticsInfo(AlertStatisticsNum unReleaseStatisticsInfo) {
        this.unReleaseStatisticsInfo = unReleaseStatisticsInfo;
    }

    public AlertStatisticsNum getReleaseStatisticsInfo() {
        return releaseStatisticsInfo;
    }

    public void setReleaseStatisticsInfo(AlertStatisticsNum releaseStatisticsInfo) {
        this.releaseStatisticsInfo = releaseStatisticsInfo;
    }

    public List<AlertStatisticsNum> getDetailStatisticsInfo() {
        return detailStatisticsInfo;
    }

    public void setDetailStatisticsInfo(List<AlertStatisticsNum> detailStatisticsInfo) {
        this.detailStatisticsInfo = detailStatisticsInfo;
    }

    public List<Integer> getParamStatisticsInfo() {
        return paramStatisticsInfo;
    }

    public void setParamStatisticsInfo(List<Integer> paramStatisticsInfo) {
        this.paramStatisticsInfo = paramStatisticsInfo;
    }

    public List<Integer> getProtectStatisticsInfo() {
        return protectStatisticsInfo;
    }

    public void setProtectStatisticsInfo(List<Integer> protectStatisticsInfo) {
        this.protectStatisticsInfo = protectStatisticsInfo;
    }

    public List<Integer> getFaultStatisticsInfo() {
        return faultStatisticsInfo;
    }

    public void setFaultStatisticsInfo(List<Integer> faultStatisticsInfo) {
        this.faultStatisticsInfo = faultStatisticsInfo;
    }


}
