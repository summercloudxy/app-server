package com.zgiot.app.server.module.sfstop.entity.vo;

import java.util.List;

/**
 * 停车预览
 */
public class StopingIndexVO {

    private Long stopElapsedTime;

    private Long stopPauseTime;

    private String operateState;

    private List<StopingArea> stopingAreas;

    public Long getStopElapsedTime() {
        return stopElapsedTime;
    }

    public void setStopElapsedTime(Long stopElapsedTime) {
        this.stopElapsedTime = stopElapsedTime;
    }

    public Long getStopPauseTime() {
        return stopPauseTime;
    }

    public void setStopPauseTime(Long stopPauseTime) {
        this.stopPauseTime = stopPauseTime;
    }

    public String getOperateState() {
        return operateState;
    }

    public void setOperateState(String operateState) {
        this.operateState = operateState;
    }

    public List<StopingArea> getStopingAreas() {
        return stopingAreas;
    }

    public void setStopingAreas(List<StopingArea> stopingAreas) {
        this.stopingAreas = stopingAreas;
    }

    public class StopingArea {

        private String areaName;

        private String areaId;

        private String lineCnt;

        private List<StopingLine> stopingLines;

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

        public List<StopingLine> getStopingLines() {
            return stopingLines;
        }

        public void setStopingLines(List<StopingLine> stopingLines) {
            this.stopingLines = stopingLines;
        }
    }

    public class StopingLine {


        private String lineName;

        private String lineId;

        private String lineRunState;

        private List<StopingThing> stopingThings;


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

        public List<StopingThing> getStopingThings() {
            return stopingThings;
        }

        public void setStopingThings(List<StopingThing> stopingThings) {
            this.stopingThings = stopingThings;
        }
    }

    public class StopingThing {

        private String thingCode;

        private String thingName;

        private String runState;

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

        public String getRunState() {
            return runState;
        }

        public void setRunState(String runState) {
            this.runState = runState;
        }
    }
}
