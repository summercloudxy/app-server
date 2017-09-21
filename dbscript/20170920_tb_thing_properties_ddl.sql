/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2017-09-21 09:26:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tb_thing_properties`
-- ----------------------------
DROP TABLE IF EXISTS `tb_thing_properties`;
CREATE TABLE `tb_thing_properties` (
  `id` int(11)  not null auto_increment,
  `thing_code` varchar(30) DEFAULT NULL,
  `prop_key` varchar(30) DEFAULT NULL,
  `prop_value` varchar(255) DEFAULT NULL,
  `prop_type` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `thing_index` (`thing_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;