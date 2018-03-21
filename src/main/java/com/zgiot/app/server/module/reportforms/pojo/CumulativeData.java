package com.zgiot.app.server.module.reportforms.pojo;

import java.math.BigDecimal;

public class CumulativeData {
    private double sumValue;
    private int count;



    public double getSumValue() {
        return sumValue;
    }

    public void setSumValue(double sumValue) {
        this.sumValue = sumValue;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public Double getAvgValue(boolean needNull) {
        if (count == 0) {
            if (needNull) {
                return null;
            } else {
                return 0.0;
            }
        } else {
            return sumValue / count;
        }
    }


    public Double getAvgValue(boolean needNull,Integer scale){
        if (count==0){
            if (needNull) {
                return null;
            }else {
                return 0.0;
            }
        }else {
            Double avgValue = sumValue/count;
            BigDecimal bigDecimal = new BigDecimal(avgValue);
            BigDecimal bigDecimalWithScale = bigDecimal.setScale(scale,BigDecimal.ROUND_HALF_UP);
            return bigDecimalWithScale.doubleValue();
        }
    }
}
