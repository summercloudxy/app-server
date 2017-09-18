package com.zgiot.app.server.service;

import com.zgiot.common.pojo.DataModel;

import java.util.List;

public interface CmdControlService {
    /**
     * 下发指令
     *
     * @param dataModel
     */
    int sendCmd(DataModel dataModel, String requestId);

    /**
     * 下发指令（批量）
     *
     * @param dataModelList
     */
    int sendCmd(List<DataModel> dataModelList, String requestId);

    /**
     * 下发脉冲指令
     * @param dataModel
     * @param retryPeriod  重试间隔
     * @param retryCount  第二个信号失败时重试次数
     * @param requestId
     * @return
     */
    int sendPulseCmd(DataModel dataModel, Integer retryPeriod, Integer retryCount, String requestId);
}
