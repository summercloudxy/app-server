package com.zgiot.app.server.module.sfmonitor.monitorservice;

import com.zgiot.app.server.module.sfmonitor.constants.SFMonitorConstant;
import com.zgiot.app.server.module.sfmonitor.controller.*;
import com.zgiot.app.server.module.sfmonitor.mapper.*;
import com.zgiot.app.server.module.sfmonitor.pojo.*;
import com.zgiot.app.server.service.impl.mapper.MetricTagRelationMapper;
import com.zgiot.app.server.service.impl.mapper.TMLMapper;
import com.zgiot.common.pojo.MetricModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.zgiot.app.server.module.sfmonitor.constants.SFMonitorConstant.*;

@Component
public class MonitorServiceImpl implements MonitorService {
    @Autowired
    private SFMonitorMapper sfMonitorMapper;
    @Autowired
    private RelSFMonitorItemMapper relSFMonitorItemMapper;
    @Autowired
    private SFMonEquipMonitorConfigMapper sfMonEquipMonitorConfigMapper;
    @Autowired
    private MetricTagRelationMapper metricTagRelationMapper;
    @Autowired
    private TMLMapper tmlMapper;
    @Autowired
    private SFMonEquipMonitorInfoMapper sfMonEquipMonitorInfoMapper;
    @Autowired
    private RelSFMonRolePermissionMapper relSFMonRolePermissionMapper;
    @Autowired
    private RelSFMonMetrictypeMetricMapper relSFMonMetrictypeMetricMapper;

    @Override
    public AddOrEditMonitorResponse addOrEditMonitorInfo(MonitorInfo monitorInfo) {
        SFMonitor sfMonitor = monitorInfo.getSfMonitor();
        List<RelSFMonItem> items = new ArrayList<>();
        List<Long> idList = new ArrayList<>();
        boolean isExist = true;
        Float count = null;
        Long id = null;
        if (sfMonitor != null && sfMonitor.getId() != null) {//edit
            MonitorEditRes monitorEditRes = editMonitor(monitorInfo);
            isExist = monitorEditRes.isExist();
            items = monitorEditRes.getItems();
            count = relSFMonitorItemMapper.getMaxSortFromMonitorByMonId(sfMonitor.getId());
            id = sfMonitor.getId();
        } else {//add
            SFMonitor monitor = sfMonitorMapper.getMonitorByName(monitorInfo.getSfMonitor().getSfMonName());
            if (monitor == null) {
                isExist = false;
            }
            id = addMonitor(monitorInfo);
        }

        if (!isExist) {
            relSFMonitorItemMapper.deleteRelRelSFMonitorItem(id);
            if ((count == null) || (count == 0f)) {
                count = 1f;
            }
            idList = saveMonitorItemInfo(count, items, monitorInfo, id);

        }
        AddOrEditMonitorResponse addOrEditMonitorResponse = new AddOrEditMonitorResponse();
        addOrEditMonitorResponse.setExsit(isExist);
        addOrEditMonitorResponse.setMonitorId(id);
        addOrEditMonitorResponse.setItems(idList);
        return addOrEditMonitorResponse;
    }

    private Long addMonitor(MonitorInfo monitorInfo) {
        SFMonitor sfMonitor = monitorInfo.getSfMonitor();
        Float sort = sfMonitorMapper.getMaxSortMonitor();
        if (sort == null) {
            sort = 1f;
        }
        sfMonitor.setSort(sort + 1);
        sfMonitorMapper.addMonitor(sfMonitor);
        return sfMonitor.getId();
    }

    private MonitorEditRes editMonitor(MonitorInfo monitorInfo) {
        SFMonitor sfMonitor = monitorInfo.getSfMonitor();
        SFMonitor monitorTemp = sfMonitorMapper.getMonitorByName(monitorInfo.getSfMonitor().getSfMonName());
        boolean isExist = true;
        List<RelSFMonItem> items = new ArrayList<>();
        if ((monitorTemp != null) && (monitorTemp.getId().longValue() == sfMonitor.getId().longValue())) {
            isExist = false;
        } else {
            SFMonitor monitor = sfMonitorMapper.getMonitorById(sfMonitor.getId());
            sfMonitor.setSort(monitor.getSort());
            sfMonitorMapper.editMonitor(sfMonitor.getSfMonName(), sfMonitor.getSort(), sfMonitor.getId());
            items = relSFMonitorItemMapper.getRelSFMonitorItem(sfMonitor.getId());
        }

        MonitorEditRes monitorEditRes = new MonitorEditRes();
        monitorEditRes.setExist(isExist);
        monitorEditRes.setItems(items);

        return monitorEditRes;
    }

