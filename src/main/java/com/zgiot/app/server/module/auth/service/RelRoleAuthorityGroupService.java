package com.zgiot.app.server.module.auth.service;

import com.zgiot.app.server.module.auth.mapper.RelRoleAuthorityGroupMapper;
import com.zgiot.app.server.module.auth.pojo.RelRoleAuthorityGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelRoleAuthorityGroupService {
    @Autowired
    private RelRoleAuthorityGroupMapper relRoleAuthorityGroupMapper;

    public int getRoleCountbyAuthGroupId(int authGroupId) {
        return relRoleAuthorityGroupMapper.getRoleCountByAuthGroupId(authGroupId);
    }

    public void addRelRoleAuthority(RelRoleAuthorityGroup relRoleAuthorityGroup) {
        relRoleAuthorityGroupMapper.insertRelRoleAuthorityGroup(relRoleAuthorityGroup);
    }

    public void deleteRelRoleAuthority(int roleId) {
        relRoleAuthorityGroupMapper.deleteRelRoleAuthorityGroup(roleId);
    }

    public List<RelRoleAuthorityGroup> getRelRoleAuthorityGroup(int roleId) {
        return relRoleAuthorityGroupMapper.getRelRoleAuthorityByRoleId(roleId);
    }
}
