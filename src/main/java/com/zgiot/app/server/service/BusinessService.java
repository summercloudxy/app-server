package com.zgiot.app.server.service;

import java.util.Map;

/**
 * 通用业务处理逻辑
 */
public interface BusinessService {

    /**
     * 原煤配比
     *
     * @return 一期  二期 配比
     */
    Map<String, String> getRowCoalCapPercent();
}
