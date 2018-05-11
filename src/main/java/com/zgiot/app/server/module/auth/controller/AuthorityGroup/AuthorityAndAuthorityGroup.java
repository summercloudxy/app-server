package com.zgiot.app.server.module.auth.controller.AuthorityGroup;

import com.zgiot.app.server.module.auth.pojo.Authority;
import com.zgiot.app.server.module.auth.pojo.AuthorityGroup;

import java.util.List;

public class AuthorityAndAuthorityGroup {
    private AuthorityGroup authorityGroup;
    private List<Authority> authorities;

    public AuthorityGroup getAuthorityGroup() {
        return authorityGroup;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorityGroup(AuthorityGroup authorityGroup) {
        this.authorityGroup = authorityGroup;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }
}
