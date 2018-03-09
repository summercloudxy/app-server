package com.zgiot.app.server.module.sfsubsc.entity.dto;

/**
 * 卡片数据
 *
 * @author jys
 */
public class CardDataDTO {


    private String cardCode;

    private Object cardData;

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
