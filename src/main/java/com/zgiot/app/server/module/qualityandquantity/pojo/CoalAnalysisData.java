package com.zgiot.app.server.module.qualityandquantity.pojo;

import com.zgiot.common.pojo.CoalAnalysisRecord;

import java.util.Date;

public class CoalAnalysisData {

    private String thingCode;
    private String thingName;
    /**
     * 灰分
     */
    private MetricDataValue aad;
    /**
     * 水分
     */
    private MetricDataValue mt;
    /**
     * 硫份
     */
    private MetricDataValue stad;
    /**
     * 发热量
     */
    private MetricDataValue qnetar;

    private Date time;
    /**
     * 化验项目
     */
    private String target;
    /**
     * 平均分选密度
     */
    private MetricDataValue avgDensity;
    /**
     * 平均顶水流量
     */
    private MetricDataValue avgFlow;
    /**
     * 系统  一期/二期
     */
    private Integer system;
    /**
     * sample:请求具体设备化验数据
     * target:请求某一项目化验数据
     */
    private String type;

    /**
     * 每一位代表是否显示该信息：0:灰  1:水  2：硫  3:密度
     */
    private Integer showBit;



    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public String getThingName() {
        return thingName;
    }

    public void setThingName(String thingName) {
        this.thingName = thingName;
    }

    public MetricDataValue getAad() {
        return aad;
    }

    public void setAad(MetricDataValue aad) {
        this.aad = aad;
    }

    public MetricDataValue getMt() {
        return mt;
    }

    public void setMt(MetricDataValue mt) {
        this.mt = mt;
    }

    public MetricDataValue getStad() {
        return stad;
    }

    public void setStad(MetricDataValue stad) {
        this.stad = stad;
    }

    public MetricDataValue getQnetar() {
        return qnetar;
    }

    public void setQnetar(MetricDataValue qnetar) {
        this.qnetar = qnetar;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public MetricDataValue getAvgDensity() {
        return avgDensity;
    }

    public void setAvgDensity(MetricDataValue avgDensity) {
        this.avgDensity = avgDensity;
    }

    public MetricDataValue getAvgFlow() {
        return avgFlow;
    }

    public void setAvgFlow(MetricDataValue avgFlow) {
        this.avgFlow = avgFlow;
    }

    public Integer getSystem() {
        return system;
    }

    public void setSystem(Integer system) {
        this.system = system;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getShowBit() {
        return showBit;
    }

    public void setShowBit(Integer showBit) {
        this.showBit = showBit;
    }

    public CoalAnalysisData() {
    }
    public CoalAnalysisData(CoalAnalysisRecord record) {
        this.thingCode = record.getSample();
        this.target = record.getTarget();
        this.system = record.getSystem();
        this.time = record.getTime();
    }
}
