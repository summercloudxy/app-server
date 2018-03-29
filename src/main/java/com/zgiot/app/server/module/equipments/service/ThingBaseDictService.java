package com.zgiot.app.server.module.equipments.service;

import com.zgiot.app.server.module.equipments.pojo.ThingBaseDict;

import java.util.List;

public interface ThingBaseDictService {

    List<ThingBaseDict> getThingBaseDictListByKey(String key);

}
