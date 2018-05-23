package com.zgiot.app.server.module.reportforms.output.service;

import com.zgiot.app.server.module.reportforms.output.dao.DutyLogMapper;
import com.zgiot.app.server.module.reportforms.output.pojo.DutyLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class DutyLogService{

    @Autowired
    private DutyLogMapper dutyLogMapper;

    /**
     * 通过工作时间，获取调度队
     * @param workTime
     * @return
     */
    public DutyLog getWhichTeam(Date workTime){
        return dutyLogMapper.getWhichTeam(workTime);
    }
}
