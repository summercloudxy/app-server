package com.zgiot.app.server.module.reportforms.handler;

import com.zgiot.app.server.module.reportforms.manager.ReportFormsManager;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.ReportFormsRecord;

import java.util.List;

public interface ReportFormsHandler {
    List<DataModel> handle(ReportFormsManager manager,ReportFormsRecord record);
    boolean isMatch(ReportFormsRecord record);
}