    private List<Long> saveMonitorItemInfo(Float count, List<RelSFMonItem> items, MonitorInfo monitorInfo, Long id) {
        List<RelSFMonItem> relSFMonItems = monitorInfo.getRelSFMonItems();
        List<Long> idList = new ArrayList<>();
        if (relSFMonItems.size() > 0) {
            for (RelSFMonItem relSFMonItem : relSFMonItems) {
                editMonitorItem(items, relSFMonItem);
                if (relSFMonItem.getSort() == null || relSFMonItem.getSort() == 0f) {
                    count = count + 1;
                    relSFMonItem.setSort(count);
                }
                relSFMonItem.setSfMonId(id);
                relSFMonitorItemMapper.addRelSFMonitorItem(relSFMonItem);
                idList.add(relSFMonItem.getId());
            }
        }

        return idList;
    }

    private void editMonitorItem(List<RelSFMonItem> items, RelSFMonItem relSFMonItem) {
        if (items != null && items.size() > 0) {//edit
            for (RelSFMonItem item : items) {
                if (item.getThingCode().equals(relSFMonItem.getThingCode()) && item.getMetricCode().equals(relSFMonItem.getMetricCode())) {
                    relSFMonItem.setSort(item.getSort());
                    break;
                }
            }
        }
    }


