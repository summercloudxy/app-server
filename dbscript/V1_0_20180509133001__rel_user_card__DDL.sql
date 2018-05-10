/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-05-09 13:29:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for rel_user_card
-- ----------------------------
CREATE TABLE `rel_user_card` (
  `user_uuid` varchar(50) NOT NULL DEFAULT '',
  `card_code` varchar(10) NOT NULL COMMENT '卡片编码',
  PRIMARY KEY (`user_uuid`,`card_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
SET FOREIGN_KEY_CHECKS=1;
