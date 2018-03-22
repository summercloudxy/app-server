package com.zgiot.app.server.module.sfsubsc.entity.vo;

/**
 * 化验数据列表详情
 *
 * @author jys
 */
public class ChemicalTestsDataLIstDetailVO {

    private String collectTime;

    private String cleanCoalAad;

    private String cleanCoalStad;

    private String mixedCoalAad;

    private String mixedCoalMt;

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

    public String getCleanCoalAad() {
        return cleanCoalAad;
    }

    public void setCleanCoalAad(String cleanCoalAad) {
        this.cleanCoalAad = cleanCoalAad;
    }

    public String getCleanCoalStad() {
        return cleanCoalStad;
    }

    public void setCleanCoalStad(String cleanCoalStad) {
        this.cleanCoalStad = cleanCoalStad;
    }

    public String getMixedCoalAad() {
        return mixedCoalAad;
    }

    public void setMixedCoalAad(String mixedCoalAad) {
        this.mixedCoalAad = mixedCoalAad;
    }

    public String getMixedCoalMt() {
        return mixedCoalMt;
    }

    public void setMixedCoalMt(String mixedCoalMt) {
        this.mixedCoalMt = mixedCoalMt;
    }
}
