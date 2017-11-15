package com.zgiot.app.server.service.impl;

import com.zgiot.common.pojo.DataModel;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

/**
 * Created by chenting on 2017/10/21.
 */
public class CmdControlServiceImplTest {

    @Test
    public void sendSecond() throws Exception {
        testSendSecond(3);
    }

    @Test
    public void sendPulseCmdBoolByShortWhenIsHolding() throws Exception {
        // 正常写1
        testSendPulseCmdBoolByShort(3, "true", "1", "5", Boolean.TRUE, "0");
        // 异常写1
        testSendPulseCmdBoolByShort(3, "true", "5", "5", Boolean.TRUE, "0");
        // 正常写0
        testSendPulseCmdBoolByShort(3, "false", "5", "1", Boolean.TRUE, "0");
        // 异常写0
        testSendPulseCmdBoolByShort(3, "false", "1", "1", Boolean.TRUE, "0");
    }

    @Test
    public void sendPulseCmdBoolByShortWhenNoHolding() throws Exception {
        // 正常写1
        testSendPulseCmdBoolByShort(3, "true", "1", "5", Boolean.FALSE, "1");
        // 异常写1
        testSendPulseCmdBoolByShort(3, "true", "5", "5", Boolean.FALSE, "1");
        // 正常写0
        testSendPulseCmdBoolByShort(3, "false", "5", "1", Boolean.FALSE, "1");
        // 异常写0
        testSendPulseCmdBoolByShort(3, "false", "1", "1", Boolean.FALSE, "1");
    }

    @Test
    public void getDataSync() throws Exception {

    }

    @Test
    public void getBit() throws Exception {
        CmdControlServiceImpl cmdControlService = new CmdControlServiceImpl();
        boolean flagTrue = cmdControlService.getBit(151, 7, 1);
        Assert.assertEquals(false, flagTrue);
        boolean flagfalse = cmdControlService.getBit(151, 7, 0);
        Assert.assertEquals(true, flagfalse);
    }

    /**
     * sendPulseCmdBoolByShort测试公用方法
     * @param position 写入位置
     * @param value 写入值
     * @param readValue 读取到的值
     * @param sendValue 第一次发送值
     * @param isHolding 是否保持
     * @param cleanReadValue 第二次发送值
     */
    private void testSendPulseCmdBoolByShort(int position, String value , String readValue, String sendValue , boolean isHolding, String cleanReadValue) {
        DataModel dataModel = new DataModel();
        dataModel.setMetricCode("test");
        dataModel.setThingCode("test");
        dataModel.setValue(value);
        CmdControlServiceImpl test = Mockito.mock(CmdControlServiceImpl.class);
        Mockito.when(test.getDataSync(dataModel)).thenReturn(readValue);
        // 设置sendPulseCmdBoolByShort实际调用
        Mockito.doCallRealMethod().when(test).sendPulseCmdBoolByShort(dataModel, null, null, "",
                position, 3, isHolding);
        // 实际调用sendPulseCmdBoolByShort
        test.sendPulseCmdBoolByShort(dataModel, null, null, "",
                position, 3, isHolding);
        dataModel.setValue(sendValue);
        // 判断sendfirst方法是否按照预期调用
        verify(test, Mockito.times(1)).sendfirst(dataModel, "");
        if (!isHolding) {
            dataModel.setValue(cleanReadValue);
            // 判断sendSecond方法是否按照预期调用
            verify(test, Mockito.times(1)).sendSecond(dataModel, null,
                    null, "", 3);
        }
    }

    private void testSendSecond(Integer retryCount){
        DataModel dataModel = new DataModel();
        dataModel.setMetricCode("test");
        dataModel.setThingCode("test");
        dataModel.setValue("");
        List<DataModel> dataModelLists = new ArrayList<DataModel>();
        dataModelLists.add(dataModel);
        CmdControlServiceImpl test =Mockito.mock(CmdControlServiceImpl.class);
        Mockito.when(test.sendCmd(dataModelLists, "")).thenThrow(Exception.class);
        Mockito.doCallRealMethod().when(test).sendSecond(dataModel, null, retryCount, "", 100);
        test.sendSecond(dataModel, null, retryCount, "", 100);
    }
}