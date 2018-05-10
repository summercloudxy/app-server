/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-05-09 13:26:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_subsc_card_store
-- ----------------------------
DROP TABLE IF EXISTS `tb_subsc_card_store`;
CREATE TABLE `tb_subsc_card_store` (
  `id` varchar(32) NOT NULL COMMENT '卡片ID',
  `card_type` varchar(10) DEFAULT NULL COMMENT '卡片类型',
  `card_source` varchar(1) DEFAULT NULL COMMENT '卡片来源',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(50) DEFAULT NULL,
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
SET FOREIGN_KEY_CHECKS=1;
