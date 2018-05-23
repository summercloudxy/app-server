package com.zgiot.app.server.module.reportforms.output.pojo;

import com.zgiot.app.server.module.reportforms.output.productionmonitor.pojo.ReportFormSystemStartRecord;
import com.zgiot.common.pojo.CoalAnalysisRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

@ApiModel("整个报表对象")
public class ReportFormsBean {

    @ApiModelProperty("调度队组")
    private Integer schedulingGroup;

    @ApiModelProperty("开车情况,key是期数，1表示第一期，2表示第二期")
    private Map<Integer, List<ReportFormSystemStartRecord>> startRecordMap;

    @ApiModelProperty("库存,生产，key表示是库存还是生产，1表示生产，2表示库存")
    private Map<Integer, ReportFormOutputStoreRecord> dutyOutputStoreRecords;

    @ApiModelProperty("指标统计，key表示是什么类型 内层map中key表示期数，value表示指标对象")
    private Map<Integer, Map<Integer, ReportFormTargetRecord>> targetInfo;

    @ApiModelProperty("551;552,气精煤;洗混煤数据  key:oneTermClened;oneTermWashed;twoTermClened;twoTermWashed value:对应的数据集合")
    private Map<String,List<CoalAnalysisRecord>> coalAnalysis;
    @ApiModelProperty("551;552,气精煤;洗混煤数据 key:oneTermClened;oneTermWashed;twoTermClened;twoTermWashed value:对应的平均值")
    private Map<String,CoalAnalysisRecord> coalAnalysisAvg;

    @ApiModelProperty("运销数据,运销表数据List集合")
    private List<Transport> transportList;
    @ApiModelProperty("外运统计数据 key:1洗混煤;2气精煤;3煤泥")
    private Map<Integer,TransportSaleStatistics> saleStatisticsOutwardMap;
    @ApiModelProperty("地销统计数据 key:1洗混煤;2气精煤;3煤泥")
    private Map<Integer,TransportSaleStatistics> saleStatisticsLocalityMap;

    @ApiModelProperty("影响时间统计,这里是影响时间统计值集合对象，InfluenceTime里面有1期和2期数据，使用One和Two进行区分")
    private List<InfluenceTimeRsp> influenceTimeRsps;
    @ApiModelProperty("影响备注以及审核人员信息")
    private InfluenceTimeRemarks influenceTimeRemarks;

    public Map<Integer, List<ReportFormSystemStartRecord>> getStartRecordMap() {
        return startRecordMap;
    }

    public void setStartRecordMap(Map<Integer, List<ReportFormSystemStartRecord>> startRecordMap) {
        this.startRecordMap = startRecordMap;
    }

    public Map<Integer, ReportFormOutputStoreRecord> getDutyOutputStoreRecords() {
        return dutyOutputStoreRecords;
    }

    public void setDutyOutputStoreRecords(Map<Integer, ReportFormOutputStoreRecord> dutyOutputStoreRecords) {
        this.dutyOutputStoreRecords = dutyOutputStoreRecords;
    }

    public Map<Integer, Map<Integer, ReportFormTargetRecord>> getTargetInfo() {
        return targetInfo;
    }

    public void setTargetInfo(Map<Integer, Map<Integer, ReportFormTargetRecord>> targetInfo) {
        this.targetInfo = targetInfo;
    }

    public Map<String, List<CoalAnalysisRecord>> getCoalAnalysis() {
        return coalAnalysis;
    }

    public void setCoalAnalysis(Map<String, List<CoalAnalysisRecord>> coalAnalysis) {
        this.coalAnalysis = coalAnalysis;
    }

    public Map<String, CoalAnalysisRecord> getCoalAnalysisAvg() {
        return coalAnalysisAvg;
    }

    public void setCoalAnalysisAvg(Map<String, CoalAnalysisRecord> coalAnalysisAvg) {
        this.coalAnalysisAvg = coalAnalysisAvg;
    }

    public List<Transport> getTransportList() {
        return transportList;
    }

    public void setTransportList(List<Transport> transportList) {
        this.transportList = transportList;
    }

    public Map<Integer, TransportSaleStatistics> getSaleStatisticsOutwardMap() {
        return saleStatisticsOutwardMap;
    }

    public void setSaleStatisticsOutwardMap(Map<Integer, TransportSaleStatistics> saleStatisticsOutwardMap) {
        this.saleStatisticsOutwardMap = saleStatisticsOutwardMap;
    }

    public Map<Integer, TransportSaleStatistics> getSaleStatisticsLocalityMap() {
        return saleStatisticsLocalityMap;
    }

    public void setSaleStatisticsLocalityMap(Map<Integer, TransportSaleStatistics> saleStatisticsLocalityMap) {
        this.saleStatisticsLocalityMap = saleStatisticsLocalityMap;
    }

    public List<InfluenceTimeRsp> getInfluenceTimeRsps() {
        return influenceTimeRsps;
    }

    public void setInfluenceTimeRsps(List<InfluenceTimeRsp> influenceTimeRsps) {
        this.influenceTimeRsps = influenceTimeRsps;
    }

    public InfluenceTimeRemarks getInfluenceTimeRemarks() {
        return influenceTimeRemarks;
    }

    public void setInfluenceTimeRemarks(InfluenceTimeRemarks influenceTimeRemarks) {
        this.influenceTimeRemarks = influenceTimeRemarks;
    }

    public Integer getSchedulingGroup() {
        return schedulingGroup;
    }

    public void setSchedulingGroup(Integer schedulingGroup) {
        this.schedulingGroup = schedulingGroup;
    }
}
