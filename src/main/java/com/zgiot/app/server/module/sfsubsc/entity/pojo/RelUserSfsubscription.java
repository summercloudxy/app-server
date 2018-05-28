package com.zgiot.app.server.module.sfsubsc.entity.pojo;

public class RelUserSfsubscription {

    private Long id;
    private String clientId;
    private String cardCode;
    private Integer sort;
    private String userUuid;

    public RelUserSfsubscription() {
    }

    public RelUserSfsubscription(String clientId, String cardCode, Integer sort, String userUuid) {
        this.clientId = clientId;
        this.cardCode = cardCode;
        this.sort = sort;
        this.userUuid = userUuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

}
