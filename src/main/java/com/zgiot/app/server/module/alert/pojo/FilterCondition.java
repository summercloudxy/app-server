package com.zgiot.app.server.module.alert.pojo;

import java.util.Date;
import java.util.List;

public class FilterCondition {
    private String assetType;
    private String category;
    private String system;
    private String metricType;
    private String thingCode;
    private Boolean enable;
    private Short level;
    private String metricCode;
    private Integer buildingId;
    private String alertType;
    private Integer page;
    private Integer offset;
    private Integer count;
    private Integer sortType;
    private String stage;
    private String excluStage;
    private Short type;
    private Long duration;
    private Date startTime;
    private Date endTime;
    private List<Short> levels;
    private List<Short> types;
    private List<Integer> buildingIds;
    private List<Integer> floors;
    private List<Integer> systems;
    private List<String> stages;
    private List<String> thingCodes;

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public Short getLevel() {
        return level;
    }

    public void setLevel(Short level) {
        this.level = level;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }


    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getExcluStage() {
        return excluStage;
    }

    public void setExcluStage(String excluStage) {
        this.excluStage = excluStage;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<Short> getLevels() {
        return levels;
    }

    public void setLevels(List<Short> levels) {
        this.levels = levels;
    }

    public List<Short> getTypes() {
        return types;
    }

    public void setTypes(List<Short> types) {
        this.types = types;
    }

    public List<Integer> getBuildingIds() {
        return buildingIds;
    }

    public void setBuildingIds(List<Integer> buildingIds) {
        this.buildingIds = buildingIds;
    }

    public List<Integer> getFloors() {
        return floors;
    }

    public void setFloors(List<Integer> floors) {
        this.floors = floors;
    }

    public List<Integer> getSystems() {
        return systems;
    }

    public void setSystems(List<Integer> systems) {
        this.systems = systems;
    }

    public List<String> getStages() {
        return stages;
    }

    public void setStages(List<String> stages) {
        this.stages = stages;
    }

    public List<String> getThingCodes() {
        return thingCodes;
    }

    public void setThingCodes(List<String> thingCodes) {
        this.thingCodes = thingCodes;
    }
}
