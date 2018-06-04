package com.zgiot.app.server.module.sfmonitor.util;

public class CompareUtil {
    /**
     * thingCode排序-仅数字
     *
     * @param thingCode1
     * @param thingCode2
     * @return
     */
    public static int compareThingCodeOnlyNum(String thingCode1, String thingCode2) {
        int num1 = Integer.parseInt(thingCode1);
        int num2 = Integer.parseInt(thingCode2);
        return num1 - num2;
    }

    /**
     * thingCode排序-含字母
     *
     * @param thingCode1
     * @param thingCode2
     * @return
     */
    public static int compareThingCodeHaveLetter(String thingCode1, String thingCode2) {
        String reg = "[A-Za-z]+";
        String num1 = thingCode1.replaceAll(".","").replaceAll("-","").split(reg)[0];
        String num2 = thingCode2.replaceAll(".","").replaceAll("-","").split(reg)[0];

        int result = 0;
        if (num1.equals(num2)) {
            // 数字相同,字母不同
            String letter1 = thingCode1.split(num1)[1];
            String letter2 = thingCode2.split(num2)[1];
            result = letter1.toCharArray()[0] - letter2.toCharArray()[0];
        } else {
            // 数字不同
            result = compareThingCodeOnlyNum(num1, num2);
        }
        return result;
    }
}
