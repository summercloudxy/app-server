package com.zgiot.app.server.module.sfsubsc.entity.dto;

public class CardDataBeanDTO {
    private String cardCode;
    private Object cardData;

    public CardDataBeanDTO() {
    }

    public CardDataBeanDTO(String cardCode, Object cardData) {
        this.cardCode = cardCode;
        this.cardData = cardData;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public Object getCardData() {
        return cardData;
    }

    public void setCardData(Object cardData) {
        this.cardData = cardData;
    }
}
