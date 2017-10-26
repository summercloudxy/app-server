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
  `metric_code` varchar(20) DEFAULT NULL,
  `label_path` varchar(64) DEFAULT NULL,
  `enabled` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1017 DEFAULT CHARSET=utf8;

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
  `metric_code` varchar(20) DEFAULT NULL,
  `metric_type1_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '维度1的叶子节点',
  `metric_type2_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '维度2的叶子节点',
  `metric_type3_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '维度3的叶子节点',
  `value_type` varchar(5) DEFAULT NULL,
  `value_unit` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_code` (`metric_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=177 DEFAULT CHARSET=utf8;

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
