package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.mapper.TMLMapper;
import com.zgiot.app.server.module.filterpress.dao.ThingPropertyMapper;
import com.zgiot.common.constants.FilterPressConstants;
import com.zgiot.common.pojo.ThingPropModel;
import com.zgiot.app.server.service.ThingService;
import com.zgiot.common.pojo.ThingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ThingServiceImpl implements ThingService {
    private final ConcurrentHashMap<String, ThingModel> thingCache = new ConcurrentHashMap<>(5000);
    @Autowired
    private TMLMapper tmlMapper;
    @Autowired
    ThingPropertyMapper thingPropertyMapper;

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
    }

    @Override
    public ConcurrentHashMap<String, ConcurrentHashMap<String, String>> getThingProperties(String thingCode, String propType, String disPropType) {
        ConcurrentHashMap<String, ConcurrentHashMap<String, String>> thingMap = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, String> base = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, String> prop = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, String> disProp = new ConcurrentHashMap<>();
        ThingModel  thingModel = thingPropertyMapper.getBasePropertiy(thingCode);
        List<ThingPropModel> properties = thingPropertyMapper.getPropOrDisPropPropertiy(thingCode, propType);
        List<ThingPropModel> disProperties = thingPropertyMapper.getPropOrDisPropPropertiy(thingCode, disPropType);
        base.put(FilterPressConstants.THING_NAME, thingModel.getThingName());
        base.put(FilterPressConstants.THING_SHORT_NAME, thingModel.getShortName());
        if (properties.size() > 0) {
            for (ThingPropModel propOrDisProp : properties) {
                prop.put(propOrDisProp.getPropKey(), propOrDisProp.getPropValue());
            }
        }
        if (disProperties.size() > 0) {
            for (ThingPropModel propOrDisProp : disProperties) {
                disProp.put(propOrDisProp.getPropKey(), propOrDisProp.getPropValue());
            }
        }

        thingMap.put(FilterPressConstants.BASE, base);
        thingMap.put(FilterPressConstants.PROP, prop);
        thingMap.put(FilterPressConstants.DIS_PROP, disProp);

        return thingMap;
    }

    @Override
    public List<ConcurrentHashMap<String, ConcurrentHashMap<String, String>>> getThings(String propType, String disPropType) {
        List<ThingModel> baseThings = thingPropertyMapper.getThings();
        ConcurrentHashMap<String, ConcurrentHashMap<String, String>> thingMap = null;
        ConcurrentHashMap<String, String> base = null;
        ConcurrentHashMap<String, String> prop = null;
        ConcurrentHashMap<String, String> disProp =  null;
        List<ConcurrentHashMap<String, ConcurrentHashMap<String, String>>> things = new ArrayList<>();
        List<ThingPropModel> properties = null;
        List<ThingPropModel> disProperties = null;

        if (baseThings.size() > 0) {
            for (ThingModel baseProperty : baseThings) {
                base = new ConcurrentHashMap<>();
                prop = new ConcurrentHashMap<>();
                disProp = new ConcurrentHashMap<>();
                thingMap = new ConcurrentHashMap<>();
                base.put(FilterPressConstants.THING_NAME, baseProperty.getThingName());
                base.put(FilterPressConstants.THING_SHORT_NAME, baseProperty.getShortName());
                properties = thingPropertyMapper.getPropOrDisPropPropertiy(baseProperty.getThingCode(), propType);
                disProperties = thingPropertyMapper.getPropOrDisPropPropertiy(baseProperty.getThingCode(), disPropType);
                /*获取prop信息*/
                if (properties.size() > 0) {
                    for (ThingPropModel propOrDisProp : properties) {
                        prop.put(propOrDisProp.getPropKey(), propOrDisProp.getPropValue());
                    }
                }
                /*获取disProp信息*/
                if (disProperties.size() > 0) {
                    for (ThingPropModel propOrDisProp : disProperties) {
                        disProp.put(propOrDisProp.getPropKey(), propOrDisProp.getPropValue());
                    }
                }
                thingMap.put(FilterPressConstants.BASE, base);
                thingMap.put(FilterPressConstants.PROP, prop);
                thingMap.put(FilterPressConstants.DIS_PROP, disProp);
                things.add(thingMap);
                properties = null;
                disProperties = null;
                base = null;
                prop = null;
                disProp = null;
                thingMap = null;
            }
        }
        return things;
    }
}
