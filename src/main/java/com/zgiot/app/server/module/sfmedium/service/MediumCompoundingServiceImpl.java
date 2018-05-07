package com.zgiot.app.server.module.sfmedium.service;

import com.zgiot.app.server.module.sfmedium.entity.po.MediumCompoundingConfigDO;
import com.zgiot.app.server.module.sfmedium.mapper.MediumCompoundingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class MediumCompoundingServiceImpl implements MediumCompoundingService {
    @Autowired
    private MediumCompoundingMapper mediumCompoundingMapper;

    @Override
    public MediumCompoundingConfigDO getMediumCompoundingConfigByPoolCode(String mediumPoolCode) {
        return mediumCompoundingMapper.getMediumCompoundingConfigByPoolCode(mediumPoolCode);
    }
}
