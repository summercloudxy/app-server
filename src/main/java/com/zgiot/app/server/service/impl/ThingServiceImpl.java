package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.mapper.TMLMapper;
import com.zgiot.app.server.service.ThingService;
import com.zgiot.common.pojo.ThingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ThingServiceImpl implements ThingService {
    private final ConcurrentHashMap<String, ThingModel> thingCache = new ConcurrentHashMap<>(5000);
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
    }
}
