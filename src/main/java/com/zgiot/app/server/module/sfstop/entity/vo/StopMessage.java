package com.zgiot.app.server.module.sfstop.entity.vo;


public class StopMessage {

    // 消息topic
    private String label;
    // 发送内容
    private Object object;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
