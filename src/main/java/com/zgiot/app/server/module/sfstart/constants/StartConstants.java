package com.zgiot.app.server.module.sfstart.constants;

import org.springframework.context.annotation.Configuration;

@Configuration
public class StartConstants {

    // 启车清除信号发送成功
    public static final int START_SEND_CLEAN_STATE = 15;
    // 启车信号发送中
    public static final int START_SEND_INFORMATION_STATE = 20;

    // 启车信号发送成功
    public static final int START_SEND_FINISH_STATE = 25;

    // 正式启车
    public static final int START_STARTING_STATE = 30;

    // 启车结束
    public static final int START_FINISH_STATE = 40;


    // 没有启车
    public static final int START_NO_STATE = 0;

    // 二期
    public static final int PRODUCTION_LINE_TWO = 2;

    // 系统类型-系统类
    public static final int SYSTEM_TYPE_SYSTEM = 1;

    // 系统类型-设备类
    public static final int SYSTEM_TYPE_DEVICE = 2;
    // 阀门开关故障
    public static final int TAP_ERROR_TYPE = 3;
    // 液位故障
    public static final int LEVEL_ERROR_TYPE = 4;
    // 阀门关到位
    public static final String TAP_CLOSE = "132";
    // 阀门开到位
    public static final String TAP_OPEN = "112";


    // 集控开阀门
    public static final String REMOTE_OPEN_TAP = "238";

    // 集控关阀门
    public static final String REMOTE_CLOSE_TAP = "239";
    // 集控就地切换
    public static final String REMOTE_LOCAL_CONTROL = "227";

    // 故障修复
    public static final String HANDLE_EXCEPTION = "159";

    // 启车选择页面使用系统大类
    public static final int SYSTEM_CATEGORY_SELECT_PAGE = 1;
    // 启车中页面使用系统大类
    public static final int SYSTEM_CATEGORY_STARTING_PAGE = 2;

    // 系统类型-总览页面系统
    public static final int SYSTEM_TYPE_BROWSE_COAL = 3;

    // 设备故障
    public static final int DEVICE_STATE_ERROR = 1;

    // 信号boolean类型正确
    public static final int VALUE_TRUE = 1;


    // 信号boolean类型错误
    public static final int VALUE_FALSE = 0;

    // short类型值
    public static final int LABEL_TYPE_SHORT = 1;

    // boolean类型值
    public static final int LABEL_TYPE_BOOLEAN = 2;


    public static final String URI_START_STATE = "/topic/start/state";// 发送启车运行状态
    // 暂停启车
    public static final String URI_START_STATE_MESSAGE_PAUSE = "pause";
    // 继续启车
    public static final String URI_START_STATE_MESSAGE_CONTINUE_START = "continueStart";
    // 结束启车
    public static final String URI_START_STATE_MESSAGE_CLOSE_START = "closeStart";
    // 结束检查，放弃本次启车
    public static final String URI_START_STATE_MESSAGE_CLOSE_EXAMINE = "closeExamine";
    // 结束检查，信号下发无误，开始启车
    public static final String URI_START_STATE_MESSAGE_SET_UP_START_TASK = "setUpStartTask";

    // 信号下发失败，可重新发送
    public static final String URI_START_STATE_MESSAGE_SEND_ERROR = "sendError";
    // PLC信号检查下发异常，结束启车
    public static final String URI_START_STATE_MESSAGE_CHECK_ERROR = "checkError";
    // 信号下发结束
    public static final String URI_START_STATE_MESSAGE_SEND_FINISH = "sendFinish";

    // 启车结束
    public static final String URI_START_STATE_MESSAGE_START_FINISH = "startFinish";
    // 自检初查完毕
    public static final String URI_START_STATE_MESSAGE_EXAMINE_FINISH = "examineFinish";

    public static final String URI_MANUAL_INTERVENTION = "/topic/start/manual/interventionStart";// 人工干预变更

    public static final String URI_MANUAL_INTERVENTION_MESSAGE_MANUAL_INTERVENTION_CHANGE = "manualInterventionChange";
    // 清除信号发送成功
    public static final String URI_START_STATE_MESSAGE_SEND_MESSAGE_START = "sendMessageStart";

    public static final String URI_START_AUTO_EXAMINE = "/topic/start/auto/examine";// 发送启车自检结果变化信息

    public static final String URI_START_BROWSE_COALCAPACITY = "/topic/start/browse/coalCapacity";// 发送启车总览带煤量


    public static final String URI_START_BROWSE_COALDEPORT = "/topic/start/browse/coalDeport";// 发送启车总览仓库信息


    public static final String URI_START_BROWSE_STATE = "/topic/start/browse/state";// 发送启车总览页面设备状态


    public static final String URI_START_DEVICE_STATE = "/topic/start/device/state";// 发送设备运行状态


    // 设定频率
    public static final String SET_FREQUENCY = "173";

    public static final String START_TYPE_NORMAL = "0";

    // zgkw_start_preset_startandcoal_pararmeter表type字段启动状态
    public static final int TYPE_STARTING = 1;

    // 逻辑删除
    public static final int IS_DELETE = 1;
    // 启车首次自检完成
    public static final int START_EXAMIN_STATE = 10;


    // 信号下发等待时间
    public static final int SEND_MESSAGE_WAIT_TIME = 10;

