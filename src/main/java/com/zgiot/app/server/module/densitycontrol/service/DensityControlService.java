package com.zgiot.app.server.module.densitycontrol.service;

import com.zgiot.app.server.module.densitycontrol.dto.DensityControlDTO;
import com.zgiot.app.server.module.densitycontrol.pojo.DensityControlConfig;
import com.zgiot.common.pojo.DataModel;

import java.util.List;

public interface DensityControlService {

    List<DensityControlConfig> getAllDensityControlConfig();

    void updateDensityControlConfig(DensityControlDTO densityControlDTO);

    void switchDensityControl(DataModel dataModel);

    void setBaseParam(DensityControlConfig densityControlConfig);
}
