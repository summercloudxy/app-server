package com.zgiot.app.server.module.sfstop.constants;

import org.springframework.context.annotation.Configuration;

@Configuration
public class StopConstants {
    /**
     * 未删除
     */
    public static final int IS_ENABLED = 0;
    /**
     * 逻辑删除
     */
    public static final int IS_DELETE = 1;

    // 启车结束
    public static final int START_FINISH_STATE = 40;

    public static final String TIMEFORMAT = "yyyy-MM-dd HH:mm:ss";

    //运行状态-待机
    public static final String RUNSTATE_1 = "1";


    //运行状态-运行
    public static final String RUNSTATE_2 = "2";


    //运行状态-故障
    public static final String RUNSTATE_4 = "4";

    //洗选大区
    public static final Long REGION_1 = 1L;


    // 原煤仓下皮带走向
    public static final String Quit_SYS_1 = "Quit_SYS_1";
    public static final String Quit_SYS_2 = "Quit_SYS_2";

    //尾矿排料阀
    public static final String PL_1 = ".PL-1";

    // 没有启车
    public static final int STOP_NO_STATE = 0;

    //停车方案选择
    public static final int STOP_CHOICE_SET = 5;


    // 启车首次自检完成
    public static final int STOP_EXAMIN_STATE = 10;
    // 正式停车
    public static final int STOP_STOPING_STATE = 30;
    // 停车暂停
    public static final int STOP_PAUSE_STATE = 35;
    // 停车结束
    public static final int STOP_FINISH_STATE = 40;

    //是否人工干预
    public static final int MANUALINTERVENTION_1 = 1;
    public static final int MANUALINTERVENTION_0 = 0;

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


    public static final String SYSTEM_1 = "1";
    public static final String SYSTEM_2 = "2";


    public static final String SUCCESS = "success";

    private static final String MESSAGE_URI = "/topic/stop/message";


    // 信号下发等待时间
    public static final int SEND_MESSAGE_WAIT_TIME = 10;

    // 信号下发等待时间
    public static final int SEND_MESSAGE_CIRCLE_TIME = 20;
    public static final String URI_STOP_STATE = "/topic/stop/state";// 发送停车运行状态

    // 结束检查，放弃本次停车
    public static final String URI_STOP_STATE_MESSAGE_CLOSE_EXAMINE = "closeExamine";

    // 自检初查完毕
    public static final String URI_STOP_STATE_MESSAGE_EXAMINE_FINISH = "examineFinish";

    public static final String URI_MANUAL_INTERVENTION = "/topic/stop/manual/interventionStop";// 人工干预变更

    public static final String URI_MANUAL_INTERVENTION_MESSAGE_MANUAL_INTERVENTION_CHANGE = "manualInterventionChange";


    // 停车车中解除干预
    public static final int MANUAL_INTERVENTION_REMOVE = 2;

    // 停车结束
    public static final String URI_STOP_STATE_MESSAGE_STOP_FINISH = "stopFinish";

    // 暂停停车
    public static final String URI_STOP_STATE_MESSAGE_PAUSE = "pause";

    // 继续停车
    public static final String URI_STOP_STATE_MESSAGE_CONTINUE_STOP = "continueStop";
    // 结束停车
    public static final String URI_STOP_STATE_MESSAGE_CLOSE_STOP = "closeStop";

    // 集控就地类故障
    public static final int REMOTE_ERROR_TYPE = 1;

    // 液位故障
    public static final int LEVEL_ERROR_TYPE = 2;


}
