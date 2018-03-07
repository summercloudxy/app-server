package com.zgiot.app.server.module.sfsubsc.pojo;

/**
 * @author jys
 * 卡片配置
 */
public class SubscriptionCardSetting {
    /**
     * 卡片编码
     */
    private String cardCode;
    /**
     * 卡片名称
     */
    private String cardName;
    /**
     * 卡片设备值
     */
    private String cardParamValue;
    /**
     * 卡片类型
     */
    private String cardType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 卡片id
     */
    private String cardId;

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardParamValue() {
        return cardParamValue;
    }

    public void setCardParamValue(String cardParamValue) {
        this.cardParamValue = cardParamValue;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
