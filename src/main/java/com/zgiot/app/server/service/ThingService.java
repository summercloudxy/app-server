package com.zgiot.app.server.service;

import com.zgiot.common.pojo.ThingModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public interface ThingService {
    ThingModel getThing(String thingCode);
    public ConcurrentHashMap<String,ConcurrentHashMap<String,String>> getThingProperties(String thingCode, String prop_type, String disPropType);

    public List<ConcurrentHashMap<String,ConcurrentHashMap<String,String>>> getThings(String thingCode, String propType);
}

