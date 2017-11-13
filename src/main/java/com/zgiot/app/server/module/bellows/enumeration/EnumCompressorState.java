package com.zgiot.app.server.module.bellows.enumeration;

/**
 * @author wangwei
 */
public enum  EnumCompressorState {

    RUNNING("RUNNING", 2),

    UNLOAD("UNLOAD", 1),

    STOPPED("STOPPED", 0),

    ERROR("ERROR", -1);


    private String state;

    private int sort;

    EnumCompressorState(String state, int sort) {
        this.state = state;
        this.sort = sort;
    }

    /**
     * 根据状态字符串获取
     * @param state
     * @return
     */
    public static EnumCompressorState getByState(String state) {
        for (EnumCompressorState e : EnumCompressorState.values()) {
            if (e.getState().equals(state)) {
                return e;
            }
        }

        return null;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
