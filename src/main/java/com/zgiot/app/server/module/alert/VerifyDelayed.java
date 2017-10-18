package com.zgiot.app.server.module.alert;

import com.zgiot.app.server.module.alert.pojo.AlertData;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class VerifyDelayed implements Delayed{
    private AlertData alertData;
    private long timeout;

    public VerifyDelayed(AlertData alertData,long duration) {
        this.alertData = alertData;
        this.timeout = alertData.getVerifyTime().getTime() + duration;
    }

    public AlertData getAlertData() {
        return alertData;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return timeout - System.currentTimeMillis()  ;
    }

    @Override
    public int compareTo(Delayed o) {
        VerifyDelayed verifyDelayed = (VerifyDelayed)o;
        return timeout - verifyDelayed.timeout >0?1:-1;
    }
}