    @Override
    @Transactional
    public void editEquipmentMonitorInfo(EquipmentRelateToSignalWrapperReq equipmentRelateToSignalWrapperReq) {
        String thingCode = equipmentRelateToSignalWrapperReq.getThingCode();
        String editor = equipmentRelateToSignalWrapperReq.getEditor();
        List<String> keyChannels = equipmentRelateToSignalWrapperReq.getKeyChannels();
        SFMonEquipMonitorConfig sfMonEquipMonitorConfig = null;
        sfMonEquipMonitorConfig = new SFMonEquipMonitorConfig();
        //保存页面”关键通道”数据
        if (keyChannels != null && keyChannels.size() != 0) {
            List<SFMonEquipMonitorConfig> keyChannelEquipments = createEquipmentMonitorConfig(keyChannels, thingCode, SFMonitorConstant.KEY_CHANNEL);
            sfMonEquipMonitorConfig.setThingCode(thingCode);
            sfMonEquipMonitorConfig.setKey(SFMonitorConstant.KEY_CHANNEL);
            sfMonEquipMonitorConfigMapper.deleteEquipmentConfig(sfMonEquipMonitorConfig);
            saveEquipmentMonitorConfigInfo(keyChannelEquipments);
        }
        //保存页面“自”设备数据
        List<String> fromEquipments = equipmentRelateToSignalWrapperReq.getFromEquipments();
        if (fromEquipments != null && fromEquipments.size() != 0) {
            List<SFMonEquipMonitorConfig> fromEquipmentInfos = createEquipmentMonitorConfig(fromEquipments, thingCode, SFMonitorConstant.FROM);
            sfMonEquipMonitorConfig.setKey(SFMonitorConstant.FROM);
            sfMonEquipMonitorConfigMapper.deleteEquipmentConfig(sfMonEquipMonitorConfig);
            saveEquipmentMonitorConfigInfo(fromEquipmentInfos);
        }
        //保存页面“至”设备数据
        List<String> toEquipments = equipmentRelateToSignalWrapperReq.getToEquipments();
        if (toEquipments != null && toEquipments.size() != 0) {
            List<SFMonEquipMonitorConfig> toEquipmentInfos = createEquipmentMonitorConfig(toEquipments, thingCode, SFMonitorConstant.TO);
            sfMonEquipMonitorConfig.setKey(SFMonitorConstant.TO);
            sfMonEquipMonitorConfigMapper.deleteEquipmentConfig(sfMonEquipMonitorConfig);
            saveEquipmentMonitorConfigInfo(toEquipmentInfos);
        }
        //保存页面“同”设备数据
        List<String> similarEquipments = equipmentRelateToSignalWrapperReq.getSimilarEquipments();
        if (similarEquipments != null && similarEquipments.size() != 0) {
            List<SFMonEquipMonitorConfig> similarEquipmentInfos = createEquipmentMonitorConfig(similarEquipments, thingCode, SFMonitorConstant.SIMILAR);
            sfMonEquipMonitorConfig.setKey(SFMonitorConstant.SIMILAR);
            sfMonEquipMonitorConfigMapper.deleteEquipmentConfig(sfMonEquipMonitorConfig);
            saveEquipmentMonitorConfigInfo(similarEquipmentInfos);
        }
        //保存页面“参数选择区”数据
        List<String> selectedparameters = equipmentRelateToSignalWrapperReq.getSelectedparameters();
        if (selectedparameters != null && selectedparameters.size() != 0) {
            List<SFMonEquipMonitorConfig> selectedparameterInfos = createEquipmentMonitorConfig(selectedparameters, thingCode, SFMonitorConstant.SELECTED_PARAMETER);
            sfMonEquipMonitorConfig.setKey(SFMonitorConstant.SELECTED_PARAMETER);
            sfMonEquipMonitorConfigMapper.deleteEquipmentConfig(sfMonEquipMonitorConfig);
            saveEquipmentMonitorConfigInfo(selectedparameterInfos);
        }
        //保存页面“辅助操作区”设备数据
        List<EquipmentRelateToSignalWrapper> equipmentRelateToSignalWrappers = equipmentRelateToSignalWrapperReq.getEquipmentRelateToSignalWrappers();
        List<String> relateEquipments = tmlMapper.findRelateThing(thingCode + SFMonitorConstant.FUZZY_QUERY_TAG);
        for (String code : relateEquipments) {
            sfMonEquipMonitorConfig.setThingCode(code);
            sfMonEquipMonitorConfig.setKey(SFMonitorConstant.AUXILIARY_AREA);
            sfMonEquipMonitorConfigMapper.deleteEquipmentConfig(sfMonEquipMonitorConfig);
        }
        //保存页面“状态控制操作区”数据
        List<SFMonEquipMonitorConfig> auxiliaryAreaInfos = createAuxiliaryAreaEquipmentMonitorConfig(equipmentRelateToSignalWrappers, SFMonitorConstant.AUXILIARY_AREA);
        saveEquipmentMonitorConfigInfo(auxiliaryAreaInfos);
        StateControlAreaInfo stateControlAreaInfo = equipmentRelateToSignalWrapperReq.getStateControlAreaInfo();
        List<SFMonEquipMonitorConfig> stateControlInfo = createStateControlAreaEquipmentMonitorConfig(stateControlAreaInfo, thingCode, SFMonitorConstant.STATE_AREA);
        sfMonEquipMonitorConfig.setThingCode(thingCode);
        sfMonEquipMonitorConfig.setKey(SFMonitorConstant.STATE_AREA);
        sfMonEquipMonitorConfigMapper.deleteEquipmentConfig(sfMonEquipMonitorConfig);
        sfMonEquipMonitorConfig.setKey(SFMonitorConstant.STATE_AREA_FIND);
        sfMonEquipMonitorConfigMapper.deleteEquipmentConfig(sfMonEquipMonitorConfig);
        for (SFMonEquipMonitorConfig stateZoneData : stateControlInfo) {
            stateZoneData.setSelected(true);
        }
        saveEquipmentMonitorConfigInfo(stateControlInfo);
        SFMonEquipMonitorInfo sfMonEquipMonitorInfo = new SFMonEquipMonitorInfo();
        sfMonEquipMonitorInfo.setThingCode(thingCode);
        sfMonEquipMonitorInfo.setConfigProgress(SFMonitorConstant.COMPLETED_CONFIG);
        sfMonEquipMonitorInfo.setEditor(editor);
        sfMonEquipMonitorInfo.setCreateDate(new Date());
        sfMonEquipMonitorInfoMapper.updateEquipmonitorInfo(sfMonEquipMonitorInfo);
    }

    private List<SFMonEquipMonitorConfig> createEquipmentMonitorConfig(List<String> thingOrMetricCodes, String thingCode, String equipmentType) {
        List<SFMonEquipMonitorConfig> sfMonEquipMonitorConfigs = new ArrayList<>();
        for (String code : thingOrMetricCodes) {
            SFMonEquipMonitorConfig sfMonEquipMonitorConfig = new SFMonEquipMonitorConfig();
            sfMonEquipMonitorConfig.setThingCode(thingCode);
            sfMonEquipMonitorConfig.setKey(equipmentType);
            sfMonEquipMonitorConfig.setValue(code);
            sfMonEquipMonitorConfigs.add(sfMonEquipMonitorConfig);
        }
        return sfMonEquipMonitorConfigs;
    }

