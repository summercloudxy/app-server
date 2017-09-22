package com.zgiot.app.server.module.generic;

import com.zgiot.app.server.service.PropertyService;
import com.zgiot.app.server.service.ThingService;
import com.zgiot.common.pojo.ThingModel;
import com.zgiot.common.pojo.ThingPropertyModel;
import com.zgiot.common.restcontroller.ServerResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/thing")
public class ThingController {
    @Autowired
    private ThingService thingService;

    @Autowired
    private PropertyService propertyService;

    @GetMapping("/{thingCode}")
    public ResponseEntity<String> getThing(@PathVariable String thingCode) {
        ThingModel tm = thingService.getThing(thingCode);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(tm)
                , HttpStatus.OK);
    }

    @GetMapping("/properties/{thingCode}")
    public ResponseEntity<String> findThingProperties(@PathVariable String thingCode) {
        Map<String, Map<String, String>> thingPropMap = new LinkedHashMap<>();
        List<ThingPropertyModel> thingPropertyModels = new ArrayList<>();
        if (StringUtils.isNotBlank(thingCode)) {
            ThingModel thingModel = thingService.getThing(thingCode);
            String[] propType = new String[]{ThingPropertyModel.PROP_TYPE_BASE, ThingPropertyModel.PROP_TYPE_DISP};
            thingPropertyModels = propertyService.findThingProperties(thingCode, propType);
            Map<String, String> baseThingMap = new LinkedHashMap<>();
            Map<String, String> propMap = new LinkedHashMap<>();
            Map<String, String> disPropMap = new LinkedHashMap<>();

            baseThingMap.put(ThingModel.THING_NAME, thingModel.getThingName());
            baseThingMap.put(ThingModel.THING_SHORT_NAME, thingModel.getShortName());
            if (thingPropertyModels.size() > 0) {
                for (ThingPropertyModel model : thingPropertyModels) {
                    if (model.getPropType().equals(ThingPropertyModel.PROP_TYPE_BASE)) {
                        propMap.put(model.getPropKey(), model.getPropValue());
                    } else if (model.getPropType().equals(ThingPropertyModel.PROP_TYPE_DISP)) {
                        disPropMap.put(model.getPropKey(), model.getPropValue());
                    }
                }
            }
            thingPropMap.put(ThingModel.BASE, baseThingMap);
            thingPropMap.put(ThingModel.PROP, propMap);
            thingPropMap.put(ThingModel.DIS_PROP, disPropMap);
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(thingPropMap)
                , HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<String> findAllThing() {
        List<ThingModel> baseThings = thingService.findAllThing();
        Map<String, Map<String, String>> thingMap = null;
        Map<String, String> base = null;
        Map<String, String> prop = null;
        Map<String, String> disProp = null;
        List<Map<String, Map<String, String>>> things = new ArrayList<>();
        List<ThingPropertyModel> thingPropertyModels = null;

        if (baseThings.size() > 0) {
            for (ThingModel baseProperty : baseThings) {
                base = new LinkedHashMap<>();
                prop = new LinkedHashMap<>();
                disProp = new LinkedHashMap<>();
                thingMap = new LinkedHashMap<>();
                base.put(ThingModel.THING_NAME, baseProperty.getThingName());
                base.put(ThingModel.THING_SHORT_NAME, baseProperty.getShortName());
                String[] propType = new String[]{ThingPropertyModel.PROP_TYPE_BASE, ThingPropertyModel.PROP_TYPE_DISP};
                thingPropertyModels = propertyService.findThingProperties(baseProperty.getThingCode(), propType);
                if (thingPropertyModels.size() > 0) {
                    for (ThingPropertyModel model : thingPropertyModels) {
                        if (model.getPropType().equals(ThingPropertyModel.PROP_TYPE_BASE)) {
                            prop.put(model.getPropKey(), model.getPropValue());
                        } else if (model.getPropType().equals(ThingPropertyModel.PROP_TYPE_DISP)) {
                            disProp.put(model.getPropKey(), model.getPropValue());
                        }
                    }
                }
                thingMap.put(ThingModel.BASE, base);
                thingMap.put(ThingModel.PROP, prop);
                thingMap.put(ThingModel.DIS_PROP, disProp);
                things.add(thingMap);
            }
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(things)
                , HttpStatus.OK);
    }
}
