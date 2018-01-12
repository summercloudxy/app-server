package com.zgiot.app.server.service;

import com.zgiot.app.server.module.thingtag.pojo.ThingTag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * Created by wangfan on 2018/1/8.
 */
@Service
public interface ThingTagService {

    /**
     * 获取ThingTag信息
     * @param thingTag
     * @return
     */
    List<ThingTag> getThingTag(ThingTag thingTag);

    /**
     * 新增ThingTag信息
     * @param thingTag
     */
    void addThingTag(ThingTag thingTag);

    /**
     * 修改ThingTag信息
     * @param thingTag
     */
    void updateThingTag(ThingTag thingTag);

    /**
     * 删除ThingTag信息
     * @param thingTag
     */
    void deleteThingTag(ThingTag thingTag);
}
