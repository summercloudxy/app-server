package com.zgiot.app.server.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface ThingPropertyService {
    public ConcurrentHashMap<String,ConcurrentHashMap<String,String>> getThingProperties(String thingCode, String prop_type, String disProp_type);

    public List<ConcurrentHashMap<String,ConcurrentHashMap<String,String>>> getThings(String thingCode,String prop_type);
}
