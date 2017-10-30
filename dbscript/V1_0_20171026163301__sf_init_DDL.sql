/*
Navicat MySQL Data Transfer

Source Server         : 192.168.5.34
Source Server Version : 50635
Source Host           : 192.168.5.34:3306
Source Database       : smartfactory2_master

Target Server Type    : MYSQL
Target Server Version : 50635
File Encoding         : 65001

Date: 2017-10-26 16:35:10
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for excel_range
-- ----------------------------
CREATE TABLE `excel_range` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `system` tinyint(4) DEFAULT NULL,
  `start_x` tinyint(4) DEFAULT NULL,
  `start_y` tinyint(4) DEFAULT NULL,
  `row` tinyint(4) DEFAULT NULL,
  `read_mode` tinyint(4) DEFAULT NULL,
  `target_gap` tinyint(4) DEFAULT NULL,
  `time_gap` tinyint(4) DEFAULT NULL,
  `device_code_gap` tinyint(4) DEFAULT NULL,
  `aad_gap` tinyint(4) DEFAULT NULL,
  `mt_gap` tinyint(4) DEFAULT NULL,
  `stad_gap` tinyint(4) DEFAULT NULL,
  `qar_gap` tinyint(4) DEFAULT NULL,
  `state` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for rel_thing_metric_label
-- ----------------------------
CREATE TABLE `rel_thing_metric_label` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(30) DEFAULT NULL,
  `metric_code` varchar(30) DEFAULT NULL,
  `label_path` varchar(64) DEFAULT NULL,
  `enabled` tinyint(4) DEFAULT NULL,
  `bool_reverse` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22576 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for rel_thingtype_metric
-- ----------------------------
CREATE TABLE `rel_thingtype_metric` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thingtype_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `metric_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_filterpress_config
-- ----------------------------
CREATE TABLE `tb_filterpress_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `param_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `param_value` double(11,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_metric
-- ----------------------------
CREATE TABLE `tb_metric` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `metric_category_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `metric_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `metric_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `metric_type1_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '维度1的叶子节点',
  `metric_type2_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '维度2的叶子节点',
  `metric_type3_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '维度3的叶子节点',
  `value_type` varchar(5) DEFAULT NULL,
  `value_unit` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_code` (`metric_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2735 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_metrictype
-- ----------------------------
CREATE TABLE `tb_metrictype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `metrictype_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `metrictype_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_code` (`metrictype_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=170 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_thing
-- ----------------------------
CREATE TABLE `tb_thing` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_thing_id` int(11) DEFAULT NULL,
  `thing_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `thing_category_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `thing_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `thing_type1_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '维度1的叶子节点',
  `thing_type2_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '维度2的叶子节点',
  `thing_type3_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '维度3的叶子节点',
  `thing_shortname` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_code` (`thing_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4525 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_thing_properties
-- ----------------------------
CREATE TABLE `tb_thing_properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(30) DEFAULT NULL,
  `prop_key` varchar(30) DEFAULT NULL,
  `prop_value` varchar(255) DEFAULT NULL,
  `prop_type` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `thing_index` (`thing_code`,`prop_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=184 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_thingtype
-- ----------------------------
CREATE TABLE `tb_thingtype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thingtype_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `thingtype_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `parent_thingtype_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for rel_metric_alert_type
-- ----------------------------
CREATE TABLE `rel_metric_alert_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `metric_code` varchar(10) DEFAULT NULL,
  `alert_type` tinyint(2) DEFAULT NULL COMMENT '信号报警类型：0.fault,2.protect,1.param',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_system
-- ----------------------------
CREATE TABLE `tb_system` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `system_name` varchar(20) DEFAULT NULL COMMENT '生产线名称',
  `parent_system_id` varchar(255) DEFAULT NULL,
  `level` int(11) DEFAULT NULL COMMENT '系统层级',
  `description` tinytext,
  `node_level` int(11) DEFAULT NULL,
  `seqno` int(11) DEFAULT NULL COMMENT '排序索引',
  `state` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8 COMMENT='生产系统';

-- ----------------------------
-- Table structure for rel_thing_system
-- ----------------------------
CREATE TABLE `rel_thing_system` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(20) DEFAULT NULL,
  `system_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4533 DEFAULT CHARSET=utf8 COMMENT='系统设备关联表';

-- ----------------------------
-- Table structure for tb_building
-- ----------------------------
CREATE TABLE `tb_building` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '建筑主键',
  `building_name` varchar(64) DEFAULT NULL COMMENT '建筑名称',
  `seq_no` int(11) DEFAULT NULL COMMENT '排序字段',
  `max_floor` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='建筑表';

-- ----------------------------
-- Table structure for tb_category
-- ----------------------------
CREATE TABLE `tb_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '类别主键',
  `code` varchar(255) DEFAULT NULL,
  `category_name` varchar(32) DEFAULT NULL COMMENT '类别名称',
  `parent` bigint(20) DEFAULT NULL COMMENT '父类别',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8mb4 COMMENT='设备类别';

-- ----------------------------
-- Table structure for tb_thing_position
-- ----------------------------
CREATE TABLE `tb_thing_position` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `thing_code` varchar(32) DEFAULT NULL COMMENT '编号',
  `building_id` int(20) DEFAULT NULL COMMENT '建筑主键',
  `floor` int(11) DEFAULT NULL COMMENT '位置楼层',
  `location_area` varchar(32) DEFAULT NULL COMMENT '位置区域',
  `location_x` double DEFAULT NULL COMMENT '位置X坐标',
  `location_y` double DEFAULT NULL COMMENT '位置Y坐标',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_code` (`thing_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4523 DEFAULT CHARSET=utf8mb4 COMMENT='设备、仪表、阀门、部件基础信息表';

-- ----------------------------
-- Table structure for tb_param_range
-- ----------------------------
CREATE TABLE `tb_param_range` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(30) DEFAULT NULL,
  `metric_code` varchar(10) DEFAULT NULL,
  `lower_limit` float(11,2) DEFAULT NULL COMMENT '报警阈值下限',
  `upper_limit` float(11,2) DEFAULT NULL COMMENT '报警阈值上限',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2016 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_alert_rule
-- ----------------------------
CREATE TABLE `tb_alert_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(30) DEFAULT NULL,
  `metric_code` varchar(10) DEFAULT NULL,
  `rule_type` tinyint(2) DEFAULT NULL COMMENT '报警规则类型：1.param,2.protect',
  `alert_level` tinyint(4) DEFAULT NULL COMMENT '报警等级（10  蓝,20  紫,30  黄） ',
  `lower_limit` float(11,2) DEFAULT NULL COMMENT '报警阈值下限',
  `upper_limit` float(11,2) DEFAULT NULL COMMENT '报警阈值上限',
  `enable` tinyint(1) DEFAULT '1' COMMENT '是否启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3326 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_fix_message
-- ----------------------------
CREATE TABLE `tb_fix_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `info` varchar(255) DEFAULT NULL,
  `module` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tb_file
-- ----------------------------
CREATE TABLE `tb_file` (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '项目图片编号',
  `filePath` varchar(1000) NOT NULL COMMENT '图片文件路径',
  `name` varchar(1000) DEFAULT NULL,
  `type` int(2) DEFAULT '0',
  `filesize` varchar(32) DEFAULT NULL,
  `state` int(2) DEFAULT '1',
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_user` varchar(50) DEFAULT NULL,
  `create_user` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8 COMMENT='文件上传表';

-- ----------------------------
-- Table structure for tb_alert_message
-- ----------------------------
CREATE TABLE `tb_alert_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `info` varchar(64) DEFAULT NULL,
  `type` tinyint(2) DEFAULT NULL,
  `alert_id` int(11) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `is_read` tinyint(1) DEFAULT '0' COMMENT '0：未读  1：已读',
  `user_id` varchar(11) DEFAULT NULL,
  `permission` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=966 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for tb_alert_mask
-- ----------------------------
CREATE TABLE `tb_alert_mask` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `alert_id` int(11) DEFAULT NULL COMMENT '屏蔽的报警id',
  `mask_id` int(11) DEFAULT NULL COMMENT '屏蔽信息id',
  `time` datetime DEFAULT NULL COMMENT '屏蔽时间',
  `user_id` varchar(11) DEFAULT NULL COMMENT '屏蔽的岗位工id',
  `param_value` double(255,0) DEFAULT NULL COMMENT '参数类报警屏蔽时的值',
  `param_lower` double(255,0) DEFAULT NULL COMMENT '参数类报警屏蔽时下限',
  `param_upper` double(255,0) DEFAULT NULL COMMENT '参数类报警屏蔽时上限',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;
-- ----------------------------
-- Table structure for tb_alert_data
-- ----------------------------
CREATE TABLE `tb_alert_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(30) DEFAULT NULL,
  `metric_code` varchar(10) DEFAULT NULL,
  `alert_source` tinyint(2) DEFAULT NULL COMMENT '报警触发方式：0.系统自动触发,1.人工生成',
  `alert_type` tinyint(2) DEFAULT NULL COMMENT '报警类型：0.故障 1.参数 2.保护 3.人工',
  `alert_level` tinyint(4) DEFAULT NULL COMMENT '报警等级（10,20,30）',
  `alert_datetime` datetime DEFAULT NULL,
  `alert_info` varchar(30) DEFAULT NULL COMMENT '报警信息',
  `reporter` varchar(20) DEFAULT NULL COMMENT '报警发起人 ',
  `alert_stage` varchar(20) DEFAULT NULL COMMENT '报警阶段："NOT_VERIFY"; 未核实、已发起  "VERIFIED";  已核实、已评级    "UNTREATED"; 未处理"REQUEST_REPAIR"; 申请维修    "REPAIRING";维修中   "REPAIRED";维修结束    "RELEASE"; 报警解除',
  `is_repair` tinyint(1) DEFAULT NULL COMMENT '发起维修 0.否 1.是',
  `repair_confirm_user` varchar(20) DEFAULT NULL COMMENT '维修确认人',
  `repair_start_time` datetime DEFAULT NULL COMMENT '维修开始时间',
  `repair_end_time` datetime DEFAULT NULL COMMENT '维修结束时间',
  `is_manual_intervention` tinyint(1) DEFAULT NULL COMMENT '是否人工干预：0.否 1.是',
  `feedback_image` varchar(500) DEFAULT NULL COMMENT '反馈图片',
  `feedback_video` varchar(255) DEFAULT NULL COMMENT '反馈视频',
  `scene_confirm_user` varchar(20) DEFAULT NULL COMMENT '现场确认人',
  `scene_confirm_time` datetime DEFAULT NULL COMMENT '现场确认时间',
  `scene_confirm_state` tinyint(2) DEFAULT NULL COMMENT '现场确认结果 0.未解除 1.已解除',
  `is_recovery` tinyint(1) DEFAULT NULL COMMENT '恢复到正常范围 0.未恢复 1.已恢复',
  `param_value` double(20,0) DEFAULT NULL COMMENT '参数类报警当前值',
  `param_lower` double(20,0) DEFAULT NULL COMMENT '参数类报警当前下限',
  `param_upper` double(20,0) DEFAULT NULL COMMENT '参数类报警当前上限',
  `last_update_time` datetime DEFAULT NULL COMMENT '参数类报警上次查询更新时间',
  `verify_time` datetime DEFAULT NULL COMMENT '已核实时间',
  `post_worker` varchar(20) DEFAULT NULL COMMENT '最后操作的岗位工',
  `dispatcher` varchar(20) CHARACTER SET utf32 DEFAULT NULL COMMENT '最后操作的调度员',
  `release_time` datetime DEFAULT NULL COMMENT '报警解除时间',
  PRIMARY KEY (`id`),
  KEY `alert_type` (`alert_type`,`alert_level`,`alert_stage`)
) ENGINE=MyISAM AUTO_INCREMENT=2363 DEFAULT CHARSET=utf8;
