package com.zgiot.app.server.module.util.validate;

import com.zgiot.common.exceptions.SysException;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class ControllerUtil {

    public static void validateBodyRequired(String bodyStr) {
        if (StringUtils.isBlank(bodyStr.trim())) {
            throw new SysException("Request body required!", SysException.EC_UNKNOWN);
        }
    }

    public static String md5Hex(String src) {
        return DigestUtils.md5DigestAsHex(src.getBytes());
    }

    public static String genUUID(){
        return UUID.randomUUID().toString().replaceAll("-","").toUpperCase();
    }

}
                                                  