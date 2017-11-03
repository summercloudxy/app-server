package com.zgiot.app.server.dataprocessor;

import com.zgiot.common.pojo.DataModel;

import java.util.List;

public interface DataCompleter {
    List<DataModel> onComplete(DataModel dm);
    void onError(DataModel dm, Throwable e);
}
