package com.zgiot.app.server.module.sfmedium.service;


import com.zgiot.app.server.module.sfmedium.entity.po.MediumDosingConfigDO;
import com.zgiot.app.server.module.sfmedium.mapper.MediumDosingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MediumDosingServiceImpl implements MediumDosingService {
    @Autowired
    private MediumDosingMapper mediumDosingMapper;

    @Override
    public List<MediumDosingConfigDO> getMediumDosingGroup() {
        return mediumDosingMapper.getMediumDosingGroup();
    }

    @Override
    public List<MediumDosingConfigDO> getSeparatingSystemById(String systemid) {
        return mediumDosingMapper.getSeparatingSystemById(systemid);
    }

    @Override
    public List<MediumDosingConfigDO> getSeparatingSystem(String systemid, String system) {
        return mediumDosingMapper.getSeparatingSystem(systemid, system);
    }

    @Override
    public List<MediumDosingConfigDO> getAllMediumDosing() {
        return mediumDosingMapper.getAllMediumDosing();
    }

    @Override
    public List<MediumDosingConfigDO> getMediumDosingConfigByMediumPoolCode(String mediumpoolCode) {
        return mediumDosingMapper.getMediumDosingConfigByMediumPoolCode(mediumpoolCode);
    }

    @Override
    public List<MediumDosingConfigDO> getChangeValue(String mediumdosingpumpCode) {
        return mediumDosingMapper.getChangeValue(mediumdosingpumpCode);
    }

    @Override
    public List<MediumDosingConfigDO> getMediumdosingvalueAndCombinedbucketCode(String mediumdosingpumpCode, String changevalueCode) {
        return mediumDosingMapper.getMediumdosingvalueAndCombinedbucketCode(mediumdosingpumpCode, changevalueCode);
    }

    @Override
    public MediumDosingConfigDO getMediumDosingConfigDO(String combinedbucketCode) {
        return mediumDosingMapper.getMediumDosingConfigDO(combinedbucketCode);
    }
}
