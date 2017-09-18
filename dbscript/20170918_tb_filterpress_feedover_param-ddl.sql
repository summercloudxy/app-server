/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50710
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50710
File Encoding         : 65001

Date: 2017-09-18 10:54:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_filterpress_feedover_param
-- ----------------------------
DROP TABLE IF EXISTS `tb_filterpress_feedover_param`;
CREATE TABLE `tb_filterpress_feedover_param` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(255) DEFAULT NULL,
  `auto_manu_confirm_state` tinyint(4) DEFAULT NULL,
  `intelligent_manu_state` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
