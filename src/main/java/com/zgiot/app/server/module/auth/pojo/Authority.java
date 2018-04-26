package com.zgiot.app.server.module.auth.pojo;

public class Authority {
    private int id;
    private String name;
    private String alias;
    private String remark;
    private int parentId;
    private String code;
    private int authorityTypeId;
    private boolean enable;
    private int platclientId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getRemark() {
        return remark;
    }

    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getAuthorityTypeId() {
        return authorityTypeId;
    }

    public void setAuthorityTypeId(int authorityTypeId) {
        this.authorityTypeId = authorityTypeId;
    }

    public int getPlatclientId() {
        return platclientId;
    }

    public void setPlatformId(int platclientId) {
        this.platclientId = platclientId;
    }
}
                                                  