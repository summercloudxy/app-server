package com.zgiot.app.server.module.equipments.service.impl;

import com.zgiot.app.server.module.equipments.mapper.RelThingtagThingMapper;
import com.zgiot.app.server.module.equipments.pojo.RelThingtagThing;
import com.zgiot.app.server.module.equipments.service.RelThingtagThingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelThingtagThingServiceImpl implements RelThingtagThingService {
    @Autowired
    private RelThingtagThingMapper relThingtagThingMapper;

    @Override
    public List<RelThingtagThing> getRelThingtagThingByThreeLevelId(Long id) {
        return relThingtagThingMapper.getRelThingtagThingByThreeLevelId(id);
    }
}
