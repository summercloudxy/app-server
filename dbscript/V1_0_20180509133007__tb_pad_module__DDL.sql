/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-05-09 13:48:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_pad_module
-- ----------------------------
CREATE TABLE `tb_pad_module` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  `host_env` varchar(60) DEFAULT NULL,
  `urn` varchar(100) DEFAULT NULL,
  `authority_code` varchar(30) DEFAULT NULL,
  `image_name` varchar(30) DEFAULT NULL,
  `open_way` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
