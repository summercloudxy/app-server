package com.zgiot.app.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.config.ModuleListConfig;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.app.server.service.cache.DataCache;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.restcontroller.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Optional;

@Service
public class DataServiceImpl implements DataService {
    private static final Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);

    @Autowired
    private DataCache dataCache;
    @Autowired
    ModuleListConfig moduleListConfig;
    @Autowired
    HistoryDataService historyDataService;
    @Autowired
    private DataEngineTemplate dataEngineTemplate;

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

    void updateCache(DataModel dataModel) {
        dataCache.updateValue(dataModel);
    }

    @Override
    public void saveData(DataModel newData) {
        Optional<DataModelWrapper> old = this.getData(newData.getThingCode(), newData.getMetricCode());
        boolean toUpdate = false;

        String oldValue = null;
        if (!old.isPresent()) {
            toUpdate = true;
        } else {
            DataModelWrapper oldW = old.get();
            oldValue = oldW.getValue();
            if (newData.getDataTimeStamp().getTime() > oldW.getDataTimeStamp().getTime()) {
                toUpdate = true;
            }
        }

        // only update later one
        if (toUpdate) {
            newData.setPreValue(oldValue);
            this.updateCache(newData);

            // hist data save?
            if (moduleListConfig.containModule(ModuleListConfig.MODULE_HIST_PERSIST)) {
                this.historyDataService.asyncSmartAddData(newData);
            }
        }

    }

    public DataModelWrapper adhocLoadData(String thingCode, String metricCode) {
        try {
            ServerResponse<String> res = this.dataEngineTemplate.getForObject(
                    DataEngineTemplate.URI_DATA_ADHOC + "/" + thingCode + "/" + metricCode
                    , ServerResponse.class);
            logger.trace("1510748530 res: `{}`", res);
            String objStr = res.getObj();
            DataModel resDM = JSON.parseObject(objStr, DataModel.class);
            return new DataModelWrapper(resDM);
        } catch (Exception e) {
            logger.error("Adhoc load data error. (errMsg=`{}`, thingCode=`{}`, metricCode=`{}`)", e.getMessage()
                    , thingCode, metricCode);
        }
        return null;
    }
}
