package com.zgiot.app.server.exception;

import com.zgiot.common.exceptions.SysException;


/**
 * Created by xiayun on 2017/9/15.
 */
public class CmdPulseFirstSendException extends SysException {
    public CmdPulseFirstSendException(Throwable error) {
        this(error.getMessage(), EC_UNKOWN);
    }

    public CmdPulseFirstSendException(String message, int errorCode) {
        super(message, errorCode);
    }

    public CmdPulseFirstSendException(String message, int errorCode, Object data) {
        super(message, errorCode, data);
    }

}