    // 信号下发等待时间
    public static final int SEND_MESSAGE_CIRCLE_TIME = 20;

    // 集控就地类故障
    public static final int REMOTE_ERROR_TYPE = 1;

    // 设备运行
    public static final int DEVICE_STATE_WORKING = 2;

    // 设备故障
    public static final int DEVICE_ERROR_TYPE = 2;
    // 设备运行状态
    public static final String DEVICE_STATE = "1200";

    // 仓库信号1
    public static final String DEPOT_ONE = "1235";

    // 仓库信号2
    public static final String DEPOT_TWO = "1236";

    // 带煤量
    public static final String COAL_CAPACITY = "1050";


    // 信号下发等待时间
    public static final int SEND_SINGLE_VALUE_WAIT_TIME = 6000;

    // 区域起始范围
    public static final int START_AREA = 1;
    // 区域非起始范围
    public static final int NO_START_AREA = 0;
    // 区域状态有效
    public static final int AREA_STATE_TRUE = 1;

    // 区域状态无效
    public static final int AREA_STATE_FALSE = 0;

    // 启车结束PLC值
    public static final double  START_PLC_FINISH = 4.0;

    // 区域允许启动
    public static final String AREA_START = "1537";
    // 人工干预
    public static final String MANUAL_INTERVENTION = "1541";

    // 延迟启动时间
    public static final String WAIT_TIME = "1531";
    // 包所属区域
    public static final String BAG_BELONG_AREA = "1529";
    // 包所属大区
    public static final String BAG_BELONG_REGION = "1530";
    // 区域所属大区
    public static final String AREA_BELONG_REGION = "1532";

    // 参与启车
    public static final String IS_STARTING = "1387";

    // 必要条件
    public static final String IS_REQUIREMENT = "1540";

    // 设备功率
    public static final String RATE_WORK = "1439";

    // 变压器号
    public static final String TRANSFORMER_ID = "1440";

    // 变压器容量
    public static final String TRANSFORMER_VALUE = "1441";

    // 等待时间
    public static final String START_WAIT_TIME = "1390";

    // 设备所属大区
    public static final String DEVICE_REGION_ID = "1528";

    // 设备所属区域
    public static final String DEVICE_AREA_ID = "1527";

    // 设备所属包
    public static final String DEVICE_BAG_ID = "1526";

    // 不干预
    public static final int MANUAL_INTERVENTION_FALSE = 0;
    // 干预中
    public static final int MANUAL_INTERVENTION_TRUE = 1;
    // 临时干预
    public static final int MANUAL_INTERVENTION_TEMPORARY = 2;

    // 设备待机
    public static final int DEVICE_STATE_STANDBY_MODE = 0;


    // 总览页面使用系统大类
    public static final int SYSTEM_CATEGORY_BROWSE_PAGE = 3;
    // 系统仓库页面使用系统大类
    public static final int SYSTEM_CATEGORY_COAL_DEPOT_PAGE = 4;

    //启车准备
    public static final int START_PREPARE_STATE = 5;

    // 尚未自检
    public static final int EXAMINE_RESULT_NO = 0;

    // 启车中解除干预
    public static final int MANUAL_INTERVENTION_REMOVE = 2;

    public static final String FILTER_TYPE_DEVICE_ID = "filter_type_device_id";

    public static final String FILTER_TYPE_CONTROL_DEVICE_ID = "filter_type_control_device_id";

    // 前置条件正确
    public static final int REQUIRENMENT_TRUE = 0;

    // 前置条件错误
    public static final int REQUIRENMENT_FALSE = 1;

    // 大于
    public static final String COMPARE_GREATER_THAN = "greater";

    // 小于
    public static final String COMPARE_LESS_THAN = "less";

    // 等于
    public static final String COMPARE_EQUAL_TO = "equal";

    // 大于等于
    public static final String COMPARE_GREATER_THAN_AND_EQUAL_TO = "greaterAndEqual";

    // 小于等于
    public static final String COMPARE_LESS_THAN_AND_EQUAL_TO = "lessAndEqual";

    // 不等于
    public static final String COMPARE_NOT_EQUAL_TO = "notEqual";

    // PLC清除命令
    public static String CLEAN_LABEL = "TCS2_12_qt_cw.TCS2_12_qt_cw.SYS/S_B/SEQ_CLEAR/0";

    // 启车结束命令
    public static String FINISH_STARTING_LABEL = "TCS2_12_qt_cw.TCS2_12_qt_cw.SYS/S_PR/SEQ_PR/0";

    // 启车暂停命令
    public static String PAUSE_STARTING_LABEL = "TCS2_12_qt_cw.TCS2_12_qt_cw.SYS/S_B/SEQ_PAUSE/0";

    // 启车命令
    public static String START_DEVICE_LABEL = "TCS2_12_qt_cw.TCS2_12_qt_cw.SYS/S_B/SEQ_START/0";


    // 信号下发错误时间
    public static final int SEND_SINGLE_VALUE_ERROR_WAIT_TIME = 10000;

    // 自检正常
    public static final int EXAMINE_RESULT_RIGHT = 1;
    // 自检异常
    public static final int EXAMINE_RESULT_ERROR = 2;


    // float类型值
    public static final int LABEL_TYPE_FLOAT = 3;


    public  static final String TRUE="true";

    public  static final String FALSE="false";

    public static final String  BOO="BOO";


}
