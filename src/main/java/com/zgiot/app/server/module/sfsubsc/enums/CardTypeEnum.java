package com.zgiot.app.server.module.sfsubsc.enums;

/**
 * @author jys
 * 卡片类型枚举
 */
public enum CardTypeEnum {
    /**
     * 历史入洗量
     */
    HISTORICAL_WASHING_CAPACITY("lsrxl", "历史入洗量"),

    /**
     * 历史产品量
     */
    HISTORICAL_PRODUCT_RATE("lscpl", "历史产品量"),

    /**
     * 原煤配比
     */
    MIXTURE_OF_RAW_COAL("ympb", "原煤配比"),

    /**
     * 分选指标
     */
    SEPARATION_INDEX("fxzb", "分选指标"),

    /**
     * 生产消耗
     */
    PRODUCTION_CONSUMABLES("scxh", "生产消耗"),

    /**
     * 瞬时入洗量
     */
    INSTANTANEOUS_WASH("ssrxl", "瞬时入洗量"),

    /**
     * 瞬时产品量
     */
    INSTANTANEOUS_PRODUCT_QUANTITY("sscpl", "瞬时产品量"),

    /**
     * 瞬时产品率
     */
    INSTANTANEOUS_PRODUCT_RATE("sscpl", "瞬时产品率"),

    /**
     * 产品产率
     */
    PRODUCT_YIELD("cpcl", "产品产率"),

    /**
     * 仓位信息
     */
    POSITION_INFORMATION("cwxx", "仓位信息"),

    /**
     * 产品外运
     */
    PRODUCT_OUTBOUND("cpwy", "产品外运"),

    /**
     * 化验数据
     */
    CHEMICAL_TESTS_DATA("hysj", "化验数据"),

    /**
     * 智能压滤
     */
    INTELLIGENT_FILTER("znyl", "智能压滤"),

    /**
     * 智能鼓风
     */
    INTELLIGENT_BLOWER("zngf", "智能鼓风"),

    /**
     * 智能告警
     */
    INTELLIGENT_ALARM("zngj", "智能告警"),

    /**
     * 当班煤质统计
     */
    COAL_QUALITY("dbmz", "当班煤质"),

    /**
     * 当班生产统计
     */
    PRODUCTION("dbsc", "当班生产");


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
