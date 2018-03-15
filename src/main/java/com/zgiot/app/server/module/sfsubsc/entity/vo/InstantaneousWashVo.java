package com.zgiot.app.server.module.sfsubsc.entity.vo;

/**
 * 瞬时入洗量
 *
 * @author jys
 */
public class InstantaneousWashVo {

    /**
     * 卡片标题
     */
    private String cardTitle;


    private String systemName;

    private String instantaneousTotalValue;

    private String instantaneousValue1;

    private String instantaneousValue2;

    private String teamTotalValue;

    private String teamValue1;

    private String teamValue2;

    private String thingCode1;

    private String thingCode2;

    public String getInstantaneousTotalValue() {
        return instantaneousTotalValue;
    }

    public void setInstantaneousTotalValue(String instantaneousTotalValue) {
        this.instantaneousTotalValue = instantaneousTotalValue;
    }

    public String getInstantaneousValue1() {
        return instantaneousValue1;
    }

    public void setInstantaneousValue1(String instantaneousValue1) {
        this.instantaneousValue1 = instantaneousValue1;
    }

    public String getInstantaneousValue2() {
        return instantaneousValue2;
    }

    public void setInstantaneousValue2(String instantaneousValue2) {
        this.instantaneousValue2 = instantaneousValue2;
    }

    public String getTeamTotalValue() {
        return teamTotalValue;
    }

    public void setTeamTotalValue(String teamTotalValue) {
        this.teamTotalValue = teamTotalValue;
    }

    public String getTeamValue1() {
        return teamValue1;
    }

    public void setTeamValue1(String teamValue1) {
        this.teamValue1 = teamValue1;
    }

    public String getTeamValue2() {
        return teamValue2;
    }

    public void setTeamValue2(String teamValue2) {
        this.teamValue2 = teamValue2;
    }

    public String getThingCode1() {
        return thingCode1;
    }

    public void setThingCode1(String thingCode1) {
        this.thingCode1 = thingCode1;
    }

    public String getThingCode2() {
        return thingCode2;
    }

    public void setThingCode2(String thingCode2) {
        this.thingCode2 = thingCode2;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }
}
