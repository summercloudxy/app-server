package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.mapper.TMLMapper;
import com.zgiot.app.server.module.filterpress.dao.ThingPropertyMapper;
import com.zgiot.app.server.service.ThingService;
import com.zgiot.common.pojo.ThingModel;
import com.zgiot.common.pojo.ThingPropertyModel;
import org.apache.commons.lang.StringUtils;
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

    @PostConstruct
    private void initCache() {
        List<ThingModel> allThings = tmlMapper.findAllThings();
        for (ThingModel thing : allThings) {
            thingCache.put(thing.getThingCode(), thing);
        }
    }

    @Override
    public ThingModel getThing(String thingCode) {
        ThingModel thingModel = thingPropertyMapper.getBasePropertiy(thingCode);
        if (thingModel == null) {
            thingModel = new ThingModel();
        }
        return thingModel;
    }

    @Override
    public List<ThingModel> findAllThing() {
        List<ThingModel> baseThings = thingPropertyMapper.getThings();
        if (baseThings.size() == 0) {
            baseThings = new ArrayList<>();
        }
        return baseThings;
    }

    @Override
    public List<ThingPropertyModel> findThingProperties(String thingCode, String[] propType) {
        List<ThingPropertyModel> temp = new ArrayList<>();
        List<ThingPropertyModel> thingPropertyModels = new ArrayList<>();
        if (StringUtils.isNotBlank(thingCode)) {
            for (String type : propType) {
                temp = thingPropertyMapper.getPropOrDisPropPropertiy(thingCode, type);
                if (temp.size() > 0) {
                    thingPropertyModels.addAll(temp);
                }
            }
            temp = null;
        }
        return thingPropertyModels;
    }
}
