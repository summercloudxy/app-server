
CREATE TABLE `tb_start_area_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operate_id` int(11) NULL DEFAULT NULL COMMENT '启车操作id',
  `area_second_id` bigint(20) NULL DEFAULT NULL COMMENT '规则涉及区域id',
  `area_rule_id` int(11) NULL DEFAULT NULL COMMENT '规则id',
  `state` tinyint(2) NULL DEFAULT NULL COMMENT '规则状态(0:规则未满足,1:规则已满足)',
  `create_date` datetime(0) NULL DEFAULT NULL,
  `is_delete` tinyint(2) NULL DEFAULT NULL COMMENT '是否删除(0:未删除,1:已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;




CREATE TABLE `tb_start_area_setting_condition`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `area_first_id` bigint(20) NULL DEFAULT NULL COMMENT '区域id1',
  `area_second_id` bigint(20) NULL DEFAULT NULL COMMENT '区域id2',
  `type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设置条件',
  `delay_time` bigint(20) NULL DEFAULT NULL COMMENT '延时时间',
  `device_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前置设备id',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新的用户id',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `parent_state` tinyint(1) NULL DEFAULT 0 COMMENT '是否区域id1为开始启车',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '区域间设定条件表' ROW_FORMAT = Compact;



CREATE TABLE `tb_start_assets`  (
  `object_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '保留字段',
  `assets_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资产名称',
  `assets_models` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规格型号',
  `category_object_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'sff_assets_category',
  `manufactor_object_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'sff_device_manufactor',
  `type` int(11) NULL DEFAULT NULL COMMENT '0 设备，1 阀门，2 部件，3 仪表',
  `assets_name_id` int(11) NULL DEFAULT NULL COMMENT '0 设备，1部件，2仪表，3阀门',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产编号',
  `create_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  `state` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`object_id`) USING BTREE,
  UNIQUE INDEX `aobjid`(`object_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资产基础类' ROW_FORMAT = Dynamic;




CREATE TABLE `tb_start_assets_params`  (
  `object_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '保留字段',
  `param_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `param_value` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `assets_object_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`object_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10625 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备、部件等参数' ROW_FORMAT = Compact;




CREATE TABLE `tb_start_device_area`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '数据主键',
  `area_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `level` int(11) NULL DEFAULT NULL COMMENT '区域等级',
  `create_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否可用',
  `region_id` bigint(20) NULL DEFAULT NULL COMMENT '所属的大区id',
  `area_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '区域类型',
  `number` int(30) NULL DEFAULT NULL COMMENT '哪个大区第几个区域编号',
  `position` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '启车区域配置—区域表' ROW_FORMAT = Dynamic;





CREATE TABLE `tb_start_device_bag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '数据主键',
  `bag_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `area_id` bigint(20) NULL DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `create_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `number` int(30) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '启停区域配置包—包下' ROW_FORMAT = Dynamic;




CREATE TABLE `tb_start_device_region`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `region_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `level` int(11) NULL DEFAULT NULL COMMENT '大区等级',
  `create_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新的用户id',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  `number` int(30) NULL DEFAULT NULL COMMENT '第几个大区编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '启车区域设置—大区表' ROW_FORMAT = Dynamic;




CREATE TABLE `tb_start_device_signal`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备信号名称',
  `type` int(11) NULL DEFAULT NULL COMMENT '1.设备名称 2.信号名称',
  `signal_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `signal_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  `state` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1564 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备信号名称表' ROW_FORMAT = Compact;




CREATE TABLE `tb_start_device_state_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `operate_id` bigint(11) NULL DEFAULT NULL,
  `device_id` varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `state` tinyint(2) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `is_delete` tinyint(2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;


CREATE TABLE `tb_start_examine_record`  (
  `examine_id` int(11) NOT NULL AUTO_INCREMENT,
  `operate_id` int(11) NULL DEFAULT NULL COMMENT '启车操作id',
  `start_device_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参与启车设备id',
  `rule_id` int(11) NULL DEFAULT NULL COMMENT '检查规则id',
  `examine_type` int(11) NULL DEFAULT NULL,
  `examine_information` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '异常详情',
  `examine_result` int(2) NULL DEFAULT NULL COMMENT '检查状态(0:未检查，1:正常,2:异常)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_delete` tinyint(2) NULL DEFAULT NULL COMMENT '是否删除（0:不删除，1:删除）',
  PRIMARY KEY (`examine_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;




CREATE TABLE `tb_start_examine_rule`  (
  `rule_id` int(11) NOT NULL AUTO_INCREMENT,
  `start_device_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参与启车设备id',
  `examine_type` int(11) NULL DEFAULT NULL,
  `examine_device_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '检查设备id',
  `examine_name` int(11) NULL DEFAULT NULL COMMENT '检查name值',
  `operator` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '比较符(1:大于,2:小于,3:等于,4:大于等于,5:小于等于,6:不等于)',
  `compare_value` float(11, 2) NULL DEFAULT NULL COMMENT '比较值',
  `create_user` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `is_delete` tinyint(2) NULL DEFAULT NULL COMMENT '是否删除（0:不删除，1:删除）',
  PRIMARY KEY (`rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 405 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;



CREATE TABLE `tb_start_examine_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则名称',
  `is_start_necessary` tinyint(2) NULL DEFAULT NULL COMMENT '是否启车必须',
  `handle_name` int(11) NULL DEFAULT NULL,
  `create_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `is_delete` tinyint(2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;



CREATE TABLE `tb_start_information`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `production_line` tinyint(2) NULL DEFAULT NULL COMMENT '期数(1：一期设备，2：二期设备)',
  `device_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备id',
  `start_rule_id` tinyint(2) NULL DEFAULT NULL COMMENT '启车规则号（可重复）',
  `start_sequence` tinyint(2) NULL DEFAULT NULL COMMENT '启车顺序（可重复）',
  `start_wait_time` int(11) NULL DEFAULT 0 COMMENT '启动延时',
  `rate_work` double(12, 2) NULL DEFAULT NULL COMMENT '功率',
  `transformer_id` int(11) NULL DEFAULT NULL COMMENT '变压器id',
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  `is_delete` tinyint(2) NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1删除）',
  `start_hierarchy` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备层级1-1-1-1',
  `rate_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1:直启2:变频',
  `start_type` int(20) NULL DEFAULT NULL COMMENT '启车类型0：正常 1：复杂',
  `Is_complex_type` tinyint(1) NULL DEFAULT 0 COMMENT '是否复杂启车0：正常 1复杂',
  `Is_intervention` tinyint(1) NULL DEFAULT 0 COMMENT '是否干预0：不干预 1:干预',
  `create_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bag_id` bigint(20) NULL DEFAULT NULL COMMENT '所属包id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 363 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;




CREATE TABLE `tb_start_location`  (
  `object_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '对象唯一ID，保留字段',
  `location_area` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区域',
  `building_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `floor` int(11) NULL DEFAULT -999 COMMENT '楼层',
  `location_pos_x` double(11, 0) NULL DEFAULT -999 COMMENT '座标(x)',
  `location_pos_y` double(11, 0) NULL DEFAULT -999 COMMENT '座标(y)',
  PRIMARY KEY (`object_id`) USING BTREE,
  UNIQUE INDEX `lobj`(`object_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '定位信息' ROW_FORMAT = Compact;




CREATE TABLE `tb_start_manual_intervention`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `device_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `state` tinyint(1) NULL DEFAULT 0 COMMENT '0:不干预1：长期干预2;临时干预',
  `create_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 358 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '人工预设' ROW_FORMAT = Dynamic;





CREATE TABLE `tb_start_manual_intervention_record`  (
  `manual_intervention_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '人工干预主键id',
  `operate_id` int(11) NULL DEFAULT NULL COMMENT '启车操作id',
  `device_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参与启车设备id',
  `intervention_state` int(11) NULL DEFAULT NULL COMMENT '干预状态(0:未干预，1:干预中,2:解除干预)',
  `intervention_person_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `relieve_person_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_delete` tinyint(2) NULL DEFAULT NULL COMMENT '是否删除（0:不删除，1:删除）',
  PRIMARY KEY (`manual_intervention_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1017 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;



CREATE TABLE `tb_start_operation_record`  (
  `operate_id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '操作记录id',
  `create_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `operate_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作用户',
  `operate_state` tinyint(4) NULL DEFAULT NULL COMMENT '操作状态，记录当前操作状态为（10:启车检查15:清除信号发送成功,20:信号发送中，25:启车信号发送成功，30:正在启车，40:完成启车）',
  `operate_system` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作的系统id（以;分割）',
  `created_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `is_delete` tinyint(2) NULL DEFAULT NULL COMMENT '是否无效(0有效1无效)',
  PRIMARY KEY (`operate_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 763 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '启车操作记录' ROW_FORMAT = Dynamic;





CREATE TABLE `tb_start_preset_startandcoal_pararmeter`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dic_signal_id` int(11) NULL DEFAULT NULL COMMENT 'name',
  `default_value` float(50, 2) NULL DEFAULT NULL COMMENT '预设值（HZ）',
  `type` int(10) NULL DEFAULT NULL COMMENT '属于启动1还是带煤2',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  `parent_deviced_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '隶属于哪个设备id',
  `create_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新的用户id',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `start_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '0正常 1带载',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 108 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '启车预设参数启动和带煤参数表' ROW_FORMAT = Dynamic;





CREATE TABLE `tb_start_production_location`  (
  `object_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `location_serial_num` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `system_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备系统id，type为1,2,3时为空',
  `parent_production_location_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上一级生产位置，type为0时为空',
  `level` int(11) NULL DEFAULT NULL,
  `setup_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `assets_object_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目前关联的资产',
  `type` int(2) NULL DEFAULT NULL COMMENT '0 设备，1 阀门，2 部件，3 仪表',
  `short_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `home_param` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `icon_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `node_level` int(11) NULL DEFAULT NULL,
  `seqno` int(11) NULL DEFAULT NULL,
  `create_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  `state` tinyint(4) NULL DEFAULT NULL,
  `full_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`object_id`) USING BTREE,
  UNIQUE INDEX `OBJID`(`object_id`) USING BTREE,
  INDEX `index_system_id`(`system_id`) USING BTREE,
  INDEX `object_id`(`object_id`, `location_serial_num`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '生产线位置表' ROW_FORMAT = Dynamic;





CREATE TABLE `tb_start_production_system`  (
  `object_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `system_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产线名称',
  `parent_system_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上一级系统id',
  `level` int(11) NULL DEFAULT NULL COMMENT '系统层级',
  `description` tinytext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `node_level` int(11) NULL DEFAULT NULL,
  `seqno` int(11) NULL DEFAULT NULL COMMENT '排序索引',
  `create_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  `state` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`object_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '生产系统' ROW_FORMAT = Compact;


CREATE TABLE `tb_start_signal`  (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `deviceId` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备IDsff_production_location',
  `typeId` int(11) NULL DEFAULT NULL COMMENT 'dic_signal 信号类别ID:16参数、17故障、18显示、19状态 20保护 21故障源',
  `name` int(11) NULL DEFAULT NULL COMMENT '信号名称：运行、故障、流量、电流、频率、集中就地等',
  `dataLabel` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据标签:kepserver',
  `type` int(11) NULL DEFAULT NULL COMMENT '信号类型 1.读取信号 2.控制信号',
  `alarmTerm` int(11) NULL DEFAULT NULL COMMENT '报警条件',
  `boolReal` int(11) NULL DEFAULT 0 COMMENT '1.bool 2.float(real) 3.int',
  `enableCondition` tinyint(4) NULL DEFAULT NULL COMMENT '使能条件',
  `term` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1.一期 2.二期',
  `deviceCode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  `state` tinyint(4) NULL DEFAULT NULL,
  `groupCode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备系统和信号类型分组',
  `unit` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位',
  `channel` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'plc所属group',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK_Reference_6`(`typeId`) USING BTREE,
  INDEX `datalabel`(`dataLabel`) USING BTREE,
  INDEX `signal_id`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18751 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '信号表' ROW_FORMAT = Dynamic;



CREATE TABLE `tb_start_system`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '系统id',
  `subsystem` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统名称',
  `system_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `production_line` int(11) NULL DEFAULT NULL COMMENT '生产线序号 1：一期，2：二期',
  `system_type` tinyint(4) NULL DEFAULT NULL COMMENT '类型（1：系统；2：设备；0：公用系统，不展示给用户看）',
  `system_category` tinyint(4) NULL DEFAULT NULL COMMENT '系统大类（1:启车选择系统，2:启车中系统，3:总览页面系统，4:仓库页面系统）',
  `system_sequence` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 206 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;




CREATE TABLE `tb_start_system_device_relation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `system_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统id',
  `device_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备生产位置id\r\n            ',
  `create_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_delete` tinyint(1) NULL DEFAULT 1 COMMENT '是否启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1184 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '启车系统和设备关联表' ROW_FORMAT = Dynamic;





CREATE TABLE `tb_start_system_synergic_relation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `system_id` bigint(11) NULL DEFAULT NULL,
  `synergic_system` bigint(11) NULL DEFAULT NULL,
  `is_delete` tinyint(2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 57 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;



CREATE TABLE `tb_start_transformer_information`  (
  `id` tinyint(2) NOT NULL AUTO_INCREMENT,
  `transformer_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变压器编号',
  `transformer_name` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变压器名',
  `transformer_tab_device_code` varchar(11) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT 'plc编码',
  `transformer_value` float(11, 2) NULL DEFAULT NULL COMMENT '启车功率',
  `building_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '位置id',
  `transformer_capacity` float(11, 2) NULL DEFAULT NULL COMMENT '变压器容量',
  `uuid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '唯一标识',
  `transformer_value_max` float(11, 2) NULL DEFAULT NULL COMMENT '允许启车最大功率',
  `transformer_system` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  `create_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;




CREATE TABLE `tb_start_type_set_delay`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `device_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备id',
  `delay_time` bigint(20) NULL DEFAULT NULL COMMENT '设备延时时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  `start_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '0正常 1带载',
  `parent_deviced_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '隶属于哪个设备id',
  `create_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新的用户id',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 593 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '启车启动条件设定延时表' ROW_FORMAT = Dynamic;





CREATE TABLE `tb_start_type_set_pararmeter`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `device_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备id',
  `dic_signal_id` int(11) NULL DEFAULT NULL COMMENT 'name',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  `comparison_operator` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '比较符1:>2:=3:<',
  `comparison_value` double(32, 3) NULL DEFAULT NULL COMMENT '值',
  `parent_deviced_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '隶属于哪个设备id',
  `create_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新的用户id',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `start_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '0正常 1带载',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 111 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '启车启动条件设定参数表' ROW_FORMAT = Dynamic;





