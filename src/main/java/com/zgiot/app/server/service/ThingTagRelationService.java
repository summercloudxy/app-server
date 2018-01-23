package com.zgiot.app.server.service;

import com.zgiot.app.server.module.thingtag.pojo.ThingTagRelation;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangfan on 2018/1/8.
 */
@Service
public interface ThingTagRelationService {

    /**
     * 获取ThingTagRelation信息
     * @param thingTagRelationId
     * @return
     */
    ThingTagRelation getThingTagRelation(Integer thingTagRelationId);

    /**
     * 获取ThingTagRelation信息
     * @param thingTagRelation
     * @return
     */
    List<ThingTagRelation> findThingTagRelation(ThingTagRelation thingTagRelation);

    /**
     * 新增ThingTagRelation信息
     * @param thingTagRelation
     */
    void addThingTagRelation(ThingTagRelation thingTagRelation);

    /**
     * 修改ThingTagRelation信息
     * @param thingTagRelation
     */
    void updateThingTagRelation(ThingTagRelation thingTagRelation);

    /**
     * 删除ThingTagRelation信息
     * @param thingTagRelation
     */
    void deleteThingTagRelation(ThingTagRelation thingTagRelation);
}
