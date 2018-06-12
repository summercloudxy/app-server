package com.zgiot.app.server.module.sfstop.entity.vo;

import java.util.List;

/**
 * 停车设备区域线概览
 */
public class StopIndexVO {

    private String thingRunCount;

    private Long thingRunTime;

    private List<StopThingArea> stopThingAreas;


    public class StopThingArea {

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
    }

    public class StopThingLine {

        private String lineName;

        private String lineId;

        private String lineRunState;

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

        public String getLineRunState() {
            return lineRunState;
        }

        public void setLineRunState(String lineRunState) {
            this.lineRunState = lineRunState;
        }
    }

    public String getThingRunCount() {
        return thingRunCount;
    }

    public void setThingRunCount(String thingRunCount) {
        this.thingRunCount = thingRunCount;
    }

    public Long getThingRunTime() {
        return thingRunTime;
    }

    public void setThingRunTime(Long thingRunTime) {
        this.thingRunTime = thingRunTime;
    }

    public List<StopThingArea> getStopThingAreas() {
        return stopThingAreas;
    }

    public void setStopThingAreas(List<StopThingArea> stopThingAreas) {
        this.stopThingAreas = stopThingAreas;
    }
}
