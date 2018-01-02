package com.zgiot.app.server.module.generic;

import com.zgiot.app.server.service.ThingService;
import com.zgiot.common.pojo.*;
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

    /**
     * /thing/{thingCode}接口返回json值的name属性名
     */
    public static final String THING_NAME = "name";

    /**
     * /thing/{thingCode}接口返回json值的shortName属性名
     */
    public static final String THING_SHORT_NAME = "shortName";

    /**
     * /thing/{thingCode}接口返回json值的base属性名
     */
    public static final String BASE = "base";

    /**
     * /thing/{thingCode}接口返回json值的prop属性名
     */
    public static final String PROP = "prop";

    /**
     * /thing/{thingCode}接口返回json值的disProp属性名
     */
    public static final String DIS_PROP = "disProp";

    public static final String THING_CODE = "thingCode";

    public static final String THING_CATEGORY_CODE = "thingCategoryCode";

    @GetMapping(value = "/{thingCode}")
    public ResponseEntity<String> getThing(@PathVariable String thingCode) {
        ThingModel tm = thingService.getThing(thingCode);
        return new ResponseEntity<>(ServerResponse.buildOkJson(tm), HttpStatus.OK);
    }

    @GetMapping("/properties/{thingCode}")
    public ResponseEntity<String> findThingProperties(@PathVariable String thingCode) {
        Map<String, Map<String, String>> thingPropMap = new LinkedHashMap<>();
        List<ThingPropertyModel> thingPropertyModels = new ArrayList<>();
        if (StringUtils.isNotBlank(thingCode)) {
            ThingModel thingModel = thingService.getThing(thingCode);
            String[] propType = new String[]{ThingPropertyModel.PROP_TYPE_PROP, ThingPropertyModel.PROP_TYPE_DISP_PROP};
            thingPropertyModels = thingService.findThingProperties(thingCode, propType);
            Map<String, String> baseThingMap = new LinkedHashMap<>();
            Map<String, String> propMap = new LinkedHashMap<>();
            Map<String, String> disPropMap = new LinkedHashMap<>();

            baseThingMap.put(THING_NAME, thingModel.getThingName());
            baseThingMap.put(THING_SHORT_NAME, thingModel.getShortName());
            baseThingMap.put(THING_CODE, thingModel.getThingCode());
            parsePropertiesByType(thingPropertyModels, propMap, disPropMap);
            thingPropMap.put(BASE, baseThingMap);
            thingPropMap.put(PROP, propMap);
            thingPropMap.put(DIS_PROP, disPropMap);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(thingPropMap), HttpStatus.OK);
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
                base.put(THING_NAME, baseProperty.getThingName());
                base.put(THING_SHORT_NAME, baseProperty.getShortName());
                base.put(THING_CODE, baseProperty.getThingCode());
                String[] propType =
                        new String[]{ThingPropertyModel.PROP_TYPE_PROP, ThingPropertyModel.PROP_TYPE_DISP_PROP};
                thingPropertyModels = thingService.findThingProperties(baseProperty.getThingCode(), propType);
                parsePropertiesByType(thingPropertyModels, prop, disProp);
                thingMap.put(BASE, base);
                thingMap.put(PROP, prop);
                thingMap.put(DIS_PROP, disProp);
                things.add(thingMap);
            }
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(things), HttpStatus.OK);
    }

    private void parsePropertiesByType(List<ThingPropertyModel> base, Map<String, String> propMap,
            Map<String, String> dispPropMap) {
        if (base.size() > 0) {
            for (ThingPropertyModel model : base) {
                if (model.getPropType().equals(ThingPropertyModel.PROP_TYPE_PROP)) {
                    propMap.put(model.getPropKey(), model.getPropValue());
                } else if (model.getPropType().equals(ThingPropertyModel.PROP_TYPE_DISP_PROP)) {
                    dispPropMap.put(model.getPropKey(), model.getPropValue());
                }
            }
        }
    }

    @GetMapping("/building")
    public ResponseEntity<String> findAllBuilding() {
        List<BuildingModel> buildingModels = thingService.findAllBuilding();
        return new ResponseEntity<>(ServerResponse.buildOkJson(buildingModels), HttpStatus.OK);
    }

    @GetMapping("/system")
    public ResponseEntity<String> findAllSystem() {
        List<SystemModel> systemModels = thingService.findAllSystem();
        return new ResponseEntity<>(ServerResponse.buildOkJson(systemModels), HttpStatus.OK);
    }

    @GetMapping("/category/asset")
    public ResponseEntity<String> getCategoryListByAssetType(String assetType) {
        List<CategoryModel> result = thingService.getCategoryListByAssetType(assetType);
        return new ResponseEntity<>(ServerResponse.buildOkJson(result), HttpStatus.OK);
    }

    @GetMapping("/category")
    public ResponseEntity<String> getCategoryList() {
        List<CategoryModel> result = thingService.getCategoryList();
        return new ResponseEntity<>(ServerResponse.buildOkJson(result), HttpStatus.OK);
    }

    @GetMapping("/metricType/assetAndCategory")
    public ResponseEntity<String> getMetricTypeByAssetAndCategory(String assetType, String category, String thingCode) {
        List<CategoryModel> result = thingService.getMetricTypeByAssetAndCategory(assetType, category, thingCode);
        return new ResponseEntity<>(ServerResponse.buildOkJson(result), HttpStatus.OK);
    }

    @GetMapping("/metric/assetAndCategory")
    public ResponseEntity<String> getMetricByAssetAndCategory(String assetType, String category, String thingCode,
            String metricType) {
        List<MetricModel> result = thingService.getMetricByAssetAndCategory(assetType, category, thingCode, metricType);
        return new ResponseEntity<>(ServerResponse.buildOkJson(result), HttpStatus.OK);
    }

    @GetMapping("/thing/assetAndCategory")
    public ResponseEntity<String> getThingCodeByAssetAndCategory(String assetType, String category, String metricCode,
            String metricType) {
        List<ThingModel> result =
                thingService.getThingCodeByAssetAndCategory(assetType, category, metricCode, metricType);
        return new ResponseEntity<>(ServerResponse.buildOkJson(result), HttpStatus.OK);
    }

}
