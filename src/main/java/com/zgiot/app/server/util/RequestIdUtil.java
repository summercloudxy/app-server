package com.zgiot.app.server.util;

import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;
import java.util.UUID;

public class RequestIdUtil {

    private RequestIdUtil() {

    }

    public static String generateRequestId() {
        UUID id = UUID.fromString(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
        return id.toString();
    }
}
