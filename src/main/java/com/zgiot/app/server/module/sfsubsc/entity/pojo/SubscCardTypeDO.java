package com.zgiot.app.server.module.sfsubsc.entity.pojo;

import java.util.Date;

/**
 * 卡片类型
 *
 * @author jys
 */
public class SubscCardTypeDO {

    private String cardId;

    private String cardName;

    private String cardCode;

    private String cardSource;

    private Date updateDta;

    private String updateUser;

    private String cardType;

    private String cardParamValue;

    private String remark;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
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

    public Date getUpdateDta() {
        return updateDta;
    }

    public void setUpdateDta(Date updateDta) {
        this.updateDta = updateDta;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardParamValue() {
        return cardParamValue;
    }

    public void setCardParamValue(String cardParamValue) {
        this.cardParamValue = cardParamValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
