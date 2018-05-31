package com.zgiot.app.server.module.sfstop.service.impl;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopChoiceSet;
import com.zgiot.app.server.module.sfstop.mapper.StopChoiceSetMapper;
import com.zgiot.app.server.module.sfstop.service.StopChoiceSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StopChoiceSetServiceImpl implements StopChoiceSetService {
    @Autowired
    private StopChoiceSetMapper stopChoiceSetMapper;

    @Override
    public void saveStopChoiceSet(StopChoiceSet stopChoiceSet) {
        stopChoiceSetMapper.saveStopChoiceSet(stopChoiceSet);
    }
}
