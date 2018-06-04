package com.zgiot.app.server.module.densitycontrol;

import com.zgiot.app.server.module.densitycontrol.service.ParamCache;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.zgiot.app.server.module.densitycontrol.constants.DensitycontrolConstants.RUNS;
import static com.zgiot.app.server.module.densitycontrol.constants.DensitycontrolConstants.TERM_TWO_SYSTEM_A_THING_CODE;
import static com.zgiot.app.server.module.densitycontrol.constants.DensitycontrolConstants.TERM_TWO_SYSTEM_B_THING_CODE;
import static com.zgiot.common.constants.MetricCodes.SETTED_HIGH_LEVEL;
import static com.zgiot.common.constants.MetricCodes.SETTED_LOW_LEVEL;

@Component
public class DensityControlManager2 {

    @Autowired
    private CmdControlService cmdControlService;
    @Autowired
    private DataService dataService;
    @Autowired
    private ParamCache paramCache;

    /**
     * 液位变化处理
     * @param dataModel
     */
    public void handleLevel(DataModel dataModel) {
        String thingCode = dataModel.getThingCode();
        Double value = Double.valueOf(dataModel.getValue());

        String levelSetHigh = paramCache.getValue(thingCode, SETTED_HIGH_LEVEL).getValue();
        String levelSetLow = paramCache.getValue(thingCode, SETTED_LOW_LEVEL).getValue();

        DataModelWrapper data = getDensityData();

        if (value > Double.parseDouble(levelSetHigh)) {
            // 高液位处理

        } else if (value < Double.parseDouble(levelSetLow)) {
            // 低液位处理

        } else {
            // 正常液位处理

        }
    }

    /**
     * 获取密度
     * @return
     */
    private DataModelWrapper getDensityData() {
        DataModelWrapper dataA = dataService.getData(TERM_TWO_SYSTEM_A_THING_CODE, RUNS).orElse(null);
        String runsA = "";
        if (dataA != null) {
            runsA = dataA.getValue();
        }
        DataModelWrapper dataB = dataService.getData(TERM_TWO_SYSTEM_B_THING_CODE, RUNS).orElse(null);
        String runsB = "";
        if (dataB != null) {
            runsB = dataB.getValue();
        }


        return null;
    }

}
