package com.zgiot.app.server.module.sfmonitor.monitorservice;

import com.zgiot.app.server.module.sfmonitor.pojo.ThingTag;
import org.springframework.stereotype.Component;

@Component
public interface SFSysMonitorThingTagService {
    /**
     * 获取菜单
     *
     * @param id
     * @return
     */
    public ThingTag getMenu(Long id);
}
