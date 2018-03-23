package com.zgiot.app.server.module.qualityandquantity.pojo;


import java.util.List;

public class AreaInfo {
    private int areaId;
    private List<CardInfo> cardInfos;
    private String areaTitle;

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public List<CardInfo> getCardInfos() {
        return cardInfos;
    }

    public void setCardInfos(List<CardInfo> cardInfos) {
        this.cardInfos = cardInfos;
    }

    public String getAreaTitle() {
        return areaTitle;
    }

    public void setAreaTitle(String areaTitle) {
        this.areaTitle = areaTitle;
    }

    public AreaInfo(int areaId, List<CardInfo> cardInfos) {
        this.areaId = areaId;
        this.cardInfos = cardInfos;
    }

    public AreaInfo() {

    }
}
