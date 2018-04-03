package com.zgiot.app.server.module.equipments.service;

import com.zgiot.app.server.module.equipments.pojo.TBSystem;

import java.util.List;

public interface TBSystemService {

    List<TBSystem> getTBSystemALL(int level);

    List<TBSystem> getSystemByParentId(Long id);

}
