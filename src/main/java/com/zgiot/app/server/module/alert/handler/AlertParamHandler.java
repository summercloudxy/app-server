package com.zgiot.app.server.module.alert.handler;

import com.zgiot.app.server.module.alert.AlertManager;
import com.zgiot.app.server.module.alert.pojo.AlertData;
import com.zgiot.app.server.module.alert.pojo.AlertRule;
import com.zgiot.app.server.service.MetricService;
import com.zgiot.common.constants.AlertConstants;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.pojo.MetricModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xiayun on 2017/9/26.
 */
@Component
public class AlertParamHandler implements AlertHandler {
    @Autowired
    private AlertManager alertManager;
    @Autowired
    private MetricService metricService;
    private Map<String, Map<String, AlertData>> alertDataCache;

    @Value("${alert.param.period}")
    private Long paramAlertUpdatePeriod;
    private final Logger logger = LoggerFactory.getLogger(AlertParamHandler.class);

    //待报警
    private Map<String, Map<String, AlertData>> waitAlertDataCache = new ConcurrentHashMap<>();

    @Override
    public void check(DataModel dataModel) {
        //获取已有报警缓存
        alertDataCache = alertManager.getAlertParamDataMap();
        String thingCode = dataModel.getThingCode();
        String metricCode = dataModel.getMetricCode();
        Double value = Double.parseDouble(dataModel.getValue());

        //根据参数获取报警级别
        Map<String, Map<String, List<AlertRule>>> alertRuleMap = alertManager.getParamRuleMap();
        AlertRule alertRule = getAlertRule(alertRuleMap,thingCode, metricCode, value);
        //获取当前报警信息
        AlertData alertData = alertManager.getAlertDataByThingAndMetricCode(thingCode, metricCode);
        if(alertData!=null){
            if(alertRule!=null){
                //1.有警报,报警级别不为空
                //这种情况可能存在待解除报警数据,需要清除待解除报警中数据
                clearRelieveCache(thingCode,metricCode);
                //更新报警等级
                updateAlertData(alertData,alertRule);
            }else{
                //2.有警报，警报级别为空,改变待解除Map内容
                alertData.setParamValue(value);
                updateRelieveCache(thingCode,metricCode,alertData);
            }
        }else{
            if(alertRule!=null){
                //4.无警报，警报级别不为空
                alertData = createAlertData(dataModel, alertRule,value);
                //清除待解除报警Map中数据
                clearRelieveCache(thingCode,metricCode);
                if(alertRule.getDelayTime()==0){
                    //立即报警,
                    addAlertData(thingCode,metricCode,alertData);
                }else{
                    //改变待报警Map内容
                    updateWaitCache(thingCode,metricCode,alertData);
                }
            }else{
                //3.无警报,警报级别为空，清除待报警缓存
                clearWaitCache(thingCode,metricCode);
            }
        }
    }

    /**
     * 解除警报
     */
    @Scheduled(cron = "0/1 * * * * ?")
    public void relieveAlertData(){
        //获取已有待解除报警缓存
        Map<String, Map<String, AlertData>> relieveAlertDataCache = alertManager.getRelieveAlertDataCache();
        for (Map.Entry<String,Map<String,AlertData>> entry:relieveAlertDataCache.entrySet()) {
            String thingCode = entry.getKey();
            Map<String, AlertData> relieveMetricAlertDataMap = entry.getValue();
            for (Map.Entry<String,AlertData> metricEntry:relieveMetricAlertDataMap.entrySet()) {
                String metricCode = metricEntry.getKey();
                AlertData relieveAlertDate = metricEntry.getValue();
                if(System.currentTimeMillis()-relieveAlertDate.getStartDelayTime().getTime()>paramAlertUpdatePeriod){
                    clearAlertData(thingCode,metricCode);
                }
            }
        }
    }

    /**
     * 清除报警
     * @param thingCode
     * @param metricCode
     */
    public void clearAlertData(String thingCode,String metricCode){
        //获取已有警报Map
        alertDataCache = alertManager.getAlertParamDataMap();
        if(alertDataCache.containsKey(thingCode) && alertDataCache.get(thingCode).containsKey(metricCode)){
            AlertData alertData = alertDataCache.get(thingCode).get(metricCode);
            alertData.setRecovery(true);
            if (!alertData.isManualIntervention()) {
                alertManager.releaseAlert(alertData);
                //清除已有报警缓存
                alertDataCache.get(thingCode).remove(metricCode);
                //清除待解除报警缓存
                clearRelieveCache(thingCode,metricCode);
            }
        }
    }

