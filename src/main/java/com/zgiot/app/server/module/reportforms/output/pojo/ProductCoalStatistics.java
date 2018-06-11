package com.zgiot.app.server.module.reportforms.output.pojo;

import java.util.Date;

public class ProductCoalStatistics {
    private Integer id;
    private Date time;
    private Integer term;
    private Integer teamId;
    // 块A/B
    private String heavyMediumLump;
    // 主/再1/2/3
    private String heavyMediumSlack;
    //配比8号煤
    private String ratio8;
    //配比13号煤
    private String ratio13;
    //原煤入洗量
    private Double rawCoal;
    //产品
    private String targetCoal;
    //化验项目
    private String targetType;
    //化验值
    private Double value;

    private double sumValue;

    private int count;

    private Date startTime;

    private Date endTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getHeavyMediumLump() {
        return heavyMediumLump;
    }

    public void setHeavyMediumLump(String heavyMediumLump) {
        this.heavyMediumLump = heavyMediumLump;
    }

    public String getHeavyMediumSlack() {
        return heavyMediumSlack;
    }

    public void setHeavyMediumSlack(String heavyMediumSlack) {
        this.heavyMediumSlack = heavyMediumSlack;
    }

    public String getRatio8() {
        return ratio8;
    }

    public void setRatio8(String ratio8) {
        this.ratio8 = ratio8;
    }

    public String getRatio13() {
        return ratio13;
    }

    public void setRatio13(String ratio13) {
        this.ratio13 = ratio13;
    }

    public Double getRawCoal() {
        return rawCoal;
    }

    public void setRawCoal(Double rawCoal) {
        this.rawCoal = rawCoal;
    }

    public String getTargetCoal() {
        return targetCoal;
    }

    public void setTargetCoal(String targetCoal) {
        this.targetCoal = targetCoal;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public double getSumValue() {
        return sumValue;
    }

    public void setSumValue(double sumValue) {
        this.sumValue = sumValue;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
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
}
