package com.zgiot.app.server.dataprocessor.impl;

import com.zgiot.app.server.dataprocessor.DataListener;
import com.zgiot.app.server.module.filterpress.FilterPressManager;
import com.zgiot.common.constants.FilterPressConstants;
import com.zgiot.common.pojo.DataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@Component
public class FilterPressDataListener implements DataListener{
    private static final Logger logger = LoggerFactory.getLogger(FilterPressDataListener.class);
    private static Set<String> metricCodes = new HashSet<>();
    @Autowired
    private FilterPressManager filterPressManager;

    static {
        Class<FilterPressConstants> clazz = FilterPressConstants.class;
        Field[] fields = clazz.getFields();
        try {
            for (Field field : fields) {
                metricCodes.add((String) field.get(clazz));
            }
        } catch (IllegalAccessException e) {
            logger.error("init error", e);
        }
    }

    @Override
    public void onDataChange(DataModel dataModel) {
        if (metricCodes.contains(dataModel.getMetricCode())) {
            handleData(dataModel);
        }
    }

    @Override
    public void onError(Throwable error) {
        logger.error("data engine error", error);
    }

    private void handleData(DataModel data) {
        filterPressManager.onDataSourceChange(data);
    }
}
