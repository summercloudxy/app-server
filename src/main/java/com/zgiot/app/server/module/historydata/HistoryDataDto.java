package com.zgiot.app.server.module.historydata;

import java.util.Date;
import java.util.List;

/**
 * @author wangwei
 */
public class HistoryDataDto {

    private Date startTime;

    private Date endTime;

    private List<String> thingCodes;

    private Integer segment;

    private Boolean isTimeCorrection;

    private String accuracy;

    private String statsMode;// 统计模式。 AVG: 算数平均值， ACCUMULATED: 累计值,见SummaryTypeEnum类

    private String nullReturn;//nullReturn字段不写的话默认为0，即在segment不为空时（分钟、小时、天），查询数据小于segment个数，用默认值0来设置这个默认时刻的值，通过给
                              //nullReturn赋不同的值，可以在值为Null时获得不同的默认值

    public String getNullReturn() {
        return nullReturn;
    }

    public void setNullReturn(String nullReturn) {
        this.nullReturn = nullReturn;
    }

    public String getStatsMode() {
        return statsMode;
    }

    public void setStatsMode(String statsMode) {
        this.statsMode = statsMode;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<String> getThingCodes() {
        return thingCodes;
    }

    public void setThingCodes(List<String> thingCodes) {
        this.thingCodes = thingCodes;
    }

    public Integer getSegment() {
        return segment;
    }

    public void setSegment(Integer segment) {
        this.segment = segment;
    }


    public Boolean getTimeCorrection() {
        return isTimeCorrection;
    }

    public void setTimeCorrection(Boolean timeCorrection) {
        isTimeCorrection = timeCorrection;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }
}
