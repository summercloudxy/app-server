package com.zgiot.app.server.module.sfstop.enums;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by lyj on 2017/6/6.
 */
public enum ResultCode {
    SUCCESS(0, "成功"), //
    WRONG(-1, "未知错误"), //
    NOT_LOGIN(-2, "尚未登录"), //
    DB_WRONG(-3, "数据库异常"), //
    WRONG_PWD(-4, "密码错误"), //
    UPDATE_INWRONG(-5, "已存在出库流水，不允许修改"), //
    UPDATE_OUTWRONG(-6, "库存不足"), //
    UPDATE_DEVICEWRONG(-7, "使用位置不正确"), //
    OFF_DUTY(-8, "休班"), //
    TOURED(9, "巡视"), //
    GESTURENULL(-9, "未设置手势密码"), //
    BATCH_NOT_EXIST(-10, "批次不存在"), //
    GROUP_NOT_EXIST(-11, "组不存在"), //
    DO_NOT_DELETE(-12, "不能删除"),//
    PARAM_EMPTY_EXCEPTION(-13, "重要参数错误,"),//
    WRONGRELATIONDEVICE(-14,"此设备已经存在所属变压器"),//此设备已经存在所属变压器
    NAMEAGAIN(-15,"名称重复"),
    NOT_MATCH_TRANSFORMATION(-16,"此设备没有匹配变压器"),
    NOT_MATCH_BAG(-17,"此设备已经匹配对应的包"),
    BAG_NOT_MATCH_DEVICE(-18,"包下无对应设备"),
    MORE_60_MIN(-19,"您本次启车配置超过60min，请重新配置，谢谢"),
    BAG_DELAY_AGAIN(-20,"包的延时时间会和已经有的启车时间重叠"),
    BAG_HAVE_LINE(-21,"此包已经拥有启车线了"),
    BAG_HAVE_DEVICE(-22,"包下有对应设备，请先删除包和设备的对应关系"),
    TRANSFORMER_HAVE_DEVICE(-23,"此变压器下有对应的设备，请先删除变压器设备对应关系"),
    REGION_HAVE_AREA(-24,"此大区下有对应区域，请先删除对应区域"),
    AREA_HAVE_BAG(-25,"此区域下有包，请先删除对应的包"),
    AREA_HAVE_LINE(-26,"此区域下有线，请先删除对应的线"),
    LINE_HAVE_BAG(-27,"线上有对应的包，请先删除线和包的对应关系"),
    DEVICE_HAVE_BAG(-28,"此设备所在包在启车线上，请先删除启车线上的包"),
    ERROR_DAO(-29,"Dao相关错误"),
    NAME_AGAIN(-30,"您输入的名称已经占用，请修改后继续添加"),
    NULL_OR_MORE_PARAM_EXCEPTION(-31, "处理数据不存在或者指向多条数据，请检查数据是否错误"),
    SYSTEM_HAVE_TRANSFORMER(-32,"此系统下有变压器，请先删除或改变变压器信息"),
    SESSION_EXPIRED(1000,"会话过期");
    private int code;
    private String info;

    private ResultCode(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public String getInfoForUrl() {
        String encodingInfo = null;
        try {
            encodingInfo = URLEncoder.encode(info, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodingInfo;
    }

    public String getInfo() {
        return info;
    }
}
