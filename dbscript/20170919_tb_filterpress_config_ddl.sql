/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50718
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2017-09-19 13:08:28
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_filterpress_config
-- ----------------------------
DROP TABLE IF EXISTS `tb_filterpress_config`;
CREATE TABLE `tb_filterpress_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `param_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `param_value` double(11,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