    private List<SFMonEquipMonitorConfig> createAuxiliaryAreaEquipmentMonitorConfig(List<EquipmentRelateToSignalWrapper> equipmentRelateToSignalWrappers, String equipmentType) {
        List<SFMonEquipMonitorConfig> sfMonEquipMonitorConfigs = new ArrayList<>();
        for (EquipmentRelateToSignalWrapper code : equipmentRelateToSignalWrappers) {
            List<SignalWrapperMetric> signalWrapperMetrics = code.getSignalWrapperMetrics();
            if ((signalWrapperMetrics != null) && (signalWrapperMetrics.size() > 0)) {
                for (SignalWrapperMetric metric : signalWrapperMetrics) {
                    SFMonEquipMonitorConfig sfMonEquipMonitorConfig = new SFMonEquipMonitorConfig();
                    sfMonEquipMonitorConfig.setThingCode(code.getThingCode());
                    sfMonEquipMonitorConfig.setMetricTagName(code.getWarpperName());
                    sfMonEquipMonitorConfig.setKey(equipmentType);
                    sfMonEquipMonitorConfig.setValue(metric.getMetricName());
                    sfMonEquipMonitorConfig.setModel(metric.getModel());
                    sfMonEquipMonitorConfig.setSelected(code.isSelected());
                    sfMonEquipMonitorConfigs.add(sfMonEquipMonitorConfig);
                }
            }
        }
        return sfMonEquipMonitorConfigs;
    }


    private List<SFMonEquipMonitorConfig> createStateControlAreaEquipmentMonitorConfig(StateControlAreaInfo stateControlAreaInfo, String thingCode, String equipmentType) {
        List<SFMonEquipMonitorConfig> sfMonEquipMonitorConfigs = new ArrayList<>();
        String wrapperName = stateControlAreaInfo.getWrapperName();
        int model = stateControlAreaInfo.getModel();

        //添加数据，用来查看状态控制区数据
        SFMonEquipMonitorConfig sfMonEquipMonitorConfig = new SFMonEquipMonitorConfig();
        sfMonEquipMonitorConfig.setThingCode(thingCode);
        sfMonEquipMonitorConfig.setKey(SFMonitorConstant.STATE_AREA_FIND);
        sfMonEquipMonitorConfig.setMetricTagName(wrapperName);
        sfMonEquipMonitorConfig.setModel(model);
        sfMonEquipMonitorConfigs.add(sfMonEquipMonitorConfig);

        //查找状态控制区metricName,状态控制包中既有参数类信号点也有控制类信号点，
        //参数类控制点默认都有查看权限，控制类信号有查看和操作权限，由页面用户选择决定
        sfMonEquipMonitorConfigs.addAll(getAllStateControlAreaMetric(wrapperName, model, thingCode, equipmentType));

        return sfMonEquipMonitorConfigs;
    }

    private void saveEquipmentMonitorConfigInfo(List<SFMonEquipMonitorConfig> equipmentInfos) {
        for (SFMonEquipMonitorConfig sfMonEquipMonitorConfig : equipmentInfos) {
            sfMonEquipMonitorConfigMapper.addEquipmentMonitorInfo(sfMonEquipMonitorConfig);
        }
    }

    private List<SFMonEquipMonitorConfig> getAllStateControlAreaMetric(String wrapperName, int model, String thingCode, String equipmentType) {
        List<SFMonEquipMonitorConfig> sfMonEquipMonitorConfigs = new ArrayList<>();
        List<String> metricCodes = metricTagRelationMapper.getAllMetric(wrapperName);
        List<MetricModel> metricModels = tmlMapper.findMetric(thingCode);
        for (MetricModel metric : metricModels) {
            if (metricCodes.contains(metric.getMetricCode())) {
                SFMonEquipMonitorConfig sfMonEquipMonitorConfig = new SFMonEquipMonitorConfig();
                sfMonEquipMonitorConfig.setThingCode(thingCode);
                sfMonEquipMonitorConfig.setKey(equipmentType);
                sfMonEquipMonitorConfig.setMetricTagName(wrapperName);
                sfMonEquipMonitorConfig.setValue(metric.getMetricName());
                if ((metric.getMetricType1Code().equals(SFMonitorConstant.PARAMETER_SET) || metric.getMetricType1Code().equals(SFMonitorConstant.STATE_SET))
                        && (model == SFMonitorConstant.OPERATE)) {
                    sfMonEquipMonitorConfig.setModel(SFMonitorConstant.OPERATE);
                } else if (model == SFMonitorConstant.FIND) {
                    sfMonEquipMonitorConfig.setModel(SFMonitorConstant.FIND);
                } else if (model == SFMonitorConstant.NO_SHOW) {
                    sfMonEquipMonitorConfig.setModel(SFMonitorConstant.NO_SHOW);
                }
                sfMonEquipMonitorConfigs.add(sfMonEquipMonitorConfig);
            }
        }

        return sfMonEquipMonitorConfigs;
    }

