package com.zgiot.app.server.module.bellows.pojo;

import java.util.Date;
import java.util.List;

/**
 * @author wangwei
 */
public class ValveTeam {

    /**
     * 分组id
     */
    private Long id;

    /**
     * 下一分组id
     */
    private Long nextId;

    /**
     * 状态
     */
    private String status;

    /**
     * 执行时间
     */
    private Date execTime;

    /**
     * 鼓风时长（分）
     */
    private Integer duration;

    /**
     * 分组类型（块煤/末煤）
     */
    private String type;


    private List<String> valveThingCodes;


    public ValveTeam() {
    }


    public ValveTeam(Long id, Long nextId, String status, Date execTime, Integer duration, String type, List<String> valveThingCodes) {
        this.id = id;
        this.nextId = nextId;
        this.status = status;
        this.execTime = execTime;
        this.duration = duration;
        this.type = type;
        this.valveThingCodes = valveThingCodes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNextId() {
        return nextId;
    }

    public void setNextId(Long nextId) {
        this.nextId = nextId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getExecTime() {
        return execTime;
    }

    public void setExecTime(Date execTime) {
        this.execTime = execTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getValveThingCodes() {
        return valveThingCodes;
    }

    public void setValveThingCodes(List<String> valveThingCodes) {
        this.valveThingCodes = valveThingCodes;
    }
}
