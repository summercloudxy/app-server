package com.zgiot.app.server.dataprocessor;

import com.zgiot.common.pojo.DataModel;

public interface DataListener {
    /**
     * 数据变化的回调
     *
     * @param dataModel
     */
    void onDataChange(DataModel dataModel);

    /**
     * 有错误发生的回调
     *
     * @param error
     */
    void onError(Throwable error);

    class ReturnData {
        public boolean stopHere;
    }
}