    /**
     * 定时从待报警中拿出数据，并且判断是否到达报警时间
     */
    @Scheduled(cron = "0/1 * * * * ?")
    public void waitChangeAlert(){
        for (Map.Entry<String,Map<String,AlertData>> entry:waitAlertDataCache.entrySet()) {
            String thingCode = entry.getKey();
            Map<String, AlertData> waitMetricAlertDataMap = entry.getValue();
            for (Map.Entry<String,AlertData> metricEntry:waitMetricAlertDataMap.entrySet()) {
                String metricCode=metricEntry.getKey();
                AlertData waitAlertData=metricEntry.getValue();
                //待报警转为报警
                Map<String, Map<String, List<AlertRule>>> alertRuleMap = alertManager.getParamRuleMap();
                AlertRule alertLevel = getAlertRule(alertRuleMap,thingCode, metricCode, waitAlertData.getParamValue());
                if(alertLevel!=null && System.currentTimeMillis()-waitAlertData.getStartDelayTime().getTime()>alertLevel.getDelayTime()*1000){
                    addAlertData(thingCode,metricCode,waitAlertData);
                }

            }
        }
    }

    /**
     * 获取报警级别
     * @param thingCode
     * @param metricCode
     * @param value
     * @return
     */
    public AlertRule getAlertRule(Map<String, Map<String, List<AlertRule>>> alertRuleMap,String thingCode, String metricCode, double value) {

        if (alertRuleMap.containsKey(thingCode) && alertRuleMap.get(thingCode).containsKey(metricCode)) {
            //根据设备编码和信号编码都存在报警规则对象进行数据处理
            List<AlertRule> alertRuleList = alertRuleMap.get(thingCode).get(metricCode);
            for (AlertRule alertRule : alertRuleList) {
                if (value < alertRule.getUpperLimit() && value >= alertRule.getLowerLimit()) {
                    return alertRule;
                }
            }
        }
        return null;
    }

    /**
     * 改变待解除报警缓存内容
     * @param thingCode
     * @param metricCode
     * @param alertData
     */
    public void updateRelieveCache(String thingCode,String metricCode,AlertData alertData){
        Map<String, Map<String, AlertData>> relieveAlertDataCache = alertManager.getRelieveAlertDataCache();
        if(!relieveAlertDataCache.containsKey(thingCode) || !relieveAlertDataCache.get(thingCode).containsKey(metricCode)){
            //将报警数据放入待解除中
            alertData.setStartDelayTime(new Date());
            Map<String, AlertData> relieveMetricAlertDataCache;
            if(relieveAlertDataCache.containsKey(thingCode)){
                relieveMetricAlertDataCache=relieveAlertDataCache.get(thingCode);
            }else{
                relieveMetricAlertDataCache=new ConcurrentHashMap<>();
                relieveAlertDataCache.put(thingCode,relieveMetricAlertDataCache);
            }
            relieveMetricAlertDataCache.put(metricCode,alertData);
        }
    }

    /**
     * 新增报警
     * @param thingCode
     * @param metricCode
     * @param alertData
     */
    public void addAlertData(String thingCode,String metricCode,AlertData alertData){
        //清除待报警缓存
        clearWaitCache(thingCode,metricCode);

        //在数据库新增一条报警信息
        alertManager.generateAlert(alertData);
        //更新缓存
        Map<String, AlertData> metricAlertDataCache;
        if (alertDataCache.containsKey(thingCode)) {
            metricAlertDataCache = alertDataCache.get(thingCode);
        } else {
            metricAlertDataCache = new ConcurrentHashMap<>();
            alertDataCache.put(thingCode, metricAlertDataCache);
        }
        //讲最后的数据放入到alertDataCache中
        metricAlertDataCache.put(metricCode, alertData);
        logger.debug("生成一条参数类报警，thing:{},metric:{}", thingCode, metricCode);
    }