    @Override
    @Transactional
    public void deleteEquipmentConfig(int id) {
        SFMonEquipMonitorInfo sfMonEquipMonitorInfo = sfMonEquipMonitorInfoMapper.getEquiupmentInfoById(id);
        sfMonEquipMonitorInfoMapper.deleteEquipmentBaseInfo(id);
        SFMonEquipMonitorConfig sfMonEquipMonitorConfig = new SFMonEquipMonitorConfig();
        List<String> relateThings = tmlMapper.findRelateThing(sfMonEquipMonitorInfo.getThingCode() + SFMonitorConstant.FUZZY_QUERY_TAG);
        for (String thingCode : relateThings) {
            sfMonEquipMonitorConfig.setThingCode(thingCode);
            sfMonEquipMonitorConfigMapper.deleteEquipmentConfig(sfMonEquipMonitorConfig);
        }
    }


    @Override
    public Map<String, Boolean> getWrapperMatchRule(String zoneCode) {
        Map<String, Boolean> map = new HashMap<>();
        List<SignalWrapperMatchRule> signalWrapperMatchRules = sfMonEquipMonitorConfigMapper.getWrapperMatchRule(zoneCode);
        for (SignalWrapperMatchRule rule : signalWrapperMatchRules) {
            map.put(rule.getSignalWrapperName(), rule.isAllMatch());
        }
        return map;
    }

    @Override
    public Map<String, Object> byUser(String userUuid) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> thingMap = new HashMap<>();
        List<String> thingCodeList = new ArrayList<>();
        List<RelSFMonRolePermission> relSFMonRolePermissionList = relSFMonRolePermissionMapper.getRelSFMonRolePermissionByUser(userUuid);
        if (relSFMonRolePermissionList != null && !relSFMonRolePermissionList.isEmpty()) {
            for (RelSFMonRolePermission relSFMonRolePermission : relSFMonRolePermissionList) {
                Map<String, Boolean> permissionMap = new HashMap<>();
                permissionMap.put(METRICTYPE_VIEW, relSFMonRolePermission.isOpView());
                permissionMap.put(METRICTYPE_SS, relSFMonRolePermission.isOpStartstop());
                permissionMap.put(METRICTYPE_CTL, relSFMonRolePermission.isOpControl());

                String thingCode = relSFMonRolePermission.getThingCode();
                thingCodeList.add(thingCode);
                if (thingMap.get(thingCode) != null) {
                    Map<String, Boolean> maxAuthMap = getMaxAuth(permissionMap, (Map<String, Boolean>) thingMap.get(thingCode));
                    thingMap.put(thingCode, maxAuthMap);
                    continue;
                }
                thingMap.put(thingCode, permissionMap);
            }
        }

        map.put(THINGAUTHZ, thingMap);

        Map<String, Object> metricTypeMap = new HashMap<>();
        List<RelSFMonMetrictypeMetric> relSFMonMetrictypeMetricList =
                relSFMonMetrictypeMetricMapper.getRelSFMonMetrictypeMetricByThingCode(thingCodeList);
        if (relSFMonMetrictypeMetricList != null && !relSFMonMetrictypeMetricList.isEmpty()) {
            for (RelSFMonMetrictypeMetric relSFMonMetrictypeMetric : relSFMonMetrictypeMetricList) {
                metricTypeMap.put(relSFMonMetrictypeMetric.getMetricCode(), relSFMonMetrictypeMetric.getSfmonMetrictype());
            }
        }

        map.put(METRICTYPERELATION, metricTypeMap);

        return map;
    }

    /**
     * 取最大权限
     *
     * @param map1
     * @param map2
     * @return
     */
    private Map<String, Boolean> getMaxAuth(Map<String, Boolean> map1, Map<String, Boolean> map2) {
        Map<String, Boolean> map = new HashMap<>();
        map.put(METRICTYPE_VIEW, map1.get(METRICTYPE_VIEW) || map2.get(METRICTYPE_VIEW));
        map.put(METRICTYPE_SS, map1.get(METRICTYPE_SS) || map2.get(METRICTYPE_SS));
        map.put(METRICTYPE_CTL, map1.get(METRICTYPE_CTL) || map2.get(METRICTYPE_CTL));
        return map;
    }

}
