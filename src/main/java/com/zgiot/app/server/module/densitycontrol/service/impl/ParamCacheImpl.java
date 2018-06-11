package com.zgiot.app.server.module.densitycontrol.service.impl;

import com.zgiot.app.server.module.densitycontrol.mapper.DensityControlConfigMapper;
import com.zgiot.app.server.module.densitycontrol.pojo.DensityControlConfig;
import com.zgiot.app.server.module.densitycontrol.service.ParamCache;
import com.zgiot.app.server.service.cache.DataCache;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ParamCacheImpl implements ParamCache {

    @Autowired
    private DataCache dataCache;
    @Autowired
    private DensityControlConfigMapper densityControlConfigMapper;
    @Autowired
    private DensityControlServiceImpl densityControlService;

    private ConcurrentHashMap<String, DataModel> cache = new ConcurrentHashMap<>();

    @Override
    public DataModelWrapper getValue(String thingCode, String metricCode) {
        // 从模块缓存中取
        DataModel data = cache.get(generateKey(thingCode, metricCode));
        DataModelWrapper dataModelWrapper = null;
        if (data != null) {
            dataModelWrapper = new DataModelWrapper(data);
        } else {
            // 从大缓存中取,并更新至模块缓存
            dataModelWrapper = dataCache.getValue(thingCode, metricCode);
            if (dataModelWrapper == null) {
                // 从数据库中取,并更新至模块缓存
                DensityControlConfig densityControlConfig = densityControlConfigMapper.getDensityControlConfigByThingMetric(thingCode, metricCode);
                String value = "0";
                if (densityControlConfig == null) {
                    value = StringUtils.isBlank(densityControlConfig.getMetricValue()) ? "0" : densityControlConfig.getMetricValue();
                }
                dataModelWrapper = new DataModelWrapper(new DataModel(null, thingCode, null,
                                metricCode, value, new Date()));
            }
            updateValue(dataModelWrapper);
        }
        return dataModelWrapper;
    }

    @Override
    public void updateValue(DataModel dataModel) {
        cache.put(generateKey(dataModel.getThingCode(), dataModel.getMetricCode()), dataModel);
    }

    @Override
    public void updateValue(DataModelWrapper dataModelWrapper) {
        DataModel dm = new DataModel();
        dm.setThingCode(dataModelWrapper.getThingCode());
        dm.setMetricCode(dataModelWrapper.getMetricCode());
        dm.setValue(dataModelWrapper.getValue());
        dm.setDataTimeStamp(new Date());
        updateValue(dm);
    }

    @Override
    public void init() {
        List<DensityControlConfig> densityControlConfigList = densityControlConfigMapper.getAllDensityControlConfig();
        if (densityControlConfigList != null && !densityControlConfigList.isEmpty()) {
            for (DensityControlConfig densityControlConfig : densityControlConfigList) {
                String thingCode = densityControlConfig.getThingCode();
                String metricCode = densityControlConfig.getMetricCode();
                String value = densityControlConfig.getMetricValue();
                // 写入模块缓存
                cache.put(generateKey(thingCode, metricCode),
                        new DataModel(null, thingCode, null, metricCode, value, new Date()));

                // 写入KepServer
                densityControlService.setBaseParam(densityControlConfig);
            }
        }
    }

    @Override
    public void clear() {
        cache.clear();
    }

    private String generateKey(String thingCode, String metricCode) {
        return thingCode + "-" + metricCode;
    }

}
