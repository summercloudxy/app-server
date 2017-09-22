package com.zgiot.app.server.service.cache;

import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component("simpleCache")
public class SimpleDataCache implements DataCache {
    private ConcurrentHashMap<String, DataModel> cache = new ConcurrentHashMap<>();
    private volatile boolean initialized = false;

    @Override
    public void initCache(List<DataModel> initValues) {
        if ((initialized = true) && initValues != null) {
            for (DataModel value : initValues) {
                String thingCode = value.getThingCode();
                String metricCode = value.getMetricCode();
                cache.put(generateKey(thingCode, metricCode), value);
            }
        }
    }

    @Override
    public DataModelWrapper getValue(String thingCode, String metricCode) {
        checkInit();
        DataModel data = cache.get(generateKey(thingCode, metricCode));
        return data == null ? null : new DataModelWrapper(data);
    }

    @Override
    public List<DataModelWrapper> findByThing(String thingCode) {
        checkInit();
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
        checkInit();
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
        checkInit();
        cache.put(generateKey(value.getThingCode(), value.getMetricCode()), value);
    }

    @Override
    public boolean hasValue(String thingCode, String metricCode) {
        checkInit();
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

    private void checkInit() {
        if (!initialized) {
            throw new IllegalStateException("Cache never initialized");
        }
    }

}
