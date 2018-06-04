package com.zgiot.app.server.module.densitycontrol.constants;

public class DensitycontrolConstants {

    public static final String CONTROL_MODE = "MODE_CONTROL";// 控制模式(高,正常,低)
    public static final String TERM_TWO_THING_CODE = "2321";// 合介桶
    public static final String TERM_TWO_SYSTEM_A_THING_CODE = "2307";// 块煤系统A
    public static final String TERM_TWO_SYSTEM_B_THING_CODE = "2308";// 块煤系统B

    // 正常液位逻辑参数
    public static final String DELAY_TEST = "DELAY_TEST";// 检测延时
    public static final String DELAY_TEST_FL = "DELAY_TEST_FL";// 分流阀开启检测延时
    public static final String DELAY_TEST_BS = "DELAY_TEST_BS";// 补水阀开启检测延时
    public static final String TIME_FL = "TIME_FL";// 分流阀开启时间
    public static final String TIME_BS = "TIME_BS";// 补水阀开启时间
    public static final String DENSITY_MAX = "DENSITY_MAX";// 密度最大范围
    public static final String DENSITY_MIN = "DENSITY_MIN";// 密度最小范围

    // 低液位逻辑参数
    public static final String DENSITY_MIN_LOW = "DENSITY_MIN_LOW";// 密度最小范围
    public static final String TIME_BS_LOW = "TIME_BS_LOW";// 补水阀开启时间
    public static final String LEVEL_MAX_LOW = "LEVEL_MAX_LOW";// 液位最大范围
    public static final String LEVEL_MIN_LOW = "LEVEL_MIN_LOW";// 液位最小范围

    // 高液位逻辑参数
    public static final String TIME_FL_HIGH = "TIME_FL_HIGH";// 分流阀开启时间
    public static final String TIME_EXECUTE_HIGH = "TIME_EXECUTE_HIGH";// 逻辑执行时间

    // 停车前逻辑参数
    public static final String TIME_STOP = "TIME_STOP";// 停车前时间

    public static final String RUNS = "RUNS";// 运行
    
}
                                                  