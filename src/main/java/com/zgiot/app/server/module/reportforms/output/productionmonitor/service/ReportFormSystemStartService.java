package com.zgiot.app.server.module.reportforms.output.productionmonitor.service;


import com.zgiot.app.server.module.reportforms.output.productionmonitor.dao.ReportFormSystemStartMapper;
import com.zgiot.app.server.module.reportforms.output.productionmonitor.listener.ReportFormSystemStartListener;
import com.zgiot.app.server.module.reportforms.output.productionmonitor.pojo.ReportFormSystemStartRecord;
import com.zgiot.app.server.module.reportforms.output.utils.ReportFormDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportFormSystemStartService {
    @Autowired
    private ReportFormSystemStartMapper reportFormSystemStartMapper;
    @Autowired
    private ReportFormSystemStartListener systemStartListener;
    @Autowired
    private ReportFormSystemStartMapper systemStartMapper;

    public void updateDescriptionAndReason(List<ReportFormSystemStartRecord> records) {
        for (ReportFormSystemStartRecord record : records) {
            updateCache(record);
            reportFormSystemStartMapper.updateRecord(record);
        }
    }

    private void updateCache(ReportFormSystemStartRecord record) {
        Map<Integer, List<ReportFormSystemStartRecord>> dutyRecords = systemStartListener.getDutyRecords();
        if (dutyRecords.containsKey(record.getTerm())) {
            List<ReportFormSystemStartRecord> termDutyRecords = dutyRecords.get(record.getTerm());
            for (ReportFormSystemStartRecord dutyRecord : termDutyRecords) {
                if (dutyRecord.getId() == record.getId()) {
                    dutyRecord.setProductionDescription(record.getProductionDescription());
                    dutyRecord.setReason(record.getReason());
                    return;
                }
            }
        }
    }

    public Map<Integer, List<ReportFormSystemStartRecord>> getSystemStartRecords(Date dutyStartTime) {
        Map<Integer, List<ReportFormSystemStartRecord>> systemStartRecords;
        if (ReportFormDateUtil.isCurrentDuty(dutyStartTime)) {
            systemStartRecords = systemStartListener.getDutyRecords();
        } else {
            systemStartRecords = new HashMap<>();
            systemStartRecords.put(1, systemStartMapper.getRecordsOnDuty(dutyStartTime, 1));
            systemStartRecords.put(2, systemStartMapper.getRecordsOnDuty(dutyStartTime, 2));
        }
        return systemStartRecords;
    }
}
