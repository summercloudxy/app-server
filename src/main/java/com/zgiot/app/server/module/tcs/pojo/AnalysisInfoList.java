package com.zgiot.app.server.module.tcs.pojo;

import java.util.Date;
import java.util.List;

public class AnalysisInfoList {
    private Date date;
    private List<TcsAnalysisInfo> tcsAnalysisInfos;


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<TcsAnalysisInfo> getTcsAnalysisInfos() {
        return tcsAnalysisInfos;
    }

    public void setTcsAnalysisInfos(List<TcsAnalysisInfo> tcsAnalysisInfos) {
        this.tcsAnalysisInfos = tcsAnalysisInfos;
    }
}
