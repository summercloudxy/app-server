package com.zgiot.app.server.service;

import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface DataService {
    /**
     * 根据thing和metric获取数据
     *
     * @param thingCode
     * @param metricCode
     * @return
     */
    Optional<DataModelWrapper> getData(String thingCode, String metricCode);

    /**
     * 根据thing获取数据
     *
     * @param thingCode
     * @return
     */
    List<DataModelWrapper> findDataByThing(String thingCode);

    /**
     * 根据metric获取数据
     *
     * @param metricCode
     * @return
     */
    List<DataModelWrapper> findDataByMetric(String metricCode);

    /**
     * 1. 数据较新的更新缓存
     * 2. 如果历史存储模块启用
     *    - 输出全量文本
     *    - 如果nosql启用，输出到nosql
     * @param dataModel
     */
    void saveData(DataModel dataModel);

    /**
     * 即时同步取设备信号，并且返回给DataListener
     * @param thingCode
     * @param metricCode
     * @return
     */
    DataModelWrapper adhocLoadData(String thingCode, String metricCode);

}
