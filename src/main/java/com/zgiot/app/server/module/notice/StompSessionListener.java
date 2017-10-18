package com.zgiot.app.server.module.notice;

public interface StompSessionListener {
    void onSessionActive();

    void onSessionInactive();

    void onError(Throwable error);
}
