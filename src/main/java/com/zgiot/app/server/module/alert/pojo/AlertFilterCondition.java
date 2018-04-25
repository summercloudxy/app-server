package com.zgiot.app.server.module.alert.pojo;

import io.swagger.annotations.ApiModel;

import java.util.Date;
import java.util.List;
@ApiModel("筛选报警的过滤条件")
public class AlertFilterCondition {
    private List<Short> levels;
    private List<Short> types;
    private List<String> stages;
    private String excluStage;
    private List<String> thingCodes;
    private Long duration;
    private Date startTime;
    private Date endTime;
    private Integer page;
    private Integer offset;
    private Integer count;
    private Integer sortType;

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

    public List<String> getStages() {
        return stages;
    }

    public void setStages(List<String> stages) {
        this.stages = stages;
    }

    public String getExcluStage() {
        return excluStage;
    }

    public void setExcluStage(String excluStage) {
        this.excluStage = excluStage;
    }

    public List<String> getThingCodes() {
        return thingCodes;
    }

    public void setThingCodes(List<String> thingCodes) {
        this.thingCodes = thingCodes;
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
}
