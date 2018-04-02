package com.zgiot.app.server.module.reportforms.handler;

import com.google.common.collect.Lists;
import com.zgiot.app.server.module.reportforms.ReportFormsUtils;
import com.zgiot.app.server.module.reportforms.manager.ReportFormsManager;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.CoalAnalysisRecord;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.ReportFormsRecord;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class SpecialHandler implements ReportFormsHandler {
    @Value("${shift.time.day}")
    private int shiftTimeDay;
    @Value("${shift.time.night}")
    private int shiftTimeNight;
    @Autowired
    private HistoryDataService historyDataService;
    @Autowired
    private CommonHandler commonHandler;
    private Set<String> specialSample = new HashSet<>();
    private static final String SPECIAL_THING_CODE = "703";
    public static final String SPECIAL_TARGET = "703生产洗混煤";
    private static final String SOURCE_THING_CODE1 = "1552";
    private static final String SOURCE_THING_CODE2 = "2552";
    private static final Integer VALID_INTERVAL = 60 * 60 * 1000;

    @PostConstruct
    private void init() {
        specialSample.add(SOURCE_THING_CODE1);
        specialSample.add(SOURCE_THING_CODE2);
    }

    @Override
    public List<DataModel> handle(ReportFormsManager manager, ReportFormsRecord record) {

        CoalAnalysisRecord result = generateResultRecord(record);
        CoalAnalysisRecord record1 = (CoalAnalysisRecord) record;
        CoalAnalysisRecord record2 = (CoalAnalysisRecord) manager.getExistRecord(generateQueryAnotherRecordCondition(record));
        if (record2 != null) {
            if (record.getTarget().contains(ReportFormsUtils.AVG_RECORD_KEYWORD)) {
                disposeAvgRecord(manager, record1, record2, result);
            } else {
                disposeCommonRecord(manager, result, record1, record2);
            }
        } else {
            result.setAad(record1.getAad());
            result.setMt(record1.getMt());
            result.setStad(record1.getStad());
            result.setQnetar(record1.getQnetar());
        }
        result.setSystem(0);
        return commonHandler.handle(manager, result);
    }

    public void disposeCommonRecord(ReportFormsManager manager, CoalAnalysisRecord result, CoalAnalysisRecord record1, CoalAnalysisRecord record2) {
        double coalValue1 = getCoalValue(manager, record1);
        double coalValue2 = getCoalValue(manager, record2);
        setParamByCount(result, record1, coalValue1, record2, coalValue2);
    }

    /**
     * 设置参数，参数通过两个设备的参数和煤量计算得到
     *
     * @param result
     * @param record1
     * @param coalValue1
     * @param record2
     * @param coalValue2
     */
    public void setParamByCount(CoalAnalysisRecord result, CoalAnalysisRecord record1, double coalValue1, CoalAnalysisRecord record2, double coalValue2) {
        result.setAad(countParamValue(record1.getAad(), coalValue1, record2.getAad(), coalValue2));
        result.setMt(countParamValue(record1.getMt(), coalValue1, record2.getMt(), coalValue2));
        result.setStad(countParamValue(record1.getStad(), coalValue1, record2.getStad(), coalValue2));
        result.setQnetar(countParamValue(record1.getQnetar(), coalValue1, record2.getQnetar(), coalValue2));
    }

    /**
     * 通过两个设备的参数和煤量计算出703的参数
     *
     * @param paramValue1
     * @param coalValue1
     * @param paramValue2
     * @param coalValue2
     * @return
     */
    private Double countParamValue(Double paramValue1, double coalValue1, Double paramValue2, double coalValue2) {
        Double paramValue;
        if (paramValue1 == null && paramValue2 == null || coalValue1 == 0 && coalValue2 == 0) {
            paramValue = null;
        } else if (paramValue1 != null && paramValue2 != null) {
            paramValue = (paramValue1 * coalValue1 + paramValue2 * coalValue2) / (coalValue1 + coalValue2);
        } else if (paramValue1 != null) {
            paramValue = paramValue1;
        } else {
            paramValue = paramValue2;
        }
        return paramValue;

    }

    /**
     * 构造一个703的record
     *
     * @param record
     * @return
     */
    private CoalAnalysisRecord generateResultRecord(ReportFormsRecord record) {
        CoalAnalysisRecord result = getRelativeRecord(record);
        String target = result.getTarget().replaceAll("\\d+", SPECIAL_THING_CODE);
        result.setTarget(target);
        result.setSample(SPECIAL_THING_CODE);
        return result;
    }

    private CoalAnalysisRecord getRelativeRecord(ReportFormsRecord record) {
        CoalAnalysisRecord result = new CoalAnalysisRecord();
        result.setTarget(record.getTarget());
        result.setTime(record.getTime());
        result.setSystem(record.getSystem());
        return result;
    }

    /**
     * 构造和该记录对应的另一个设备记录的查询条件
     *
     * @param record
     * @return
     */
    private CoalAnalysisRecord generateQueryAnotherRecordCondition(ReportFormsRecord record) {
        CoalAnalysisRecord result = getRelativeRecord(record);
        String anotherSample = getAnotherSample(record);
        result.setSample(anotherSample);
        return result;
    }

    /**
     * 根据当前设备查到另一个设备号
     *
     * @param record
     * @return
     */
    private String getAnotherSample(ReportFormsRecord record) {
        String sample = record.getSample();
        String anotherSample;
        if (SOURCE_THING_CODE1.equals(sample)) {
            anotherSample = SOURCE_THING_CODE2;
        } else {
            anotherSample = SOURCE_THING_CODE1;
        }
        return anotherSample;
    }


    /**
     * 获取煤量
     *
     * @param manager
     * @param currentRecord
     * @return
     */
    private Double getCoalValue(ReportFormsManager manager, CoalAnalysisRecord currentRecord) {
        Double coalValue;
        CoalAnalysisRecord recentRecord = (CoalAnalysisRecord) manager.getRecentRecord(currentRecord);
        //如果是该班的第一条数据，煤量为当前时间的班累计
        if (recentRecord == null || isFirstRecordOnDuty(recentRecord.getTime(), currentRecord.getTime())) {
            coalValue = getClassCoalByCurrentRecordTime(currentRecord);
        }
        //如果不是第一条，煤量为该数据与上一条数据班累计的差
        else {
            coalValue = getClassCoalByCurrentAndRecentRecordTime(currentRecord, recentRecord);
        }
        return coalValue;

    }

    /**
     * 取该条化验数据与上条化验数据（相同化验项目）的班累计量的差作为煤量
     *
     * @param currentRecord
     * @param recentRecord
     * @return
     */
    private double getClassCoalByCurrentAndRecentRecordTime(CoalAnalysisRecord currentRecord, CoalAnalysisRecord recentRecord) {
        double coalValue = 0.0;
        DataModel recentClassCoal = getClassCoalDataModel(recentRecord);
        DataModel currentClassCoal = getClassCoalDataModel(currentRecord);
        if (recentClassCoal != null && currentClassCoal != null) {
            coalValue = Double.parseDouble(currentClassCoal.getValue()) - Double.valueOf(recentClassCoal.getValue());
        }
        return coalValue;
    }

    /**
     * 取该条化验数据的时间点的班累计量作为煤量
     *
     * @param currentRecord
     * @return
     */
    private double getClassCoalByCurrentRecordTime(CoalAnalysisRecord currentRecord) {
        double coalValue = 0.0;
        DataModel currentClassCoal = historyDataService.findClosestHistoryDataInDuration(Lists.newArrayList(currentRecord.getSample()), Lists.newArrayList(MetricCodes.CT_C), currentRecord.getTime(), HistoryDataService.QUERY_TIME_TYPE_BEFORE);
        if (currentClassCoal != null) {
            coalValue = Double.parseDouble(currentClassCoal.getValue());
        }
        return coalValue;
    }


    private void disposeAvgRecord(ReportFormsManager manager, CoalAnalysisRecord avgRecord, CoalAnalysisRecord anotherAvgRecord, CoalAnalysisRecord resultRecord) {

        avgRecord.setTarget(StringUtils.chomp(avgRecord.getTarget(), ReportFormsUtils.AVG_RECORD_KEYWORD));
        anotherAvgRecord.setTarget(StringUtils.chomp(anotherAvgRecord.getTarget(), ReportFormsUtils.AVG_RECORD_KEYWORD));
        double classCoal1 = getAvgClassCoal(manager, avgRecord);
        double classCoal2 = getAvgClassCoal(manager, anotherAvgRecord);
        setParamByCount(resultRecord, avgRecord, classCoal1, anotherAvgRecord, classCoal2);

    }

    /**
     * 平均记录的煤量是该班最后一条数据的时间点的班累计量
     *
     * @param manager
     * @param avgRecord
     * @return
     */
    private double getAvgClassCoal(ReportFormsManager manager, CoalAnalysisRecord avgRecord) {
        //获取该班该项目的最后一条化验数据的时间（为了计算班累计量）
        CoalAnalysisRecord lastRecordOnDuty = (CoalAnalysisRecord) manager.getLastRecordOnDuty(avgRecord, DateUtils.addHours(avgRecord.getTime(), 12));
        double classCoal = 0.0;
        if (lastRecordOnDuty != null) {
            classCoal = getClassCoalByCurrentRecordTime(lastRecordOnDuty);
        }
        return classCoal;
    }


    /**
     * 取某一时刻的班累计量（最多往前取一个小时）
     *
     * @param record
     * @return
     */
    private DataModel getClassCoalDataModel(CoalAnalysisRecord record) {
        DataModel recentClassCoal = historyDataService.findClosestHistoryDataInDuration(Lists.newArrayList(record.getSample()), Lists.newArrayList(MetricCodes.CT_C), record.getTime(), HistoryDataService.QUERY_TIME_TYPE_BEFORE);
        if (recentClassCoal != null && record.getTime().getTime() - recentClassCoal.getDataTimeStamp().getTime() > VALID_INTERVAL) {
            recentClassCoal = null;
        }
        return recentClassCoal;
    }

    /**
     * 判断该条数据是否为该班的第一条数据
     *
     * @param recentTime
     * @param currentTime
     * @return
     */
    private boolean isFirstRecordOnDuty(Date recentTime, Date currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(recentTime);
        int recentHour = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.setTime(currentTime);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        return (recentHour < shiftTimeDay && currentHour >= shiftTimeDay) ||
                (recentHour < shiftTimeNight && currentHour >= shiftTimeNight);
    }


    @Override
    public boolean isMatch(ReportFormsRecord record) {
        return (record instanceof CoalAnalysisRecord) && specialSample.contains(record.getSample());
    }


}
