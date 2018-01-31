package com.zgiot.app.server.module.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

/**
 * Created by wangfan on 2018/1/8.
 */
public class ValidateParamUtil {

    private static Logger logger = LoggerFactory.getLogger(ValidateParamUtil.class);

    public static String getBindingResultError(BindingResult bindingResult){
        StringBuffer errormsg = new StringBuffer();
        for(ObjectError objectError:bindingResult.getAllErrors()){
            errormsg.append(objectError.getDefaultMessage())
                    .append(";");
        }
        logger.error("必传参数错误,错误信息:{}", errormsg);
        return errormsg.toString();
    }

}
