package com.zgiot.app.server.module.sfsubsc.entity.dto;

import java.util.List;

public class CardSubDTO {

    private String userUuid;
    private List<CardDataBeanDTO> cardDataBeanList;

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public List<CardDataBeanDTO> getCardDataBeanList() {
        return cardDataBeanList;
    }

    public void setCardDataBeanList(List<CardDataBeanDTO> cardDataBeanList) {
        this.cardDataBeanList = cardDataBeanList;
    }
}
