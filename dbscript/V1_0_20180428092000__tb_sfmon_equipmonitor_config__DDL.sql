/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-04-28 09:19:11
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_sfmon_equipmonitor_config
-- ----------------------------
CREATE TABLE `tb_sfmon_equipmonitor_config` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(20) DEFAULT NULL,
  `metric_tag_name` varchar(30) DEFAULT NULL,
  `key` varchar(20) DEFAULT NULL,
  `value` varchar(30) DEFAULT NULL,
  `selected` bit(1) DEFAULT NULL,
  `model` int(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8192 DEFAULT CHARSET=utf8 COMMENT='model:0为查看，\r\nmodel:1为操作';
SET FOREIGN_KEY_CHECKS=1;
