package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.module.thingtag.dao.ThingTagGroupMapper;
import com.zgiot.app.server.module.thingtag.pojo.ThingTagGroup;
import com.zgiot.app.server.service.ThingTagGroupService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by chenting on 2018/1/12.
 */
public class ThingTagGroupServiceImpl implements ThingTagGroupService{

    @Autowired
    ThingTagGroupMapper thingTagGroupMapper;

    @Override
    public List<ThingTagGroup> getThingTagGroup(ThingTagGroup thingTagGroup) {
        List<ThingTagGroup> thingTagGroups = thingTagGroupMapper.getThingTagGroup(thingTagGroup);
        return thingTagGroups;
    }

    @Override
    public void addThingTagGroup(ThingTagGroup thingTagGroup) {
        thingTagGroupMapper.addThingTagGroup(thingTagGroup);
    }

    @Override
    public void updateThingTagGroup(ThingTagGroup thingTagGroup) {
        thingTagGroupMapper.updateThingTagGroup(thingTagGroup);
    }

    @Override
    public void deleteThingTagGroup(ThingTagGroup thingTagGroup) {
        thingTagGroupMapper.deleteThingTagGroup(thingTagGroup);
    }
}
