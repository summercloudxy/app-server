package com.zgiot.app.server.module.equipments.service.impl;

import com.zgiot.app.server.module.equipments.mapper.TBSystemMapper;
import com.zgiot.app.server.module.equipments.pojo.TBSystem;
import com.zgiot.app.server.module.equipments.service.TBSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TBSystemServiceImpl implements TBSystemService {


    @Autowired
    private TBSystemMapper tbSystemMapper;
    @Override
    public List<TBSystem> getTBSystemALL(int level) {
        return tbSystemMapper.getTBSystemALL(level);
    }
}
