/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-05-09 13:20:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for rel_user_platformclient
-- ----------------------------
CREATE TABLE `rel_user_platformclient` (
  `user_id` bigint(20) DEFAULT NULL,
  `platform_client_id` int(11) DEFAULT NULL,
  KEY `user_id` (`user_id`),
  KEY `platform_client_id` (`platform_client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
