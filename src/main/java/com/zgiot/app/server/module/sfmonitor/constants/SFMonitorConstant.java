package com.zgiot.app.server.module.sfmonitor.constants;

public class SFMonitorConstant {
    public static final String FROM = "from";//自
    public static final String TO = "to";//至
    public static final String SIMILAR = "similar";//同
    public static final String KEY_CHANNEL = "keychannel";//关键通道
    public static final String SELECTED_PARAMETER = "selectedparameter";
    public static final String EQUIPMENT = "EQUIPMENT";//设备
    public static final String NOT_CONFIG = "未配置";
    public static final String COMPLETED_CONFIG = "配置完成";
    public static final String AUXILIARY_AREA = "auxiliary_area";//辅助区
    public static final String STATE_AREA = "state_area";//状态区
    public static final String STATE_AREA_FIND = "state_area_find";//查看状态区包名称标记
    public static final String PARAMETER_SET = "PS";//参数设定类信号
    public static final String STATE_SET = "SS";//状态设定类信号
    public static final int FIND = 0;//查看
    public static final int OPERATE = 1;//操作
    public static final int NO_SHOW = 2;//不显示
    public static final String ALL_PARAMETER = "all";
    public static final String MONITOR_GROUP = "智能监控";
    public static final String FUZZY_QUERY_TAG = "%";
    public static final String STATE = "STATE";
    public static final int SECOND_TO_MILLSECOND = 1000;
//    rule描述：
//            1:不同部件所有点放到一个信号包中
//            2:只针对远程阀门，信号包中包含不同阀门thingCode和信号点
//            3:信号保重需要显示设备名称，一个信号包中只包含一个部件和信号点
    public static final int COMPOSITION_ALL_PARTION = 1;
    public static final int REMOTE_VALVE = 2;
    public static final int SINGLE_PARTION = 3;
    public static final String STATE_CONTRL = "状态控制";
    public static final int DIRECTION = 0;//只对设备保护包有效，1：pad显示设备保护左边部分包，2：pad显示右边部分包
    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_RIGHT = 2;
    public static final String EQUIPMENT_STATE = "设备状态";
}
