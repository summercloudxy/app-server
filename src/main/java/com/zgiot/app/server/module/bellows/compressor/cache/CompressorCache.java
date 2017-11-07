package com.zgiot.app.server.module.bellows.compressor.cache;

import com.zgiot.app.server.module.bellows.compressor.Compressor;

import java.util.List;

/**
 * @author wangwei
 */
public interface CompressorCache {

    /**
     * 根据thingCode获取
     * @param thingCode
     * @return
     */
    Compressor findByThingCode(String thingCode);

    /**
     * 根据类型获取列表
     * @param type
     * @return
     */
    List<Compressor> findByType(String type);

    /**
     * 存入数据
     * @param thingCode
     * @param compressor
     */
    void put(String thingCode, Compressor compressor);
}
