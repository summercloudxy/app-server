/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-05-09 17:23:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for rel_authoritygroup_authority
-- ----------------------------
CREATE TABLE `rel_authoritygroup_authority` (
  `authority_group_id` int(11) DEFAULT NULL,
  `authority_id` int(11) DEFAULT NULL,
  KEY `authority_group_id` (`authority_group_id`),
  KEY `authority_id` (`authority_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
