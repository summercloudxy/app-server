package com.zgiot.app.server.module.sfmonitor.pojo;

public class SFMonStyle {
    private int id;
    private String code;
    private String name;
    private String comment;
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

    public String getComment() {
        return comment;
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

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setPreviewImageName(String previewImageName) {
        this.previewImageName = previewImageName;
    }
}
