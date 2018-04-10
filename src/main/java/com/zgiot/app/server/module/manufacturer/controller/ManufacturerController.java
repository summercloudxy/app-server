package com.zgiot.app.server.module.manufacturer.controller;

import com.zgiot.app.server.module.equipments.controller.PageHelpInfo;
import com.zgiot.app.server.module.manufacturer.pojo.Manufacturer;
import com.zgiot.app.server.module.manufacturer.service.ManufacturerService;
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

@Controller
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/manufacturer")
public class ManufacturerController {

    @Autowired
    private ManufacturerService manufacturerService;


    /**
     *厂家管理-添加
     * @param manufacturer
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> addManufacturer(@RequestBody Manufacturer manufacturer) {
        manufacturerService.addManufacturer(manufacturer);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     *厂家管理-编辑
     * @param manufacturer
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public ResponseEntity<String> editManufacturer(@RequestBody Manufacturer manufacturer) {
        manufacturerService.editManufacturer(manufacturer);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     *厂家管理-删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delManufacturer(@PathVariable("id") Long id) {
        manufacturerService.deleteManufacturer(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    /**
     *厂家管理-查询-条件：厂家类型&&（编码或名称）
     * @param typeCode
     * @param codeOrName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/query/{typeCode}/codeOrName/{codeOrName}/pageNum/{pageNum}/pageSize/{pageSize}",
            method = RequestMethod.GET)
    public ResponseEntity<String> queryManufacturerByCodeOrName(@PathVariable String typeCode,
                                                                @PathVariable String codeOrName,
                                                                @PathVariable int pageNum,
                                                                @PathVariable int pageSize) {
        PageHelpInfo pageHelpInfo = manufacturerService.getManufacturerByCodeOrName(typeCode,codeOrName,pageNum,pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageHelpInfo), HttpStatus.OK);
    }

    /**
     *厂家管理-查询-条件：厂家类型
     * @param typeCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/query/{typeCode}/pageNum/{pageNum}/pageSize/{pageSize}",
            method = RequestMethod.GET)
    public ResponseEntity<String> queryManufacturerByType(@PathVariable String typeCode,@PathVariable int pageNum,
                                                                @PathVariable int pageSize) {
        PageHelpInfo pageHelpInfo = manufacturerService.getManufacturerByType(typeCode,pageNum,pageSize);
        return new ResponseEntity<>(ServerResponse.buildOkJson(pageHelpInfo), HttpStatus.OK);
    }

    /**
     * 厂家管理-查询-判断名字是否重复
     * @param manufacturerName
     * @return
     */
    @RequestMapping(value = "/isNameRepeat/{manufacturerName}", method = RequestMethod.GET)
    public ResponseEntity<String> isNameRepeat(@PathVariable("manufacturerName") String manufacturerName) {
        boolean flag = manufacturerService.isNameRepeat(manufacturerName);
        return new ResponseEntity<>(ServerResponse.buildOkJson(flag), HttpStatus.OK);
    }

    /**
     * 厂家管理-获取编码
     * @param typeCode
     * @return
     */
    @RequestMapping(value = "/getManufacturerCode/{typeCode}", method = RequestMethod.GET)
    public ResponseEntity<String> getManufacturerCodeByCode(@PathVariable("typeCode") String typeCode) {
        String manufacturerCode = manufacturerService.getManufacturerCodeByCode(typeCode);
        return new ResponseEntity<>(ServerResponse.buildOkJson(manufacturerCode), HttpStatus.OK);
    }
}
