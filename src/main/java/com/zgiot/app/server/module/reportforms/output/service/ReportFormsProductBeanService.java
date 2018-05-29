package com.zgiot.app.server.module.reportforms.output.service;

import com.zgiot.app.server.module.reportforms.output.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReportFormsProductBeanService {

    @Autowired
    private ProductTransportService productTransportService;

    @Autowired
    private ReportFormProductOutputAndStoreService productOutputAndStoreService;

    public ReportFormsProductBean getReportFromProduct(Date date) {
        ReportFormsProductBean reportFormBean=new ReportFormsProductBean();
        reportFormBean.setProductTransPortMap(productTransportService.getProductTransportBean(date));
        reportFormBean.setProductTransportRemarks(productTransportService.getProductTransportMessage(date));
        reportFormBean.setProductOutputInfo(productOutputAndStoreService.getProductOutputInfo(date));
        reportFormBean.setProductQualityInfo(productOutputAndStoreService.getProductQuality(date));
        reportFormBean.setProductStoreInfo(productOutputAndStoreService.getProductStoreInfo(date));
        return reportFormBean;
    }

    public void updateProductTransportMessage(ProductTransportRemarks productTransport) {
        productTransportService.updateProductTransportMessage(productTransport);
    }
}
