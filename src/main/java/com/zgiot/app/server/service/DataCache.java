package com.zgiot.app.server.service;

import com.zgiot.common.pojo.DataModel;

import java.util.concurrent.ConcurrentHashMap;

public class DataCache {
    private ConcurrentHashMap<String, DataModel> cache = new ConcurrentHashMap<>();

    public DataModel getValue(String cacheKey) {
        return null;
    }

    public void updateValue(String cacheKey, DataModel value) {

    }

    public boolean hasKey(String cacheKey) {
        return false;
    }

}
