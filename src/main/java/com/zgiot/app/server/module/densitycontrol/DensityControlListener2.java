package com.zgiot.app.server.module.densitycontrol;

import com.zgiot.app.server.dataprocessor.DataListener;
import com.zgiot.common.pojo.DataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.zgiot.app.server.module.densitycontrol.constants.DensitycontrolConstants.TERM_TWO_THING_CODE;
import static com.zgiot.common.constants.MetricCodes.CURRENT_LEVEL_M;

@Component
public class DensityControlListener2 implements DataListener {

    private final Logger logger = LoggerFactory.getLogger(DensityControlListener2.class);
    @Autowired
    private DensityControlManager2 densityControlManager2;

    @Override
    public void onDataChange(DataModel dataModel) {
        if (TERM_TWO_THING_CODE.equals(dataModel.getThingCode()) && CURRENT_LEVEL_M.equals(dataModel.getMetricCode())) {
            if (!densityControlManager2.isRunning()) {
                // 当前液位
                densityControlManager2.handleLevel(dataModel);
            }
        }
    }

    @Override
    public void onError(Throwable error) {
        logger.error("data invalid", error);
    }

}
