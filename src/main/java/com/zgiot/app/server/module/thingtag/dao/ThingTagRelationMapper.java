package com.zgiot.app.server.module.thingtag.dao;

import com.zgiot.app.server.module.thingtag.pojo.ThingTagRelation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by wangfan on 2018/1/8.
 */
@Mapper
public interface ThingTagRelationMapper {

    List<ThingTagRelation> findThingTagRelation(ThingTagRelation thingTagRelation);

    void addThingTagRelation(ThingTagRelation thingTagRelation);

    void updateThingTagRelation(ThingTagRelation thingTagRelation);

    void deleteThingTagRelation(ThingTagRelation thingTagRelation);

}
