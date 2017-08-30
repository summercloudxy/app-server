package com.zgiot.app.server.exception;

import com.zgiot.common.exceptions.SysException;

public class CmdSendException extends SysException{
    public CmdSendException(Throwable error) {
        this(error.getMessage(), EC_UNKOWN);
    }

    public CmdSendException(String message, int errorCode) {
        super(message, errorCode);
    }

    public CmdSendException(String message, int errorCode, Object data) {
        super(message, errorCode, data);
    }


}
