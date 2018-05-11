/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-04-26 13:42:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for s_user
-- ----------------------------
CREATE TABLE `s_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(50) DEFAULT NULL,
  `version` int(11) NOT NULL,
  `login_name` varchar(25) DEFAULT NULL COMMENT '登录名',
  `password` varchar(255) DEFAULT NULL,
  `person_name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `regist_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `login_date` datetime DEFAULT NULL,
  `logout_date` datetime DEFAULT NULL,
  `account_non_expired` bit(1) DEFAULT b'1',
  `credentials_non_expired` bit(1) DEFAULT b'1',
  `account_non_locked` bit(1) DEFAULT b'1',
  `enabled` bit(1) DEFAULT b'1',
  `is_login` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sq_su_login_name` (`login_name`),
  KEY `uuid` (`uuid`),
  KEY `person_name` (`person_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
