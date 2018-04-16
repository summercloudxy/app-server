package com.zgiot.app.server.module.auth.controller.AuthorityGroup;

import com.zgiot.app.server.module.auth.pojo.AuthorityGroup;

import java.util.List;

public class AuthorityGroupReturn {
    private List<AuthorityGroup> authorityGroups;
    private int sum;

    public List<AuthorityGroup> getAuthorityGroups() {
        return authorityGroups;
    }

    public int getSum() {
        return sum;
    }

    public void setAuthorityGroups(List<AuthorityGroup> authorityGroups) {
        this.authorityGroups = authorityGroups;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
