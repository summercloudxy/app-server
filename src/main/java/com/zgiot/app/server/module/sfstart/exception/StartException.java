package com.zgiot.app.server.module.sfstart.exception;

public class StartException extends RuntimeException {

    public StartException() {
        super("启车异常");
    }

    public StartException(String message) {
        super(message);
    }
}
