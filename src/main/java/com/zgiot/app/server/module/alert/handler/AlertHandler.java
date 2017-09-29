package com.zgiot.app.server.module.alert.handler;

import com.zgiot.common.pojo.DataModel;

/**
 * Created by xiayun on 2017/9/25.
 */
public interface AlertHandler {
    void check(DataModel dataModel);
}
