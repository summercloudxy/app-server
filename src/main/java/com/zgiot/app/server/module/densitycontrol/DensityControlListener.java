package com.zgiot.app.server.module.densitycontrol;

import com.zgiot.app.server.dataprocessor.DataListener;
import com.zgiot.app.server.module.densitycontrol.handle.AddingMediumHandler;
import com.zgiot.app.server.module.densitycontrol.handle.BackFlowHandler;
import com.zgiot.app.server.module.densitycontrol.handle.NotifyAlertHandler;
import com.zgiot.app.server.module.densitycontrol.pojo.MonitoringParam;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.DataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DensityControlListener implements DataListener {

    @Autowired
    private AddingMediumHandler addingMediumHandler;
    @Autowired
    private BackFlowHandler backFlowHandler;
    @Autowired
    private NotifyAlertHandler notifyAlertHandler;

    private final Logger logger = LoggerFactory.getLogger(DensityControlListener.class);


    @Override
    public void onDataChange(DataModel dataModel) {

        switch (dataModel.getMetricCode()) {
            case MetricCodes.DENSITY_CONTROL_NOTIFY_ADD_MEDIUM:
                if (Boolean.valueOf(dataModel.getValue())) {
                    MonitoringParam monitoringParam = new MonitoringParam(dataModel.getThingCode());
                    addingMediumHandler.dispose(monitoringParam);
                } else {
                    addingMediumHandler.cancel(dataModel.getThingCode());
                }
                break;
            case MetricCodes.DENSTIY_CONTROL_NOTIFY_BACK_FLOW:
                if (Boolean.valueOf(dataModel.getValue())) {
                    MonitoringParam monitoringParam = new MonitoringParam(dataModel.getThingCode());
                    backFlowHandler.dispose(monitoringParam);
                } else {
                    backFlowHandler.cancel(dataModel.getThingCode());
                }
                break;
            case MetricCodes.DENSITY_CONTROL_NOTIFY_HIGH_DENSTIY_ALERT:
                if (Boolean.valueOf(dataModel.getValue())) {
                    MonitoringParam monitoringParam = new MonitoringParam(dataModel.getThingCode());
                    notifyAlertHandler.dispose(monitoringParam);
                } else {
                    notifyAlertHandler.cancel(dataModel.getThingCode());
                }
                break;
            default:
                break;
        }

    }



    @Override
    public void onError(Throwable error) {
        logger.error("data invalid", error);
    }
}
