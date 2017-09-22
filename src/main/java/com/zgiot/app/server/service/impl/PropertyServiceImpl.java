package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.mapper.TMLMapper;
import com.zgiot.app.server.service.PropertyService;
import com.zgiot.common.pojo.ThingPropertyModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class PropertyServiceImpl implements PropertyService {

    private Map<String,ThingPropertyModel> propertyCache = new ConcurrentHashMap<>(50000);

    @Autowired
    private TMLMapper tmlMapper;

    @PostConstruct
    private void initCache(){
        List<ThingPropertyModel> allProperties = tmlMapper.findAllProperties();
        for(ThingPropertyModel property:allProperties){
            propertyCache.put(property.getThingCode() + "_" + property.getPropKey(),property);
        }
    }

    @Override
    public List<ThingPropertyModel> findThingProperties() {
        List<ThingPropertyModel> thingPropertyModelList = new ArrayList<>();
        thingPropertyModelList = tmlMapper.findAllProperties();
        return thingPropertyModelList;
    }

    @Override
    public List<ThingPropertyModel> findThingProperties(String thingCode,String[] propType) {
        List<ThingPropertyModel> thingPropertyModelList = new ArrayList<>();
        for(String propKey:propertyCache.keySet()){
            for(String type:propType){
                if(propKey.startsWith(thingCode + "_") && (propertyCache.get(propKey) != null)
                        && (StringUtils.isNotBlank(propertyCache.get(propKey).getPropType()))
                        && propertyCache.get(propKey).getPropType().equals(type)){
                    thingPropertyModelList.add(propertyCache.get(propKey));
                }
            }
        }
        return thingPropertyModelList;
    }
}
