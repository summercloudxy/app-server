/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-05-09 17:12:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for s_authority
-- ----------------------------
CREATE TABLE `s_authority` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `alias` varchar(200) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `code` varchar(100) DEFAULT NULL,
  `authority_type_id` int(20) DEFAULT NULL,
  `enable` int(6) DEFAULT NULL,
  `platclient_id` int(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
