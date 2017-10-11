package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.service.cache.DataCache;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DataServiceImpl implements DataService {
    @Autowired
    private DataCache dataCache;

    @Override
    public Optional<DataModelWrapper> getData(String thingCode, String metricCode) {
        DataModelWrapper data = dataCache.getValue(thingCode, metricCode);
        return (data == null) ? Optional.empty() : Optional.of(data);
    }

    @Override
    public List<DataModelWrapper> findDataByThing(String thingCode) {
        return dataCache.findByThing(thingCode);
    }

    @Override
    public List<DataModelWrapper> findDataByMetric(String metricCode) {
        return dataCache.findByMetric(metricCode);
    }

    @Override
    public void updateCache(DataModel dataModel) {
        dataCache.updateValue(dataModel);
    }

    @Override
    public void persistData(DataModel dataModel) {

    }

    @Override
    public void persist2NoSQL(DataModel dataModel) {

    }
}
