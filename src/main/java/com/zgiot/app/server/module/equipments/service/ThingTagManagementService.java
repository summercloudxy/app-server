package com.zgiot.app.server.module.equipments.service;

import com.zgiot.app.server.module.equipments.pojo.ThingTag;

import java.util.List;

public interface ThingTagManagementService {

    public List<ThingTag> getAllEquipmentType();

    public List<ThingTag> getThingTagByParentId(Long id);
}
