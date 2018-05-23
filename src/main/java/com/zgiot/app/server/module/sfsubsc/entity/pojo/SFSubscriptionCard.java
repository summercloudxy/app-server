package com.zgiot.app.server.module.sfsubsc.entity.pojo;

public class SFSubscriptionCard {

    private String cardId;// 卡片Id
    private String cardData;// 卡片数据
    private String refreshTime;// 刷新时间
    private String cardCode;// 卡片编码
    private String cardSource;// 卡片来源
    private String cardType;// 卡片类型（判断卡片显示样式）

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardData() {
        return cardData;
    }

    public void setCardData(String cardData) {
        this.cardData = cardData;
    }

    public String getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(String refreshTime) {
        this.refreshTime = refreshTime;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardSource() {
        return cardSource;
    }

    public void setCardSource(String cardSource) {
        this.cardSource = cardSource;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
