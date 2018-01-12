package com.zgiot.app.server.module.thingtag.dao;

import com.zgiot.app.server.module.thingtag.pojo.ThingTag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by wangfan on 2018/1/8.
 */
@Mapper
public interface ThingTagMapper {

    List<ThingTag> getThingTag(ThingTag thingTag);

    void addThingTag(ThingTag thingTag);

    void updateThingTag(ThingTag thingTag);

    void deleteThingTag(ThingTag thingTag);

}
