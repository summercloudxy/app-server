package com.zgiot.app.server.com.zgiot.app.server.module.alert;

import com.zgiot.app.server.module.alert.AlertManager;
import com.zgiot.app.server.module.alert.handler.AlertFaultHandler;
import com.zgiot.app.server.module.alert.pojo.AlertData;
import com.zgiot.app.server.service.MetricService;
import com.zgiot.app.server.service.impl.DataServiceImpl;
import com.zgiot.app.server.service.impl.MetricServiceImpl;
import com.zgiot.app.server.service.impl.ThingServiceImpl;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.pojo.MetricModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlertTest {
    @MockBean
    ThingServiceImpl mockThingService;
    @MockBean
    DataServiceImpl mockDataService;
//    @MockBean
//    AlertManager mockAlertManager;
    @MockBean
    MetricServiceImpl mockMetricService;
    @Autowired
    AlertFaultHandler alertFaultHandler;
    @Autowired
    AlertManager alertManager;

    private Set<String> metricSetContainState(){
        return new HashSet<String>(){{
            add(MetricCodes.STATE);
            add(MetricCodes.RESET);
        }};
    }

    private Set<String> metricSetWithoutState(){
        return new HashSet<String>(){{
            add(MetricCodes.RESET);
        }};
    }

    private Optional<DataModelWrapper> runToFaultState(){
        DataModel dataModel =new DataModel();
        dataModel.setValue("4");
        dataModel.setValue("2");
        return Optional.of(new DataModelWrapper(dataModel));
    }

    private Optional<DataModelWrapper> stopToFaultState(){
        DataModel dataModel = new DataModel();
        dataModel.setValue("4");
        dataModel.setPreValue("1");
        return Optional.of(new DataModelWrapper(dataModel));
    }

    private DataModel faultData(){
        DataModel dataModel = new DataModel();
        dataModel.setValue(Boolean.TRUE.toString());
        dataModel.setThingCode("1301");
        dataModel.setMetricCode("故障");
        return dataModel;
    }

    private DataModel normalData(){
        DataModel dataModel = new DataModel();
        dataModel.setValue(Boolean.FALSE.toString());
        dataModel.setThingCode("1301");
        dataModel.setMetricCode("故障");
        return dataModel;
    }

    private MetricModel metricModel(){
        MetricModel metricModel = new MetricModel();
        metricModel.setMetricName("故障");
        return metricModel;
    }

    @Test
    public void testFaultHandle(){
        testReleaseFault();
        DataModel dataModel = faultData();
        testRunToFault(dataModel);
        AlertData runToFault
                = alertManager.getAlertDataByThingAndMetricCode(dataModel.getThingCode(), dataModel.getMetricCode());
        Short alertLevel = 30;
        assertThat(alertLevel.equals(runToFault.getAlertLevel())).isTrue();

        testReleaseFault();
        testStopToFault(dataModel);
        alertLevel =20;
        AlertData stopToFault = alertManager.getAlertDataByThingAndMetricCode(dataModel.getThingCode(), dataModel.getMetricCode());
        assertThat(alertLevel.equals(stopToFault.getAlertLevel())).isTrue();

        testReleaseFault();
        testWithOutState(dataModel);
        alertLevel =10;
        AlertData withoutStateFault = alertManager.getAlertDataByThingAndMetricCode(dataModel.getThingCode(), dataModel.getMetricCode());
        assertThat(alertLevel.equals(withoutStateFault.getAlertLevel())).isTrue();

    }


    public void testRunToFault(DataModel dataModel){
        given(mockThingService.findMetricsOfThing(dataModel.getThingCode())).willReturn(metricSetContainState());
        given(mockDataService.getData(dataModel.getThingCode(), MetricCodes.STATE)).willReturn(runToFaultState());
//        given(mockAlertManager.getAlertDataByThingAndMetricCode(dataModel.getThingCode(),dataModel.getMetricCode())).willReturn(null);
        given(mockMetricService.getMetric(dataModel.getMetricCode())).willReturn(metricModel());
        alertFaultHandler.check(dataModel);
    }

    public void testStopToFault(DataModel dataModel){
        given(mockThingService.findMetricsOfThing(dataModel.getThingCode())).willReturn(metricSetContainState());
        given(mockDataService.getData(dataModel.getThingCode(), MetricCodes.STATE)).willReturn(stopToFaultState());
        given(mockMetricService.getMetric(dataModel.getMetricCode())).willReturn(metricModel());
        alertFaultHandler.check(dataModel);
    }

    public void testWithOutState(DataModel dataModel){
        given(mockThingService.findMetricsOfThing(dataModel.getThingCode())).willReturn(metricSetWithoutState());
        given(mockDataService.getData(dataModel.getThingCode(), MetricCodes.STATE)).willReturn(stopToFaultState());
        given(mockMetricService.getMetric(dataModel.getMetricCode())).willReturn(metricModel());
        alertFaultHandler.check(dataModel);
    }

    public void testReleaseFault(){
        alertFaultHandler.check(normalData());
    }
}
