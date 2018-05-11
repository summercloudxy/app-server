package com.zgiot.app.server.module.equipments.service.impl;

import com.zgiot.app.server.module.equipments.mapper.ThingBaseDictMapper;
import com.zgiot.app.server.module.equipments.pojo.ThingBaseDict;
import com.zgiot.app.server.module.equipments.service.ThingBaseDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThingBaseDictServiceImpl implements ThingBaseDictService {
    @Autowired
    private ThingBaseDictMapper thingBaseDictMapper;

    @Override
    public List<ThingBaseDict> getThingBaseDictListByKey(String key) {
        return thingBaseDictMapper.getThingBaseDictListByKey(key);
    }
}
