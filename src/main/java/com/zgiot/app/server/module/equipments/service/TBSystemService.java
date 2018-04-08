package com.zgiot.app.server.module.equipments.service;

import com.zgiot.app.server.module.equipments.pojo.TBSystem;

import java.util.List;

public interface TBSystemService {

    /**
     * 系统-列表-根据level查询
     * @param level
     * @return
     */
    List<TBSystem> getTBSystemByLevel(int level);

    /**
     * 系统-列表-根据父id查询
     * @param id
     * @return
     */
    List<TBSystem> getSystemByParentId(Long id);

}
