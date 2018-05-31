package com.zgiot.app.server.module.sfstop.entity.vo;

import java.util.List;

/**
 * 停车设备自检
 */

public class StopManualInterventionAreaVO {


    private String areaName;

    private String areaId;

    private String lineCnt;

    private List<StopThingLine> stopThingLines;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getLineCnt() {
        return lineCnt;
    }

    public void setLineCnt(String lineCnt) {
        this.lineCnt = lineCnt;
    }

    public List<StopThingLine> getStopThingLines() {
        return stopThingLines;
    }

    public void setStopThingLines(List<StopThingLine> stopThingLines) {
        this.stopThingLines = stopThingLines;
    }

    public class StopThingLine {

        private String lineName;

        private String lineId;

        private String manualCnt;

        public String getLineName() {
            return lineName;
        }

        public void setLineName(String lineName) {
            this.lineName = lineName;
        }

        public String getLineId() {
            return lineId;
        }

        public void setLineId(String lineId) {
            this.lineId = lineId;
        }

        public String getManualCnt() {
            return manualCnt;
        }

        public void setManualCnt(String manualCnt) {
            this.manualCnt = manualCnt;
        }
    }

}

