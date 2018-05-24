package com.zgiot.app.server.module.filterpress;

import com.zgiot.app.server.config.ApplicationContextListener;
import com.zgiot.app.server.module.historydata.job.HistoryMinDataManager;
import com.zgiot.app.server.service.CmdControlService;
import com.zgiot.app.server.service.impl.CmdControlServiceImpl;
import com.zgiot.common.constants.FilterPressMetricConstants;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.pojo.DataModel;
import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Set;

public class FilterPressSignalOperateJob implements Job {
    private static final String SYS_1_YL = "SYS_1_YL";
    private static final String SYS_2_YL = "SYS_2_YL";
    private static final int TERM1 = 1;
    private static final int TERM2 = 2;
    private static final int R_BAN_RUN_FLAG = 256;
    private static final String GB_TERM1 = "1502";
    private static final String GB_TERM2 = "2502";
    private static final Logger LOGGER = LoggerFactory.getLogger(FilterPressSignalOperateJob.class);

    private CmdControlServiceImpl cmdControlService = null;
    private FilterPressManager filterPressManager = null;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("filterPress chain job start!");
        ApplicationContext context = ApplicationContextListener.getApplicationContext();
        CmdControlServiceImpl cmdControlService = (CmdControlServiceImpl) context.getBean("cmdControlServiceImpl");
        DataModel dataModel = new DataModel();
        dataModel.setThingCode(SYS_1_YL);
        dataModel.setMetricCode(FilterPressMetricConstants.YL_LS);
        String lsTerm1Value = cmdControlService.getDataSync(dataModel);
        dataModel.setThingCode(SYS_2_YL);
        String lsTerm2Value = cmdControlService.getDataSync(dataModel);
        dataModel.setThingCode(GB_TERM1);
        dataModel.setMetricCode(FilterPressMetricConstants.STATE);
        String stateTerm1Value = cmdControlService.getDataSync(dataModel);
        dataModel.setThingCode(GB_TERM2);
        String stateTerm2Value = cmdControlService.getDataSync(dataModel);
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("filterPress term1 chain value:" + lsTerm1Value);
            LOGGER.debug("filterPress term2 chain value:" + lsTerm2Value);
            LOGGER.debug(" 1502 state value:" + stateTerm1Value);
            LOGGER.debug(" 2502 state value:" + stateTerm2Value);
        }
        FilterPressManager filterPressManager = (FilterPressManager) context.getBean("filterPressManager");
        Set<String> thingCodesTerm1 = filterPressManager.getAllFilterPressCode(TERM1);
        Set<String> thingCodesTerm2 = filterPressManager.getAllFilterPressCode(TERM2);
        for(String thingCode:thingCodesTerm1){
            rBanRunOperate(TERM1,thingCode,lsTerm2Value,lsTerm1Value,stateTerm2Value,stateTerm1Value);
        }

        for(String thingCode:thingCodesTerm2){
            rBanRunOperate(TERM2,thingCode,lsTerm2Value,lsTerm1Value,stateTerm2Value,stateTerm1Value);
        }
    }

    private void rBanRunOperate(int term,String thingCode,String lsTerm2Value,String lsTerm1Value,String stateTerm2Value,String stateTerm1Value){
        String value = getRBanRunValue(thingCode);
        int rBanRunFlag = 0;
        int expectValue = 0;
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("read R_BAN_RUN value from kepserver:" + rBanRunFlag);
        }
        if(!StringUtils.isBlank(value)){
            rBanRunFlag = Integer.valueOf(value) & R_BAN_RUN_FLAG;
            if(term == TERM1){
                expectValue = getExpectValue(lsTerm1Value,stateTerm1Value);
            }else if(term == TERM2){
                expectValue = getExpectValue(lsTerm2Value,stateTerm2Value);
            }
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("term:" + term);
                LOGGER.debug("expect R_BAN_RUN value:" + expectValue);
            }
            sendSignal(rBanRunFlag,expectValue,thingCode);
        }
    }

    private void sendSignal(int rBanRunFlag,int expectValue,String thingCode ){
        if(rBanRunFlag != expectValue){
            ApplicationContext context = ApplicationContextListener.getApplicationContext();
            FilterPressManager filterPressManager = (FilterPressManager) context.getBean("filterPressManager");
            if(expectValue == 0){
                filterPressManager.setRBanRun(thingCode,Boolean.FALSE.toString());
                LOGGER.info("send R_BAN_RUN to {} and value:" + thingCode,Boolean.FALSE.toString());
            }else if(expectValue == 1){
                filterPressManager.setRBanRun(thingCode,Boolean.TRUE.toString());
                LOGGER.info("send R_BAN_RUN to {} and value:" + thingCode,Boolean.TRUE.toString());
            }
        }
    }

    private String getRBanRunValue(String thingCode){
        DataModel dataModel = new DataModel();
        dataModel.setThingCode(thingCode);
        dataModel.setMetricCode(FilterPressMetricConstants.R_BAN_RUN);
        ApplicationContext context = ApplicationContextListener.getApplicationContext();
        CmdControlServiceImpl cmdControlService = (CmdControlServiceImpl) context.getBean("cmdControlServiceImpl");
        return cmdControlService.getDataSync(dataModel);
    }

    private int getExpectValue(String lsValue,String stateValue){
        int value = 0;
        if(!Boolean.parseBoolean(lsValue)){
            value = 0;
        }else if((Boolean.parseBoolean(lsValue)) && (GlobalConstants.STATE_RUNNING == Short.valueOf(stateValue))){
            value = 0;
        }else if((Boolean.parseBoolean(lsValue)) && (GlobalConstants.STATE_RUNNING != Short.valueOf(stateValue))){
            value = 1;
        }
        return value;
    }
}
