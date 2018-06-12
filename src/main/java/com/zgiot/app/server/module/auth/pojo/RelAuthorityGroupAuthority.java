package com.zgiot.app.server.module.auth.pojo;

public class RelAuthorityGroupAuthority {
    private Integer id;
    private int authorityGroupId;
    private int authorityId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAuthorityGroupId() {
        return authorityGroupId;
    }

    public int getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityGroupId(int authorityGroupId) {
        this.authorityGroupId = authorityGroupId;
    }

    public void setAuthorityId(int authorityId) {
        this.authorityId = authorityId;
    }
}
