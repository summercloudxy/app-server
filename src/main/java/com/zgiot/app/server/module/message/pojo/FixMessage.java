package com.zgiot.app.server.module.message.pojo;

/**
 * Created by xiayun on 2017/9/28.
 */
public class FixMessage {
    private Long id;
    private String info;
    private String module;
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
