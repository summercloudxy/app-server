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

    void smartUpdateCache(DataModel dataModel);

}
