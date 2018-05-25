package com.zgiot.app.server.module.reportforms.output.productionmonitor.util;

public class BitOperationUtil {
    /**
     * 位运算，设置一个整数的第pos位为0
     * @param i 要操作的数
     * @param pos 第几位
     * @return 把 i 的第pos 位变为0后的值
     */
    public static int do0(int i,int pos){
        int src= Integer.MAX_VALUE^(1<<(pos-1));
        return i&src;
    }
    /**
     * 位运算，设置一个整数的第pos位为0
     * @param i 要操作的数
     * @param pos 第几位
     * @return 把 i 的第pos 位变为1后的值
     */
    public static int do1(int i,int pos){
        return i|(1<<pos-1);
    }
}
