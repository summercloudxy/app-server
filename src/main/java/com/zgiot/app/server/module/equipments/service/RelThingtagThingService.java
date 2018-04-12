package com.zgiot.app.server.module.equipments.service;

import com.zgiot.app.server.module.equipments.pojo.RelThingtagThing;

import java.util.List;

public interface RelThingtagThingService {

    /**
     * 根据thingtag三级id查询thingtag设备关系信息
     *
     * @param id
     * @return
     */
    public List<RelThingtagThing> getRelThingtagThingByThreeLevelId(Long id);

}
