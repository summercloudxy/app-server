package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.module.thingtag.dao.ThingTagRelationMapper;
import com.zgiot.app.server.module.thingtag.pojo.ThingTagRelation;
import com.zgiot.app.server.service.ThingTagRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by chenting on 2018/1/16.
 */
@Service
public class ThingTagRelationServiceImpl implements ThingTagRelationService {

    @Autowired
    ThingTagRelationMapper thingTagRelationMapper;

    @Override
    public ThingTagRelation getThingTagRelation(Integer thingTagRelationId){
        ThingTagRelation thingTagRelation = new ThingTagRelation();
        thingTagRelation.setThingTagRelationId(thingTagRelationId);
        List<ThingTagRelation> thingTagRelations = thingTagRelationMapper.findThingTagRelation(thingTagRelation);
        if(thingTagRelations.isEmpty()){
            return null;
        }
        return thingTagRelations.get(0);
    }

    @Override
    public List<ThingTagRelation> findThingTagRelation(ThingTagRelation thingTagRelation) {
        List<ThingTagRelation> thingTagRelations = thingTagRelationMapper.findThingTagRelation(thingTagRelation);
        return thingTagRelations;
    }

    @Override
    public void addThingTagRelation(ThingTagRelation thingTagRelation) {
        thingTagRelation.setCreateDate(new Date());
        thingTagRelationMapper.addThingTagRelation(thingTagRelation);
    }

    @Override
    public void updateThingTagRelation(ThingTagRelation thingTagRelation) {
        thingTagRelationMapper.updateThingTagRelation(thingTagRelation);
    }

    @Override
    public void deleteThingTagRelation(ThingTagRelation thingTagRelation) {
        thingTagRelationMapper.deleteThingTagRelation(thingTagRelation);
    }
}
