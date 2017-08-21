package com.zgiot.app.server.com.zgiot.app.server.module.demo;

import com.zgiot.app.server.module.demo.DemoBusiness;
import com.zgiot.app.server.service.DataService;
import com.zgiot.app.server.service.ThingService;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.DataModelWrapper;
import com.zgiot.common.pojo.ThingModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoBusinessTest {

    @MockBean
    private DataService mockDataService;

    @MockBean
    private ThingService mockThingService;

    @Autowired
    private DemoBusiness demoBusiness;

    private ThingModel buildThingMedel() {
        ThingModel mockThing = new ThingModel();
        mockThing.setCategoryCode(DemoBusiness.THING_CATEGORY_DEVICE);
        mockThing.setCode("");
        mockThing.setName("name1");
        return mockThing;
    }

    @Test
    public void testCalStatusBAD() {
        /* Prepare test data */
        String mockThingCode = "1048";
        String mockMetricCode = "CUR";
        Float mockValue = 11f;

        /* do test */
        String status = doTest(DemoBusiness.THING_CATEGORY_DEVICE, mockThingCode, mockMetricCode, mockValue);

        /* assertion */
        assertThat(DemoBusiness.STATUS_TOO_HIGH.equals(status)).isTrue();
    }

    @Test
    public void testCalStatusOK1() {
        /* Prepare test data */
        String mockThingCode = "1048";
        String mockMetricCode = "CUR";
        Float mockValue = 9f;

        /* do test */
        String status = doTest(DemoBusiness.THING_CATEGORY_DEVICE, mockThingCode, mockMetricCode, mockValue);

        /* assertion */
        assertThat(DemoBusiness.STATUS_NORMAL.equals(status)).isTrue();
    }

    @Test
    public void testCalStatusOK2() {
        /* Prepare test data */
        String mockThingCode = "1048";
        String mockMetricCode = "CUR";
        Float mockValue = 11f;

        /* do test */
        String status = doTest("other", mockThingCode, mockMetricCode, mockValue);

        /* assertion */
        assertThat(DemoBusiness.STATUS_NORMAL.equals(status)).isTrue();
    }

    private String doTest(String thingCate, String mockThingCode, String mockMetricCode, Float mockValue) {
        ThingModel mockThing = buildThingMedel();
        mockThing.setCategoryCode(thingCate);
        mockThing.setCode(mockThingCode);
        given(mockThingService.getThing(mockThingCode)).willReturn(mockThing);

        DataModel mockData = new DataModel();
        mockData.setThingCode(mockThingCode);
        mockData.setMetricCode(mockMetricCode);
        mockData.setValue(mockValue);

        DataModelWrapper wrapper = new DataModelWrapper(mockData);
        given(mockDataService.getData(mockThing.getCode(), mockMetricCode)).willReturn(wrapper);

        /* do test */
        return this.demoBusiness.doCalStatus(mockThingCode, mockMetricCode);
    }


}
