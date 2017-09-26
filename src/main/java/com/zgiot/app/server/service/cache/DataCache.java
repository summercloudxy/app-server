package com.zgiot.app.server.service.cache;

import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;

import java.util.List;

public interface DataCache {
    /**
     * 从缓存中获取数据
     *
     * @param thingCode
     * @param metricCode
     * @return 当从缓存中获取不到时返回 {@code null}
     */
    DataModelWrapper getValue(String thingCode, String metricCode);

    /**
     * 根据物体从缓存中获取数据(获取该thing所有metric的最新值)
     *
     * @param thingCode
     * @return 当缓存中获取不到时返回 {@code size()} 为0
     */
    List<DataModelWrapper> findByThing(String thingCode);

    /**
     * 根据metric从缓存中获取数据(获取所有该metric的最新值)
     *
     * @param metricCode
     * @return 当缓存中获取不到时返回 {@code size()} 为0
     */
    List<DataModelWrapper> findByMetric(String metricCode);

    /**
     * 更新缓存数据
     *
     * @param value
     */
    void updateValue(DataModel value);

    /**
     * 根据thing和metric查找是否包含其值
     *
     * @param thingCode
     * @param metricCode
     * @return
     */
    boolean hasValue(String thingCode, String metricCode);
}
