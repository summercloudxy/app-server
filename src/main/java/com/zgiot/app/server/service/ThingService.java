package com.zgiot.app.server.service;

import com.zgiot.common.pojo.ThingModel;
import com.zgiot.common.pojo.ThingPropertyModel;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface ThingService {
    /*
    * 根据thingCode获取thing基本信息
    * */
    public ThingModel getThing(String thingCode);

    /*
    * 获取所有thing基本信息
    * */
    public List<ThingModel> findAllThing();

    /*
    * 根据thingCode和propType获取thing property信息
    * */
    public List<ThingPropertyModel> findThingProperties(String thingCode, String[] propType);

}

