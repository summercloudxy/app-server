package com.zgiot.app.server.module.equipments.service.impl;

import com.zgiot.app.server.module.equipments.mapper.ThingTagManagementMapper;
import com.zgiot.app.server.module.equipments.pojo.ThingTag;
import com.zgiot.app.server.module.equipments.service.ThingTagManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThingTagManagementServiceImpl implements ThingTagManagementService {
    @Autowired
    private ThingTagManagementMapper thingTagMapper;

    @Override
    public List<ThingTag> getAllEquipmentType() {
        return thingTagMapper.getAllEquipmentType();
    }

    @Override
    public List<ThingTag> getThingTagByParentId(Long id) {
        return thingTagMapper.getThingTagByParentId(id);
    }

    @Override
    public ThingTag getMenu(Long id, int level) {
        level--;
        // 当前节点
        ThingTag thingTag = thingTagMapper.getThingTagById(id);
        if(level <= 0){
            return thingTag;
        }
        // 子节点
        List<ThingTag> thingTagList = thingTagMapper.getThingTagByParentId(id);
        if (thingTagList != null && thingTagList.size() > 0) {
            for (int i = 0; i < thingTagList.size(); i++) {
                ThingTag tt = getMenu(thingTagList.get(i).getId(), level);// 递归
                // 将子节点添加至父节点list中
                thingTag.getNodeList().add(tt);
            }
        }
        return thingTag;
    }

}
