package com.zgiot.app.server.module.equipments.service;

import com.zgiot.app.server.module.equipments.pojo.ThingTag;

import java.util.List;

public interface ThingTagManagementService {

    /**
     * 获取所有设备类型
     * @return
     */
    public List<ThingTag> getAllEquipmentType();

    /**
     * 根据父节点id获取
     * @param id
     * @return
     */
    public List<ThingTag> getThingTagByParentId(Long id);

    /**
     * 获取菜单
     * @param id
     * @return
     */
    public ThingTag getMenu(Long id);

}
