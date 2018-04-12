package com.zgiot.app.server.module.equipments.service.impl;

import com.zgiot.app.server.module.equipments.mapper.RelThingSystemMapper;
import com.zgiot.app.server.module.equipments.pojo.RelThingSystem;
import com.zgiot.app.server.module.equipments.service.RelThingSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelThingSystemServiceImpl implements RelThingSystemService {

    @Autowired
    private RelThingSystemMapper relThingSystemMapper;

    @Override
    public void addRelThingSystem(RelThingSystem relThingSystem) {
        String thingCode = relThingSystem.getThingCode();
        Long systemId = relThingSystem.getSystemId();
        relThingSystemMapper.updateRelThingSystemByThingCode(systemId, thingCode);
    }

    @Override
    public void deleteRelThingSystemByThingCode(String thing_code) {
        relThingSystemMapper.updateRelThingSystemByThingCode(-1L, thing_code);
    }
}
