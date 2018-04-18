package com.zgiot.app.server.module.auth.service;

import com.zgiot.app.server.module.auth.constants.AuthConstants;
import com.zgiot.app.server.module.auth.mapper.AuthorityMapper;
import com.zgiot.app.server.module.auth.pojo.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorityService {

    @Autowired
    private AuthorityMapper authorityMapper;

    public List<Authority> getAuthorityByTypeId(int authorityTypeId) {
        return authorityMapper.getAuthorityByAuthorityTypeId(authorityTypeId);
    }

    public List<Authority> getAuthorityByAuthGroupId(int authGroupId) {
        return authorityMapper.getAuthorityByAuthGroupId(authGroupId);
    }

    public List<Authority> findAuthoritiesByUserUuid(String userUuid) {
        List<Authority> list = authorityMapper.findAuthorityViaRoleOrAuthGroupByUserUuid(userUuid);
        List<String> defaultAuthorityCode = new ArrayList<>();
        defaultAuthorityCode.add(AuthConstants.DEFAULT_MODULE_CODE);
        List<Authority> defaultModules = authorityMapper.getAuthorityByAuthorityCode(defaultAuthorityCode);
        boolean isExist = false;
        if (defaultModules != null && defaultModules.size() > 0) {
            for (Authority authority : defaultModules) {
                for (Authority auth : list) {
                    if (auth.getCode().equals(authority.getCode())) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    list.add(authority);
                }
            }
        }
        return list;
    }

}
