/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50720
Source Host           : localhost:3307
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-05-10 16:14:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for rel_sfspemon_thing_subthing
-- ----------------------------
DROP TABLE IF EXISTS `rel_sfspemon_thing_subthing`;
CREATE TABLE `rel_sfspemon_thing_subthing` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `thing_tag_code` varchar(40) DEFAULT NULL,
  `thing_code` varchar(40) DEFAULT NULL,
  `sub_thing_code` varchar(40) DEFAULT NULL COMMENT '附属设备code',
  `sub_metric_code` varchar(40) DEFAULT NULL,
  `sub_metric_name` varchar(40) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_thing_tag_code` (`thing_tag_code`) USING BTREE,
  KEY `idx_thing_tag` (`thing_code`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rel_sfspemon_thing_subthing
-- ----------------------------
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('1', 'SLOT', '1307', '1307.FL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('2', 'SLOT', '1307', '1307.BS-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('3', 'SLOT', '1308', '1308.FL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('4', 'SLOT', '1308', '1308.BS-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('5', 'SLOT', '2307', '2307.FL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('6', 'SLOT', '2307', '2307.BS-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('7', 'SLOT', '2308', '2308.FL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('8', 'SLOT', '2308', '2308.BS-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('9', 'DENSITY-CYCLONE-MAIN', '1339', '1339.FL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('10', 'DENSITY-CYCLONE-MAIN', '1339', '1339.BS-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('11', 'DENSITY-CYCLONE-MAIN', '1340', '1340.FL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('12', 'DENSITY-CYCLONE-MAIN', '1340', '1340.BS-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('13', 'DENSITY-CYCLONE-MAIN', '1341', '1341.FL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('14', 'DENSITY-CYCLONE-MAIN', '1341', '1341.BS-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('15', 'DENSITY-CYCLONE-MAIN', '2339', '2339.FL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('16', 'DENSITY-CYCLONE-MAIN', '2339', '2339.BS-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('17', 'DENSITY-CYCLONE-MAIN', '2340', '2340.FL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('18', 'DENSITY-CYCLONE-MAIN', '2340', '2340.BS-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('19', 'DENSITY-CYCLONE-MAIN', '2341', '2341.FL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('20', 'DENSITY-CYCLONE-MAIN', '2341', '2341.BS-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('21', 'DENSITY-CYCLONE-MAIN', '1386', '1386.FL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('22', 'DENSITY-CYCLONE-MAIN', '1386', '1386.BS-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('23', 'DENSITY-CYCLONE-MAIN', '1387', '1387.FL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('24', 'DENSITY-CYCLONE-MAIN', '1387', '1387.BS-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('25', 'DENSITY-CYCLONE-MAIN', '1388', '1388.FL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('26', 'DENSITY-CYCLONE-MAIN', '1388', '1388.BS-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('27', 'DENSITY-CYCLONE-MAIN', '2386', '2386.FL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('28', 'DENSITY-CYCLONE-MAIN', '2386', '2386.BS-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('29', 'DENSITY-CYCLONE-MAIN', '2387', '2387.FL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('30', 'DENSITY-CYCLONE-MAIN', '2387', '2387.BS-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('31', 'DENSITY-CYCLONE-MAIN', '2388', '2388.FL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('32', 'DENSITY-CYCLONE-MAIN', '2388', '2388.BS-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('33', 'TCS', '1857-1', '1857-1.PL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('34', 'TCS', '1857-1', '1857-1.DYV-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('35', 'TCS', '1857-2', '1857-2.PL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('36', 'TCS', '1857-2', '1857-2.DYV-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('37', 'TCS', '1857-3', '1857-3.PL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('38', 'TCS', '1857-3', '1857-3.DYV-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('39', 'TCS', '1858-1', '1858-1.PL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('40', 'TCS', '1858-1', '1858-1.DYV-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('41', 'TCS', '1858-2', '1858-2.PL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('42', 'TCS', '1858-2', '1858-2.DYV-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('43', 'TCS', '1858-3', '1858-3.PL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('44', 'TCS', '1858-3', '1858-3.DYV-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('45', 'TCS', '1859-1', '1859-1.PL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('46', 'TCS', '1859-1', '1859-1.DYV-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('47', 'TCS', '1859-2', '1859-2.PL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('48', 'TCS', '1859-2', '1859-2.DYV-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('49', 'TCS', '1859-3', '1859-3.PL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('50', 'TCS', '1859-3', '1859-3.DYV-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('51', 'TCS', '2857-1', '2857-1.PL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('52', 'TCS', '2857-1', '2857-1.DYV-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('53', 'TCS', '2857-2', '2857-2.PL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('54', 'TCS', '2857-2', '2857-2.DYV-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('55', 'TCS', '2857-3', '2857-3.PL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('56', 'TCS', '2857-3', '2857-3.DYV-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('57', 'TCS', '2858-1', '2858-1.PL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('58', 'TCS', '2858-1', '2858-1.DYV-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('59', 'TCS', '2858-2', '2858-2.PL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('60', 'TCS', '2858-2', '2858-2.DYV-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('61', 'TCS', '2858-3', '2858-3.PL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('62', 'TCS', '2858-3', '2858-3.DYV-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('63', 'TCS', '2859-1', '2859-1.PL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('64', 'TCS', '2859-1', '2859-1.DYV-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('65', 'TCS', '2859-2', '2859-2.PL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('66', 'TCS', '2859-2', '2859-2.DYV-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('67', 'TCS', '2859-3', '2859-3.PL-1', 'TAP_OPEN', '开到位,关到位', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('68', 'TCS', '2859-3', '2859-3.DYV-1', 'PRE_READ', '比例阀开度', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('69', 'PRESSURE-CYCLONE-MAIN', '1339', '1336', 'PRESS_CUR', '当前频率', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('70', 'PRESSURE-CYCLONE-MAIN', '1340', '1337', 'PRESS_CUR', '当前频率', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('71', 'PRESSURE-CYCLONE-MAIN', '1341', '1338', 'PRESS_CUR', '当前频率', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('72', 'PRESSURE-CYCLONE-MAIN', '2339', '2336', 'PRESS_CUR', '当前频率', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('73', 'PRESSURE-CYCLONE-MAIN', '2340', '2337', 'PRESS_CUR', '当前频率', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('74', 'PRESSURE-CYCLONE-MAIN', '2341', '2338', 'PRESS_CUR', '当前频率', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('75', 'PRESSURE-CYCLONE-MAIN', '1386', '1383', 'PRESS_CUR', '当前频率', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('76', 'PRESSURE-CYCLONE-MAIN', '1387', '1384', 'PRESS_CUR', '当前频率', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('77', 'PRESSURE-CYCLONE-MAIN', '1388', '1385', 'PRESS_CUR', '当前频率', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('78', 'PRESSURE-CYCLONE-MAIN', '2386', '2383', 'PRESS_CUR', '当前频率', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('79', 'PRESSURE-CYCLONE-MAIN', '2387', '2384', 'PRESS_CUR', '当前频率', null, null);
INSERT INTO `rel_sfspemon_thing_subthing` VALUES ('80', 'PRESSURE-CYCLONE-MAIN', '2388', '2385', 'PRESS_CUR', '当前频率', null, null);
