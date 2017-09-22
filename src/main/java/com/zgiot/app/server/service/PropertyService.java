package com.zgiot.app.server.service;

import com.zgiot.common.pojo.ThingPropertyModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PropertyService {
    /**
     *
     * @return List<ThingPropertyModel>
     *  获取所有property
     */
    public List<ThingPropertyModel> findThingProperties();

    /**
     *
     * @param thingCode
     * @param propType
     * @return List<ThingPropertyModel>
     * 从缓存中根据thingCode和propType数组查询thing proerties信息
     */
    public  List<ThingPropertyModel> findThingProperties(String thingCode,String[] propType);

}
