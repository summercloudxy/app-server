package com.zgiot.app.server.service;

import com.zgiot.app.server.module.thingtag.pojo.ThingTagGroup;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangfan on 2018/1/8.
 */
@Service
public interface ThingTagGroupService {

    /**
     * 获取ThingTagGroup信息
     * @param thingTagGroup
     * @return
     */
    List<ThingTagGroup> getThingTagGroup(ThingTagGroup thingTagGroup);

    /**
     * 新增ThingTagGroup信息
     * @param thingTagGroup
     */
    void addThingTagGroup(ThingTagGroup thingTagGroup);

    /**
     * 修改ThingTagGroup信息
     * @param thingTagGroup
     */
    void updateThingTagGroup(ThingTagGroup thingTagGroup);

    /**
     * 删除ThingTagGroup信息
     * @param thingTagGroup
     */
    void deleteThingTagGroup(ThingTagGroup thingTagGroup);

}
