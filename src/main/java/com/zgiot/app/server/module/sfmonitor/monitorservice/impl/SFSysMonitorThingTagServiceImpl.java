package com.zgiot.app.server.module.sfmonitor.monitorservice.impl;

import com.zgiot.app.server.module.sfmonitor.mapper.SFSysMonitorThingTagMapper;
import com.zgiot.app.server.module.sfmonitor.monitorservice.SFSysMonitorThingTagService;
import com.zgiot.app.server.module.sfmonitor.pojo.ThingTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SFSysMonitorThingTagServiceImpl implements SFSysMonitorThingTagService {
    @Autowired
    private SFSysMonitorThingTagMapper thingTagMapper;

    @Override
    public ThingTag getMenu(Long id) {
        // 当前节点
        ThingTag thingTag = thingTagMapper.getThingTagById(id);
        // 子节点
        List<ThingTag> thingTagList = thingTagMapper.getThingTagByParentId(id);
        if (thingTagList != null && thingTagList.size() > 0) {
            for (ThingTag aThingTagList : thingTagList) {
                ThingTag tt = getMenu(aThingTagList.getId());// 递归
                // 将子节点添加至父节点list中
                thingTag.getNodeList().add(tt);
            }
        }
        return thingTag;
    }
}
