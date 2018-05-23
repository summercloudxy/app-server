package com.zgiot.app.server.module.reportforms.output.pojo;

import com.zgiot.common.pojo.CoalAnalysisRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiModel("551、552，气精煤、洗混煤")
public class CoalAnalysisBean {

    @ApiModelProperty("551;552,气精煤;洗混煤数据  key:oneTermClened;oneTermWashed;twoTermClened;twoTermWashed value:对应的数据集合")
    private Map<String,List<CoalAnalysisRecord>> coalAnalysis=new HashMap<>();

    @ApiModelProperty("551;552,气精煤;洗混煤数据 key:oneTermClened;oneTermWashed;twoTermClened;twoTermWashed value:对应的平均值")
    private Map<String,CoalAnalysisRecord> coalAnalysisAvg=new HashMap<>();

    @ApiModelProperty("当班开始时间")
    private Date dutyStartTime;

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

    public Date getDutyStartTime() {
        return dutyStartTime;
    }

    public void setDutyStartTime(Date dutyStartTime) {
        this.dutyStartTime = dutyStartTime;
    }
}
