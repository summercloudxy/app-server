package com.zgiot.app.server.module.auth.service;

import com.zgiot.app.server.module.auth.mapper.ModuleMapper;
import com.zgiot.app.server.module.auth.pojo.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {
    @Autowired
    private ModuleMapper moduleMapper;

    public List<Module> getModulesByClientId(int platformClientId) {
        return moduleMapper.getModulesByClientId(platformClientId);
    }
}
