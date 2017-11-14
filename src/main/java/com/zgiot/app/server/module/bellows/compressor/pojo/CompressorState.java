package com.zgiot.app.server.module.bellows.compressor.pojo;

import java.util.Date;

/**
 * @author wangwei
 */
public class CompressorState {

    /**
     * id
     */
    private Long id;

    /**
     * 设备号
     */
    private String thingCode;

    /**
     * 修改后状态
     */
    private String postState;

    /**
     * 修改前状态
     */
    private String preState;

    /**
     * 创建时间
     */
    private Date time;

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

    public String getPostState() {
        return postState;
    }

    public void setPostState(String postState) {
        this.postState = postState;
    }

    public String getPreState() {
        return preState;
    }

    public void setPreState(String preState) {
        this.preState = preState;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
