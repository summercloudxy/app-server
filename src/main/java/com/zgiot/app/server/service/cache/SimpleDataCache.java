package com.zgiot.app.server.service.cache;

import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component("simpleCache")
class SimpleDataCache implements DataCache {
    private ConcurrentHashMap<String, DataModel> cache = new ConcurrentHashMap<>();

    @Override
    public DataModelWrapper getValue(String thingCode, String metricCode) {
        DataModel data = cache.get(generateKey(thingCode, metricCode));
        DataModelWrapper rtn = null;
        if (data != null) {
            rtn = new DataModelWrapper(data);
        }
        return rtn;
    }

    @Override
    public List<DataModelWrapper> findByThing(String thingCode) {
        List<DataModelWrapper> result = new ArrayList<>();
        cache.forEach((key, value) -> {
            if (isThing(key, thingCode)) {
                result.add(new DataModelWrapper(value));
            }
        });
        return result;
    }

    @Override
    public List<DataModelWrapper> findByMetric(String metricCode) {
        List<DataModelWrapper> result = new ArrayList<>();
        cache.forEach((key, value) -> {
            if (isMetric(key, metricCode)) {
                result.add(new DataModelWrapper(value));
            }
        });
        return result;
    }

    @Override
    public void updateValue(DataModel value) {
        cache.put(generateKey(value.getThingCode(), value.getMetricCode()), value);
    }

    @Override
    public boolean hasValue(String thingCode, String metricCode) {
        return cache.containsKey(generateKey(thingCode, metricCode));
    }

    private String generateKey(String thingCode, String metricCode) {
        return thingCode + "-" + metricCode;
    }

    private boolean isThing(String key, String thingCode) {
        return key.startsWith(thingCode);
    }

    private boolean isMetric(String key, String metricCode) {
        return key.endsWith(metricCode);
    }

}