    /**
     * 改变待报警缓存内容
     * @param thingCode
     * @param metricCode
     * @param alertData
     */
    public void updateWaitCache(String thingCode,String metricCode,AlertData alertData){
        if(waitAlertDataCache.containsKey(thingCode) && waitAlertDataCache.get(thingCode).containsKey(metricCode)){
            waitAlertDataCache.get(thingCode).get(metricCode).setParamValue(alertData.getParamValue());
        }else{
            Map<String, AlertData> waitMetricAlertDataCache;
            if(waitAlertDataCache.containsKey(thingCode)){
                waitMetricAlertDataCache=waitAlertDataCache.get(thingCode);
            }else{
                waitMetricAlertDataCache=new ConcurrentHashMap<>();
                waitAlertDataCache.put(thingCode,waitMetricAlertDataCache);
            }
            //设置开始延迟时间
            alertData.setStartDelayTime(new Date());
            waitMetricAlertDataCache.put(metricCode,alertData);
        }
    }

    /**
     * 创建报警对象alertData
     * @param dataModel
     * @param alertRule
     * @return
     */
    public AlertData createAlertData(DataModel dataModel,AlertRule alertRule,Double value){
        //根据信号获取信号模型
        String alertInfo = getAlertInfo(dataModel);

        AlertData alertData=new AlertData(dataModel, AlertConstants.TYPE_PARAM, alertRule.getAlertLevel(), alertInfo,
                AlertConstants.SOURCE_SYSTEM, AlertConstants.REPORTER_SYSTEM);
        alertData.setParamValue(Double.parseDouble(dataModel.getValue()));
        alertData.setParamLower(alertRule.getLowerLimit());
        alertData.setParamUpper(alertRule.getUpperLimit());
        alertData.setLastUpdateTime(new Date());
        alertData.setParamValue(value);
        return alertData;
    }

    public String getAlertInfo(DataModel dataModel) {
        MetricModel metricModel = metricService.getMetric(dataModel.getMetricCode());
        BigDecimal valueStr = new BigDecimal(dataModel.getValue());
        //精确到两位小数
        valueStr = valueStr.setScale(2, BigDecimal.ROUND_HALF_UP);
        //描述信息
        return metricModel.getMetricName() + "-" + valueStr + metricModel.getValueUnit();
    }


    /**
     * 清除待报警缓存中数据
     * @param thingCode
     * @param metricCode
     */
    public void clearWaitCache(String thingCode,String metricCode){
        if(waitAlertDataCache.containsKey(thingCode) && waitAlertDataCache.get(thingCode).containsKey(metricCode)){
            Map<String, AlertData> waitMetricAlertDataCache = waitAlertDataCache.get(thingCode);
            waitMetricAlertDataCache.remove(metricCode);
        }
    }

    /**
     * 清除待解除报警缓存数据
     * @param thingCode
     * @param metricCode
     */
    public void clearRelieveCache(String thingCode,String metricCode){
        Map<String, Map<String, AlertData>> relieveAlertDataCache = alertManager.getRelieveAlertDataCache();
       if(relieveAlertDataCache.containsKey(thingCode)&& relieveAlertDataCache.get(thingCode).containsKey(metricCode)){
           Map<String, AlertData> relieveMetricAlertDataCache = relieveAlertDataCache.get(thingCode);
            relieveMetricAlertDataCache.remove(metricCode);
       }
    }

    /**
     * 改变报警级别
     * @param alertData
     * @param alertRule
     */
    private void updateAlertData(AlertData alertData,AlertRule alertRule) {
        if(alertData.getAlertLevel()!=alertRule.getAlertLevel()){
            alertData.setAlertLevel(alertRule.getAlertLevel());
            alertData.setParamUpper(alertRule.getUpperLimit());
            alertData.setParamLower(alertRule.getLowerLimit());

            //改变报警等级时设置人工不可点击,因为中途可能人工干预，当只要有报警等级之后人工便不可再次点击了
            alertData.setRecovery(false);

            //根据信号编码获取到信号信息
            MetricModel metricModel = metricService.getMetric(alertData.getMetricCode());
            //警报描述信息
            String alertInfo = metricModel.getMetricName() + "-" + alertData.getParamValue()
                    + metricModel.getValueUnit();
            alertData.setAlertInfo(alertInfo);
            //最新操作时间
            alertData.setLastUpdateTime(new Date());
            //更新报警信息
            alertManager.updateAlert(alertData);
            logger.debug("调整报警等级，thingCode {}，metricCode {}，当前等级为{}", alertData.getThingCode(), alertData.getMetricCode(),
                    alertRule.getAlertLevel());
        }
    }

}
