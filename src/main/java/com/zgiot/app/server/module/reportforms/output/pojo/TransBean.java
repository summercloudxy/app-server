package com.zgiot.app.server.module.reportforms.output.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiModel("运销表")
public class TransBean {

    @ApiModelProperty("运销数据,这是运销表数据的List集合")
    private List<Transport> transportList=new ArrayList<>();//运销表数据

    @ApiModelProperty("外运统计数据,key是煤的种类,value是对应的外运统计对象")
    private Map<Integer,TransportSaleStatistics> saleStatisticsOutwardMap=new HashMap<>();  //外运

    @ApiModelProperty("地销统计数据,key是煤的种类,value是对应的地销统计对象")
    private Map<Integer,TransportSaleStatistics> saleStatisticsLocalityMap=new HashMap<>(); //地销

    public List<Transport> getTransportList() {
        return transportList;
    }

    public void setTransportList(List<Transport> transportList) {
        this.transportList = transportList;
    }

    public Map<Integer, TransportSaleStatistics> getSaleStatisticsOutwardMap() {
        return saleStatisticsOutwardMap;
    }

    public void setSaleStatisticsOutwardMap(Map<Integer, TransportSaleStatistics> saleStatisticsOutwardMap) {
        this.saleStatisticsOutwardMap = saleStatisticsOutwardMap;
    }

    public Map<Integer, TransportSaleStatistics> getSaleStatisticsLocalityMap() {
        return saleStatisticsLocalityMap;
    }

    public void setSaleStatisticsLocalityMap(Map<Integer, TransportSaleStatistics> saleStatisticsLocalityMap) {
        this.saleStatisticsLocalityMap = saleStatisticsLocalityMap;
    }
}
