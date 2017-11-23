package com.zgiot.app.server.module.bellows.valve.cache;

import com.zgiot.app.server.module.bellows.valve.Valve;

import java.util.List;
import java.util.Set;

/**
 * @author wangwei
 */
public interface ValveCache {

    Valve findByThingCode(String thingCode);


    List<Valve> findAll();


    Set<String> findAllThingCode();


    List<String> findThingCodeByTeam(Long teamId);


    List<Valve> findByTeam(Long teamId);


    void put(String thingCode, Valve valve);
}
