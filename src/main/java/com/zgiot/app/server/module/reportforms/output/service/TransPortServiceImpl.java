package com.zgiot.app.server.module.reportforms.output.service;

import com.zgiot.app.server.module.reportforms.output.constant.ReportFormConstant;
import com.zgiot.app.server.module.reportforms.output.dao.TransPortMapper;
import com.zgiot.app.server.module.reportforms.output.pojo.*;
import com.zgiot.app.server.module.reportforms.output.utils.ReportFormDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class TransPortServiceImpl implements TransPortService {

    private static final TransBean transBean=new TransBean();

    @Autowired
    private TransPortMapper transPortMapper;

    @Autowired
    private OutputStoreAndTargetService outputService;

    public static final Integer TYPE_OUTPUT = 1;
    public static final Integer TYPE_STORE = 2;


    public void init() {
        //根据当前时间获取所有符合的运销对象
        Date nowDutyStartTime = ReportFormDateUtil.getNowDutyStartTime(new Date());
        List<Transport> transportList = transBean.getTransportList();
        List<Transport> transports=transPortMapper.getTransPortByDate(nowDutyStartTime);
        transportList.addAll(transports);

        //更新外运
        updateOutSaleStatistics();

        //查询是否存在地销
        List<TransportSaleStatistics> transportSaleStatisticsList = transPortMapper.getSaleByDateLocality(nowDutyStartTime);
        Map<Integer, TransportSaleStatistics> saleStatisticsLocalityMap = transBean.getSaleStatisticsLocalityMap();
        if(transportSaleStatisticsList!=null && transportSaleStatisticsList.size()>0){
            //存在
            for (TransportSaleStatistics ts:transportSaleStatisticsList) {
                saleStatisticsLocalityMap.put(ts.getCoalType(),ts);
            }
        }else{
            //不存在
            addSaleLocality(nowDutyStartTime);
        }
    }


    @Override
    public void updateTransPort(List<Transport> transports) {
        if(transports!=null && transports.size()>0){
            for (Transport transport:transports) {
                if (transport.getId() == null) {
                    //新建
                    createTransPort(transport);
                }else{
                    //更改
                    editTransPort(transport);
                }
            }
            //更改库存
            updateOutputRecord();
        }
    }

    /**
     * 更新地销
     * @param transportSaleStatisticsList
     */
    @Override
    public void updateSaleStatistics(List<TransportSaleStatistics> transportSaleStatisticsList) {
        Map<Integer, TransportSaleStatistics> saleStatisticsLocalityMap = transBean.getSaleStatisticsLocalityMap();
        for (TransportSaleStatistics ts:transportSaleStatisticsList) {
            //这里只有更改,新增在定时任务和项目启动时创建
            //根据当班开始时间和煤种类获取最新数据
            TransportSaleStatistics mostNewsale=transPortMapper.getMostNewSaleStatisticsLocality(ts);
            if(mostNewsale!=null){
                saleStatisticsByDate(ts,ts.getDutyStartTime(),mostNewsale);
            }else{
                ts.setMonthTrainNumber(ts.getTrainNumber());
                ts.setMonthCoalVolunm(ts.getCoalVolunm());
                ts.setYearTrainNumber(ts.getTrainNumber());
                ts.setYearCoalVolunm(ts.getCoalVolunm());
            }
            transPortMapper.editTransportSaleStatistics(ts);
            saleStatisticsLocalityMap.put(ts.getCoalType(),ts);
        }
        //更新库存
        updateOutputRecord();
    }

    /**
     * 新增运销
     * @param transport
     */
    public void createTransPort(Transport transport){
        //表明新增
        List<Transport> transportList = transBean.getTransportList();

        //设置批次
        if (transport.getCoalType() != null) {
            //判断缓存中是否存在符合数据
            Transport  mostNewTransport = transPortMapper.getMostNewTransPortByType(transport.getCoalType());
            //设置transport中的批次数据
            if (mostNewTransport == null) {
                transport.setBatch(1);
            } else {
                transport.setBatch(mostNewTransport.getBatch() + 1);
            }
        }

        //这里需要根据结束时间来进行判断是否放入缓存
        if (transport.getTransportEndTime() != null) {
            //设置装车用时
            Long dateLength = transport.getTransportEndTime().getTime() - transport.getTransportStartTime().getTime();
            transport.setDuration((int) (dateLength / 1000 / 60));

            //获取当班开始时间,这里以结束时间开始计算
            Date endDutyStartTime = ReportFormDateUtil.getNowDutyStartTime(transport.getTransportEndTime());
            transport.setDutyStartTime(endDutyStartTime);
            //获取当前时间所属于的当班开始时间
            Date nowDutyStartTime = ReportFormDateUtil.getNowDutyStartTime(new Date());

            transPortMapper.createTransPort(transport);
            if(endDutyStartTime.equals(nowDutyStartTime)){
                //在当班开始时间之内,添加到缓存
                transportList.add(transport);
            }
        }else{
            //无结束时间,不能判断是否是当前班次，但是也要显示
            transPortMapper.createTransPort(transport);
            transportList.add(transport);
        }
        updateOutSaleStatistics();
    }

    /**
     * 更改运销
     * @param transport
     */
    private void editTransPort(Transport transport) {

        if(transport.getCoalType()!=null && transport.getBatch()==null){
            //判断缓存中是否存在符合数据
            Transport  mostNewTransport = transPortMapper.getMostNewTransPortByType(transport.getCoalType());
            //设置transport中的批次数据
            if (mostNewTransport == null) {
                transport.setBatch(1);
            } else {
                transport.setBatch(mostNewTransport.getBatch() + 1);
            }
        }

        //这里需要重新计算是为避免用户更改结束时间
        if(transport.getTransportEndTime()!=null && transport.getTransportStartTime()!=null){
            Long dateLength = transport.getTransportEndTime().getTime() - transport.getTransportStartTime().getTime();
            transport.setDuration((int) (dateLength / 1000 / 60));

            //获取当班开始时间,这里以结束时间开始计算
            Date endDutyStartTime = ReportFormDateUtil.getNowDutyStartTime(transport.getTransportEndTime());
            transport.setDutyStartTime(endDutyStartTime);
            //获取当前时间所属于的当班开始时间
            Date nowDutyStartTime = ReportFormDateUtil.getNowDutyStartTime(new Date());
            transPortMapper.editTransPort(transport);
            if(endDutyStartTime.equals(nowDutyStartTime)){
                updateTransportList(transport);
            }else{
                //不在当班时间之内需要清除Bean中数据
                removeBeanTransport(transport);
            }
        }else{
            transPortMapper.editTransPort(transport);
            //没有结束时间但是也需要展示
            updateTransportList(transport);
        }
        updateOutSaleStatistics();
    }

    private void removeBeanTransport(Transport transport) {
        List<Transport> transportList = transBean.getTransportList();
        int index=0;
        for (int i=0;i<transportList.size();i++){
            if(transportList.get(i).getId().equals(transport.getId())){
                index=i;
            }
        }
        if(index!=0){
            transportList.remove(index);
        }
    }

    /**
     * 更新Bean中运销数据
     * @param transport
     */
    public void updateTransportList(Transport transport){
        List<Transport> transportList = transBean.getTransportList();
        Integer index=-1;
        for (int i=0;i<transportList.size();i++){
            if(transportList.get(i).getId().equals(transport.getId())){
                index=i;
            }
        }
        if(index==-1){
            //表明Bean不存在该数据
            transportList.add(transport);
        }else{
            //表明Bean已存在该数据
            transportList.set(index,transport);
        }
    }

    /**
     * 更新外运统计
     */
    public void updateOutSaleStatistics(){
        //获取当前时间的当班时间
        Date nowDutyStartTime = ReportFormDateUtil.getNowDutyStartTime(new Date());
        Map<Integer, TransportSaleStatistics> map = getSaleStatisticsByDate(nowDutyStartTime);
        getBeforeSaleStatusticsByDate(map, nowDutyStartTime);

        Map<Integer, TransportSaleStatistics> saleStatisticsOutwardMap = transBean.getSaleStatisticsOutwardMap();
        //根据当前当班时间查询数据库是否存在数据
        List<TransportSaleStatistics> nowSaleStatisticsList=transPortMapper.getSaleStatisticsByDutyStartTimeOut(nowDutyStartTime);
        if(nowSaleStatisticsList!=null && nowSaleStatisticsList.size()>0){
            //存在数据
            for (TransportSaleStatistics ts:nowSaleStatisticsList) {
                TransportSaleStatistics transportSaleStatistics = map.get(ts.getCoalType());
                transportSaleStatistics.setId(ts.getId());
                transPortMapper.editTransportSaleStatistics(transportSaleStatistics);
            }
            saleStatisticsOutwardMap.putAll(map);
        }else{
            //不存在数据
            for (Map.Entry<Integer,TransportSaleStatistics> entry:map.entrySet()) {
                TransportSaleStatistics transportSaleStatistics = entry.getValue();
                transPortMapper.createSaleStatistics(transportSaleStatistics);
                saleStatisticsOutwardMap.put(entry.getKey(),transportSaleStatistics);
            }
        }

    }


    /**
     * 根据当前当班时间得到一个统计Map
     * @return
     */
    public Map<Integer,TransportSaleStatistics> getSaleStatisticsByDate(Date nowDutyStartTime){
        //获取当班运输总量
        List<TransportVolume> transportVolumeList=transPortMapper.getTransportVolumeByDate(nowDutyStartTime);
        //创建一个Map用于数据封装以及后期数据改变
        Map<Integer,TransportSaleStatistics> map=new HashMap<>();
        for (TransportVolume transportVolume:transportVolumeList) {
            TransportSaleStatistics transportSaleStatistics=new TransportSaleStatistics();
            transportSaleStatistics.setCoalType(transportVolume.getCoalType());
            transportSaleStatistics.setDutyStartTime(nowDutyStartTime);
            transportSaleStatistics.setStastisticsType(ReportFormConstant.STASTISTICS_TYPE_OUTWARD);
            transportSaleStatistics.setTrainNumber(transportVolume.getTrainNumber());
            transportSaleStatistics.setCoalVolunm(transportVolume.getTransportVolume());
            transportSaleStatistics.setMonthTrainNumber(0);
            transportSaleStatistics.setMonthCoalVolunm(0.0);
            transportSaleStatistics.setYearTrainNumber(0);
            transportSaleStatistics.setYearCoalVolunm(0.0);
            map.put(transportVolume.getCoalType(),transportSaleStatistics);
        }

        if(map.get(ReportFormConstant.COAL_TYPE_WASHED_NUM)==null){
            TransportSaleStatistics transportSaleStatistics=new TransportSaleStatistics();
            transportSaleStatistics.setCoalType(ReportFormConstant.COAL_TYPE_WASHED_NUM);
            transportSaleStatistics.setDutyStartTime(nowDutyStartTime);
            transportSaleStatistics.setStastisticsType(ReportFormConstant.STASTISTICS_TYPE_OUTWARD);
            transportSaleStatistics.setTrainNumber(0);
            transportSaleStatistics.setCoalVolunm(0.0);
            transportSaleStatistics.setMonthTrainNumber(0);
            transportSaleStatistics.setMonthCoalVolunm(0.0);
            transportSaleStatistics.setYearTrainNumber(0);
            transportSaleStatistics.setYearCoalVolunm(0.0);
            map.put(ReportFormConstant.COAL_TYPE_WASHED_NUM,transportSaleStatistics);
        }

        if(map.get(ReportFormConstant.COAL_TYPE_GAS_REFINDED_NUM)==null){
            TransportSaleStatistics transportSaleStatistics=new TransportSaleStatistics();
            transportSaleStatistics.setCoalType(ReportFormConstant.COAL_TYPE_GAS_REFINDED_NUM);
            transportSaleStatistics.setDutyStartTime(nowDutyStartTime);
            transportSaleStatistics.setStastisticsType(ReportFormConstant.STASTISTICS_TYPE_OUTWARD);
            transportSaleStatistics.setTrainNumber(0);
            transportSaleStatistics.setCoalVolunm(0.0);
            transportSaleStatistics.setMonthTrainNumber(0);
            transportSaleStatistics.setMonthCoalVolunm(0.0);
            transportSaleStatistics.setYearTrainNumber(0);
            transportSaleStatistics.setYearCoalVolunm(0.0);
            map.put(ReportFormConstant.COAL_TYPE_GAS_REFINDED_NUM,transportSaleStatistics);
        }

        if(map.get(ReportFormConstant.COAL_TYPE_SLIME_NUM)==null){
            TransportSaleStatistics transportSaleStatistics=new TransportSaleStatistics();
            transportSaleStatistics.setCoalType(ReportFormConstant.COAL_TYPE_SLIME_NUM);
            transportSaleStatistics.setDutyStartTime(nowDutyStartTime);
            transportSaleStatistics.setStastisticsType(ReportFormConstant.STASTISTICS_TYPE_OUTWARD);
            transportSaleStatistics.setTrainNumber(0);
            transportSaleStatistics.setCoalVolunm(0.0);
            transportSaleStatistics.setMonthTrainNumber(0);
            transportSaleStatistics.setMonthCoalVolunm(0.0);
            transportSaleStatistics.setYearTrainNumber(0);
            transportSaleStatistics.setYearCoalVolunm(0.0);
            map.put(ReportFormConstant.COAL_TYPE_SLIME_NUM,transportSaleStatistics);
        }
        return map;
    }

    /**
     *获取前一班次数据并封装到当前班次Map中
     * @param map
     * @param nowDutyStartTime
     * @return
     */
    public Map<Integer,TransportSaleStatistics> getBeforeSaleStatusticsByDate(Map<Integer,TransportSaleStatistics> map,Date nowDutyStartTime){
        //查询当前班次时间之前的最新外运数据
        List<TransportSaleStatistics> beforeSaleStatisticsList=transPortMapper.getMostNewSaleStatisticsOut(nowDutyStartTime);
        if(beforeSaleStatisticsList!=null && beforeSaleStatisticsList.size()>0){
            for (TransportSaleStatistics ts:beforeSaleStatisticsList) {
                TransportSaleStatistics transportSaleStatistics = map.get(ts.getCoalType());
                saleStatisticsByDate(transportSaleStatistics,nowDutyStartTime,ts);
            }
        }else{
            //表明是第一次
            for (Map.Entry<Integer,TransportSaleStatistics> entry:map.entrySet()) {
                TransportSaleStatistics transportSaleStatistics = entry.getValue();
                transportSaleStatistics.setMonthTrainNumber(transportSaleStatistics.getTrainNumber());
                transportSaleStatistics.setMonthCoalVolunm(transportSaleStatistics.getCoalVolunm());
                transportSaleStatistics.setYearTrainNumber(transportSaleStatistics.getTrainNumber());
                transportSaleStatistics.setYearCoalVolunm(transportSaleStatistics.getCoalVolunm());
            }
        }
        return map;
    }

    /**
     * 根据时间封装transportSaleStatistics
     * @param transportSaleStatistics
     * @param nowDutyStartTime
     * @param ts
     */
    public void saleStatisticsByDate(TransportSaleStatistics transportSaleStatistics,Date nowDutyStartTime,TransportSaleStatistics ts){
        //这里需要对ts是否是当月或者当年数据进行处理
        if(!ReportFormDateUtil.isYearSame(ts.getDutyStartTime(),nowDutyStartTime)){
            //表明时间不在同一年,需要将年统计设置为0
            ts.setYearTrainNumber(0);
            ts.setYearCoalVolunm(0.0);
        }
        if(!ReportFormDateUtil.isMonthSame(ts.getDutyStartTime(),nowDutyStartTime)){
            //表明不在同一个月
            ts.setMonthTrainNumber(0);
            ts.setMonthCoalVolunm(0.0);
        }

        if(ReportFormDateUtil.isYearFirstDay(nowDutyStartTime)){
            //是否是一年的第一天
            transportSaleStatistics.setMonthTrainNumber(transportSaleStatistics.getTrainNumber());
            transportSaleStatistics.setMonthCoalVolunm(transportSaleStatistics.getCoalVolunm());
            transportSaleStatistics.setYearTrainNumber(transportSaleStatistics.getTrainNumber());
            transportSaleStatistics.setYearCoalVolunm(transportSaleStatistics.getCoalVolunm());
        }else{
            if(ReportFormDateUtil.isMonthFirstDay(nowDutyStartTime)){
                //是否是一月的第一天
                transportSaleStatistics.setMonthTrainNumber(transportSaleStatistics.getTrainNumber());
                transportSaleStatistics.setMonthCoalVolunm(transportSaleStatistics.getCoalVolunm());
                transportSaleStatistics.setYearTrainNumber(transportSaleStatistics.getYearTrainNumber()+ts.getYearTrainNumber());
                transportSaleStatistics.setYearCoalVolunm(transportSaleStatistics.getYearCoalVolunm()+ts.getYearCoalVolunm());
            }else{
                transportSaleStatistics.setMonthTrainNumber(transportSaleStatistics.getTrainNumber()+ts.getMonthTrainNumber());
                transportSaleStatistics.setMonthCoalVolunm(transportSaleStatistics.getCoalVolunm()+ts.getMonthCoalVolunm());
                transportSaleStatistics.setYearTrainNumber(transportSaleStatistics.getTrainNumber()+ts.getYearTrainNumber());
                transportSaleStatistics.setYearCoalVolunm(transportSaleStatistics.getCoalVolunm()+ts.getYearCoalVolunm());
            }
        }
    }


    public void addSaleLocality(Date nowDutyStartTime){
        Map<Integer, TransportSaleStatistics> saleLocality = createSaleLocality(nowDutyStartTime);
        List<TransportSaleStatistics> saleList = transPortMapper.getMostNewSaleByDate(nowDutyStartTime);
        //首先需要创建三个类型地销
        for (TransportSaleStatistics ts:saleList) {
            TransportSaleStatistics transportSaleStatistics = saleLocality.get(ts.getCoalType());
            saleStatisticsByDate(transportSaleStatistics,nowDutyStartTime,ts);
        }

        Map<Integer, TransportSaleStatistics> saleStatisticsLocalityMap = transBean.getSaleStatisticsLocalityMap();
        for (Map.Entry<Integer,TransportSaleStatistics> entry:saleLocality.entrySet()) {
            TransportSaleStatistics transportSaleStatistics = entry.getValue();
            transPortMapper.createSaleStatistics(transportSaleStatistics);
            saleStatisticsLocalityMap.put(transportSaleStatistics.getCoalType(),transportSaleStatistics);
        }

    }

    public Map<Integer,TransportSaleStatistics> createSaleLocality(Date nowDutyStartTime){
        Map<Integer,TransportSaleStatistics> map=new HashMap<>();
        TransportSaleStatistics washed=new TransportSaleStatistics();
        washed.setDutyStartTime(nowDutyStartTime);
        washed.setCoalType(ReportFormConstant.COAL_TYPE_WASHED_NUM);
        washed.setTrainNumber(0);
        washed.setCoalVolunm(0.0);
        washed.setMonthTrainNumber(0);
        washed.setMonthCoalVolunm(0.0);
        washed.setYearTrainNumber(0);
        washed.setYearCoalVolunm(0.0);
        washed.setStastisticsType(ReportFormConstant.STASTISTICS_TYPE_LOCALITY);
        map.put(washed.getCoalType(),washed);

        TransportSaleStatistics gasRefinded=new TransportSaleStatistics();
        gasRefinded.setDutyStartTime(nowDutyStartTime);
        gasRefinded.setCoalType(ReportFormConstant.COAL_TYPE_GAS_REFINDED_NUM);
        gasRefinded.setTrainNumber(0);
        gasRefinded.setCoalVolunm(0.0);
        gasRefinded.setMonthTrainNumber(0);
        gasRefinded.setMonthCoalVolunm(0.0);
        gasRefinded.setYearTrainNumber(0);
        gasRefinded.setYearCoalVolunm(0.0);
        gasRefinded.setStastisticsType(ReportFormConstant.STASTISTICS_TYPE_LOCALITY);
        map.put(gasRefinded.getCoalType(),gasRefinded);


        TransportSaleStatistics slime=new TransportSaleStatistics();
        slime.setDutyStartTime(nowDutyStartTime);
        slime.setCoalType(ReportFormConstant.COAL_TYPE_SLIME_NUM);
        slime.setTrainNumber(0);
        slime.setCoalVolunm(0.0);
        slime.setMonthTrainNumber(0);
        slime.setMonthCoalVolunm(0.0);
        slime.setYearTrainNumber(0);
        slime.setYearCoalVolunm(0.0);
        slime.setStastisticsType(ReportFormConstant.STASTISTICS_TYPE_LOCALITY);
        map.put(slime.getCoalType(),slime);
        return map;
    }


    @Scheduled(cron = "0 0 8,20 * * ?")
    public void updateBeanJob(){
        Date nowDutyStartTime = ReportFormDateUtil.getNowDutyStartTime(new Date());
        List<Transport> transportList = transBean.getTransportList();
        transportList.clear();
        List<Transport> transports=transPortMapper.getTransPortByDate(nowDutyStartTime);
        transportList.addAll(transports);
        //更新外运缓存
        updateOutSaleStatistics();

        //查询是否存在地销
        List<TransportSaleStatistics> transportSaleStatisticsList = transPortMapper.getSaleByDateLocality(nowDutyStartTime);
        Map<Integer, TransportSaleStatistics> saleStatisticsLocalityMap = transBean.getSaleStatisticsLocalityMap();
        if(transportSaleStatisticsList!=null && transportSaleStatisticsList.size()>0){
            //存在
            for (TransportSaleStatistics ts:transportSaleStatisticsList) {
                saleStatisticsLocalityMap.put(ts.getCoalType(),ts);
            }
        }else{
            //不存在
            addSaleLocality(nowDutyStartTime);
        }
        updateOutputRecord();
    }

    @Override
    public TransBean getTransPortCacheBean(Date date) {
        Date dateDutyStartTime = ReportFormDateUtil.getNowDutyStartTime(date);
        Date nowDutyStartTime = ReportFormDateUtil.getNowDutyStartTime(new Date());
        if(dateDutyStartTime.equals(nowDutyStartTime) && transBean.getSaleStatisticsOutwardMap().size()>0 && transBean.getSaleStatisticsLocalityMap().size()>0){
            return transBean;
        }else{
            TransBean transBeanRsp=new TransBean();
            transBeanRsp.setTransportList(transPortMapper.getTransPortByDate(dateDutyStartTime));
            List<TransportSaleStatistics> saleByDateLocality = transPortMapper.getSaleByDateLocality(dateDutyStartTime);
            Map<Integer, TransportSaleStatistics> saleStatisticsLocalityMap = transBeanRsp.getSaleStatisticsLocalityMap();
            if(saleByDateLocality!=null && saleByDateLocality.size()>0){
                for (TransportSaleStatistics ts:saleByDateLocality) {
                    saleStatisticsLocalityMap.put(ts.getCoalType(),ts);
                }
            }

            List<TransportSaleStatistics> saleStatisticsByDutyStartTimeOut = transPortMapper.getSaleStatisticsByDutyStartTimeOut(dateDutyStartTime);
            Map<Integer, TransportSaleStatistics> saleStatisticsOutwardMap = transBeanRsp.getSaleStatisticsOutwardMap();
            if(saleStatisticsByDutyStartTimeOut!=null && saleStatisticsByDutyStartTimeOut.size()>0){
                for (TransportSaleStatistics ts:saleStatisticsByDutyStartTimeOut) {
                    saleStatisticsOutwardMap.put(ts.getCoalType(),ts);
                }
            }
            return transBeanRsp;
        }
    }

    @Override
    public Map<Integer, List<Transport>> getTransportInDuration(Date startTime, Date endTime) {
        return null;
    }

    /**
     * 更新库存记录
     *
     */
    public void updateOutputRecord() {
        //这里只需要获取当前时间即可
        Date dutyStartTime = ReportFormDateUtil.getNowDutyStartTime(new Date());
        ReportFormOutputStoreRecord lastStoreRecord = outputService.getLastStoreRecord(dutyStartTime);
        ReportFormOutputStoreRecord currentStoreRecord = outputService.getOutputStoreRecord(dutyStartTime).get(TYPE_STORE);

        //获取当班生产
        Map<Integer, ReportFormOutputStoreRecord> outputStoreRecord = outputService.getOutputStoreRecord(dutyStartTime);

        Map<Integer, TransportSaleStatistics> saleStatisticsOutwardMap = transBean.getSaleStatisticsOutwardMap();
        Map<Integer, TransportSaleStatistics> saleStatisticsLocalityMap = transBean.getSaleStatisticsLocalityMap();

        if(currentStoreRecord==null){
            currentStoreRecord=outputService.getCurrentStoreRecord(dutyStartTime,lastStoreRecord);
        }
        Double washedCoalOutWardVolunm = 0.0;
        if(saleStatisticsOutwardMap.containsKey(ReportFormConstant.COAL_TYPE_WASHED_NUM) && saleStatisticsOutwardMap.get(ReportFormConstant.COAL_TYPE_WASHED_NUM).getCoalVolunm()!=null){
            washedCoalOutWardVolunm = saleStatisticsOutwardMap.get(ReportFormConstant.COAL_TYPE_WASHED_NUM).getCoalVolunm();
        }

        Double washedCoalLocalityVolunm=0.0;
        if(saleStatisticsLocalityMap.containsKey(ReportFormConstant.COAL_TYPE_WASHED_NUM) && saleStatisticsLocalityMap.get(ReportFormConstant.COAL_TYPE_WASHED_NUM).getCoalVolunm()!=null){
            washedCoalLocalityVolunm=saleStatisticsLocalityMap.get(ReportFormConstant.COAL_TYPE_WASHED_NUM).getCoalVolunm();
        }
        Double washedCoalValue=lastStoreRecord.getWashedCoal()-washedCoalOutWardVolunm-washedCoalLocalityVolunm;

        //这是减去自用,只有洗混煤才需要减去自用
        washedCoalValue=outputService.getStoreValueWithoutSelfUse(washedCoalValue,outputService.getOtherTargetInfo(dutyStartTime));
        if(outputStoreRecord.containsKey(1) && outputStoreRecord.get(1)!=null &&  outputStoreRecord.get(1).getWashedCoal()!=null){
            washedCoalValue=washedCoalValue+outputStoreRecord.get(1).getWashedCoal();
        }
        currentStoreRecord.setWashedCoal(washedCoalValue);




        Double clencedCoalOutWardVolunm=0.0;
        if(saleStatisticsLocalityMap.containsKey(ReportFormConstant.COAL_TYPE_GAS_REFINDED_NUM) && saleStatisticsLocalityMap.get(ReportFormConstant.COAL_TYPE_GAS_REFINDED_NUM).getCoalVolunm()!=null){
            clencedCoalOutWardVolunm = saleStatisticsLocalityMap.get(ReportFormConstant.COAL_TYPE_GAS_REFINDED_NUM).getCoalVolunm();
        }

        Double clencedCoalLocalityVolunm=0.0;
        if(saleStatisticsOutwardMap.containsKey(ReportFormConstant.COAL_TYPE_GAS_REFINDED_NUM) && saleStatisticsOutwardMap.get(ReportFormConstant.COAL_TYPE_GAS_REFINDED_NUM).getCoalVolunm()!=null){

            clencedCoalLocalityVolunm=saleStatisticsOutwardMap.get(ReportFormConstant.COAL_TYPE_GAS_REFINDED_NUM).getCoalVolunm();
        }

        Double clencedCoalValue=lastStoreRecord.getClenedCoal()-clencedCoalOutWardVolunm-clencedCoalLocalityVolunm;

        if(outputStoreRecord.containsKey(1) && outputStoreRecord.get(1)!=null && outputStoreRecord.get(1).getClenedCoal()!=null){
            clencedCoalValue=clencedCoalValue+outputStoreRecord.get(1).getClenedCoal();
        }
        currentStoreRecord.setClenedCoal(clencedCoalValue);






        Double slimeCoalOutWardVolunm=0.0;
        if(saleStatisticsOutwardMap.containsKey(ReportFormConstant.COAL_TYPE_SLIME_NUM) && saleStatisticsOutwardMap.get(ReportFormConstant.COAL_TYPE_SLIME_NUM).getCoalVolunm()!=null){
            slimeCoalOutWardVolunm=saleStatisticsOutwardMap.get(ReportFormConstant.COAL_TYPE_SLIME_NUM).getCoalVolunm();
        }

        Double slimeCoalLocationtyVolunm=0.0;
        if(saleStatisticsLocalityMap.containsKey(ReportFormConstant.COAL_TYPE_SLIME_NUM) && saleStatisticsLocalityMap.get(ReportFormConstant.COAL_TYPE_SLIME_NUM).getCoalVolunm()!=null){
            slimeCoalLocationtyVolunm=saleStatisticsLocalityMap.get(ReportFormConstant.COAL_TYPE_SLIME_NUM).getCoalVolunm();
        }
        Double slimeValue=lastStoreRecord.getSlime()-slimeCoalOutWardVolunm-slimeCoalLocationtyVolunm;

        if(outputStoreRecord.containsKey(1) && outputStoreRecord.get(1)!=null && outputStoreRecord.get(1).getSlime()!=null){
            slimeValue=slimeValue+outputStoreRecord.get(1).getSlime();
        }
        currentStoreRecord.setSlime(slimeValue);


        outputService.insertOrUpdateRecord(currentStoreRecord);
        Map<Integer, ReportFormOutputStoreRecord> dutyOutputStoreRecords = outputService.getOutputStoreRecord(dutyStartTime);
        dutyOutputStoreRecords.put(2,currentStoreRecord);
    }
}
