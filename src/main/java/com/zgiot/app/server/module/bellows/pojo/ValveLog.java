package com.zgiot.app.server.module.bellows.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zgiot.app.server.module.bellows.util.DoubleSerializer;

import java.util.Date;

/**
 * @author wangwei
 */
public class ValveLog {

    /**
     * id
     */
    private Long id;

    /**
     * 设备号
     */
    private String thingCode;

    /**
     * 设备名称
     */
    private String name;

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
    @JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue)
    private String postState;

    /**
     * 确认时间
     */
    @JSONField(serialize = false)
    private Date confirmTime;

    /**
     * 确认时高压
     */
    @JSONField(serializeUsing = DoubleSerializer.class)
    private Double highPressure;

    /**
     * 确认时低压
     */
    @JSONField(serializeUsing = DoubleSerializer.class)
    private Double lowPressure;

    /**
     * 请求id
     */
    @JSONField(serialize = false)
    private String requestId;

    /**
     * 分组号
     */
    private Long teamId;

    /**
     * 备注
     */
    @JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue)
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Double getHighPressure() {
        return highPressure;
    }

    public void setHighPressure(Double highPressure) {
        this.highPressure = highPressure;
    }

    public Double getLowPressure() {
        return lowPressure;
    }

    public void setLowPressure(Double lowPressure) {
        this.lowPressure = lowPressure;
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

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
}
