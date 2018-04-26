package com.zgiot.app.server.module.equipments.service;

import com.zgiot.app.server.module.equipments.pojo.ThingBaseDict;

import java.util.List;

public interface ThingBaseDictService {

    /**
     * 根据key获取数据字典信息
     *
     * @param key
     * @return
     */
    List<ThingBaseDict> getThingBaseDictListByKey(String key);

}
