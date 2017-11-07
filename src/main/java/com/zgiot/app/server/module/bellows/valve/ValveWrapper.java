package com.zgiot.app.server.module.bellows.valve;

/**
 * @author wangwei
 */
public class ValveWrapper {

    private Valve valve;

    public ValveWrapper(Valve valve) {
        this.valve = valve;
    }

    public String getThingCode() {
        return valve.getThingCode();
    }

    public String getName() {
        return valve.getName();
    }

    public int getSort() {
        return valve.getSort();
    }

    public int getType() {
        return valve.getType();
    }

    public boolean isIntelligent() {
        return valve.isIntelligent();
    }

    public Integer getTeamId() {
        return valve.getTeamId();
    }

    public int getClosed() {
        return valve.getClosed();
    }

    public int getOpen() {
        return valve.getOpen();
    }

    public boolean isBucketRunning() {
        return valve.isBucketRunning();
    }

    public ValveManager getValveManager() {
        return valve.getValveManager();
    }

    public Valve toValve() {
        return valve.clone();
    }
}
