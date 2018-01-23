package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.module.thingtag.dao.ThingTagGroupMapper;
import com.zgiot.app.server.module.thingtag.pojo.ThingTagGroup;
import com.zgiot.app.server.service.ThingTagGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangfan on 2018/1/9.
 */
@Service
public class ThingTagGroupServiceImpl implements ThingTagGroupService{

    @Autowired
    ThingTagGroupMapper thingTagGroupMapper;

    @Override
    public ThingTagGroup getThingTagGroup(Integer thingTagGroupId, String thingTagCode){
        ThingTagGroup thingTagGroup = new ThingTagGroup();
        thingTagGroup.setThingTagGroupId(thingTagGroupId);
        thingTagGroup.setCode(thingTagCode);
        List<ThingTagGroup> thingTagGroups = thingTagGroupMapper.getThingTagGroup(thingTagGroup);
        if(null == thingTagGroups){
            return null;
        }
        return thingTagGroups.get(0);
    }

    @Override
    public List<ThingTagGroup> findThingTagGroup(ThingTagGroup thingTagGroup) {
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
