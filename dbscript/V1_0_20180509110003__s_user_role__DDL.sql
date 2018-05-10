/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-04-26 13:42:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for s_user_role
-- ----------------------------
DROP TABLE IF EXISTS `s_user_role`;
CREATE TABLE `s_user_role` (
  `role_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FK9E393B9EE3FD50AD` (`role_id`),
  KEY `FK9E393B9E8E7A934D` (`user_id`),
  CONSTRAINT `FK9E393B9E8E7A934D` FOREIGN KEY (`user_id`) REFERENCES `s_user` (`id`),
  CONSTRAINT `FK9E393B9EE3FD50AD` FOREIGN KEY (`role_id`) REFERENCES `s_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
