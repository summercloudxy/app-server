package com.zgiot.app.server.module.bellows.valve.cache;


import com.zgiot.app.server.module.bellows.valve.Valve;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
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
    public Set<String> findAllThingCode() {
        return map.keySet();
    }


    @Override
    public List<String> findThingCodeByTeam(Long teamId) {
        List<String> res = new ArrayList<>(map.size());
        map.forEach((key, value) -> {
            if (teamId.equals(value.getTeamId())) {
                res.add(value.getThingCode());
            }
        });
        return res;
    }


    @Override
    public List<Valve> findByTeam(Long teamId) {
        List<Valve> res = new ArrayList<>(map.size());
        map.forEach((key, value)->{
            if (teamId.equals(value.getTeamId())) {
                res.add(value);
            }
        });
        return res;
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
