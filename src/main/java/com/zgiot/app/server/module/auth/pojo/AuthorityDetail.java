package com.zgiot.app.server.module.auth.pojo;

public class AuthorityDetail {
    private String authorityId;
    private String resourceType;
    private String resourceDesc;
    private String resourcePosition;
    private String ciconcls;
    private String resourceLevel;

    public String getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceDesc() {
        return resourceDesc;
    }

    public void setResourceDesc(String resourceDesc) {
        this.resourceDesc = resourceDesc;
    }

    public String getResourcePosition() {
        return resourcePosition;
    }

    public void setResourcePosition(String resourcePosition) {
        this.resourcePosition = resourcePosition;
    }

    public String getCiconcls() {
        return ciconcls;
    }

    public void setCiconcls(String ciconcls) {
        this.ciconcls = ciconcls;
    }

    public String getResourceLevel() {
        return resourceLevel;
    }

    public void setResourceLevel(String resourceLevel) {
        this.resourceLevel = resourceLevel;
    }
}
