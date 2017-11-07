package com.zgiot.app.server.module.bellows.valve.cache;


import com.zgiot.app.server.module.bellows.valve.Valve;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ValveCacheImpl implements ValveCache {

    private Map<String, Valve> map = new ConcurrentHashMap<>(20);

    @Override
    public Valve findByThingCode(String thingCode) {
        Valve valve = map.get(thingCode);
        return valve;
    }

    @Override
    public List<Valve> findAll() {
        List<Valve> res = new ArrayList<>(map.values());
        return sort(res);
    }

    @Override
    public List<Valve> findByTeam(int teamId) {
        List<Valve> res = new ArrayList<>(map.size());
        map.forEach((key, value) -> {
            if (teamId == value.getTeamId()) {
                res.add(value);
            }
        });
        return sort(res);
    }

    @Override
    public void put(String thingCode, Valve valve) {
        map.put(thingCode, valve);
    }


    private List<Valve> sort(List<Valve> list) {
        list.sort(new Comparator<Valve>() {
            @Override
            public int compare(Valve o1, Valve o2) {
                return Integer.compare(o1.getSort(), o2.getSort());
            }
        });
        return list;
    }
}
