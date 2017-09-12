package com.zgiot.app.server.util;

import java.util.UUID;

public class RequestIdUtil {

    private RequestIdUtil() {

    }

    public static String generateRequestId() {
        UUID id = UUID.randomUUID();
        return id.toString();
    }
}
