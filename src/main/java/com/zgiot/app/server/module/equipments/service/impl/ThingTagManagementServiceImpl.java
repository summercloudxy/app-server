package com.zgiot.app.server.module.equipments.service.impl;

import com.zgiot.app.server.module.equipments.mapper.ThingTagManagementMapper;
import com.zgiot.app.server.module.equipments.pojo.ThingTag;
import com.zgiot.app.server.module.equipments.service.ThingTagManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThingTagManagementServiceImpl implements ThingTagManagementService {
    @Autowired
    private ThingTagManagementMapper thingTagMapper;

    @Override
    public List<ThingTag> getAllEquipmentType() {
        return thingTagMapper.getAllEquipmentType();
    }

    @Override
    public List<ThingTag> getThingTagByParentId(Long id) {
        return thingTagMapper.getThingTagByParentId(id);
    }
}
