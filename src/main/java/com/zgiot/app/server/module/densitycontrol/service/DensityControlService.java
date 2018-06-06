package com.zgiot.app.server.module.densitycontrol.service;

import com.zgiot.app.server.module.densitycontrol.pojo.DensityControlConfig;
import com.zgiot.common.pojo.DataModel;

import java.util.List;

public interface DensityControlService {

    List<DensityControlConfig> getAllDensityControlConfig();

    void updateDensityControlConfig(List<DensityControlConfig> densityControlConfigList);

    void switchDensityControl(DataModel dataModel);

    void setBaseParam(DensityControlConfig densityControlConfig);

    void setTargetDensity(DataModel dataModel);
}
