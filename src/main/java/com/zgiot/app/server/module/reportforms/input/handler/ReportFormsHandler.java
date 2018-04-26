package com.zgiot.app.server.module.reportforms.input.handler;

import com.zgiot.app.server.module.reportforms.input.manager.ReportFormsManager;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.ReportFormsRecord;

import java.util.List;

public interface ReportFormsHandler {
    List<DataModel> handle(ReportFormsManager manager,ReportFormsRecord record);
    boolean isMatch(ReportFormsRecord record);
}
