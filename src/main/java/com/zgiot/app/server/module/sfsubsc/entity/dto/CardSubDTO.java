package com.zgiot.app.server.module.sfsubsc.entity.dto;

import java.util.List;

public class CardSubDTO {

    private String userUuid;
    private List<String> cardCodeList;

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public List<String> getCardCodeList() {
        return cardCodeList;
    }

    public void setCardCodeList(List<String> cardCodeList) {
        this.cardCodeList = cardCodeList;
    }
}
