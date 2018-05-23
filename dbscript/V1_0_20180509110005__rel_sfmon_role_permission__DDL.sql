/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-04-24 10:06:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for rel_sfmon_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `rel_sfmon_role_permission`;
CREATE TABLE `rel_sfmon_role_permission` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  `thing_code` varchar(30) DEFAULT NULL COMMENT '设备编号',
  `op_view` tinyint(1) DEFAULT NULL COMMENT '查看信息',
  `op_startstop` tinyint(1) DEFAULT NULL COMMENT '启停设备',
  `op_control` tinyint(1) DEFAULT NULL COMMENT '状态控制',
  `create_dt` datetime DEFAULT NULL COMMENT '创建时间',
  `update_dt` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
