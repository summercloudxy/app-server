/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-04-26 13:43:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for rel_sfmon_metrictype_metric
-- ----------------------------
CREATE TABLE `rel_sfmon_metrictype_metric` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `metric_code` varchar(30) DEFAULT NULL COMMENT '信号编号',
  `sfmon_metrictype` varchar(10) DEFAULT NULL COMMENT '监控信号类别:ss启停设备;ctl状态控制',
  `create_dt` datetime DEFAULT NULL,
  `update_dt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
