package com.zgiot.app.server.service;

import com.zgiot.common.pojo.DataModel;

import java.util.List;

public interface CmdControlService {

    /**
     * 下发指令
     *
     * @param dataModel
     */
    CmdSendResponseData sendCmd(DataModel dataModel, String requestId);

    /**
     * 下发指令（批量）
     *
     * @param dataModelList
     */
    CmdSendResponseData sendCmd(List<DataModel> dataModelList, String requestId);

    /**
     * 下发脉冲指令
     * @param dataModel
     * @param retryPeriod  重试间隔
     * @param retryCount  第二个信号失败时重试次数
     * @param requestId
     * @return
     */
    int sendPulseCmd(DataModel dataModel, Integer retryPeriod, Integer retryCount, String requestId);

    class CmdSendResponseData {
        int okCount;
        String errorMessage;

        public int getOkCount() {
            return okCount;
        }

        public void setOkCount(int okCount) {
            this.okCount = okCount;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

    }

}
