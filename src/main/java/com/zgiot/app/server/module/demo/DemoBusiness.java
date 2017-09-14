package com.zgiot.app.server.module.demo;

import com.zgiot.app.server.dataprocessor.DataListener;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.ThingService;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.pojo.ThingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class DemoBusiness implements DataListener {
    public static final String STATUS_NORMAL = "NOR";
    public static final String STATUS_TOO_HIGH = "HIG";
    public static final String THING_CATEGORY_DEVICE = "DEV";
    public static final Float THRESHOLD_BAD = Float.valueOf(10f);

    @Autowired
    private DataService dataService;

    @Autowired
    private ThingService thingService;

    /**
     * Value less or eq than 10, is 'NORMAL'
     * Once the thing is device and value greater than 10, would be 'HIGH'.
     * Thing is not a device and value greater than 10, would be 'NORMAL'
     *
     * @param thingCode
     * @param metricCode
     * @return
     */
    public String doCalStatus(String thingCode, String metricCode) {
        String destStatus = STATUS_NORMAL;

        DataModelWrapper data = this.dataService.getData(thingCode, metricCode);
        ThingModel thing = this.thingService.getThing(thingCode);

        Float valueF = Float.valueOf(data.getValue());

        if (THING_CATEGORY_DEVICE.equals(thing.getThingCategoryCode())
                && valueF > THRESHOLD_BAD) {
            destStatus = STATUS_TOO_HIGH;
        }

        return destStatus;
    }

    @Override
    public void onDataChange(DataModel dataModel) {
        // cal new status via new data value
        String sValue = doCalStatus(dataModel.getThingCode(), dataModel.getMetricCode());
        DataModel sData = new DataModel(ThingModel.CATEGORY_DEVICE
                , dataModel.getThingCode(), dataModel.getMetricCategoryCode()
                , "NEW_STATUS", sValue, dataModel.getDataTimeStamp()
        );

        // save new status to cache
        this.dataService.updateCache(sData);

        // save to history data
        // ...
    }

    @Override
    public void onError(Throwable error) {

    }
}
