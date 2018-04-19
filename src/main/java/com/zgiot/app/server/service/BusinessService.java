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

    /**
     * 液位计算
     *LEH_SET , LEL_SET两个信号点：
     * 00：低液位
     * 01：正常
     * 11：高液位
     * 10：未知
     * @return 液位高低
     */
    String getLevelByThingCode(String thingCode);
}
