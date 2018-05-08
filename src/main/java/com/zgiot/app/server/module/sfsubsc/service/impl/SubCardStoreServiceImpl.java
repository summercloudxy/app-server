package com.zgiot.app.server.module.sfsubsc.service.impl;

import com.zgiot.app.server.module.sfsubsc.entity.pojo.SubscCardStore;
import com.zgiot.app.server.module.sfsubsc.mapper.SubCardStoreMapper;
import com.zgiot.app.server.module.sfsubsc.service.SubCardStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCardStoreServiceImpl implements SubCardStoreService {

    @Autowired
    private SubCardStoreMapper subCardStoreMapper;

    @Override
    public List<SubscCardStore> getAllSubscCardStore() {
        return subCardStoreMapper.getAllSubscCardStore();
    }

}
