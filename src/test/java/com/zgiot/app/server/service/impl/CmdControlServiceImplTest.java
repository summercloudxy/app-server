package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.common.pojo.DataModel;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by chenting on 2017/10/21.
 */
public class CmdControlServiceImplTest {
    @Test
    public void getBit() throws Exception {
        CmdControlServiceImpl cmdControlService = new CmdControlServiceImpl();
        boolean flagTrue = cmdControlService.getBit(151, 7, 1);
        Assert.assertEquals(false, flagTrue);
        boolean flagfalse = cmdControlService.getBit(151, 7, 0);
        Assert.assertEquals(true, flagfalse);
    }
}