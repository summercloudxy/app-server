/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50720
Source Host           : localhost:3307
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-04-23 11:39:26
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_sfsysmon_thing_tag_style
-- ----------------------------
DROP TABLE IF EXISTS `tb_sfsysmon_thing_tag_style`;
CREATE TABLE `tb_sfsysmon_thing_tag_style` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thing_tag_code` varchar(40) DEFAULT NULL,
  `thing_tag_name` varchar(40) DEFAULT NULL,
  `image_name` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_sfsysmon_thing_tag_style
-- ----------------------------
INSERT INTO `tb_sfsysmon_thing_tag_style` VALUES ('1', 'SAVECOAL', '原煤储运系统', 'zhenggeimeiyou.png');
INSERT INTO `tb_sfsysmon_thing_tag_style` VALUES ('2', 'READYCOAL', '原煤准备系统', 'posuiji.png');
INSERT INTO `tb_sfsysmon_thing_tag_style` VALUES ('3', 'SEDIMENTATION', '沉降系统', 'chengjiang.png');
INSERT INTO `tb_sfsysmon_thing_tag_style` VALUES ('4', 'PRODUCTION', '生产辅助系统', 'shuibeng.png');
INSERT INTO `tb_sfsysmon_thing_tag_style` VALUES ('5', 'COALBYPASS', '末煤旁路系统', 'huxingshai.png');
INSERT INTO `tb_sfsysmon_thing_tag_style` VALUES ('6', 'WASHINGPUBLIC', '洗选公用系统', 'guabanzhu.png');
INSERT INTO `tb_sfsysmon_thing_tag_style` VALUES ('7', 'LUMPCOAL', '块煤系统', 'jingqiancaoyou.png');
INSERT INTO `tb_sfsysmon_thing_tag_style` VALUES ('8', 'CONCENTRATION', '浓缩系统', 'nongsuoji.png');
INSERT INTO `tb_sfsysmon_thing_tag_style` VALUES ('9', 'FASTLOADING', '快速装车系统', 'huoche.png');
INSERT INTO `tb_sfsysmon_thing_tag_style` VALUES ('10', 'TRANSPORTATION', '矸石运输系统', 'daigeimeiyou.png');
