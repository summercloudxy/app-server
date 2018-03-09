package com.zgiot.app.server.module.sfsubsc.enums;

/**
 * @author jys
 * 卡片类型枚举
 */
public enum CardTypeEnum {
    /**
     * 一期历史入洗量
     */
    HISTORYWASHINGQUANTITY_ONE("lsrxl_01", "一期历史入洗量"),
    /**
     * 二期历史入洗量
     */
    HISTORYWASHINGQUANTITY_TWO("lsrxl_02", "二期历史入洗量"),
    /**
     * 一期历史产品量
     */
    HISTORYPRODUCTRATE_ONE("lscpl_01", "一期历史产品量"),
    /**
     * 二期历史产品量
     */
    HISTORYPRODUCTRATE_TWO("lscpl_02", "二期历史产品量"),

    /**
     * 原煤配比
     */
    COALPRECENT("ympb", "原煤配比");


    private String cardCode;

    private String cardTypeName;

    CardTypeEnum(String cardCode, String cardTypeName) {
        this.cardCode = cardCode;
        this.cardTypeName = cardTypeName;
    }

    public static CardTypeEnum getByCardCode(String cardCode) {
        for (CardTypeEnum e : CardTypeEnum.values()) {
            if (e.getCardCode().equals(cardCode)) {
                return e;
            }
        }
        return null;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }
}
