package com.zgiot.app.server.module.densitycontrol.constants;

public class DensitycontrolConstants {

    public static final String LEVEL_MODE = "LEVEL_MODE";// 液位模式(高,正常,低)
    public static final String CONTROL_MODE = "CONTROL_MODE";// 分流阀控制模式(主控,非主控)
    public static final String DENSITY_CONTROL_MODE = "DENSITY_CONTROL_MODE";// 密度计控制模式(主控,非主控)
    public static final String DENSITY_FLU2 = "DENSITY_FLU2";// 密度波动(高,正常,低)
    public static final String LOW_LEVEL_FLU = "LOW_LEVEL_FLU";// 低液位波动(高,正常,低)

    public static final String LE_JJ_SET = "LE_JJ_SET";// 加介液位设定

    public static final String TERM_TWO_THING_CODE = "2321";// 合介桶
    public static final String TERM_TWO_SYSTEM_A_THING_CODE = "2307";// 块煤系统A
    public static final String TERM_TWO_SYSTEM_B_THING_CODE = "2308";// 块煤系统B
    public static final String TERM_TWO_SYSTEM_A_FL_THING_CODE = "2307.FL-1";// 块煤系统A分流阀
    public static final String TERM_TWO_SYSTEM_B_FL_THING_CODE = "2308.FL-1";// 块煤系统B分流阀
    public static final String TERM_TWO_SYSTEM_A_BS_THING_CODE = "2307.BS-1";// 块煤系统A补水阀
    public static final String TERM_TWO_SYSTEM_B_BS_THING_CODE = "2308.BS-1";// 块煤系统B补水阀

    public static final String LE_N_O_TIME = "LE_N_O_TIME";// 正常液位开启时间
    public static final String LE_L_O_TIME = "LE_L_O_TIME";// 低液位开启时间
    public static final String LE_H_O_TIME = "LE_H_O_TIME";// 高液位开启时间

    // 正常液位逻辑参数
    public static final String TEST_DELAY = "TEST_DELAY";// 检测延时
    public static final String FL_TEST_DELAY = "FL_TEST_DELAY";// 分流阀开启检测延时
    public static final String BS_TEST_DELAY = "BS_TEST_DELAY";// 补水阀开启检测延时
    public static final String LE_N_DENSITY_RANGE_MAX = "LE_N_DENSITY_RANGE_MAX";// 密度最大范围
    public static final String LE_N_DENSITY_RANGE_MIN = "LE_N_DENSITY_RANGE_MIN";// 密度最小范围

    // 低液位逻辑参数
    public static final String LE_L_DENSITY_RANGE_MIN = "LE_L_DENSITY_RANGE_MIN";// 密度最小范围
    public static final String LE_L_LEVEL_RANGE_MAX = "LE_L_LEVEL_RANGE_MAX";// 液位最大范围
    public static final String LE_L_LEVEL_RANGE_MIN = "LE_L_LEVEL_RANGE_MIN";// 液位最小范围

    // 高液位逻辑参数
    public static final String LE_H_EXECUTE_TIME = "LE_H_EXECUTE_TIME";// 逻辑执行时间

    // 停车前逻辑参数
    public static final String PRE_STOP_TIME = "PRE_STOP_TIME";// 停车前时间

    // 设备运行状态
    public static final String RUN_STATE_1 = "1";// 运行

    public static final Integer SYSTEM_STATUS_0 = 0;// 无系统运行
    public static final Integer SYSTEM_STATUS_1 = 1;// 单系统运行
    public static final Integer SYSTEM_STATUS_2 = 2;// 双系统运行

    public static final String HIGH_MODE = "1";// 高模式值
    public static final String NORMAL_MODE = "2";// 正常模式值
    public static final String LOW_MODE = "4";// 低模式值

}
                                                  