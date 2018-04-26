package com.zgiot.app.server.module.reportforms.input.pojo;

import java.math.BigDecimal;

public class CumulativeData {
    private BigDecimal sumValue = BigDecimal.ZERO;
    private int count;


    public BigDecimal getSumValue() {
        return sumValue;
    }

    public void setSumValue(BigDecimal sumValue) {
        this.sumValue = sumValue;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }



    public Double getAvgValue(boolean needNull,Integer scale){
        if (count==0){
            if (needNull) {
                return null;
            }else {
                return 0.0;
            }
        }else {

            BigDecimal countDecimal = BigDecimal.valueOf(count);
            BigDecimal divide = sumValue.divide(countDecimal,scale, BigDecimal.ROUND_HALF_UP);
            return divide.doubleValue();
        }
    }
}
