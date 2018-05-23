package com.zgiot.app.server.module.reportforms.output.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.core.annotation.Order;

import java.io.Serializable;

/**
 * restapi的返回值json结果
 *
 * @param <T>
 */
@ApiModel(value = "请求响应")
public class Ret<T> implements Serializable {
    @Order(1)
    @ApiModelProperty(value = "错误码")
    private String code;

    @Order(2)
    @ApiModelProperty(value = "错误描述")
    private String msg;

    @Order(3)
    @ApiModelProperty(value = "请求时间")
    private Long ctime;

    @Order(4)
    @ApiModelProperty(value = "请求编号")
    private String requestID;

    @Order(5)
    @ApiModelProperty(value = "响应数据")
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

