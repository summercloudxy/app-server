package com.zgiot.app.server.module.auth.controller.authority;

import java.util.List;

public class AuthorityResponse {
    private String title;
    private int id;
    private Boolean checked;
    private Boolean expand;
    List<AuthorityResponse> children;

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public Boolean getChecked() {
        return checked;
    }

    public List<AuthorityResponse> getChildren() {
        return children;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public void setChildren(List<AuthorityResponse> children) {
        this.children = children;
    }

    public Boolean getExpand() {
        return expand;
    }

    public void setExpand(Boolean expand) {
        this.expand = expand;
    }
}
                                                  