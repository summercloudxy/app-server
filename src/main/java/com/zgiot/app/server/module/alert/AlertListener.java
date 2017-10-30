package com.zgiot.app.server.module.alert;

import com.zgiot.app.server.dataprocessor.DataListener;
import com.zgiot.app.server.module.alert.handler.AlertFaultHandler;
import com.zgiot.app.server.module.alert.handler.AlertParamHandler;
import com.zgiot.app.server.module.alert.handler.AlertProtectHandler;
import com.zgiot.common.constants.AlertConstants;
import com.zgiot.common.pojo.DataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiayun on 2017/9/22.
 */
@Component
public class AlertListener implements DataListener {
    @Autowired
    private AlertFaultHandler faultHandler;
    @Autowired
    private AlertParamHandler paramHandler;
    @Autowired
    private AlertProtectHandler protectHandler;
    @Autowired
    private AlertManager alertManager;

    @Override
    public void onDataChange(DataModel dataModel) {
        Map<String, Short> metricAlertTypeMap = alertManager.getMetricAlertTypeMap();
        if (metricAlertTypeMap.containsKey(dataModel.getMetricCode())) {
            switch (metricAlertTypeMap.get(dataModel.getMetricCode())) {
                case AlertConstants.TYPE_FAULT:
                    faultHandler.check(dataModel);
                    break;
                case AlertConstants.TYPE_PARAM:
                    paramHandler.check(dataModel);
                    break;
                case AlertConstants.TYPE_PROTECT:
                    protectHandler.check(dataModel);
                    break;
            }

        }

    }

    @Override
    public void onError(Throwable error) {

    }
}
