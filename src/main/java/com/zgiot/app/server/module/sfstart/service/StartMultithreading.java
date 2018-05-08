package com.zgiot.app.server.module.sfstart.service;

import com.zgiot.app.server.module.sfstart.constants.StartStopConstants;
import com.zgiot.app.server.module.sfstart.controller.StartHandler;
import com.zgiot.app.server.module.sfstart.exception.StartException;
import com.zgiot.app.server.module.sfstart.pojo.StartExamineRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;


public class StartMultithreading implements Runnable {

    public static final int SEND_INFORMATION = 1; // 发送信号
    public static final int SEND_STARTING = 2; // 正式启车信号
    public static final int SET_UP_AUTO_TEST = 3; // 启车自检开始

    private static final Logger logger = LoggerFactory.getLogger(StartMultithreading.class);
    private Integer operate; // 操作内容
    private Integer operateId; // 启车操作号
    private Set<String> startDeviceIds; //启动设备
    private String userId; // 操作人
    private StartService startService;
    private StartHandler startHandler;


    @Override
    public void run() {
        switch (operate) {
            case StartMultithreading.SEND_INFORMATION:
                // 发送信号
                try {
                    sendStartDeviceInformation(operateId);
                } catch (Exception e) {
                    logger.error("多线程发送信号错误，错误内容为:{}", e);
                    startService.updateStartOperate(StartStopConstants.START_SEND_CLEAN_STATE);
                    startHandler.sendMessageTemplateByJson(StartStopConstants.URI_START_STATE, StartStopConstants.URI_START_STATE_MESSAGE_SEND_ERROR);
                    throw new StartException("多线程发送信号错误,请重新发送");
                }
                break;
            case StartMultithreading.SEND_STARTING:
                // 实际启车
                try {
                    // 启车信号复检
                    startHandler.sendStartInformation(operateId);
                    sendStarting();
                } catch (Exception e) {
                    logger.error("多线程启车错误，错误内容为:{}", e);
                    logger.error("关闭异常启车进度");
                    startService.closeStartOperate();
                    startHandler.sendMessageTemplateByJson(StartStopConstants.URI_START_STATE, StartStopConstants.URI_START_STATE_MESSAGE_CHECK_ERROR);
                    throw new StartException("多线程启车错误");
                }
                break;
            case StartMultithreading.SET_UP_AUTO_TEST:
                try {
                    setUpAutoTest();
                } catch (Exception e) {
                    logger.error("启车自检过程异常，异常信息为:{}", e);
                    startService.closeStartOperate();
                    startHandler.sendMessageTemplateByJson(StartStopConstants.URI_START_STATE, StartStopConstants.URI_START_STATE_MESSAGE_CLOSE_EXAMINE);
                }
                break;
            default:
                break;
        }
    }


    /**
     * 发送启车设备参数到plc
     *
     * @param operateId
     */
    public void sendStartDeviceInformation(Integer operateId) throws Exception {

        startHandler.sendStartInformation(operateId);
        // 修改状态为准备启车
        startService.updateStartOperate(StartStopConstants.START_SEND_FINISH_STATE);
        // 通知前端信号下发成功结束
        startHandler.sendMessageTemplateByJson(StartStopConstants.URI_START_STATE, StartStopConstants.URI_START_STATE_MESSAGE_SEND_FINISH);
    }

    /**
     * 发送启车设备参数到plc
     */
    public void sendStarting() throws Exception {

        // 建立启车检查
        startHandler.createDeviceRequirement(startDeviceIds);
        // 信息发送无误后，发送启车命令
        startHandler.writeSignalByLabel(StartStopConstants.START_DEVICE_LABEL, StartStopConstants.VALUE_TRUE, StartStopConstants.LABEL_TYPE_BOOLEAN);
        // 建立区域判断开始
        startHandler.createAreaRuleExamine(null);
    }

    /**
     * 启车自检流程
     *
     * @throws Exception
     */
    public void setUpAutoTest() throws Exception {
        logger.info("进入启车自检流程");
        // 获取自建规则范围
        List<StartExamineRule> startExamineRules = startService.getStartExamineRuleByDeviceIds(startDeviceIds);
        // 启车自检
        startHandler.autoExamineStarting(startExamineRules);
        // 建立人工干预
        startHandler.createManualInterventionRecord(startDeviceIds, operateId);
        // 建立区域自检
        startService.setUpAreaExamine(operateId);
        // 创建本次启车设备记录
        startService.saveDeviceStateRecord(startDeviceIds, operateId);
        // 修改启车状态
        startService.updateStartOperate(StartStopConstants.START_EXAMIN_STATE);
        startHandler.sendMessageTemplateByJson(StartStopConstants.URI_START_STATE, StartStopConstants.URI_START_STATE_MESSAGE_EXAMINE_FINISH);
    }


    public static int getSendInformation() {
        return SEND_INFORMATION;
    }

    public static int getSendStarting() {
        return SEND_STARTING;
    }

    public static int getSetUpAutoTest() {
        return SET_UP_AUTO_TEST;
    }

    public static Logger getLogger() {
        return logger;
    }

    public Integer getOperate() {
        return operate;
    }

    public void setOperate(Integer operate) {
        this.operate = operate;
    }

    public Integer getOperateId() {
        return operateId;
    }

    public void setOperateId(Integer operateId) {
        this.operateId = operateId;
    }

    public Set<String> getStartDeviceIds() {
        return startDeviceIds;
    }

    public void setStartDeviceIds(Set<String> startDeviceIds) {
        this.startDeviceIds = startDeviceIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public StartService getStartService() {
        return startService;
    }

    public void setStartService(StartService startService) {
        this.startService = startService;
    }

    public StartHandler getStartHandler() {
        return startHandler;
    }

    public void setStartHandler(StartHandler startHandler) {
        this.startHandler = startHandler;
    }
}
