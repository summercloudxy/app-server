package com.zgiot.app.server.module.util;

public class StringUtils {
    /**
     * 判断是否是数字
     *
     * @param code
     * @return
     */
    public static boolean isNum(String code) {
        try {
            Integer.parseInt(code);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
