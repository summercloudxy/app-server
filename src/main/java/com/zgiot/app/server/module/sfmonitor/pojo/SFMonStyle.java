package com.zgiot.app.server.module.sfmonitor.pojo;

public class SFMonStyle {
    private int id;
    private String code;
    private String name;
    private String comments;
    private String previewImageName;

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getComments() {
        return comments;
    }

    public String getPreviewImageName() {
        return previewImageName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setPreviewImageName(String previewImageName) {
        this.previewImageName = previewImageName;
    }
}
