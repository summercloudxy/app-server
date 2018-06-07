package com.zgiot.app.server.module.sfstop.service;

import com.zgiot.app.server.module.sfstop.constants.StopConstants;
import com.zgiot.app.server.module.sfstop.controller.StopHandler;
import com.zgiot.app.server.module.sfstop.entity.pojo.StopExamineRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public class StopMultithreading implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(StopMultithreading.class);
    private String system;
    private Integer operateId; // 停车操作号
    private Set<String> stopThingCodes; //停车设备
    private String userId; // 操作人
    private StopService stopService;
    private StopHandler stopHandler;

    @Override
    public void run() {
        try {
            setUpAutoTest(system);
        } catch (Exception e) {
            logger.error("停车自检过程异常，异常信息为:{}", e);
            stopService.closeStopOperate(system);
            stopHandler.sendMessageTemplateByJson(StopConstants.URI_STOP_STATE, StopConstants.URI_STOP_STATE_MESSAGE_CLOSE_EXAMINE);
        }


    }

    /**
     * 停车自检流程
     *
     * @throws Exception
     */
    public void setUpAutoTest(String system) throws Exception {
        logger.info("进入停车车自检流程");
        // 获取自建规则范围
        List<StopExamineRule> stopExamineRules = stopService.getStopExamineRuleByDeviceIds(stopThingCodes);
        // 启车自检
        stopHandler.autoExamineStarting(system, stopExamineRules);
        // 建立人工干预
        // stopHandler.createManualInterventionRecord(stopDeviceIds, operateId);

        // 创建本次启车设备记录
        //  stopService.saveDeviceStateRecord(stopDeviceIds, operateId);
        // 修改启车状态
        //  stopService.updateStartOperate(StopConstants.STOP_EXAMIN_STATE);
        stopHandler.sendMessageTemplateByJson(StopConstants.URI_STOP_STATE, StopConstants.URI_STOP_STATE_MESSAGE_EXAMINE_FINISH);
    }


    public static Logger getLogger() {
        return logger;
    }


    public Integer getOperateId() {
        return operateId;
    }

    public void setOperateId(Integer operateId) {
        this.operateId = operateId;
    }

    public Set<String> getStopThingCodes() {
        return stopThingCodes;
    }

    public void setStopThingCodes(Set<String> stopThingCodes) {
        this.stopThingCodes = stopThingCodes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public StopService getStopService() {
        return stopService;
    }

    public void setStopService(StopService stopService) {
        this.stopService = stopService;
    }

    public StopHandler getStopHandler() {
        return stopHandler;
    }

    public void setStopHandler(StopHandler stopHandler) {
        this.stopHandler = stopHandler;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }
}
