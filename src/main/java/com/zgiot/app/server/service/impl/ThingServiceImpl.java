package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.mapper.TMLMapper;
import com.zgiot.app.server.service.ThingService;
import com.zgiot.common.pojo.BuildingModel;
import com.zgiot.common.pojo.SystemModel;
import com.zgiot.common.pojo.ThingModel;
import com.zgiot.common.pojo.ThingPropertyModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ThingServiceImpl implements ThingService {
    private final ConcurrentHashMap<String, ThingModel> thingCache = new ConcurrentHashMap<>(5000);
    private Map<String, ThingPropertyModel> propertyCache = new ConcurrentHashMap<>(50000);
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
}
