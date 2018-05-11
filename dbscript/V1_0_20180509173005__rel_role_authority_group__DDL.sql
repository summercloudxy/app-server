/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-05-09 17:23:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for rel_role_authority_group
-- ----------------------------
CREATE TABLE `rel_role_authority_group` (
  `role_id` int(11) DEFAULT NULL,
  `authority_group_id` int(11) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT NULL,
  KEY `role_id` (`role_id`),
  KEY `authority_group_id` (`authority_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
