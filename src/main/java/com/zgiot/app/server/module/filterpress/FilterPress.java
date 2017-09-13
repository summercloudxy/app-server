package com.zgiot.app.server.module.filterpress;

public class FilterPress {
    private final String code;
    private FilterPressManager manager;

    public FilterPress(String code, FilterPressManager manager) {
        this.code = code;
        this.manager = manager;
    }

    public void onRun() {

    }

    public void onStop() {

    }

    public void onFault() {

    }

    public void onLoosen() {

    }

    public void onTaken() {

    }

    public void onPull() {

    }

    public void onPress() {

    }

    public void onFeed() {

    }

    public void onFeedOver() {

    }

    public void onBlow() {

    }

    public void onCycle() {

    }

    public String getCode() {
        return code;
    }
}
