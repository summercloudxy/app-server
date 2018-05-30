package com.zgiot.app.server.module.reportforms.output.dao;

import com.zgiot.app.server.module.reportforms.output.pojo.InfluenceTime;
import com.zgiot.app.server.module.reportforms.output.pojo.InfluenceTimeRemarks;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface InfluenceTimeMapper {
    List<InfluenceTime> getInfluenceTimeByDutyDate(Date nowDutyStartTime);

    InfluenceTimeRemarks getRemarksByDutyDate(Date nowDutyStartTime);

    List<InfluenceTime> getMostNewinfluenceTimeByDate(Date nowDutyStartTime);

    void createinfluenceTime(InfluenceTime influenceTime);

    void editinfluenceTimeRemarks(InfluenceTimeRemarks influenceTimeRemarks);

    void createinfluenceTimeRemarks(InfluenceTimeRemarks influenceTimeRemarks);

    void editinfluenceTime(InfluenceTime influenceTime);

    void createPersonnel(InfluenceTimeRemarks influenceTimeRemarks);

    void editPersonnel(InfluenceTimeRemarks influenceTimeRemarks);

    InfluenceTimeRemarks InfluenceTimeRemarks(Date dutyStartTime);
}
