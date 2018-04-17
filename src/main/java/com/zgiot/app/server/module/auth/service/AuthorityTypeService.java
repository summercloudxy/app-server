package com.zgiot.app.server.module.auth.service;

import com.zgiot.app.server.module.auth.mapper.AuthorityTypeMapper;
import com.zgiot.app.server.module.auth.pojo.AuthorityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityTypeService {
    @Autowired
    private AuthorityTypeMapper authorityTypeMapper;

    public List<AuthorityType> getAuthorityTypeByClientAndModuleId(int platformClientId, int moduleId) {
        return authorityTypeMapper.getAuthorityTypeByClientAndModuleId(platformClientId, moduleId);
    }
}
