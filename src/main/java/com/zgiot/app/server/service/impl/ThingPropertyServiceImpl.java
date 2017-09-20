package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.module.filterpress.dao.ThingPropertyMapper;
import com.zgiot.app.server.module.filterpress.pojo.BaseProperty;
import com.zgiot.app.server.module.filterpress.pojo.PropOrDisProp;
import com.zgiot.app.server.service.ThingPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class ThingPropertyServiceImpl implements ThingPropertyService {
    @Autowired
    ThingPropertyMapper thingPropertyMapper;

    @Override
    public ConcurrentHashMap<String, ConcurrentHashMap<String, String>> getThingProperties(String thingCode, String prop_type, String disProp_type) {
        ConcurrentHashMap<String, ConcurrentHashMap<String, String>> thingMap = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, String> base = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, String> prop = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, String> disProp = new ConcurrentHashMap<>();
        BaseProperty basePropertiy = thingPropertyMapper.getBasePropertiy(thingCode);
        List<PropOrDisProp> properties = thingPropertyMapper.getPropOrDisPropPropertiy(thingCode, prop_type);
        List<PropOrDisProp> disProperties = thingPropertyMapper.getPropOrDisPropPropertiy(thingCode, disProp_type);
        base.put("name", basePropertiy.getName());
        base.put("shortName", basePropertiy.getShortName());
        if (properties.size() >= 1) {
            for (PropOrDisProp propOrDisProp : properties) {
                prop.put(propOrDisProp.getPro_key(), propOrDisProp.getProp_value());
            }
        }
        if (disProperties.size() >= 1) {
            for (PropOrDisProp propOrDisProp : disProperties) {
                disProp.put(propOrDisProp.getPro_key(), propOrDisProp.getPro_key());
            }
        }

        thingMap.put("base", base);
        thingMap.put("prop", prop);
        thingMap.put("disProp", disProp);

        return thingMap;
    }

    @Override
    public List<ConcurrentHashMap<String, ConcurrentHashMap<String, String>>> getThings(String prop_type, String disProp_type) {
        List<BaseProperty> baseThings = thingPropertyMapper.getThings();
        ConcurrentHashMap<String, ConcurrentHashMap<String, String>> thingMap = null;
        ConcurrentHashMap<String, String> base = null;
        ConcurrentHashMap<String, String> prop = null;
        ConcurrentHashMap<String, String> disProp =  null;
        List<ConcurrentHashMap<String, ConcurrentHashMap<String, String>>> things = new ArrayList<>();
        List<PropOrDisProp> properties = null;
        List<PropOrDisProp> disProperties = null;

        if (baseThings.size() >= 1) {
            for (BaseProperty baseProperty : baseThings) {
                base = new ConcurrentHashMap<>();
                prop = new ConcurrentHashMap<>();
                disProp = new ConcurrentHashMap<>();
                thingMap = new ConcurrentHashMap<>();
                base.put("name", baseProperty.getName());
                base.put("shortName", baseProperty.getShortName());
                properties = thingPropertyMapper.getPropOrDisPropPropertiy(baseProperty.getThingCode(), prop_type);
                disProperties = thingPropertyMapper.getPropOrDisPropPropertiy(baseProperty.getThingCode(), disProp_type);
                //获取prop信息
                if (properties.size() >= 1) {
                    for (PropOrDisProp propOrDisProp : properties) {
                        prop.put(propOrDisProp.getPro_key(), propOrDisProp.getProp_value());
                    }
                }
                //获取disProp信息
                if (disProperties.size() >= 1) {
                    for (PropOrDisProp propOrDisProp : disProperties) {
                        disProp.put(propOrDisProp.getPro_key(), propOrDisProp.getProp_value());
                    }
                }
                thingMap.put("base", base);
                thingMap.put("prop", prop);
                thingMap.put("disProp", disProp);
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
