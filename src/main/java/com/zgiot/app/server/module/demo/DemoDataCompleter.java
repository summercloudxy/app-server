package com.zgiot.app.server.module.demo;

import com.zgiot.app.server.dataprocessor.DataCompleter;
import com.zgiot.common.pojo.DataModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
public class DemoDataCompleter implements DataCompleter{

    @Override
    public List<DataModel> onComplete(DataModel dm) {
        int valueI = Integer.parseInt(dm.getValue());
        int newValue = valueI * 1000;

        DataModel newDM = new DataModel();
        BeanUtils.copyProperties(dm, newDM);
        newDM.setMetricCode("demo1");
        newDM.setValue(String.valueOf(newValue));

        List<DataModel> list = new ArrayList<>();
        list.add(newDM);

        return list;
    }

    @Override
    public void onError(DataModel dm, Throwable e) {
        System.out.println("some log");
    }
}

