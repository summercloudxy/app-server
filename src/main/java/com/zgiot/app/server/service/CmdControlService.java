package com.zgiot.app.server.service;

import com.zgiot.common.pojo.DataModel;

import java.util.List;

public interface CmdControlService {
    /**
     * 下发指令
     *
     * @param dataModel
     */
    int sendCmd(DataModel dataModel);

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
    int sendCmd(List<DataModel> dataModelList);

    /**
     * 下发指令（批量）
     *
     * @param dataModelList
     */
    int sendCmd(List<DataModel> dataModelList, String requestId);
}
