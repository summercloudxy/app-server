/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-05-09 13:32:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_sfsubscription_card
-- ----------------------------
CREATE TABLE `tb_sfsubscription_card` (
  `card_id` varchar(32) NOT NULL COMMENT '卡片Id',
  `card_data` varchar(2000) DEFAULT NULL COMMENT '卡片数据',
  `refresh_time` varchar(1) DEFAULT NULL COMMENT '刷新时间',
  `card_code` varchar(20) NOT NULL DEFAULT '' COMMENT '卡片编码',
  `card_source` varchar(1) DEFAULT NULL COMMENT '卡片来源',
  `card_type` varchar(10) DEFAULT NULL COMMENT '卡片类型（判断卡片显示样式）',
  PRIMARY KEY (`card_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
SET FOREIGN_KEY_CHECKS=1;
