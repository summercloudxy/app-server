package com.zgiot.app.server.module.reportforms.output.pojo;


import com.zgiot.common.pojo.CoalAnalysisRecord;

import java.util.Date;

public class ReportFormProductQuality {

    private Integer id;
    private Date productStartTime;
    private Integer coalType;
    private Double aad;
    private Double mt;
    private Double stad;
    private Integer qnetar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getProductStartTime() {
        return productStartTime;
    }

    public void setProductStartTime(Date productStartTime) {
        this.productStartTime = productStartTime;
    }

    public Integer getCoalType() {
        return coalType;
    }

    public void setCoalType(Integer coalType) {
        this.coalType = coalType;
    }

    public Double getAad() {
        return aad;
    }

    public void setAad(Double aad) {
        this.aad = aad;
    }

    public Double getMt() {
        return mt;
    }

    public void setMt(Double mt) {
        this.mt = mt;
    }

    public Double getStad() {
        return stad;
    }

    public void setStad(Double stad) {
        this.stad = stad;
    }

    public Integer getQnetar() {
        return qnetar;
    }

    public void setQnetar(Integer qnetar) {
        this.qnetar = qnetar;
    }

    public ReportFormProductQuality() {
    }

    public ReportFormProductQuality(Date productStartTime, Integer coalType, CoalAnalysisRecord record) {
        this.productStartTime = productStartTime;
        this.coalType = coalType;
        this.aad = record.getAad();
        this.mt = record.getMt();
        this.stad = record.getStad();
        if (record.getQnetar() != null) {
            this.qnetar = record.getQnetar().intValue();
        }
    }
}
