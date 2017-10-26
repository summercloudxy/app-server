package com.zgiot.app.server.dataprocessor;

import com.zgiot.common.pojo.DataModel;

public interface DataCompleter {
    void onComplete(DataModel dm);
    void onError(DataModel dm, Throwable e);
}
