package com.zgiot.app.server.module.sfstop.service.impl;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopSysParameter;
import com.zgiot.app.server.module.sfstop.mapper.StopSysParameterMapper;
import com.zgiot.app.server.module.sfstop.service.StopSysParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StopSysParameterServiceImpl implements StopSysParameterService {

    @Autowired
    private StopSysParameterMapper stopSysParameterMapper;

    @Override
    public List<StopSysParameter> getStopSysParameterList() {
        List<StopSysParameter> stopSysParameterList = stopSysParameterMapper.getStopSysParameterList();
        return stopSysParameterList;
    }
}
