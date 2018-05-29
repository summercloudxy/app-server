package com.zgiot.app.server.module.reportforms.output.service;

import com.zgiot.app.server.module.reportforms.output.pojo.ProductTransport;
import com.zgiot.app.server.module.reportforms.output.pojo.ProductTransportRemarks;
import com.zgiot.app.server.module.reportforms.output.pojo.ReportFormsProductBean;

import java.util.Date;
import java.util.Map;

public interface ProductTransportService {

    Map<Integer,Map<Integer,ProductTransport>> getProductTransportBean(Date date);

    ProductTransportRemarks getProductTransportMessage(Date date);

    void updateProductTransportMessage(ProductTransportRemarks productTransport);

}
