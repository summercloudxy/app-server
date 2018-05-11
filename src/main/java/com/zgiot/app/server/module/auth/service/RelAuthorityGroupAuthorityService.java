package com.zgiot.app.server.module.auth.service;

import com.zgiot.app.server.module.auth.mapper.RelAuthorityGroupAuthorityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelAuthorityGroupAuthorityService {
    @Autowired
    private RelAuthorityGroupAuthorityMapper relAuthorityGroupAuthorityMapper;

    public void deleteRelAuthorityGroupAuthorityById(int authGroupId) {
        relAuthorityGroupAuthorityMapper.deleteRelAuthGroupAuthByAuthGroupId(authGroupId);
    }
}
