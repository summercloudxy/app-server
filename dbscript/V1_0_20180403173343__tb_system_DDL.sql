/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-04-03 17:33:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_system
-- ----------------------------
DROP TABLE IF EXISTS `tb_system`;
CREATE TABLE `tb_system` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `system_name` varchar(20) DEFAULT NULL COMMENT '生产线名称',
  `parent_system_id` varchar(255) DEFAULT NULL,
  `level` int(11) DEFAULT NULL COMMENT '系统层级',
  `description` tinytext,
  `node_level` int(11) DEFAULT NULL,
  `seqno` int(11) DEFAULT NULL COMMENT '排序索引',
  `state` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8 COMMENT='生产系统';
