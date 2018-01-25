package com.zgiot.app.server.module.tcs.pojo;

import java.util.*;

public class FilterCondition {
    private Integer system;
    private Integer page;
    private Integer offset;
    private Integer count;
    private Integer sortType;
    private Long duration;
    private Date startTime;
    private Date endTime;
    private List<String> targetList;
    private String sample;


    public List<String> getTargetList() {
        return targetList;
    }

    public void setTargetList(List<String> targetList) {
        this.targetList = targetList;
    }

    public Integer getSystem() {
        return system;
    }

    public void setSystem(Integer system) {
        this.system = system;
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

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public static class FilterConditionBuilder{
        private Integer page;
        private Integer offset;
        private Integer count;
        private Integer sortType;
        private Long duration;
        private Date startTime;
        private Date endTime;
        private Integer system;
        private Set<String> targetLists = new HashSet<>(10);
        public static FilterConditionBuilder newFilterCondition(){
            return new FilterConditionBuilder();
        }
        public FilterCondition build(){
            FilterCondition filterCondition = new FilterCondition();
            filterCondition.setPage(page);
            filterCondition.setOffset(offset);
            filterCondition.setCount(count);
            filterCondition.setSortType(sortType);
            filterCondition.setDuration(duration);
            filterCondition.setStartTime(startTime);
            filterCondition.setEndTime(endTime);
            filterCondition.setSystem(system);
            if(!targetLists.isEmpty()){
                filterCondition.setTargetList(new ArrayList<>(targetLists));
            }
            return filterCondition;
        }
        public FilterConditionBuilder page(int page){
            this.page = page;
            if(count!=null){
                offset = count * page;
            }
            return this;
        }
        public FilterConditionBuilder count(int count){
            this.count = count;
            if(page != null){
                offset = count * page;
            }
            return this;
        }
        public FilterConditionBuilder sortType(int sortType){
            this.sortType = sortType;
            return this;
        }
        public FilterConditionBuilder startTime(Date startTime){
            this.startTime = startTime;
            if(duration != null){
                endTime = new Date(startTime.getTime() + duration);
            }
            return this;
        }
        public FilterConditionBuilder endTime(Date endTime){
            this.endTime = endTime;
            if(duration != null){
                startTime = new Date(endTime.getTime() - duration);
            }
            return this;
        }
        public FilterConditionBuilder duration(Long duration){
            this.duration = duration;

            if(startTime == null && endTime != null){
                startTime = new Date(endTime.getTime() - duration);
            }else if(startTime != null && endTime == null){
                endTime = new Date(startTime.getTime() + duration);
            }
            return this;
        }

        public FilterConditionBuilder system(int system){
            this.system = system;
            return this;
        }

        public FilterConditionBuilder addTarget(String target){
            targetLists.add(target);
            return this;
        }
    }

}
