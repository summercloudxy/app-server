package com.zgiot.app.server.module.auth.service;

import com.zgiot.app.server.module.auth.mapper.PlatformClientMapper;
import com.zgiot.app.server.module.auth.pojo.PlatformClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformClientService {
    @Autowired
    private PlatformClientMapper platformClientMapper;

    public List<PlatformClient> getAllPlatformClient() {
        return platformClientMapper.findAll();
    }
}
