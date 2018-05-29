package com.zgiot.app.server.util;

import java.util.UUID;

public class RequestIdUtil {

    private RequestIdUtil() {

    }

    public static String generateRequestId(String prefix) {
        String realPrefix = (prefix == null) ? "" : prefix;
        UUID id = UUID.randomUUID();
        return realPrefix + "_" + id.toString();
    }
}
