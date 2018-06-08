package com.zgiot.app.server.module.reportforms.output.service;

import com.zgiot.app.server.module.reportforms.output.enums.ReportFormCoalTypeEnum;
import com.zgiot.app.server.module.reportforms.output.pojo.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Set;

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
        Map<Integer, ReportFormProductOutput> productOutputInfo = productOutputAndStoreService.getProductOutputInfo(date);
        Map<Integer, ReportFormProductQuality> productQuality = productOutputAndStoreService.getProductQuality(date);
        Map<Integer, ReportFormProductStore> productStoreInfo = productOutputAndStoreService.getProductStoreInfo(date);
        Set<Integer> codes = ReportFormCoalTypeEnum.getCodes();
        for (Integer type:codes) {
                if(productOutputInfo!=null && productOutputInfo.get(type)==null){
                    productOutputInfo.put(type,null);
                }

                if(productQuality!=null && productQuality.get(type)==null){
                    productQuality.put(type,null);
                }

                if(productStoreInfo!=null && productStoreInfo.get(type)==null){
                    productStoreInfo.put(type,null);
                }
            }
        reportFormBean.setProductOutputInfo(productOutputInfo);
        reportFormBean.setProductQualityInfo(productQuality);
        reportFormBean.setProductStoreInfo(productStoreInfo);
        return reportFormBean;
    }

    public void updateProductTransportMessage(ProductTransportRemarks productTransport) {
        productTransportService.updateProductTransportMessage(productTransport);
    }
}
