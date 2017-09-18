package com.zgiot.app.server.exception;

import com.zgiot.common.exceptions.SysException;

/**
 * Created by xiayun on 2017/9/15.
 */
public class CmdPulseSecondSendException extends SysException{
    public CmdPulseSecondSendException(Throwable error) {
        this(error.getMessage(), EC_UNKOWN);
    }

    public CmdPulseSecondSendException(String message, int errorCode) {
        super(message, errorCode);
    }

    public CmdPulseSecondSendException(String message, int errorCode, Object data) {
        super(message, errorCode, data);
    }

}
