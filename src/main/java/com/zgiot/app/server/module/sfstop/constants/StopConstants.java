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
    public static final String Quit_SYS_2 = "Quit_SYS_2";
    //尾矿排料阀
    public static final String PL_1 = ".PL-1";

    // 没有启车
    public static final int STOP_NO_STATE = 0;

    //停车准备
    public static final int STOP_PREPARE_STATE = 5;
    // 启车首次自检完成
    public static final int STOP_EXAMIN_STATE = 10;
    // 停车信号发送中
    public static final int STOP_SEND_FINISH_STATE = 25;
    // 正式停车
    public static final int STOP_STARTING_STATE = 30;
    // 停车结束
    public static final int STOP_FINISH_STATE = 40;


}
