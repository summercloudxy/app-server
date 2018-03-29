package com.zgiot.app.server.module.equipments.service.impl;

import com.zgiot.app.server.module.equipments.mapper.ThingTagMapper;
import com.zgiot.app.server.module.equipments.pojo.ThingTag;
import com.zgiot.app.server.module.equipments.service.ThingTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThingTagServiceImpl implements ThingTagService {
    @Autowired
    private ThingTagMapper thingTagMapper;

    @Override
    public List<ThingTag> getAllEquipmentType() {
        return thingTagMapper.getAllEquipmentType();
    }
}
