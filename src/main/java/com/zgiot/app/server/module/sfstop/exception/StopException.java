package com.zgiot.app.server.module.sfstop.exception;

public class StopException extends RuntimeException {


    public StopException() {
        super("停车异常");
    }

    public StopException(String message) {
        super(message);
    }
}
