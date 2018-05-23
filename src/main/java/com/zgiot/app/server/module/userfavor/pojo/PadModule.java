package com.zgiot.app.server.module.userfavor.pojo;

public class PadModule {
    private long id;
    private String name;
    private String hostEnv;
    private String urn;
    private String authorityCode;
    private String imageName;
    private String openWay;
    /**
     * 是否默认常用
     */
    private boolean isDefaultFavor;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHostEnv() {
        return hostEnv;
    }

    public String getUrn() {
        return urn;
    }

    public String getAuthorityCode() {
        return authorityCode;
    }

    public String getImageName() {
        return imageName;
    }

    public String getOpenWay() {
        return openWay;
    }

    public boolean isDefaultFavor() {
        return isDefaultFavor;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHostEnv(String hostEnv) {
        this.hostEnv = hostEnv;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public void setAuthorityCode(String authorityCode) {
        this.authorityCode = authorityCode;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setOpenWay(String openWay) {
        this.openWay = openWay;
    }

    public void setDefaultFavor(boolean defaultFavor) {
        isDefaultFavor = defaultFavor;
    }
}
