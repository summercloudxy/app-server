package com.zgiot.app.server.com.zgiot.app.server.module.alert;

import com.zgiot.app.server.module.alert.handler.AlertFaultHandler;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlertTest {

    @MockBean
    DataService mockDataService;
    @Autowired
    AlertFaultHandler alertFaultHandler;

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

    private Optional<DataModelWrapper> otherToFaultState(){
        DataModel dataModel = new DataModel();
        dataModel.setValue("4");
        dataModel.setPreValue("3");
        return Optional.of(new DataModelWrapper(dataModel));
    }

    private Optional<DataModelWrapper> nullState(){
        return Optional.empty();
    }

    private DataModel faultData(){
        DataModel dataModel = new DataModel();
        dataModel.setValue(Boolean.TRUE.toString());
        dataModel.setThingCode("1301");
        dataModel.setMetricCode("故障");
        return dataModel;
    }

//    private DataModel normalData(){
//        DataModel dataModel = new DataModel();
//        dataModel.setValue(Boolean.FALSE.toString());
//        dataModel.setThingCode("1301");
//        dataModel.setMetricCode("故障");
//        return dataModel;
//    }
//
//    private MetricModel metricModel(){
//        MetricModel metricModel = new MetricModel();
//        metricModel.setMetricName("故障");
//        return metricModel;
//    }

    @Test
    public void testFaultHandle() throws Exception{
        Short level = testRunToFault(faultData());
        Short alertLevel = 30;
        assertThat(alertLevel.equals(level)).isTrue();

        alertLevel =20;
        level = testStopToFault(faultData());
        assertThat(alertLevel.equals(level)).isTrue();

        alertLevel =10;
        level = testWithOutState(faultData());
        assertThat(alertLevel.equals(level)).isTrue();

        alertLevel = 30;
        level = testNullState(faultData());
        assertThat(alertLevel.equals(level)).isTrue();

        level = testOtherToFault(faultData());
        assertThat(alertLevel.equals(level)).isTrue();
    }


    public Short testRunToFault(DataModel dataModel) throws Exception{
        given(mockDataService.getData(dataModel.getThingCode(), MetricCodes.STATE)).willReturn(runToFaultState());
        Method testGetLevel = alertFaultHandler.getClass().getDeclaredMethod("getAlertLevel", String.class,Set.class);
        testGetLevel.setAccessible(true);
        return  (Short) testGetLevel.invoke(alertFaultHandler, dataModel.getThingCode(),metricSetContainState());
    }

    public Short testStopToFault(DataModel dataModel)  throws Exception{
        given(mockDataService.getData(dataModel.getThingCode(), MetricCodes.STATE)).willReturn(stopToFaultState());
        Method testGetLevel = alertFaultHandler.getClass().getDeclaredMethod("getAlertLevel", String.class,Set.class);
        testGetLevel.setAccessible(true);
        return (Short) testGetLevel.invoke(alertFaultHandler, dataModel.getThingCode(),metricSetContainState());
    }


    public Short testOtherToFault(DataModel dataModel) throws Exception{
        given(mockDataService.getData(dataModel.getThingCode(), MetricCodes.STATE)).willReturn(otherToFaultState());
        Method testGetLevel = alertFaultHandler.getClass().getDeclaredMethod("getAlertLevel", String.class,Set.class);
        testGetLevel.setAccessible(true);
        return (Short) testGetLevel.invoke(alertFaultHandler, dataModel.getThingCode(),metricSetContainState());
    }

    public Short testNullState(DataModel dataModel) throws Exception{
        given(mockDataService.getData(dataModel.getThingCode(), MetricCodes.STATE)).willReturn(nullState());
        Method testGetLevel = alertFaultHandler.getClass().getDeclaredMethod("getAlertLevel", String.class,Set.class);
        testGetLevel.setAccessible(true);
        return (Short) testGetLevel.invoke(alertFaultHandler, dataModel.getThingCode(),metricSetContainState());
    }

    public Short testWithOutState(DataModel dataModel) throws Exception{
        given(mockDataService.getData(dataModel.getThingCode(), MetricCodes.STATE)).willReturn(stopToFaultState());
        Method testGetLevel = alertFaultHandler.getClass().getDeclaredMethod("getAlertLevel", String.class,Set.class);
        testGetLevel.setAccessible(true);
        return  (Short) testGetLevel.invoke(alertFaultHandler, dataModel.getThingCode(),metricSetWithoutState());
    }
//
//    public void testReleaseFault(){
//        alertFaultHandler.check(normalData());
//    }
}
