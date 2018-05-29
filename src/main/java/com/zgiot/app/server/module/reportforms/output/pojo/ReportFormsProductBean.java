package com.zgiot.app.server.module.reportforms.output.pojo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

public class ReportFormsProductBean {

    @ApiModelProperty("产品外运")
    private Map<Integer,Map<Integer,ProductTransport>> productTransPortMap;

    @ApiModelProperty("备注及人员信息")
    private ProductTransportRemarks productTransportRemarks;

    private Map<Integer, ReportFormProductOutput> productOutputInfo;

    private  Map<Integer, ReportFormProductQuality> productQualityInfo;

    private Map<Integer, ReportFormProductStore> productStoreInfo;

    public Map<Integer, Map<Integer, ProductTransport>> getProductTransPortMap() {
        return productTransPortMap;
    }

    public void setProductTransPortMap(Map<Integer, Map<Integer, ProductTransport>> productTransPortMap) {
        this.productTransPortMap = productTransPortMap;
    }

    public ProductTransportRemarks getProductTransportRemarks() {
        return productTransportRemarks;
    }

    public void setProductTransportRemarks(ProductTransportRemarks productTransportRemarks) {
        this.productTransportRemarks = productTransportRemarks;
    }

    public Map<Integer, ReportFormProductOutput> getProductOutputInfo() {
        return productOutputInfo;
    }

    public void setProductOutputInfo(Map<Integer, ReportFormProductOutput> productOutputInfo) {
        this.productOutputInfo = productOutputInfo;
    }

    public Map<Integer, ReportFormProductQuality> getProductQualityInfo() {
        return productQualityInfo;
    }

    public void setProductQualityInfo(Map<Integer, ReportFormProductQuality> productQualityInfo) {
        this.productQualityInfo = productQualityInfo;
    }

    public Map<Integer, ReportFormProductStore> getProductStoreInfo() {
        return productStoreInfo;
    }

    public void setProductStoreInfo(Map<Integer, ReportFormProductStore> productStoreInfo) {
        this.productStoreInfo = productStoreInfo;
    }
}
