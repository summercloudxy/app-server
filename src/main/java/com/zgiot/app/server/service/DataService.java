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
     * 更新cache的值
     *
     * @param dataModel
     */
    void updateCache(DataModel dataModel);

    /**
     * 将数据持久化至关系型数据库（默认MySQL）
     *
     * @param dataModel
     */
    void persistData(DataModel dataModel);

    /**
     * 将数据持久化至NoSQL数据库
     *
     * @param dataModel
     */
    void persist2NoSQL(DataModel dataModel);
}
