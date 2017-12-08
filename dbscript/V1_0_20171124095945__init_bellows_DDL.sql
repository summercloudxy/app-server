-- ----------------------------
-- Table structure for tb_bellows_compressor_log
-- ----------------------------
CREATE TABLE `tb_bellows_compressor_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(30) DEFAULT NULL COMMENT '设备号',
  `operation` varchar(10) DEFAULT NULL COMMENT '操作类型(启动/停止，加载/卸载）',
  `operate_type` varchar(10) DEFAULT NULL COMMENT '操作类型(手动/智能)',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `pre_state` varchar(10) DEFAULT NULL COMMENT '操作前状态',
  `post_state` varchar(10) DEFAULT NULL COMMENT '操作后状态',
  `confirm_time` datetime DEFAULT NULL COMMENT '确认操作后状态时间',
  `pressure` double(64,2) DEFAULT NULL COMMENT '确认时压力',
  `request_id` varchar(64) DEFAULT NULL COMMENT '请求id',
  `memo` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_bellows_compressor_state
-- ----------------------------
CREATE TABLE `tb_bellows_compressor_state` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(30) DEFAULT NULL COMMENT '设备号',
  `post_state` varchar(20) DEFAULT NULL COMMENT '修改后状态',
  `time` datetime DEFAULT NULL COMMENT '创建时间',
  `pre_state` varchar(20) DEFAULT NULL COMMENT '修改前状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_bellows_config
-- ----------------------------
CREATE TABLE `tb_bellows_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(30) DEFAULT NULL,
  `param_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `param_value` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_bellows_valve_log
-- ----------------------------
CREATE TABLE `tb_bellows_valve_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(30) DEFAULT NULL COMMENT '设备号',
  `operation` varchar(10) DEFAULT NULL COMMENT '操作类型(开/关阀门）',
  `operate_type` varchar(10) DEFAULT NULL COMMENT '操作类型(手动/智能)',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `pre_state` varchar(10) DEFAULT NULL COMMENT '操作前状态',
  `post_state` varchar(10) DEFAULT NULL COMMENT '操作后状态',
  `confirm_time` datetime DEFAULT NULL COMMENT '确认操作后状态时间',
  `low_pressure` double(64,2) DEFAULT NULL COMMENT '确认时低压',
  `high_pressure` double(64,2) DEFAULT NULL COMMENT '确认时高压',
  `request_id` varchar(64) DEFAULT NULL COMMENT '请求id',
  `memo` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=426 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_bellows_valve_team
-- ----------------------------
CREATE TABLE `tb_bellows_valve_team` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `next_id` bigint(20) DEFAULT NULL COMMENT '下一组id',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  `exec_time` datetime DEFAULT NULL COMMENT '执行时间',
  `duration` int(11) DEFAULT NULL COMMENT '执行时长（分）',
  `type` varchar(10) DEFAULT NULL COMMENT '类型（块煤/末煤）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
