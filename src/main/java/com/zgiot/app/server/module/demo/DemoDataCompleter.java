package com.zgiot.app.server.module.demo;

import com.zgiot.app.server.dataprocessor.DataCompleter;
import com.zgiot.common.pojo.DataModel;
import org.springframework.beans.BeanUtils;

public class DemoDataCompleter implements DataCompleter{

    @Override
    public void onComplete(DataModel dm) {
        int valueI = Integer.parseInt(dm.getValue());
        int newValue = valueI * 1000;

        DataModel newDM = new DataModel();
        BeanUtils.copyProperties(dm, newDM);
        newDM.setValue(String.valueOf(newValue));

        // update cache

        // add to history data , buz will decide save or not

    }

    @Override
    public void onError(DataModel dm, Throwable e) {
        System.out.println("some log");
    }
}

