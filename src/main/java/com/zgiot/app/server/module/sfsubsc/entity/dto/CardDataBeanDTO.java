package com.zgiot.app.server.module.sfsubsc.entity.dto;

public class CardDataBeanDTO {
    private String cardCode;
    private Object cardData;
    private Integer sort;

    public CardDataBeanDTO() {
    }

    public CardDataBeanDTO(String cardCode, Object cardData, Integer sort) {
        this.cardCode = cardCode;
        this.cardData = cardData;
        this.sort = sort;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
