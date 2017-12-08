package com.zgiot.app.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.config.ModuleListConfig;
import com.zgiot.app.server.service.DataService.DataAccessCounterItem;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.zgiot.common.pojo.DataModel.genKey;

@Service
public class DataServiceImpl implements DataService {
    private static final Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);
    private Object readLock = new Object();
    private Object writeLock = new Object();

    @Autowired
    private DataCache dataCache;
    @Autowired
    ModuleListConfig moduleListConfig;
    @Autowired
    HistoryDataService historyDataService;
    @Autowired
    private DataEngineTemplate dataEngineTemplate;
    private DataAccessCounter dataAccessCounter = new DataAccessCounter();

    @Override
    public Optional<DataModelWrapper> getData(String thingCode, String metricCode) {
        DataModelWrapper data = internalGetData(thingCode, metricCode, true);
        return (data == null) ? Optional.empty() : Optional.of(data);
    }

    private DataModelWrapper internalGetData(String thing, String metric, boolean toCount) {
        synchronized (this.readLock) {
            DataModelWrapper data = dataCache.getValue(thing, metric);
            if (toCount && data != null) {
                this.dataAccessCounter.readUp(data.getThingCode(), data.getMetricCode());
            }
            return data;
        }
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
        synchronized (this.writeLock) {
            dataCache.updateValue(dataModel);
            this.dataAccessCounter.writeUp(dataModel.getThingCode(), dataModel.getMetricCode());
        }
    }

    @Override
    public void saveData(DataModel newData) {
        DataModelWrapper old = this.internalGetData(newData.getThingCode(), newData.getMetricCode(), false);
        boolean toUpdate = false;

        String oldValue = null;
        if (old == null) {
            toUpdate = true;
        } else {
            oldValue = old.getValue();
            if (newData.getDataTimeStamp().getTime() > old.getDataTimeStamp().getTime()) {
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
        return null; // todo change to optional ans share ww
    }

    public Map<String, DataAccessCounterItem> getAccessCounterMap() {
        return this.dataAccessCounter.counterMap;
    }

}

class DataAccessCounter {
    Map<String, DataAccessCounterItem> counterMap = new ConcurrentHashMap<>();

    public void readUp(String thingCode, String metricCode) {
        String key = genKey(thingCode, metricCode);
        DataAccessCounterItem counter = counterMap.get(key);
        if (counter == null) {
            counter = new DataAccessCounterItem();
            counter.readCount.incrementAndGet();
            counterMap.put(key, counter);
        } else {
            counter.readCount.incrementAndGet();
        }
    }

    public void writeUp(String thingCode, String metricCode) {
        String key = genKey(thingCode, metricCode);
        DataAccessCounterItem counter = counterMap.get(key);
        if (counter == null) {
            counter = new DataAccessCounterItem();
            counter.writeCount.incrementAndGet();
            counterMap.put(key, counter);
        } else {
            counter.writeCount.incrementAndGet();
        }
    }

}

