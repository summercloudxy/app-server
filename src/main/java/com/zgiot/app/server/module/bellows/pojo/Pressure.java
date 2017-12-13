package com.zgiot.app.server.module.bellows.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.zgiot.app.server.module.bellows.util.DoubleSerializer;

/**
 * @author wangwei
 */
public class Pressure {

    @JSONField(serializeUsing = DoubleSerializer.class)
    private double high;

    @JSONField(serializeUsing = DoubleSerializer.class)
    private double low;

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }
}
