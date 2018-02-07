package com.zgiot.app.server.service.impl.mapper;

import com.zgiot.app.server.module.thingtag.pojo.ThingTagGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by wangfan on 2018/1/8.
 */
@Mapper
public interface ThingTagGroupMapper {

    List<ThingTagGroup> findThingTagGroup(ThingTagGroup thingTagGroup);

    void addThingTagGroup(ThingTagGroup thingTagGroup);

    void updateThingTagGroup(ThingTagGroup thingTagGroup);

    void deleteThingTagGroup(ThingTagGroup thingTagGroup);

}
