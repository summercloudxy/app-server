package com.zgiot.app.server.module.qualityandquantity.service;

import com.zgiot.app.server.module.coalanalysis.mapper.CoalAnalysisMapper;
import com.zgiot.app.server.module.qualityandquantity.mapper.QualityAndQuantityMapper;
import com.zgiot.app.server.module.qualityandquantity.pojo.*;
import com.zgiot.app.server.module.produtioninspect.mapper.ProductionInspectMapper;
import com.zgiot.app.server.module.qualityandquantity.CardParser;
import com.zgiot.app.server.module.qualityandquantity.CardParserSelector;
import com.zgiot.app.server.module.tcs.pojo.FilterCondition;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.common.pojo.*;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QualityAndQuantityServiceImp implements QualityAndQuantityService {
    @Autowired
    private HistoryDataService historyDataService;
    @Autowired
    private QualityAndQuantityDataManager dataManager;
    @Autowired
    private ProductionInspectMapper productionInspectMapper;
    @Autowired
    private QualityAndQuantityDateUtils dateUtils;
    @Autowired
    private CoalAnalysisMapper coalAnalysisMapper;
    @Autowired
    private CardParserSelector cardParserSelector;
    @Autowired
    private QualityAndQuantityMapper qualityAndQuantityMapper;

    private static final int SORT_TYPE_ASC = 0;






    @Override
    public List<DutyInfoInOneDay> getMaxValueOnDuty(String thingCode, String metricCode, Date startTime, Date endTime) {
        List<DutyInfo> dutyInfos = new ArrayList<>();
        while (startTime.before(endTime)) {
            DutyInfo dutyInfo = dateUtils.getCurrentDutyInfo(startTime);
            Date dutyEndTime = dutyInfo.getDutyEndTime();
            DataModel maxValueDataInDuration = historyDataService.findMaxValueDataInDuration(thingCode, metricCode, startTime, dutyEndTime);
            if (maxValueDataInDuration != null) {
                dutyInfo.setMaxValue(maxValueDataInDuration.getValue());
            }
            startTime = dutyEndTime;
            dutyInfos.add(dutyInfo);
        }
        List<DutyInfoInOneDay> dutyInfoInOneDays = mergeDayAndNightData(dutyInfos);
        Collections.reverse(dutyInfoInOneDays);
        return dutyInfoInOneDays;

    }

    private List<DutyInfoInOneDay> mergeDayAndNightData(List<DutyInfo> dutyInfos){
        Map<Date,DutyInfoInOneDay> mergeDateMap = new TreeMap<>();
        for (DutyInfo dutyInfo:dutyInfos){
            Date dayTime = DateUtils.truncate(dutyInfo.getDutyStartTime(), java.util.Calendar.DATE);
            if (mergeDateMap.containsKey(dayTime)){
                DutyInfoInOneDay dutyInfoInOneDay = mergeDateMap.get(dayTime);
                setDutyInfo(dutyInfoInOneDay,dutyInfo);
            }else {
                DutyInfoInOneDay dutyInfoInOneDay = new DutyInfoInOneDay();
                dutyInfoInOneDay.setDayTime(dayTime);

                setDutyInfo(dutyInfoInOneDay,dutyInfo);
                mergeDateMap.put(dayTime,dutyInfoInOneDay);
            }
        }
        return new ArrayList<>(mergeDateMap.values());

    }


    public void setDutyInfo( DutyInfoInOneDay dutyInfoInOneDay,DutyInfo dutyInfo){
        if (QualityAndQuantityDateUtils.DUTY_TYPE_DAY.equals(dutyInfo.getDutyType())) {
            dutyInfoInOneDay.setDayValue(dutyInfo.getMaxValue());
        }else {
            dutyInfoInOneDay.setNightValue(dutyInfo.getMaxValue());
        }
    }




    @Override
    public List<CoalAnalysisData> getCoalAnalysisHistoryData(FilterCondition filterCondition) {
        if (filterCondition.getPage()!=null&&filterCondition.getCount()!=null){
            filterCondition.setOffset(filterCondition.getPage() * filterCondition.getCount());
        }
        List<CoalAnalysisRecord> recordsMatchCondition = coalAnalysisMapper.getRecordsMatchCondition(filterCondition);
        List<CoalAnalysisData> coalAnalysisDataList = dataManager.transCoalAnalysisRecordToData(recordsMatchCondition);
        return coalAnalysisDataList;
    }


    @Override
    public List<ProductionInspectData> getProductionInspectData(FilterCondition filterCondition){
        if (filterCondition.getPage()!=null&&filterCondition.getCount()!=null){
            filterCondition.setOffset(filterCondition.getPage() * filterCondition.getCount());
        }
        List<ProductionInspectRecord> recordsMatchCondition = productionInspectMapper.getRecordsMatchCondition(filterCondition);
        List<ProductionInspectData> productionInspectDataList = dataManager.transProductionInspectRecordToData(recordsMatchCondition);
        return productionInspectDataList;
    }

    
    @Override
    public List<AreaInfo> getAreaInfos(List<Integer> areaIds){
        List<AreaInfo> areaInfos = new ArrayList<>();
        for (Integer areaId:areaIds){
            AreaInfo areaInfo = qualityAndQuantityMapper.getAreaInfo(areaId);
            List<CardInfo> cardInfosInArea = getCardInfosInArea(areaId);
            areaInfo.setCardInfos(cardInfosInArea);
            areaInfos.add(areaInfo);
        }
        return areaInfos;
    }



    
    private List<CardInfo> getCardInfosInArea(int areaId){
        List<CardInfo> cardInfos = qualityAndQuantityMapper.getCardInfosInArea(areaId);
        for (CardInfo cardInfo:cardInfos){
            CardParser cardParser = cardParserSelector.getParserByName(cardInfo.getParserName());
            if (cardParser!= null) {
                Object parse = cardParser.parse(cardInfo.getCardParamValue());
                cardInfo.setCardDetailInfo(parse);
            }
        }
        return cardInfos;

    }




}
