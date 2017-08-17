package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.service.DataService;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;

import java.util.List;

public class DataServiceImpl implements DataService{
    @Override
    public DataModelWrapper getData(String thingCode, String metricCode) {
        return null;
    }

    @Override
    public List<DataModelWrapper> findDataByThing(String thingCode) {
        return null;
    }

    @Override
    public List<DataModelWrapper> findDataByMetric(String metricCode) {
        return null;
    }

    @Override
    public void updateCache(DataModel dataModel) {

    }

    @Override
    public void persistData(DataModel dataModel) {

    }

    @Override
    public void persist2NoSQL(DataModel dataModel) {

    }
}
