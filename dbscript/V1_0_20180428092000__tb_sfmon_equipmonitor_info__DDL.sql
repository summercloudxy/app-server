/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-04-28 09:19:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_sfmon_equipmonitor_info
-- ----------------------------
CREATE TABLE `tb_sfmon_equipmonitor_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(20) DEFAULT NULL,
  `thing_name` varchar(30) DEFAULT NULL,
  `config_progress` varchar(10) DEFAULT NULL,
  `editor` varchar(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `comment` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1024 DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
