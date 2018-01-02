package com.zgiot.app.server.service;

import com.zgiot.common.pojo.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface ThingService {
    /**
     * @param thingCode
     * @return ThingModel
     *         根据thingCode获取thing基本信息
     */
    ThingModel getThing(String thingCode);

    /**
     * @return List<ThingModel>
     *         获取所有thing基本信息
     */
    List<ThingModel> findAllThing();

    /**
     * @return List<ThingPropertyModel>
     *         获取所有properties
     */
    List<ThingPropertyModel> findThingProperties();

    /**
     * @param thingCode
     * @param propType
     * @return List<ThingPropertyModel>
     *         根据thingCode获取propType类型为PROP和DISP_PROP的所有properties
     */
    List<ThingPropertyModel> findThingProperties(String thingCode, String[] propType);

    /**
     * 获取thing下所有的metric
     *
     * @param thingCode
     * @return
     */
    Set<String> findMetricsOfThing(String thingCode);

    /**
     * 获取所有建筑信息
     * 
     * @return
     */
    List<BuildingModel> findAllBuilding();

    /**
     * 获取所有系统信息
     * 
     * @return
     */
    List<SystemModel> findAllSystem();

    void validateThing(String thingCode);

    List<CategoryModel> getCategoryListByAssetType(String assetType);

    List<CategoryModel> getCategoryList();

    List<CategoryModel> getMetricTypeByAssetAndCategory(String assetType, String category, String thingCode);

    List<MetricModel> getMetricByAssetAndCategory(String assetType, String category, String thingCode,
            String metricType);

    List<ThingModel> getThingCodeByAssetAndCategory(String assetType, String category, String metricCode,
            String metricType);
}
