package com.zgiot.app.server.module.equipments.service.impl;

import com.zgiot.app.server.module.equipments.mapper.TBSystemMapper;
import com.zgiot.app.server.module.equipments.pojo.TBSystem;
import com.zgiot.app.server.module.equipments.service.TBSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TBSystemServiceImpl implements TBSystemService {

    @Autowired
    private TBSystemMapper tbSystemMapper;

    @Override
    public List<TBSystem> getTBSystemByLevel(int level) {
        return tbSystemMapper.getTBSystemALL(level);
    }

    @Override
    public List<TBSystem> getSystemByParentId(Long id) {
        return tbSystemMapper.getSystemByParentId(id);
    }

    @Override
    public List<TBSystem> getSystemById(Long id) {
        List<TBSystem> tbSystemList = new ArrayList<>();
        tbSystemList.add(tbSystemMapper.getSystemById(id));
        return tbSystemList;
    }

}
