package com.zgiot.app.server.module.util.validate;

import com.zgiot.common.exceptions.SysException;
import org.apache.commons.lang.StringUtils;

public class ControllerUtil {
    public static void validateBodyRequired(String bodyStr) {
        if (StringUtils.isBlank(bodyStr.trim())) {
            throw new SysException("Request body required!", SysException.EC_UNKNOWN);
        }
    }

}
                                                  