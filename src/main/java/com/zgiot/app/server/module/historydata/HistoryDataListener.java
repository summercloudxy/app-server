package com.zgiot.app.server.module.historydata;

import com.zgiot.app.server.dataprocessor.DataListener;
import com.zgiot.app.server.mapper.TMLMapper;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.ThingMetricModel;
import com.zgiot.common.reloader.Reloader;
import com.zgiot.common.reloader.ServerReloadManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HistoryDataListener implements DataListener, Reloader {
    private static final Logger logger = LoggerFactory.getLogger(HistoryDataListener.class);
    /**
     * {thingCode: {metricCode: null }}
     */
    private static final Map<String, Map<String, Object>> WHITE_MAP = new HashMap();
    private static Boolean inited = false;

    @Autowired
    TMLMapper tmlMapper;

    public void init() {
        synchronized (inited) {
            if (inited) {
                return;
            }

            ServerReloadManager.addReloader(this);
            reload();
        }
    }

    @Override
    public void onDataChange(DataModel dm) {
        synchronized (inited) {
            if (!inited) {
                logger.info("Start to init HistoryDataListener ... ");
                init();
            }
        }

        boolean toStore = false;
        if (WHITE_MAP.containsKey(dm.getThingCode())) {
            Map metricMap = WHITE_MAP.get(dm.getThingCode());

            if (metricMap != null && metricMap.containsKey(dm.getMetricCode())) {
                toStore = true;
            }
        }

        if (toStore) {
            // todo to call svc to add data
        }
    }

    @Override
    public void onError(Throwable error) {
        logger.error(error.getMessage());
    }

    @Override
    public void reload() {
        synchronized (inited) {
            inited = false;

            WHITE_MAP.clear();
            List<ThingMetricModel> list = this.tmlMapper.findAllHistdataWhitelist();
            for (ThingMetricModel tm : list) {
                Map metricMap = WHITE_MAP.get(tm.getThingCode());
                if (metricMap == null) {
                    metricMap = new HashMap();
                    WHITE_MAP.put(tm.getThingCode(), metricMap);
                }
                metricMap.put(tm.getMetricCode(), null);
            }

            inited = true;
        }
    }
}

