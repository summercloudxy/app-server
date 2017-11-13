package com.zgiot.app.server.module.bellows.compressor.pojo;

import java.util.Date;

/**
 * @author wangwei
 */
public class CompressorLog {

    /**
     * id
     */
    private Long id;

    /**
     * 设备号
     */
    private String thingCode;

    /**
     * 操作
     */
    private String operation;

    /**
     * 操作类型（手动、智能）
     */
    private String operateType;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 操作前状态
     */
    private String preState;

    /**
     * 操作后确认状态
     */
    private String postState;

    /**
     * 确认时间
     */
    private Date confirmTime;

    /**
     * 确认时压力
     */
    private Double pressure;

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 备注
     */
    private String memo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getPreState() {
        return preState;
    }

    public void setPreState(String preState) {
        this.preState = preState;
    }

    public String getPostState() {
        return postState;
    }

    public void setPostState(String postState) {
        this.postState = postState;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
