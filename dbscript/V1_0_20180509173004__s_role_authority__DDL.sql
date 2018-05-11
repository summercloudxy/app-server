/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-05-09 17:10:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for s_role_authority
-- ----------------------------
CREATE TABLE `s_role_authority` (
  `authority_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`,`authority_id`),
  KEY `FKA8E80006E3FD50AD` (`role_id`),
  KEY `FKA8E80006D9EB1665` (`authority_id`),
  KEY `FKA8E800063A4AA91D` (`role_id`),
  KEY `FKA8E80006555383F5` (`authority_id`),
  CONSTRAINT `FKA8E800063A4AA91D` FOREIGN KEY (`role_id`) REFERENCES `s_role` (`id`),
  CONSTRAINT `FKA8E80006555383F5` FOREIGN KEY (`authority_id`) REFERENCES `s_authority` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
