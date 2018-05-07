package com.zgiot.app.server.module.sfmedium.service;

import com.zgiot.app.server.module.sfmedium.entity.po.MediumCompoundingConfigDO;

public interface MediumCompoundingService {

    MediumCompoundingConfigDO getMediumCompoundingConfigByPoolCode(String mediumPoolCode);
}
