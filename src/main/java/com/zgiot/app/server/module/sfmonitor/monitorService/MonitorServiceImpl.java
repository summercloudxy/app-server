package com.zgiot.app.server.module.sfmonitor.monitorService;

import com.zgiot.app.server.module.sfmonitor.controller.AddOrEditMonitorResponse;
import com.zgiot.app.server.module.sfmonitor.controller.MonitorInfo;
import com.zgiot.app.server.module.sfmonitor.mapper.RelSFMonitorItemMapper;
import com.zgiot.app.server.module.sfmonitor.mapper.SFMonitorMapper;
import com.zgiot.app.server.module.sfmonitor.pojo.RelSFMonItem;
import com.zgiot.app.server.module.sfmonitor.pojo.SFMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MonitorServiceImpl implements MonitorService {
    @Autowired
    private SFMonitorMapper sfMonitorMapper;
    @Autowired
    private RelSFMonitorItemMapper relSFMonitorItemMapper;

    @Override
    public AddOrEditMonitorResponse addOrEditMonitorInfo(MonitorInfo monitorInfo) {
        SFMonitor sfMonitor = monitorInfo.getSfMonitor();
        List<RelSFMonItem> relSFMonItems = monitorInfo.getRelSFMonItems();
        List<RelSFMonItem> items = null;
        List<Long> idList = new ArrayList<>();
        SFMonitor monitorTemp = sfMonitorMapper.getMonitorByName(monitorInfo.getSfMonitor().getSfMonName());
        boolean isExist = true;
        if(sfMonitor != null && sfMonitor.getId() != null){//edit
            if(monitorTemp != null && sfMonitor.getId() == sfMonitor.getId()){
                isExist = false;
            }else{
                SFMonitor monitor = sfMonitorMapper.getMonitorById(sfMonitor.getId());
                sfMonitor.setSort(monitor.getSort());
                sfMonitorMapper.editMonitor(sfMonitor.getSfMonName(),sfMonitor.getSort(),sfMonitor.getId());
                items = relSFMonitorItemMapper.getRelSFMonitorItem(sfMonitor.getId());
            }
        }else{//add
            if(monitorTemp == null){
                isExist = false;
            }
            Float sort = sfMonitorMapper.getMaxSortMonitor();
            if(sort == null){
                sort = 1f;
            }
            sfMonitor.setSort(sort);
            sfMonitorMapper.addMonitor(sfMonitor);
        }

        if(!isExist){
            relSFMonitorItemMapper.deleteRelRelSFMonitorItem(sfMonitor.getId());
            Float count = relSFMonitorItemMapper.getMaxSortFromMonitorByMonId(sfMonitor.getId());
            if(count == null){
                count = 1f;
            }
            if(relSFMonItems.size() > 0){
                for(RelSFMonItem relSFMonItem:relSFMonItems){
                    if(items != null && items.size() > 0){//edit
                        for(RelSFMonItem item:items){
                            if(item.getThingCode().equals(relSFMonItem.getThingCode()) && item.getMetricCode().equals(relSFMonItem.getMetricCode())){
                                relSFMonItem.setSort(item.getSort());
                                break;
                            }
                        }
                    }
                    if(relSFMonItem.getSort() == null){
                        count = count + 1;
                        relSFMonItem.setSort(count);
                    }
                    relSFMonItem.setSfMonId(sfMonitor.getId());
                    relSFMonitorItemMapper.addRelSFMonitorItem(relSFMonItem);
                    idList.add(relSFMonItem.getId());
                }
            }

        }
        AddOrEditMonitorResponse addOrEditMonitorResponse = new AddOrEditMonitorResponse();
        addOrEditMonitorResponse.setExsit(isExist);
        addOrEditMonitorResponse.setMonitorId(sfMonitor.getId());
        addOrEditMonitorResponse.setItems(idList);
        return addOrEditMonitorResponse;
    }
}
