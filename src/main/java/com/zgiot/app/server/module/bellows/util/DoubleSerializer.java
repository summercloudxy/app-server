package com.zgiot.app.server.module.bellows.util;


import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * @author wangwei
 * double保留两位小数
 */
public class DoubleSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer jsonSerializer, Object o, Object o1, Type type, int i) throws IOException {
        if (o == null) {
            jsonSerializer.write(o);
            return;
        }
        if (type.getTypeName().toLowerCase().equals(double.class.getName())) {
            double d = new BigDecimal((double)o).setScale(2, BigDecimal.ROUND_CEILING).doubleValue();
            jsonSerializer.write(d);
        } else {
            jsonSerializer.write(o);
        }
    }
}
