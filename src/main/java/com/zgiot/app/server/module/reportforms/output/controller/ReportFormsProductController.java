package com.zgiot.app.server.module.reportforms.output.controller;

import com.zgiot.app.server.module.reportforms.output.pojo.ProductTransportRemarks;
import com.zgiot.app.server.module.reportforms.output.pojo.ReportFormProductStore;
import com.zgiot.app.server.module.reportforms.output.pojo.ReportFormsProductBean;
import com.zgiot.app.server.module.reportforms.output.service.ProductTransportService;
import com.zgiot.app.server.module.reportforms.output.service.ReportFormProductOutputAndStoreService;
import com.zgiot.app.server.module.reportforms.output.service.ReportFormsProductBeanService;
import com.zgiot.app.server.module.reportforms.output.utils.ReportFormDateUtil;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class ReportFormsProductController {

    @Autowired
    private ReportFormsProductBeanService reportFormsProductBeanService;
    @Autowired
    private ReportFormProductOutputAndStoreService reportFormProductOutputAndStoreService;

    @ApiOperation("获取产品外运情况数据")
    @GetMapping( "/reportform/product")
    public ResponseEntity<String> getReportFromProduct(@RequestParam Date date) {
        ReportFormsProductBean reportFormsProductBean=reportFormsProductBeanService.getReportFromProduct(date);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(reportFormsProductBean), HttpStatus.OK);
    }


    @ApiOperation("更新产品备注以及人员信息")
    @PostMapping("/reportform/product/transport/update")
    public ResponseEntity<String> updateProductTransportMessage(@RequestBody ProductTransportRemarks productTransport){
        productTransport.setProductStartTime(ReportFormDateUtil.getProductStartTime(productTransport.getProductStartTime()));
        reportFormsProductBeanService.updateProductTransportMessage(productTransport);
        return new ResponseEntity<>(ServerResponse.buildOkJsonWithNonStringKey(null), HttpStatus.OK);
    }

    @ApiOperation("更新产品介质库存")
    @PostMapping(value = "/reportform/product/medium")
    public ResponseEntity<String> setMediumStore(@RequestBody ReportFormProductStore reportFormProductStore) {
        reportFormProductOutputAndStoreService.addMediumStore(reportFormProductStore);
        return new ResponseEntity<>(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }
}
