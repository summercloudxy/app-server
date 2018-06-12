package com.zgiot.app.server.module.sfstop.entity.vo;

import java.util.List;

/**
 * 系统自检详情
 */
public class StopExamineIndexVO {


    private String examineLineCnt;


    private String unusualRecord;

    private String thingRunCount;

    private List<StopExamineArea> stopExamineAreas;


    public String getExamineLineCnt() {
        return examineLineCnt;
    }

    public void setExamineLineCnt(String examineLineCnt) {
        this.examineLineCnt = examineLineCnt;
    }

    public String getUnusualRecord() {
        return unusualRecord;
    }

    public void setUnusualRecord(String unusualRecord) {
        this.unusualRecord = unusualRecord;
    }

    public String getThingRunCount() {
        return thingRunCount;
    }

    public void setThingRunCount(String thingRunCount) {
        this.thingRunCount = thingRunCount;
    }

    public List<StopExamineArea> getStopExamineAreas() {
        return stopExamineAreas;
    }

    public void setStopExamineAreas(List<StopExamineArea> stopExamineAreas) {
        this.stopExamineAreas = stopExamineAreas;
    }

    public class StopExamineArea {

        private String areaName;

        private String areaId;

        private String lineCnt;

        private List<StopExamineLine> stopExamineLines;


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

        public List<StopExamineLine> getStopExamineLines() {
            return stopExamineLines;
        }

        public void setStopExamineLines(List<StopExamineLine> stopExamineLines) {
            this.stopExamineLines = stopExamineLines;
        }
    }

    public class StopExamineLine {

        private String lineName;

        private String lineId;

        private String lineRunState;

        private String isUnusual;

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

        public String getIsUnusual() {
            return isUnusual;
        }

        public void setIsUnusual(String isUnusual) {
            this.isUnusual = isUnusual;
        }
    }
}
