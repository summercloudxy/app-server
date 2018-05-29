package com.zgiot.app.server.module.reportforms.output.pojo;


import java.util.Date;

public class ReportFormProductStore {

    private Integer id;
    private Date productStartTime;
    private Integer coalType;
    private Double value = 0.0;

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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
