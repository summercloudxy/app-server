package com.zgiot.app.server.module.auth.service;

import com.zgiot.app.server.module.auth.controller.AuthorityGroup.AuthorityAndAuthorityGroup;
import com.zgiot.app.server.module.auth.controller.AuthorityGroup.PlatformClientAndModule;
import com.zgiot.app.server.module.auth.mapper.AuthorityGroupMapper;
import com.zgiot.app.server.module.auth.mapper.AuthorityMapper;
import com.zgiot.app.server.module.auth.mapper.RelAuthorityGroupAuthorityMapper;
import com.zgiot.app.server.module.auth.pojo.Authority;
import com.zgiot.app.server.module.auth.pojo.AuthorityGroup;
import com.zgiot.app.server.module.auth.pojo.RelAuthorityGroupAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AuthorityGroupService {
    @Autowired
    private AuthorityGroupMapper authorityGroupMapper;

    @Autowired
    private RelAuthorityGroupAuthorityMapper relAuthorityGroupAuthorityMapper;

    @Autowired
    private AuthorityMapper authorityMapper;

    public List<AuthorityGroup> getAuthorityByModuleAndClientId(int platformClientId, int moduleId) {
        return authorityGroupMapper.getAuthorityByModuleAndClientId(platformClientId, moduleId);
    }

    public AuthorityGroup getAuthgroupByName(String name) {
        return authorityGroupMapper.getAuthGroupByName(name);
    }

    public boolean addOrEditAuthGroup(AuthorityAndAuthorityGroup authorityAndAuthorityGroup) {
        boolean isExist = false;
        AuthorityGroup authorityGroup = authorityAndAuthorityGroup.getAuthorityGroup();
        List<Authority> authorities = authorityAndAuthorityGroup.getAuthorities();
        AuthorityGroup existAuthorityGroup = authorityGroupMapper.getAuthGroupByName(authorityGroup.getName());
        int authGroupId = authorityGroup.getId();
        if (authGroupId != 0) {//edit
            isExist = editAuthGroup(authorityGroup, existAuthorityGroup, authorities);
        } else {
            isExist = addAuthGroup(authorityGroup, existAuthorityGroup);
        }
        authGroupId = authorityGroup.getId();
        List<RelAuthorityGroupAuthority> relAuthorityGroupAuthorities = new ArrayList<>();
        if (authorities.size() > 0) {
            for (Authority authority : authorities) {
                RelAuthorityGroupAuthority relAuthorityGroupAuthority = new RelAuthorityGroupAuthority();
                relAuthorityGroupAuthority.setAuthorityGroupId(authGroupId);
                relAuthorityGroupAuthority.setAuthorityId(Integer.valueOf(authority.getId()));
                Authority auth = authorityMapper.getAuthority(relAuthorityGroupAuthority.getAuthorityId());
                auth.setEnable(authority.getEnable());
                authorityMapper.update(auth);
                relAuthorityGroupAuthorities.add(relAuthorityGroupAuthority);
            }
            relAuthorityGroupAuthorityMapper.insertRelAuthGroupAuth(relAuthorityGroupAuthorities);
        }

        return isExist;
    }

    private boolean editAuthGroup(AuthorityGroup authorityGroup, AuthorityGroup existAuthorityGroup, List<Authority> authorities) {
        boolean isExist = false;
        int authGroupId = authorityGroup.getId();
        if (authGroupId != 0) {//edit
            if (existAuthorityGroup != null && existAuthorityGroup.getId() != authGroupId) {
                isExist = true;
            } else if (authorities.size() == 0) {
                relAuthorityGroupAuthorityMapper.deleteRelAuthGroupAuthByAuthGroupId(authorityGroup.getId());
            } else {
                relAuthorityGroupAuthorityMapper.deleteRelAuthGroupAuthByAuthGroupId(authGroupId);
                authorityGroup.setUpdateDate(new Date());
                authorityGroupMapper.upgradeAuthGroup(authorityGroup);
            }
        }
        return isExist;
    }

    private boolean addAuthGroup(AuthorityGroup authorityGroup, AuthorityGroup existAuthorityGroup) {
        boolean isExist = false;
        if (existAuthorityGroup != null) {
            isExist = true;
        } else {
            authorityGroup.setUpdateDate(new Date());
            authorityGroupMapper.addAuthGroup(authorityGroup);
            authorityGroup.setCode(String.valueOf(authorityGroup.getId()));
            authorityGroupMapper.upgradeAuthGroup(authorityGroup);
        }
        return isExist;
    }

    public void deleteAuthGroupById(int authGroupId) {
        authorityGroupMapper.deleteAuthGroupById(authGroupId);
    }

    public PlatformClientAndModule getClientAndModuleByAuthGroupId(int authGroupId) {
        return authorityGroupMapper.getClientAndModuleByAuthGroupId(authGroupId);
    }

    public int getAuthGroupCountByModuleAndClientId(int platformClientId, int moduleId) {
        return authorityGroupMapper.getAuthGroupCountByModuleAndClientId(platformClientId, moduleId);
    }
}
