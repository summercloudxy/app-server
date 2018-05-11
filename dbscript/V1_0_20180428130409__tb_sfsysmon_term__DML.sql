/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50720
Source Host           : localhost:3307
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-04-23 11:45:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_sfsysmon_term
-- ----------------------------
DROP TABLE IF EXISTS `tb_sfsysmon_term`;
CREATE TABLE `tb_sfsysmon_term` (
  `id` smallint(4) NOT NULL AUTO_INCREMENT,
  `term` smallint(4) NOT NULL COMMENT '期数',
  `comment` varchar(80) DEFAULT NULL COMMENT '注释',
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_sfsysmon_term
-- ----------------------------
INSERT INTO `tb_sfsysmon_term` VALUES ('1', '1', '一期', null, null);
INSERT INTO `tb_sfsysmon_term` VALUES ('2', '2', '二期', null, null);
