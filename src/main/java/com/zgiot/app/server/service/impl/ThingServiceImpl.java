package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.service.ThingService;
import com.zgiot.app.server.service.impl.mapper.TMLMapper;
import com.zgiot.common.constants.MetricTypes;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ThingServiceImpl implements ThingService {
    private final ConcurrentHashMap<String, ThingModel> thingCache = new ConcurrentHashMap<>(5000);
    private Map<String, ThingPropertyModel> propertyCache = new ConcurrentHashMap<>(50000);
    private Map<Integer, SystemModel> systemCache = new ConcurrentHashMap<>(100);
    private Map<String, CategoryModel> categoryCache = new ConcurrentHashMap<>(200);
    @Autowired
    private TMLMapper tmlMapper;

    @Override
    public ThingModel getThing(String thingCode) {
        return thingCache.get(thingCode);
    }

    @PostConstruct
    private void initCache() {
        List<ThingModel> allThings = tmlMapper.findAllThings();
        for (ThingModel thing : allThings) {
            thingCache.put(thing.getThingCode(), thing);
        }

        List<ThingPropertyModel> allProperties = tmlMapper.findAllProperties();
        for (ThingPropertyModel property : allProperties) {
            propertyCache.put(property.getThingCode() + "_" + property.getPropKey(), property);
        }

        List<SystemModel> allSystems = tmlMapper.findAllSystems();
        for (SystemModel system : allSystems){
            systemCache.put(system.getId(),system);
        }

        List<CategoryModel> categoryList = tmlMapper.getCategoryList();
        for (CategoryModel category : categoryList){
            categoryCache.put(category.getCode() ,category);
        }
    }

    @Override
    public List<ThingModel> findAllThing() {
        List<ThingModel> baseThings = tmlMapper.findAllThings();
        if (baseThings.size() == 0) {
            baseThings = new ArrayList<>();
        }
        return baseThings;
    }

    @Override
    public List<ThingPropertyModel> findThingProperties() {
        List<ThingPropertyModel> thingPropertyModelList = new ArrayList<>();
        thingPropertyModelList = tmlMapper.findAllProperties();
        return thingPropertyModelList;
    }

    @Override
    public List<ThingPropertyModel> findThingProperties(String thingCode, String[] propType) {
        List<ThingPropertyModel> thingPropertyModelList = new ArrayList<>();
        for (String propKey : propertyCache.keySet()) {
            for (String type : propType) {
                if (propKey.startsWith(thingCode + "_") && (propertyCache.get(propKey) != null)
                        && (StringUtils.isNotBlank(propertyCache.get(propKey).getPropType()))
                        && propertyCache.get(propKey).getPropType().equals(type)) {
                    thingPropertyModelList.add(propertyCache.get(propKey));
                }
            }
        }
        return thingPropertyModelList;
    }

    @Override
    public Set<String> findMetricsOfThing(String thingCode) {
        return tmlMapper.findMetricsOfThing(thingCode);
    }

    @Override
    public List<BuildingModel> findAllBuilding() {
        return tmlMapper.findAllBuildings();
    }

    @Override
    public List<SystemModel> findAllSystem() {
        return tmlMapper.findAllSystems();
    }

    @Override
    public SystemModel findSystemById(Integer id){
        if(id == null){
            return null;
        }
        return systemCache.get(id);
    }

    @Override
    public CategoryModel findCategoryByCode(String categoryCode){
        if (categoryCode == null){
            return null;
        }
        return categoryCache.get(categoryCode);
    }

    @Override
    public void validateThing(String thingCode) {
        if (thingCode == null) {
            throw new SysException("thingCode is required.", SysException.EC_UNKNOWN);
        }

        ThingModel tm = this.getThing(thingCode);
        if (tm == null) {
            throw new SysException("thingCode not found. (thingCode=`" + thingCode + "`)", SysException.EC_UNKNOWN);
        }
    }

    /**
     * 根据资产类别获取设备/部件类别
     * 
     * @param assetType
     */
    public List<CategoryModel> getCategoryListByAssetType(String assetType) {
        return tmlMapper.getCategoryListByAssetType(assetType);
    }

    /**
     * 获取全部设备/部件类别
     *
     * @return
     */
    public List<CategoryModel> getCategoryList() {
        return tmlMapper.getCategoryList();
    }

    /**
     * 获取指定的资产、类别、设备上可能存在的信号类型
     *
     * @param assetType
     * @param category
     * @return
     */
    public List<CategoryModel> getMetricTypeByAssetAndCategory(String assetType, String category, String thingCode) {
        return tmlMapper.getMetricTypeByAssetAndCategory(assetType, category, thingCode);
    }

    /**
     * 获取指定资产、类别、设备、信号类型下可能存在的信号
     * 
     * @param assetType
     * @param category
     * @param thingCode
     * @param metricType
     * @return
     */
    public List<MetricModel> getMetricByAssetAndCategory(String assetType, String category, String thingCode,
            String metricType) {
        return tmlMapper.getMetricByAssetAndCategory(assetType, category, thingCode, metricType, MetricTypes.PR);
    }

    /**
     * 获取指定资产、类别、设备、信号类型下拥有该信号的设备
     * 
     * @param assetType
     * @param category
     * @param metricCode
     * @param metricType
     * @return
     */

    public List<ThingModel> getThingCodeByAssetAndCategory(String assetType, String category, String metricCode,
            String metricType) {
        return tmlMapper.getThingCodeByAssetAndCategory(assetType, category, metricCode, metricType);
    }

}
