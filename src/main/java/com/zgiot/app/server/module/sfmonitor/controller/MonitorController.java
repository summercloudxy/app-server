package com.zgiot.app.server.module.sfmonitor.controller;
 
import com.alibaba.fastjson.JSON; 
import com.zgiot.app.server.module.sfmonitor.mapper.RelSFMonitorItemMapper;
import com.zgiot.app.server.module.sfmonitor.mapper.SFMonitorMapper;
import com.zgiot.app.server.module.sfmonitor.monitorService.MonitorService;
import com.zgiot.app.server.module.sfmonitor.pojo.RelSFMonItem;
import com.zgiot.app.server.module.sfmonitor.pojo.SFMonitor;
import com.zgiot.app.server.service.impl.mapper.TMLMapper; 
import com.zgiot.common.constants.GlobalConstants; 
import com.zgiot.common.constants.MetricTypes; 
import com.zgiot.common.exceptions.SysException; 
import com.zgiot.common.pojo.MetricModel; 
import com.zgiot.common.restcontroller.ServerResponse; 
import org.apache.commons.lang.StringUtils; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity; 
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.*; 
 
import java.util.ArrayList; 
import java.util.List; 
 
@Controller 
@RequestMapping(value = GlobalConstants.API  + GlobalConstants.API_VERSION + "/sfmonitor") 
public class MonitorController { 
 
    @Autowired 
    private TMLMapper tmlMapper; 
 
    @Autowired 
    private SFMonitorMapper sfMonitorMapper; 
 
    @Autowired 
    private RelSFMonitorItemMapper relSFMonitorItemMapper; 
 
    @Autowired 
    private MonitorService monitorService; 
 
    @RequestMapping("/metrics/thing/{thingCode}") 
    public ResponseEntity<String> getMetrics(@PathVariable String thingCode){ 
        List<MetricModel> metricModels = tmlMapper.findMetricByThingCode(thingCode, MetricTypes.PR); 
        return new ResponseEntity<>(ServerResponse.buildOkJson(metricModels),HttpStatus.OK); 
    } 
 
    @RequestMapping("/userId/{userId}") 
    public ResponseEntity<String> getMonitor(@PathVariable long userId){ 
        List<SFMonitor> monitors = sfMonitorMapper.getMonitorByUserId(userId); 
        return new ResponseEntity<>(ServerResponse.buildOkJson(monitors),HttpStatus.OK); 
    } 
 
    @RequestMapping("/userName/{userName}") 
    public ResponseEntity<String> getUserMonitorWrapper(@PathVariable String userName){ 
        List<MonitorWrapper> monitorWrappers = new ArrayList<>(); 
        List<SFMonitor> monitors = sfMonitorMapper.getMonitorByUserName(userName); 
        if(monitors != null && monitors.size() > 0){ 
            for(SFMonitor sfMonitor:monitors){ 
                MonitorWrapper monitorWrapper = new MonitorWrapper(); 
                monitorWrapper.setSfMonitor(sfMonitor); 
                List<MonitorItemInfo> monitorItemInfos = relSFMonitorItemMapper.getRelSFMonitorItemByMonId(sfMonitor.getId());
                monitorWrapper.setMonitorItemInfos(monitorItemInfos);
                int count = relSFMonitorItemMapper.getEquipmentCount(sfMonitor.getId()); 
                monitorWrapper.setEquipmentCount(count);
                monitorWrappers.add(monitorWrapper);
            } 
        } 
 
        return new ResponseEntity<>(ServerResponse.buildOkJson(monitorWrappers),HttpStatus.OK); 
    } 
 
