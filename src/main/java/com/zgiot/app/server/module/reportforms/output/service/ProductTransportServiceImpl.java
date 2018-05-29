package com.zgiot.app.server.module.reportforms.output.service;

import com.zgiot.app.server.module.reportforms.output.constant.ReportFormCoalTypeConstant;
import com.zgiot.app.server.module.reportforms.output.constant.ReportFormConstant;
import com.zgiot.app.server.module.reportforms.output.constant.ReportFormTargetConstant;
import com.zgiot.app.server.module.reportforms.output.constant.ReportFormTransPortType;
import com.zgiot.app.server.module.reportforms.output.dao.ProductTransportMapper;
import com.zgiot.app.server.module.reportforms.output.pojo.*;
import com.zgiot.app.server.module.reportforms.output.utils.ReportFormDateUtil;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductTransportServiceImpl implements ProductTransportService{

    @Autowired
    private ProductTransportMapper productTransportMapper;

    @Autowired
    private TransPortService transPortService;

    @Autowired
    private OutputStoreAndTargetService outputStoreAndTargetService;

    @Autowired
    private ReportFormProductOutputAndStoreService reportFormProductionService;


    public Map<Integer,Map<Integer,ProductTransport>> getProductTransportBean(Date date) {
        Map<Integer,Map<Integer,ProductTransport>> map=new HashMap<>();
        //方便前端判断线将数据设置
        Map<Integer, ProductTransport> washedCoalMap=new HashMap<>();
        washedCoalMap.put(ReportFormTransPortType.railway,null);
        washedCoalMap.put(ReportFormTransPortType.localSales,null);
        map.put(ReportFormCoalTypeConstant.washedCoal,washedCoalMap);

        Map<Integer, ProductTransport> clenedCoalMap=new HashMap<>();
        clenedCoalMap.put(ReportFormTransPortType.railway,null);
        clenedCoalMap.put(ReportFormTransPortType.localSales,null);
        map.put(ReportFormCoalTypeConstant.clenedCoal,clenedCoalMap);

        Map<Integer, ProductTransport> slimeMap=new HashMap<>();
        slimeMap.put(ReportFormTransPortType.railway,null);
        slimeMap.put(ReportFormTransPortType.localSales,null);
        map.put(ReportFormCoalTypeConstant.slime,slimeMap);

        Map<Integer, ProductTransport> washeryRejectsMap=new HashMap<>();
        washeryRejectsMap.put(ReportFormTransPortType.total,null);
        map.put(ReportFormCoalTypeConstant.washeryRejects,washeryRejectsMap);

        Map<Integer, ProductTransport> selfUseCoalTon=new HashMap<>();
        selfUseCoalTon.put(ReportFormTransPortType.total,null);
        map.put(ReportFormTargetConstant.SELF_USE_COAL_TON,selfUseCoalTon);

        //获取开始时间
        Date productStartTime = ReportFormDateUtil.getProductStartTime(date);
        //首先判断是不是当天
        if(ReportFormDateUtil.isProductCurrentDuty(date) || ReportFormDateUtil.isCurrentDuty(DateUtils.addHours(date,8))){
            //表明是当天，查询其他表
            getProductTransPort(date, map,productStartTime);
            return map;
        }else{
            //表明不是当天，先查询数据库是否存在开始时间的数据，不存在则查询其他表并且在表中新建一个
            List<ProductTransport> productTransportList=productTransportMapper.getProductTransport(productStartTime);
            if(productTransportList!=null && productTransportList.size()>0){
                //有数据
                getProductTransPortMap(map, productTransportList);
                return map;
            }else{
                getProductTransPortAndInsert(date, map, productStartTime);
                return map;
            }

        }
    }

    private void getProductTransPortAndInsert(Date date, Map<Integer, Map<Integer, ProductTransport>> map, Date productStartTime) {
        //无数据
        getProductTransPort(date,map,productStartTime);
        for (Map.Entry<Integer,Map<Integer,ProductTransport>> entry:map.entrySet()) {
            Map<Integer, ProductTransport> productTransportMap = entry.getValue();
            for (Map.Entry<Integer,ProductTransport> entryProduct:productTransportMap.entrySet()) {
                if(entryProduct.getValue()!=null){
                    productTransportMapper.insertProductTransport(entryProduct.getValue());
                }
            }
        }
    }

    @Override
    public ProductTransportRemarks getProductTransportMessage(Date date) {
        Date productStartTime = ReportFormDateUtil.getProductStartTime(date);
        ProductTransportRemarks productTransportRemarks=productTransportMapper.getProductTransportMessage(productStartTime);
        return productTransportRemarks;
    }

    @Override
    public void updateProductTransportMessage(ProductTransportRemarks productTransport) {
        ProductTransportRemarks productTransportMessage = productTransportMapper.getProductTransportMessage(productTransport.getProductStartTime());
        if(productTransportMessage!=null){
            productTransport.setId(productTransportMessage.getId());
            productTransportMapper.updateProductTransportMessage(productTransport);
        }else{
            productTransportMapper.insertProductTransportMessage(productTransport);
        }
    }

    private void getProductTransPortMap(Map<Integer, Map<Integer, ProductTransport>> map, List<ProductTransport> productTransportList) {
        for (ProductTransport productTransport:productTransportList) {
            Map<Integer, ProductTransport> productTransportMap;
            if(map.containsKey(productTransport.getCoalType()) && map.get(productTransport.getCoalType())!=null){
                productTransportMap = map.get(productTransport.getCoalType());
                productTransportMap.put(productTransport.getTransportType(),productTransport);
            }else{
                productTransportMap=new HashMap<>();
                productTransportMap.put(productTransport.getTransportType(),productTransport);
            }
            map.put(productTransport.getCoalType(),productTransportMap);
        }
    }

    private void getProductTransPort(Date date, Map<Integer, Map<Integer, ProductTransport>> map,Date productStartTime) {
        //获取结束时间
        Date productEndTime = ReportFormDateUtil.getProductEndTime(date);
        List<ProductTransport> transportList = getTransPort(productStartTime, productEndTime);

        //将运销数据放入到Map中
        for (ProductTransport productTransport:transportList) {
            Map<Integer, ProductTransport> productTransportMap = map.get(productTransport.getCoalType());
            if(productTransportMap==null){
                productTransportMap=new HashMap<>();
            }
            productTransport.setProductStartTime(productStartTime);
            productTransport.setTransportType(ReportFormTransPortType.railway);
            productTransportMap.put(ReportFormTransPortType.railway,productTransport);
            map.put(productTransport.getCoalType(),productTransportMap);
        }


        //获取地销数据
        Date afterDate = DateUtils.addHours(date, 8);
        Date beforeDate=DateUtils.addHours(date,-4);
        Map<Integer, TransportSaleStatistics> saleStatisticsLocalityMap = getSaleStatisticsLocality(afterDate, beforeDate);

        //将地销放入Map
        for (Map.Entry<Integer,TransportSaleStatistics> entry:saleStatisticsLocalityMap.entrySet()) {
            Map<Integer, ProductTransport> productTransportMap = map.get(entry.getKey());
            if(productTransportMap==null){
                productTransportMap=new HashMap<>();
            }
            TransportSaleStatistics saleStatistics = entry.getValue();
            ProductTransport productTransport=new ProductTransport();
            productTransport.setCoalType(saleStatistics.getCoalType());
            productTransport.setTransportType(ReportFormTransPortType.localSales);
            productTransport.setProductStartTime(productStartTime);
            productTransport.setDayVolume(saleStatistics.getCoalVolunm());
            productTransport.setMonthVolume(saleStatistics.getMonthCoalVolunm());
            productTransport.setYearVolume(saleStatistics.getYearCoalVolunm());
            productTransportMap.put(ReportFormTransPortType.localSales,productTransport);
            map.put(entry.getKey(),productTransportMap);
        }


        //获取自用数据
        ReportFormTargetRecord reportFormTargetRecord = getTargetInfo(afterDate, beforeDate);

        //封装自用煤
        ProductTransport productTransport=new ProductTransport();
        productTransport.setProductStartTime(productStartTime);
        productTransport.setTransportType(ReportFormTransPortType.total);
        productTransport.setCoalType(ReportFormTargetConstant.SELF_USE_COAL_TON);
        productTransport.setDayVolume(reportFormTargetRecord.getClassValue());
        productTransport.setMonthVolume(reportFormTargetRecord.getMonthValue());
        productTransport.setYearVolume(reportFormTargetRecord.getYearValue());
        Map<Integer, ProductTransport> productTransportMap = map.get(productTransport.getCoalType());
        if(productTransportMap==null){
            productTransportMap=new HashMap<>();
        }
        productTransportMap.put(ReportFormTransPortType.total,productTransport);
        map.put(ReportFormTargetConstant.SELF_USE_COAL_TON,productTransportMap);

        //获取洗矸煤并放入Map
        Map<Integer, ReportFormProductOutput> productOutputInfo = reportFormProductionService.getProductOutputInfo(date);
        ReportFormProductOutput reportFormProductOutput = productOutputInfo.get(ReportFormCoalTypeConstant.washeryRejects);
        if(reportFormProductOutput!=null){
            ProductTransport washeryRejects=new ProductTransport();
            washeryRejects.setCoalType(ReportFormCoalTypeConstant.washeryRejects);
            washeryRejects.setTransportType(ReportFormTransPortType.total);
            washeryRejects.setProductStartTime(productStartTime);
            washeryRejects.setDayVolume(reportFormProductOutput.getRealDay().doubleValue());
            washeryRejects.setMonthVolume(reportFormProductOutput.getRealMonth().doubleValue());
            washeryRejects.setYearVolume(reportFormProductOutput.getRealYear().doubleValue());
            Map<Integer, ProductTransport> washeryRejectsMap = map.get(washeryRejects.getCoalType());
            if(washeryRejectsMap==null){
                washeryRejectsMap=new HashMap<>();
            }
            washeryRejectsMap.put(ReportFormTransPortType.total,washeryRejects);
            map.put(ReportFormCoalTypeConstant.washeryRejects,washeryRejectsMap);
        }
    }

    private ReportFormTargetRecord getTargetInfo(Date afterDate, Date beforeDate) {
        Map<Integer, Map<Integer, ReportFormTargetRecord>> afterTargetInfo = outputStoreAndTargetService.getTargetInfo(afterDate);
        Map<Integer, Map<Integer, ReportFormTargetRecord>> beforeTargetInfo = outputStoreAndTargetService.getTargetInfo(beforeDate);
        ReportFormTargetRecord reportFormTargetRecord=new ReportFormTargetRecord();
        if(beforeTargetInfo.containsKey(ReportFormTargetConstant.SELF_USE_COAL_TON) && beforeTargetInfo.get(ReportFormTargetConstant.SELF_USE_COAL_TON)!=null){
            ReportFormTargetRecord beforeTargetRecord = beforeTargetInfo.get(ReportFormTargetConstant.SELF_USE_COAL_TON).get(0);
            reportFormTargetRecord.setClassValue(beforeTargetRecord.getClassValue());
            reportFormTargetRecord.setMonthValue(beforeTargetRecord.getMonthValue());
            reportFormTargetRecord.setYearValue(beforeTargetRecord.getYearValue());
        }

        setTargetRecord(afterTargetInfo, reportFormTargetRecord);
        return reportFormTargetRecord;
    }

    private void setTargetRecord(Map<Integer, Map<Integer, ReportFormTargetRecord>> afterTargetInfo, ReportFormTargetRecord reportFormTargetRecord) {
        if(afterTargetInfo.containsKey(ReportFormTargetConstant.SELF_USE_COAL_TON) && afterTargetInfo.get(ReportFormTargetConstant.SELF_USE_COAL_TON)!=null){
            ReportFormTargetRecord afterTargetRecord = afterTargetInfo.get(ReportFormTargetConstant.SELF_USE_COAL_TON).get(0);
            if(reportFormTargetRecord.getClassValue()==null){
                reportFormTargetRecord.setClassValue(afterTargetRecord.getClassValue());
            }else if(reportFormTargetRecord.getClassValue()!=null && afterTargetRecord.getClassValue()!=null){
                reportFormTargetRecord.setClassValue(reportFormTargetRecord.getClassValue()+afterTargetRecord.getClassValue());
            }


            if(reportFormTargetRecord.getMonthValue()==null){
                reportFormTargetRecord.setMonthValue(afterTargetRecord.getMonthValue());
            }else if(reportFormTargetRecord.getMonthValue()!=null && afterTargetRecord.getMonthValue()!=null){
                reportFormTargetRecord.setMonthValue(reportFormTargetRecord.getMonthValue()+afterTargetRecord.getMonthValue());
            }

            if(reportFormTargetRecord.getYearValue()==null){
                reportFormTargetRecord.setYearValue(afterTargetRecord.getYearValue());
            }else if (reportFormTargetRecord.getYearValue()!=null && afterTargetRecord.getYearValue()!=null){
                reportFormTargetRecord.setYearValue(reportFormTargetRecord.getYearValue()+afterTargetRecord.getYearValue());
            }
        }
    }

    private Map<Integer, TransportSaleStatistics> getSaleStatisticsLocality(Date afterDate, Date beforeDate) {
        TransBean transPortCacheBeanAfter = transPortService.getTransPortCacheBean(afterDate);
        Map<Integer, TransportSaleStatistics> saleStatisticsLocalityMapAfter = transPortCacheBeanAfter.getSaleStatisticsLocalityMap();
        TransBean transPortCacheBeanBefore = transPortService.getTransPortCacheBean(beforeDate);
        Map<Integer, TransportSaleStatistics> saleStatisticsLocalityMapBefore = transPortCacheBeanBefore.getSaleStatisticsLocalityMap();

        Map<Integer,TransportSaleStatistics> saleStatisticsMap=new HashMap<>();
        saleStatisticsMap.put(ReportFormConstant.COAL_TYPE_WASHED_NUM,new TransportSaleStatistics());
        saleStatisticsMap.put(ReportFormConstant.COAL_TYPE_GAS_REFINDED_NUM,new TransportSaleStatistics());
        saleStatisticsMap.put(ReportFormConstant.COAL_TYPE_SLIME_NUM,new TransportSaleStatistics());


        for (Map.Entry<Integer,TransportSaleStatistics> entry:saleStatisticsMap.entrySet()) {
            TransportSaleStatistics saleStatistics = entry.getValue();
            TransportSaleStatistics transportSaleStatisticsAfter = saleStatisticsLocalityMapAfter.get(entry.getKey());
            TransportSaleStatistics transportSaleStatisticsBefore = saleStatisticsLocalityMapBefore.get(entry.getKey());

            saleStatistics.setCoalType(entry.getKey());
            setSaleStaticsLocatity(saleStatistics, transportSaleStatisticsAfter, transportSaleStatisticsBefore);
        }
        return saleStatisticsMap;
    }

    /**
     * 设置地销数据
     * @param saleStatistics
     * @param transportSaleStatisticsAfter
     * @param transportSaleStatisticsBefore
     */
    private void setSaleStaticsLocatity(TransportSaleStatistics saleStatistics, TransportSaleStatistics transportSaleStatisticsAfter, TransportSaleStatistics transportSaleStatisticsBefore) {
        if(transportSaleStatisticsAfter!=null){
            if(transportSaleStatisticsAfter.getCoalVolunm()!=null){
                saleStatistics.setCoalVolunm(transportSaleStatisticsAfter.getCoalVolunm());
            }
            saleStatistics.setMonthCoalVolunm(transportSaleStatisticsAfter.getMonthCoalVolunm());
            saleStatistics.setYearCoalVolunm(transportSaleStatisticsAfter.getYearCoalVolunm());
        }

        setLocationtyAndHaveData(saleStatistics, transportSaleStatisticsBefore);
    }

    private void setLocationtyAndHaveData(TransportSaleStatistics saleStatistics, TransportSaleStatistics transportSaleStatisticsBefore) {
        if(transportSaleStatisticsBefore!=null){
            if(saleStatistics.getCoalVolunm()==null && transportSaleStatisticsBefore.getCoalVolunm()!=null){
                saleStatistics.setCoalVolunm(transportSaleStatisticsBefore.getCoalVolunm());
            }else if(saleStatistics.getCoalVolunm()!=null && transportSaleStatisticsBefore.getCoalVolunm()!=null){
                saleStatistics.setCoalVolunm(saleStatistics.getCoalVolunm()+transportSaleStatisticsBefore.getCoalVolunm());
            }

            if(saleStatistics.getMonthCoalVolunm()==null){
                saleStatistics.setMonthCoalVolunm(transportSaleStatisticsBefore.getMonthCoalVolunm());
            }else if(saleStatistics.getMonthCoalVolunm()!=null && transportSaleStatisticsBefore.getMonthCoalVolunm()!=null){
                saleStatistics.setMonthCoalVolunm(saleStatistics.getMonthCoalVolunm()+transportSaleStatisticsBefore.getMonthCoalVolunm());
            }

            if(saleStatistics.getYearCoalVolunm()==null){
                saleStatistics.setYearCoalVolunm(transportSaleStatisticsBefore.getYearCoalVolunm());
            }else if(saleStatistics.getYearCoalVolunm()!=null && transportSaleStatisticsBefore.getYearCoalVolunm()!=null){
                saleStatistics.setYearCoalVolunm(saleStatistics.getYearCoalVolunm()+transportSaleStatisticsBefore.getYearCoalVolunm());
            }
        }
    }

    private List<ProductTransport> getTransPort(Date productStartTime, Date productEndTime) {
        //获取运销数据
        ProductTransPortCond productTransPortCond=new ProductTransPortCond();
        productTransPortCond.setProductStartTime(productStartTime);
        productTransPortCond.setProductEndTime(productEndTime);
        List<ProductTransport> transportListDay=productTransportMapper.getTransPortDayVolume(productTransPortCond);

        ProductTransPortCond productTransPortCondMonth=new ProductTransPortCond();
        productTransPortCondMonth.setProductEndTime(productEndTime);
        productTransPortCondMonth.setProductStartTime(ReportFormDateUtil.getProductMonthFirstDay(productStartTime));
        List<ProductTransport> transportListMonth=productTransportMapper.getTransPortMonthVolume(productTransPortCondMonth);

        ProductTransPortCond productTransPortCondYear=new ProductTransPortCond();
        productTransPortCondYear.setProductEndTime(productEndTime);
        productTransPortCondYear.setProductStartTime(ReportFormDateUtil.getProductYearFirstDay(productStartTime));
        List<ProductTransport> transportList=productTransportMapper.getTransPortYearVolume(productTransPortCondYear);

        for (ProductTransport productTransport:transportList) {
            for (ProductTransport productTransportMonth:transportListMonth) {
                if(productTransport.getCoalType().equals(productTransportMonth.getCoalType())){
                    productTransport.setMonthColumnNumber(productTransportMonth.getMonthColumnNumber());
                    productTransport.setMonthVolume(productTransportMonth.getMonthVolume());
                    break;
                }
            }
            for (ProductTransport productTransportDay:transportListDay) {
                if(productTransport.getCoalType().equals(productTransportDay.getCoalType())){
                    productTransport.setDayCarNumber(productTransportDay.getDayCarNumber());
                    productTransport.setDayColumnNumber(productTransportDay.getDayColumnNumber());
                    productTransport.setDayVolume(productTransportDay.getDayVolume());
                    break;
                }
            }

            productTransport.setProductStartTime(productStartTime);
            productTransport.setTransportType(ReportFormTransPortType.railway);
            productTransport.setCoalType(productTransport.getCoalType());
        }
        return transportList;
    }

}
