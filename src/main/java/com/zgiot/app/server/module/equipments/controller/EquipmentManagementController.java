package com.zgiot.app.server.module.equipments.controller;

import com.zgiot.app.server.module.equipments.pojo.Building;
import com.zgiot.app.server.module.equipments.service.BuildingService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(value = GlobalConstants.API  + GlobalConstants.API_VERSION + "/equipments")
public class EquipmentManagementController {

    @Autowired
    private BuildingService buildingService;

    @RequestMapping(value = "/building/list",method = RequestMethod.GET)
    public ResponseEntity<String> getBuildingList(){
        List<Building> buildingList = buildingService.getBuildingAll();
        return new ResponseEntity<>(ServerResponse.buildOkJson(buildingList), HttpStatus.OK);
    }

    @RequestMapping(value = "/building/add",method = RequestMethod.POST)
    public ResponseEntity<String> addBuilding(@RequestBody Building building){
        buildingService.addBuilding(building);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(value = "/building/edit/{id}",method = RequestMethod.PUT)
    public ResponseEntity<String> editBuilding(@RequestBody Building building,@PathVariable("id") Long id){
        building.setId(id);
        buildingService.editBuilding(building);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(value = "/building/delete/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<String> delBuilding(@PathVariable("id") Long id){
        buildingService.deleteBuilding(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    @RequestMapping(value = "/building/find/{id}",method = RequestMethod.GET)
    public ResponseEntity<String> findBuilding(@PathVariable("id") Long id){
        Building building = buildingService.getBuildingById(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(building), HttpStatus.OK);
    }
}
