package com.zgiot.app.server.module.reportforms.output.service;

import com.zgiot.app.server.module.reportforms.output.constant.ReportFormConstant;
import com.zgiot.app.server.module.reportforms.output.enums.InfluenceTimeEnum;
import com.zgiot.app.server.module.reportforms.output.enums.InfluenceTimeTypeEnum;
import com.zgiot.app.server.module.reportforms.output.dao.InfluenceTimeMapper;
import com.zgiot.app.server.module.reportforms.output.pojo.*;
import com.zgiot.app.server.module.reportforms.output.productionmonitor.pojo.ReportFormSystemStartRecord;
import com.zgiot.app.server.module.reportforms.output.productionmonitor.service.ReportFormSystemStartService;
import com.zgiot.app.server.module.reportforms.output.utils.ReportFormDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InfluenceTimeServiceImpl implements InfluenceTimeService {

    @Autowired
    private InfluenceTimeMapper influenceTimeMapper;

    private static final InfluenceTimeBean influenceTimeBean=new InfluenceTimeBean();

    @Autowired
    private ReportFormSystemStartService reportFormSystemStartService;

    public void init(){
        //项目启动时初始化
        //根据当前时间查询当前当班时间
        Date nowDutyStartTime = ReportFormDateUtil.getNowDutyStartTime(new Date());
        influenceTimeBean.setDutyStartTime(nowDutyStartTime);

        //根据时间查询备注
        InfluenceTimeRemarks influenceTimeRemarks=influenceTimeMapper.getRemarksByDutyDate(nowDutyStartTime);
        if(influenceTimeRemarks!=null){
            influenceTimeBean.setInfluenceTimeRemarks(influenceTimeRemarks);
        }

        List<InfluenceTime> influenceTimeList = influenceTimeMapper.getInfluenceTimeByDutyDate(nowDutyStartTime);
        //判断，不为空直接放入缓存
        if(influenceTimeList==null || influenceTimeList.size()<1){
            //创建当班影响时间并将数据放入缓存中
            influenceTimeList = createDutyinfluenceTime(nowDutyStartTime);
            for (InfluenceTime influenceTime:influenceTimeList) {
                influenceTimeMapper.createinfluenceTime(influenceTime);
            }
        }
        //更新influenceTimeBean中的List
        influenceTimeBeanUpdateData(influenceTimeList,influenceTimeBean.getInfluenceTimeRsps());
    }


    @Override
    public void influenceTimeService(InfluenceTimeReq influenceTimeReq) {
        //updateRemarks(influenceTimeReq);

        List<InfluenceTime> influenceTimes = influenceTimeReq.getInfluenceTimeList();
        if(influenceTimes!=null && influenceTimes.size()>0){
        //根据当班开始时间查询数据库是否存在数据
        List<InfluenceTime> influenceTimeByDutyList = influenceTimeMapper.getInfluenceTimeByDutyDate(influenceTimeReq.getDutyStartTime());
        List<InfluenceTime> dutyinfluenceTimeList = createDutyinfluenceTime(influenceTimeReq.getDutyStartTime());

        if(influenceTimeByDutyList!=null && influenceTimeByDutyList.size()>0){
            editinfluenceTime(influenceTimeReq.getDutyStartTime(), influenceTimes, influenceTimeByDutyList, dutyinfluenceTimeList);
        }else{
            addFulenceTime(influenceTimes, dutyinfluenceTimeList);
        }
        }

    }

    public void handle(List<ReportFormSystemStartRecord> reportList) {
        if(reportList!=null && reportList.size()>0){
            //因为都是一个班的数据,所以这里取出第一个的当班开始时间即可
            //为避免不一致,进行转换一下
            Date dutyStartTime = ReportFormDateUtil.getNowDutyStartTime(reportList.get(0).getDutyStartTime());

            //将数据进行封装
            List<InfluenceTime> influenceTimes=new ArrayList<>();
            for (ReportFormSystemStartRecord report:reportList) {
                setInfluenceTimes(influenceTimes, report);
            }

            setInfluenctTimeData(dutyStartTime, influenceTimes);

            //根据当班时间查询数据
            List<InfluenceTime> influenceTimeByDutyList = influenceTimeMapper.getInfluenceTimeByDutyDate(dutyStartTime);
            List<InfluenceTime> dutyinfluenceTimeList = createDutyinfluenceTime(dutyStartTime);
            if(influenceTimeByDutyList!=null && influenceTimeByDutyList.size()>0){
                editinfluenceTime(dutyStartTime, influenceTimes, influenceTimeByDutyList, dutyinfluenceTimeList);
            }else{
                addFulenceTime(influenceTimes, dutyinfluenceTimeList);
            }
        }
    }

    private void setInfluenctTimeData(Date dutyStartTime, List<InfluenceTime> influenceTimes) {
        Set<Integer> integers = InfluenceTimeTypeEnum.influenceTypeCodes();
        for (Integer type:integers) {
            Integer oneNum=0;
            Integer twoNum=0;
            for (InfluenceTime influence:influenceTimes) {
                if(influence.getInfluenceType().equals(type) && influence.getTerm().equals(ReportFormConstant.INFLUENCE_TIME_ONE_TERM)){
                    oneNum=1;
                }

                if(influence.getInfluenceType().equals(type) && influence.getTerm().equals(ReportFormConstant.INFLUENCE_TIME_TWO_TERM)){
                    twoNum=1;
                }
            }

            if(oneNum==0){
                InfluenceTime oneInfluence=new InfluenceTime();
                oneInfluence.setDutyStartTime(dutyStartTime);
                oneInfluence.setTerm(ReportFormConstant.INFLUENCE_TIME_ONE_TERM);
                oneInfluence.setClassDuration(0L);
                oneInfluence.setMonthDuration(0L);
                oneInfluence.setYearDuration(0L);
                oneInfluence.setInfluenceType(type);
                influenceTimes.add(oneInfluence);
            }

            if(twoNum==0){
                InfluenceTime twoInfluence=new InfluenceTime();
                twoInfluence.setDutyStartTime(dutyStartTime);
                twoInfluence.setTerm(ReportFormConstant.INFLUENCE_TIME_TWO_TERM);
                twoInfluence.setClassDuration(0L);
                twoInfluence.setMonthDuration(0L);
                twoInfluence.setYearDuration(0L);
                twoInfluence.setInfluenceType(type);
                influenceTimes.add(twoInfluence);
            }

        }
    }

    private void setInfluenceTimes(List<InfluenceTime> influenceTimes, ReportFormSystemStartRecord report) {
        if(report.getProductionDescription()==null){
            return;
        }
        InfluenceTime influence=null;
        for (InfluenceTime influenceTime:influenceTimes) {
            if(influenceTime.getInfluenceType().equals(report.getProductionDescription().intValue()) && influenceTime.getTerm().equals(report.getTerm())){
                influence=influenceTime;
            }
        }

        if(influence!=null){
            Long influenceClass=0L;
            Long startCar=0L;
            if(influence.getClassDuration()!=null){
                influenceClass=influence.getClassDuration();
            }
            if(report.getDuration()!=null){
                startCar=report.getDuration();
            }
            influence.setClassDuration(influenceClass+startCar);
        }else {
            influence=new InfluenceTime();
            influence.setDutyStartTime(report.getDutyStartTime());
            influence.setInfluenceType(report.getProductionDescription().intValue());
            influence.setTerm(report.getTerm());
            influence.setClassDuration(report.getDuration());
            influenceTimes.add(influence);
        }
    }

    @Override
    public InfluenceTimeBean getData(Date date) {
        Date dutyStartTime = ReportFormDateUtil.getNowDutyStartTime(date);

        //每次在获取报表数据之前需要将开车情况数据进行设置
        Map<Integer, List<ReportFormSystemStartRecord>> systemStartRecords = reportFormSystemStartService.getSystemStartRecords(date);
        List<ReportFormSystemStartRecord> list=new ArrayList<>();
        for (Map.Entry<Integer,List<ReportFormSystemStartRecord>> entry:systemStartRecords.entrySet()) {
            list.addAll(entry.getValue());
        }
        handle(list);

        Date nowDutyStartTime = ReportFormDateUtil.getNowDutyStartTime(new Date());
        if(nowDutyStartTime.equals(dutyStartTime) && influenceTimeBean.getInfluenceTimeRsps().size()>0){
            return influenceTimeBean;
        }else{
            InfluenceTimeBean influenceTimeBeanRsp=new InfluenceTimeBean();
            influenceTimeBeanRsp.setDutyStartTime(dutyStartTime);

            //根据时间查询备注
            InfluenceTimeRemarks influenceTimeRemarks=influenceTimeMapper.getRemarksByDutyDate(dutyStartTime);
            if(influenceTimeRemarks!=null){
                influenceTimeBeanRsp.setInfluenceTimeRemarks(influenceTimeRemarks);
            }

            List<InfluenceTime> influenceTimeList = influenceTimeMapper.getInfluenceTimeByDutyDate(dutyStartTime);
            if(influenceTimeList==null || influenceTimeList.size()<1){
                influenceTimeList = createDutyinfluenceTime(dutyStartTime);
                for (InfluenceTime influenceTime:influenceTimeList) {
                    influenceTimeMapper.createinfluenceTime(influenceTime);
                }
            }
            //返回新Bean
            influenceTimeBeanUpdateData(influenceTimeList,influenceTimeBeanRsp.getInfluenceTimeRsps());
            return influenceTimeBeanRsp;
        }
    }

    @Override
    public void updatePersonnel(InfluenceTimeRemarks influenceTimeRemarks) {
        InfluenceTimeRemarks influence= influenceTimeMapper.InfluenceTimeRemarks(influenceTimeRemarks.getDutyStartTime());
        if(influence!=null){
            //修改
            influenceTimeRemarks.setId(influence.getId());
            influenceTimeMapper.editPersonnel(influenceTimeRemarks);
        }else{
            //新增
            influenceTimeMapper.createPersonnel(influenceTimeRemarks);
        }

        InfluenceTimeRemarks personnel = influenceTimeBean.getInfluenceTimeRemarks();
        if(personnel!=null){
            personnel.setDutyStartTime(influenceTimeRemarks.getDutyStartTime());
            personnel.setRemarks(influenceTimeRemarks.getRemarks());
            personnel.setDispatcher(influenceTimeRemarks.getDispatcher());
            personnel.setChecker(influenceTimeRemarks.getChecker());
            personnel.setFactoryDutyLeader(influenceTimeRemarks.getFactoryDutyLeader());
        }else{
            influenceTimeBean.setInfluenceTimeRemarks(influenceTimeRemarks);
        }
    }

    private void addFulenceTime(List<InfluenceTime> influenceTimes, List<InfluenceTime> dutyinfluenceTimeList) {
        //表明没有数据
        for (InfluenceTime influenceTimeDuty:dutyinfluenceTimeList) {
            for (InfluenceTime influenceTime:influenceTimes) {
                if(influenceTimeDuty.getInfluenceType().equals(influenceTime.getInfluenceType()) && influenceTimeDuty.getTerm().equals(influenceTime.getTerm()) && influenceTime.getClassDuration()!=null){
                    influenceTimeDuty.setClassDuration(influenceTime.getClassDuration());
                    influenceTimeDuty.setMonthDuration(influenceTime.getClassDuration()+influenceTimeDuty.getMonthDuration());
                    influenceTimeDuty.setYearDuration(influenceTime.getClassDuration()+influenceTimeDuty.getYearDuration());
                }
            }

            influenceTimeMapper.createinfluenceTime(influenceTimeDuty);
        }
    }

    private void editinfluenceTime(Date dutyStartTime, List<InfluenceTime> influenceTimes, List<InfluenceTime> influenceTimeByDutyList, List<InfluenceTime> dutyinfluenceTimeList) {
        //表明有数据
        for (InfluenceTime influenceTime:influenceTimes) {

            for (InfluenceTime dutyinfluenceTime:dutyinfluenceTimeList) {
                if(influenceTime.getInfluenceType().equals(dutyinfluenceTime.getInfluenceType()) && influenceTime.getTerm().equals(dutyinfluenceTime.getTerm()) && influenceTime.getClassDuration()!=null){
                    //类型和期数相同,设置月累计和年累计
                    setInitMonthAndYear(dutyinfluenceTime);
                    influenceTime.setMonthDuration(influenceTime.getClassDuration()+dutyinfluenceTime.getMonthDuration());
                    influenceTime.setYearDuration(influenceTime.getClassDuration()+dutyinfluenceTime.getYearDuration());
                    break;
                }

            }

            //遍历数据库的数据然后设置id
            for (InfluenceTime influenceTimeDuty:influenceTimeByDutyList) {
                if(influenceTime.getInfluenceType().equals(influenceTimeDuty.getInfluenceType()) && influenceTime.getTerm().equals(influenceTimeDuty.getTerm())){
                    influenceTime.setId(influenceTimeDuty.getId());
                    break;
                }
            }
            influenceTimeMapper.editinfluenceTime(influenceTime);
        }
        if(ReportFormDateUtil.getNowDutyStartTime(new Date()).equals(dutyStartTime)){
            influenceTimeBeanUpdateData(influenceTimes,influenceTimeBean.getInfluenceTimeRsps());
        }
    }

    private void setInitMonthAndYear(InfluenceTime dutyinfluenceTime) {
        if(dutyinfluenceTime.getMonthDuration()==null){
            dutyinfluenceTime.setMonthDuration(0L);
        }
        if(dutyinfluenceTime.getYearDuration()==null){
            dutyinfluenceTime.setYearDuration(0L);
        }
    }

    /**
     * 更新备注并缓存中也同步
     * @param influenceTimeReq
     */
    private void updateRemarks(InfluenceTimeReq influenceTimeReq) {
        //表明有写备注
        if(influenceTimeReq.getInfluenceTimeRemarks()!=null){
            InfluenceTimeRemarks influenceTimeRemarksNowDate= influenceTimeMapper.InfluenceTimeRemarks(influenceTimeReq.getDutyStartTime());
            if(influenceTimeRemarksNowDate!=null){
                influenceTimeReq.getInfluenceTimeRemarks().setId(influenceTimeRemarksNowDate.getId());
                //表明是更改
                influenceTimeMapper.editinfluenceTimeRemarks(influenceTimeReq.getInfluenceTimeRemarks());
            }else{
                //新增
                influenceTimeMapper.createinfluenceTimeRemarks(influenceTimeReq.getInfluenceTimeRemarks());
            }

            if(influenceTimeBean.getInfluenceTimeRemarks()!=null){
                InfluenceTimeRemarks influenceTimeRemarks = influenceTimeBean.getInfluenceTimeRemarks();
                influenceTimeRemarks.setDutyStartTime(influenceTimeReq.getInfluenceTimeRemarks().getDutyStartTime());
                influenceTimeRemarks.setRemarks(influenceTimeReq.getInfluenceTimeRemarks().getRemarks());
            }else{
                influenceTimeBean.setInfluenceTimeRemarks(influenceTimeReq.getInfluenceTimeRemarks());
            }
        }
    }


    /**
     * 更新Bean中数据(有数据设置,没有数据新增)
     * @param influenceTimeList
     */
    private void influenceTimeBeanUpdateData(List<InfluenceTime> influenceTimeList, List<InfluenceTimeRsp> influenceTimeRsps) {
        for (InfluenceTime influenceTime:influenceTimeList) {
            int index=-1;
            for(int i=0;i<influenceTimeRsps.size();i++){
                //无论遍历第一期还是第二期都会有这个类型
                if(influenceTime.getInfluenceType().equals(influenceTimeRsps.get(i).getInfluenceType())){
                    index=i;
                    break;
                }
            }

            InfluenceTimeRsp influenceTimeRsp;
            if(index==-1) {
                //表明不存在数据
                //根据influenceTime创建influenceTimeRsps
                influenceTimeRsp = new InfluenceTimeRsp();
                influenceTimeRsp.setDutyStratTime(influenceTime.getDutyStartTime());
                influenceTimeRsp.setInfluenceType(influenceTime.getInfluenceType());
                influenceTimeRsps.add(influenceTimeRsp);
            }else{
                //表明存在数据
                influenceTimeRsp=influenceTimeRsps.get(index);
            }
            influenceTimeToRsp(influenceTime, influenceTimeRsp);
        }
    }

    /**
     * 将InfuleneTime对象数据封装到influenceTimeRsp
     * @param influenceTime
     * @param influenceTimeRsp
     */
    private void influenceTimeToRsp(InfluenceTime influenceTime, InfluenceTimeRsp influenceTimeRsp) {
        if(influenceTime.getTerm().equals(ReportFormConstant.INFLUENCE_TIME_ONE_TERM)){
            //表明是一期数据
            influenceTimeRsp.setOneTermId(influenceTime.getId());
            influenceTimeRsp.setOneTermDutyTimeLong(influenceTime.getClassDuration());
            influenceTimeRsp.setOneTermMonthTimeLong(influenceTime.getMonthDuration());
            influenceTimeRsp.setOneTermYearTimeLong(influenceTime.getYearDuration());
            setOneDutyVolunm(influenceTime, influenceTimeRsp);
        }else{
            //二期数据
            influenceTimeRsp.setTwoTermId(influenceTime.getId());
            influenceTimeRsp.setTwoTermDutyTimeLong(influenceTime.getClassDuration());
            influenceTimeRsp.setTwoTermMonthTimeLong(influenceTime.getMonthDuration());
            influenceTimeRsp.setTwoTermYearTimeLong(influenceTime.getYearDuration());
            setTwoDutyVolunm(influenceTime, influenceTimeRsp);
        }
    }

    private void setTwoDutyVolunm(InfluenceTime influenceTime, InfluenceTimeRsp influenceTimeRsp) {
        if(influenceTime.getClassDuration()!=null){
            if(influenceTimeRsp.getOneTermDutyTimeLong()!=null){
                influenceTimeRsp.setDutyVolunm((influenceTimeRsp.getOneTermDutyTimeLong()+influenceTime.getClassDuration())/2.0);
            }else{
                influenceTimeRsp.setDutyVolunm(influenceTime.getClassDuration()/2.0);
            }
        }else{
            if(influenceTimeRsp.getOneTermDutyTimeLong()!=null){
                influenceTimeRsp.setDutyVolunm(influenceTimeRsp.getOneTermDutyTimeLong()/2.0);
            }else{
                influenceTimeRsp.setDutyVolunm(null);
            }
        }

        if(influenceTime.getMonthDuration()==null){
            influenceTime.setMonthDuration(0L);
        }
        if(influenceTimeRsp.getOneTermMonthTimeLong()==null){
            influenceTimeRsp.setOneTermMonthTimeLong(0L);
        }
        influenceTimeRsp.setMonthVolunm((influenceTime.getMonthDuration()+influenceTimeRsp.getOneTermMonthTimeLong())/2.0);

        if(influenceTime.getYearDuration()==null){
            influenceTime.setYearDuration(0L);
        }
        if(influenceTimeRsp.getOneTermYearTimeLong()==null){
            influenceTimeRsp.setOneTermYearTimeLong(0L);
        }
        influenceTimeRsp.setYearVolunm((influenceTime.getYearDuration()+influenceTimeRsp.getOneTermYearTimeLong())/2.0);

    }


    private void setOneDutyVolunm(InfluenceTime influenceTime, InfluenceTimeRsp influenceTimeRsp) {
        if(influenceTime.getClassDuration()!=null){
            if(influenceTimeRsp.getTwoTermDutyTimeLong()!=null){
                influenceTimeRsp.setDutyVolunm((influenceTimeRsp.getTwoTermDutyTimeLong()+influenceTime.getClassDuration())/2.0);
            }else{
                influenceTimeRsp.setDutyVolunm(influenceTime.getClassDuration()/2.0);
            }
        }else{
            if(influenceTimeRsp.getTwoTermDutyTimeLong()!=null){
                influenceTimeRsp.setDutyVolunm(influenceTimeRsp.getTwoTermDutyTimeLong()/2.0);
            }else{
                influenceTimeRsp.setDutyVolunm(null);
            }
        }

        if(influenceTime.getMonthDuration()==null){
            influenceTime.setMonthDuration(0L);
        }
        if(influenceTimeRsp.getTwoTermMonthTimeLong()==null){
            influenceTimeRsp.setTwoTermMonthTimeLong(0L);
        }
        influenceTimeRsp.setMonthVolunm((influenceTime.getMonthDuration()+influenceTimeRsp.getTwoTermMonthTimeLong())/2.0);


        if(influenceTime.getYearDuration()==null){
            influenceTime.setYearDuration(0L);
        }
        if(influenceTimeRsp.getTwoTermYearTimeLong()==null){
            influenceTimeRsp.setTwoTermYearTimeLong(0L);
        }
        influenceTimeRsp.setYearVolunm((influenceTime.getYearDuration()+influenceTimeRsp.getTwoTermYearTimeLong())/2.0);
    }

    /**
     * 创建当班影响时间
     * @param nowDutyStartTime
     */
    private List<InfluenceTime> createDutyinfluenceTime(Date nowDutyStartTime) {
        //获取当前时间之前的最新影响时间数据
        List<InfluenceTime> influenceTimes = getMostNewinfluenceTimeByDate(nowDutyStartTime);

        List<InfluenceTime> influenceTimesList=new ArrayList<>();
        //创建当前班次的影响时间数据
        Set<Integer> integers = InfluenceTimeEnum.influenceTypeCodes();
        for (Integer type:integers) {
            InfluenceTime influenceTimeOneTerm=new InfluenceTime();
            influenceTimeOneTerm.setTerm(ReportFormConstant.INFLUENCE_TIME_ONE_TERM);
            influenceTimeOneTerm.setDutyStartTime(nowDutyStartTime);
            influenceTimeOneTerm.setInfluenceType(type);

            InfluenceTime influenceTimeTwoTerm=new InfluenceTime();
            influenceTimeTwoTerm.setTerm(ReportFormConstant.INFLUENCE_TIME_TWO_TERM);
            influenceTimeTwoTerm.setDutyStartTime(nowDutyStartTime);
            influenceTimeTwoTerm.setInfluenceType(type);

            if(influenceTimes!=null && influenceTimes.size()>0){
                setInfluenceTime(nowDutyStartTime, influenceTimes, type, influenceTimeOneTerm, influenceTimeTwoTerm);
            }else{
                influenceTimeOneTerm.setMonthDuration(0L);
                influenceTimeOneTerm.setYearDuration(0L);
                influenceTimeTwoTerm.setMonthDuration(0L);
                influenceTimeTwoTerm.setYearDuration(0L);
            }
            influenceTimesList.add(influenceTimeOneTerm);
            influenceTimesList.add(influenceTimeTwoTerm);
        }
        return influenceTimesList;
    }

    /**
     * 根据最新集合设置影响时间对象
     * @param nowDutyStartTime
     * @param influenceTimes
     * @param type
     * @param influenceTimeOneTerm
     * @param influenceTimeTwoTerm
     */
    private void setInfluenceTime(Date nowDutyStartTime, List<InfluenceTime> influenceTimes, Integer type, InfluenceTime influenceTimeOneTerm, InfluenceTime influenceTimeTwoTerm) {
        Integer count=0;
        for (InfluenceTime influenceTimeBefore:influenceTimes) {
            if(count==2){
                break;
            }
            if(influenceTimeBefore.getInfluenceType().equals(type)){
                //表明类型相同
                if(influenceTimeBefore.getTerm().equals(ReportFormConstant.INFLUENCE_TIME_ONE_TERM)){
                    //第一期
                    setDuration(influenceTimeOneTerm,nowDutyStartTime,influenceTimeBefore);
                    count=count+1;
                }

                if(influenceTimeBefore.getTerm().equals(ReportFormConstant.INFLUENCE_TIME_TWO_TERM)){
                    //第二期
                    setDuration(influenceTimeTwoTerm,nowDutyStartTime,influenceTimeBefore);
                    count=count+1;
                }
            }

        }
    }

    /**
     * 根据当前时间设置月累计和年累计
     * @param influenceTime
     * @param nowDutyStartTime
     */
    private void setDuration(InfluenceTime influenceTime, Date nowDutyStartTime, InfluenceTime influenceTimeBefore) {
        if(ReportFormDateUtil.isYearFirstDay(nowDutyStartTime)){
            //是一年的第一天
            influenceTime.setMonthDuration(0L);
            influenceTime.setYearDuration(0L);
        }else{
            if(ReportFormDateUtil.isMonthFirstDay(nowDutyStartTime)){
                //月的第一天
                influenceTime.setMonthDuration(0L);
                influenceTime.setYearDuration(influenceTimeBefore.getYearDuration());
            }else{
                //不是一年或者一月的第一天
                influenceTime.setMonthDuration(influenceTimeBefore.getMonthDuration());
                influenceTime.setYearDuration(influenceTimeBefore.getYearDuration());
            }
        }
    }

    /**
     * 根据时间查询最新的影响时间数据（并且根据当前时间判断是不是同一月，或者同一年,如果不是设置为0）
     * @param nowDutyStartTime
     * @return
     */
    private List<InfluenceTime> getMostNewinfluenceTimeByDate(Date nowDutyStartTime) {
        List<InfluenceTime> influenceTimes = influenceTimeMapper.getMostNewinfluenceTimeByDate(nowDutyStartTime);
        if(influenceTimes!=null && influenceTimes.size()>0){
            for (InfluenceTime influenceTime:influenceTimes) {
                //这里需要判断是不是在同一年和同一月
                if(!ReportFormDateUtil.isYearSame(influenceTime.getDutyStartTime(),nowDutyStartTime)){
                    //表明不在同一年
                    influenceTime.setYearDuration(0L);
                }

                if(!ReportFormDateUtil.isMonthSame(influenceTime.getDutyStartTime(),nowDutyStartTime)){
                    //表明不在同一月
                    influenceTime.setMonthDuration(0L);
                }
            }
        }
        return influenceTimes;
    }

    /**
     * 定时任务
     */
    @Scheduled(cron = "0 0 8,20 * * ?")
    public void updateBeanJob(){
        Date nowDutyStartTime = ReportFormDateUtil.getNowDutyStartTime(new Date());
        //重新设置Bean
        influenceTimeBean.setDutyStartTime(nowDutyStartTime);

        influenceTimeBean.setInfluenceTimeRemarks(null);
        //根据时间查询备注
        InfluenceTimeRemarks influenceTimeRemarks=influenceTimeMapper.getRemarksByDutyDate(nowDutyStartTime);
        if(influenceTimeRemarks!=null){
            influenceTimeBean.setInfluenceTimeRemarks(influenceTimeRemarks);
        }

        //清空一下缓存
        List<InfluenceTimeRsp> influenceTimeRsps = influenceTimeBean.getInfluenceTimeRsps();
        influenceTimeRsps.clear();

        List<InfluenceTime> influenceTimeList = influenceTimeMapper.getInfluenceTimeByDutyDate(nowDutyStartTime);
        //判断，不为空直接放入缓存
        if(influenceTimeList==null || influenceTimeList.size()<1){
            //创建当班影响时间并将数据放入缓存中
            influenceTimeList = createDutyinfluenceTime(nowDutyStartTime);
            for (InfluenceTime influenceTime:influenceTimeList) {
                influenceTimeMapper.createinfluenceTime(influenceTime);
            }
        }
        //更新缓存
        influenceTimeBeanUpdateData(influenceTimeList,influenceTimeRsps);
    }

}
