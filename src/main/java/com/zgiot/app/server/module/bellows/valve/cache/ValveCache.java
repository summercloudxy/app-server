package com.zgiot.app.server.module.bellows.valve.cache;

import com.zgiot.app.server.module.bellows.valve.Valve;

import java.util.List;

/**
 * @author wangwei
 */
public interface ValveCache {

    Valve findByThingCode(String thingCode);


    List<Valve> findAll();


    List<Valve> findByTeam(int teamId);


    void put(String thingCode, Valve valve);
}