    @RequestMapping(value = "/monitorItems/{monId}",method = RequestMethod.GET) 
    public ResponseEntity<String> getMonitorItems(@PathVariable long monId){ 
        List<MonitorItemInfo> monitorItemInfos = relSFMonitorItemMapper.getRelSFMonitorItemByMonId(monId);
        if(monitorItemInfos == null || monitorItemInfos.size() == 0 ){
            List<RelSFMonItem> relSFMonItems =  relSFMonitorItemMapper.getRelSFMonitorItem(monId);
            for(RelSFMonItem relSFMonItem:relSFMonItems){
                MonitorItemInfo monitorItemInfo = new MonitorItemInfo();
                monitorItemInfo.setRelSFMonItem(relSFMonItem);
                monitorItemInfos = new ArrayList<>();
                monitorItemInfos.add(monitorItemInfo);
            }
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(monitorItemInfos),HttpStatus.OK); 
    }
 
 
    @RequestMapping(value="/addOrEditMonitorInfo",method= RequestMethod.POST) 
    public ResponseEntity<String> addOrEditMonitorInfo(@RequestBody String bodyStr){ 
        MonitorInfo monitorInfo = JSON.parseObject(bodyStr,MonitorInfo.class); 
        if(monitorInfo == null){ 
            ServerResponse res = new ServerResponse<>( 
                    "monitorInfo is empty" , SysException.EC_UNKNOWN, 0); 
            String resJson = JSON.toJSONString(res); 
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST); 
        } 
        boolean isExist = true;
        AddOrEditMonitorResponse addOrEditMonitorResponse = new AddOrEditMonitorResponse();
        if((monitorInfo.getSfMonitor() != null) && (!StringUtils.isBlank(monitorInfo.getSfMonitor().getSfMonName()))){
            addOrEditMonitorResponse = monitorService.addOrEditMonitorInfo(monitorInfo);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(addOrEditMonitorResponse),HttpStatus.OK);
    } 
 
    @RequestMapping(value = "/monitorItem/setTop/monitor/{monId}/item/{id}",method=RequestMethod.POST) 
    public ResponseEntity<String> setMonitorItemTop(@PathVariable("monId") long monId,@PathVariable("id") long id){ 
        RelSFMonItem relSFMonItem = relSFMonitorItemMapper.getRelSFMonitorItemById(id); 
        if(relSFMonItem != null){ 
            Float sort = relSFMonitorItemMapper.getMinSortFromMonitorByMonId(monId);
            if(sort == null){
                sort = 1f;
            }
            sort = sort/2; 
            relSFMonItem.setSort(sort); 
            relSFMonitorItemMapper.modifyMonItem(id,sort);
        } 
        return new ResponseEntity<>(ServerResponse.buildOkJson(null),HttpStatus.OK); 
    } 
 
    @RequestMapping(value = "/setTop/monitor/{monId}",method=RequestMethod.POST) 
    public ResponseEntity<String> setMonitorTop(@PathVariable("monId") long monId){ 
        SFMonitor sfMonitor = sfMonitorMapper.getMonitorById(monId); 
        if(sfMonitor != null){ 
            Float sort = sfMonitorMapper.getMinSortMonitor();
            if(sort == null){
                sort = 1f;
            }
            sort = sort/2; 
            sfMonitor.setSort(sort); 
            sfMonitorMapper.editMonitor(sfMonitor.getSfMonName(),sort,monId);
        } 
        return new ResponseEntity<>(ServerResponse.buildOkJson(null),HttpStatus.OK); 
    } 
 
    @RequestMapping(value = "/monitorItem/deleteMonitorItem/{id}",method = RequestMethod.DELETE) 
    public ResponseEntity<String> deleteMonitorItem(@PathVariable long id){ 
        relSFMonitorItemMapper.deleteMetricMonitor(id); 
        return new ResponseEntity<>(ServerResponse.buildOkJson(null),HttpStatus.OK); 
    } 
 
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE) 
    public ResponseEntity<String> deleteMonitor(@PathVariable long id){ 
        relSFMonitorItemMapper.deleteRelRelSFMonitorItem(id); 
        sfMonitorMapper.deleteMonitor(id); 
        return new ResponseEntity<>(ServerResponse.buildOkJson(null),HttpStatus.OK); 
    } 
 
    @GetMapping(value = "/equipmentCount/{sfMonId}") 
    public ResponseEntity<String> getEquipmentCount(@PathVariable long sfMonId){ 
        return new ResponseEntity<>(ServerResponse.buildOkJson(relSFMonitorItemMapper.getEquipmentCount(sfMonId)),HttpStatus.OK); 
    } 
} 