package com.zgiot.app.server.module.tcs.pojo;

import com.zgiot.common.pojo.CoalAnalysisRecord;

public class TcsAnalysisInfo {
    private CoalAnalysisRecord concentrateRecord;
    private CoalAnalysisRecord tailRecord;
    private CoalAnalysisRecord rawCoal01;
    private CoalAnalysisRecord rawCoal02;

    public CoalAnalysisRecord getConcentrateRecord() {
        return concentrateRecord;
    }

    public void setConcentrateRecord(CoalAnalysisRecord concentrateRecord) {
        this.concentrateRecord = concentrateRecord;
    }

    public CoalAnalysisRecord getTailRecord() {
        return tailRecord;
    }

    public void setTailRecord(CoalAnalysisRecord tailRecord) {
        this.tailRecord = tailRecord;
    }

    public CoalAnalysisRecord getRawCoal01() {
        return rawCoal01;
    }

    public void setRawCoal01(CoalAnalysisRecord rawCoal01) {
        this.rawCoal01 = rawCoal01;
    }

    public CoalAnalysisRecord getRawCoal02() {
        return rawCoal02;
    }

    public void setRawCoal02(CoalAnalysisRecord rawCoal02) {
        this.rawCoal02 = rawCoal02;
    }
}
