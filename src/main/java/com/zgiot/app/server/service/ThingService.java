package com.zgiot.app.server.service;

import com.zgiot.common.pojo.ThingModel;
import com.zgiot.common.pojo.ThingPropertyModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface ThingService {
    /**
     * @param thingCode
     * @return ThingModel
     * 根据thingCode获取thing基本信息
     */
    public ThingModel getThing(String thingCode);


    /**
     * @return List<ThingModel>
     * 获取所有thing基本信息
     */
    public List<ThingModel> findAllThing();

    /**
     * @return List<ThingPropertyModel>
     * 获取所有properties
     */
    public List<ThingPropertyModel> findThingProperties();


    /**
     * @param thingCode
     * @param propType
     * @return List<ThingPropertyModel>
     * 根据thingCode获取propType类型为PROP和DISP_PROP的所有properties
     */
    public List<ThingPropertyModel> findThingProperties(String thingCode, String[] propType);

    /**
     * 获取thing下所有的metric
     *
     * @param thingCode
     * @return
     */
    Set<String> findMetricsOfThing(String thingCode);

}

