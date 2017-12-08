package com.zgiot.app.server.module.bellows.compressor.cache;

import com.zgiot.app.server.module.bellows.compressor.Compressor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangwei
 */
@Component
public class CompressorCacheImpl implements CompressorCache {

    private Map<String, Compressor> cache = new ConcurrentHashMap<>(6);

    @Override
    public Compressor findByThingCode(String thingCode) {
        return cache.get(thingCode);
    }

    @Override
    public List<Compressor> findByType(String type) {
        List<Compressor> result = new ArrayList<>();
        cache.forEach((key, value)-> {
            if (type.equals(value.getType())) {
                result.add(value);
            }
        });

        //排序
        result.sort(new Comparator<Compressor>() {
            @Override
            public int compare(Compressor o1, Compressor o2) {
                return Integer.compare(o1.getSort(), o2.getSort());
            }
        });

        return result;
    }

    @Override
    public void put(String thingCode, Compressor compressor) {
        cache.put(thingCode, compressor);
    }
}
