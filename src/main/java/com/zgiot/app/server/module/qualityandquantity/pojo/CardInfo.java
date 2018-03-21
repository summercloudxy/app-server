package com.zgiot.app.server.module.qualityandquantity.pojo;


import com.alibaba.fastjson.annotation.JSONField;

public class CardInfo {
    private int id;
    @JSONField(serialize = false)
    private String parserName;
    private int cardStyleId;
    @JSONField(serialize = false)
    private String cardParamValue;
    private Object cardDetailInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCardStyleId() {
        return cardStyleId;
    }

    public void setCardStyleId(int cardStyleId) {
        this.cardStyleId = cardStyleId;
    }

    public String getCardParamValue() {
        return cardParamValue;
    }

    public void setCardParamValue(String cardParamValue) {
        this.cardParamValue = cardParamValue;
    }

    public Object getCardDetailInfo() {
        return cardDetailInfo;
    }

    public void setCardDetailInfo(Object cardDetailInfo) {
        this.cardDetailInfo = cardDetailInfo;
    }

    public String getParserName() {
        return parserName;
    }

    public void setParserName(String parserName) {
        this.parserName = parserName;
    }
}
