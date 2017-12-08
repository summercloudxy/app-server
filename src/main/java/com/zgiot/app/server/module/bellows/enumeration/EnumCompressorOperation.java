package com.zgiot.app.server.module.bellows.enumeration;

/**
 * @author wangwei
 */
public enum EnumCompressorOperation {

    /**
     * 启动，脉冲
     */
    START(Boolean.TRUE.toString(), 1, false),

    /**
     * 停止，脉冲
     */
    STOP(Boolean.TRUE.toString(), 2, false),

    /**
     * 加载，非脉冲
     */
    LOAD(Boolean.FALSE.toString(), 3, true),

    /**
     * 卸载，非脉冲
     */
    UNLOAD(Boolean.TRUE.toString(), 3, true);




    private String value;
    private int position;
    private boolean holding;

    EnumCompressorOperation(String value, int position, boolean holding) {
        this.value = value;
        this.position = position;
        this.holding = holding;
    }

    public String getValue() {
        return value;
    }

    public int getPosition() {
        return position;
    }

    public boolean isHolding() {
        return holding;
    }
}
