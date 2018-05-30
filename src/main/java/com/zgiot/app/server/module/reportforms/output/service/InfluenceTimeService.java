package com.zgiot.app.server.module.reportforms.output.service;

import com.zgiot.app.server.module.reportforms.output.pojo.InfluenceTimeBean;
import com.zgiot.app.server.module.reportforms.output.pojo.InfluenceTimeRemarks;
import com.zgiot.app.server.module.reportforms.output.pojo.InfluenceTimeReq;
import com.zgiot.app.server.module.reportforms.output.productionmonitor.pojo.ReportFormSystemStartRecord;

import java.util.Date;
import java.util.List;

public interface InfluenceTimeService {

    void influenceTimeService(InfluenceTimeReq influenceTimeReq);

    void handle(List<ReportFormSystemStartRecord> reportList);

    InfluenceTimeBean getData(Date date);

    void updatePersonnel(InfluenceTimeRemarks influenceTimeRemarks);
}
                                                  