package com.zgiot.app.server.module.sfstart.pojo;


public class StartMessage {

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
