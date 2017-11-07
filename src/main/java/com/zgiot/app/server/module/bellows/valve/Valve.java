package com.zgiot.app.server.module.bellows.valve;

/**
 * @author wangwei
 */
public class Valve implements Cloneable {

    private static final String PARAM_INTELLIGENT = "intelligent";

    private static final String PARAM_TEAM_ID = "teamId";


    private String thingCode;

    private String name;    //显示名称

    private int sort;   //排序

    private int type;  //阀门分类

    private boolean intelligent;    //是否智能操作

    private Integer teamId; //分组号

    private int closed; //关到位

    private int open;   //开到位

    private boolean bucketRunning;  //介质桶运行中

    private ValveManager valveManager;


    public Valve() {

    }

    public Valve(String thingCode, int type, ValveManager valveManager) {
        this.thingCode = thingCode;
        this.type = type;
        this.valveManager = valveManager;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isIntelligent() {
        return intelligent;
    }

    public void setIntelligent(boolean intelligent) {
        this.intelligent = intelligent;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public int getClosed() {
        return closed;
    }

    public void setClosed(int closed) {
        this.closed = closed;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public boolean isBucketRunning() {
        return bucketRunning;
    }

    public void setBucketRunning(boolean bucketRunning) {
        this.bucketRunning = bucketRunning;
    }

    public ValveManager getValveManager() {
        return valveManager;
    }

    public void setValveManager(ValveManager valveManager) {
        this.valveManager = valveManager;
    }

    @Override
    public Valve clone() {
        try {
            return (Valve) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}
