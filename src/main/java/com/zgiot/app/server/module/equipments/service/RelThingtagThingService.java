package com.zgiot.app.server.module.equipments.service;

import com.zgiot.app.server.module.equipments.pojo.RelThingtagThing;

import java.util.List;

public interface RelThingtagThingService {

    public List<RelThingtagThing> getRelThingtagThingByThreeLevelId(Long id);

}
