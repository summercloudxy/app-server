/*
MySQL - 5.6.35 : Database - smartfactory2
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`smartfactory2` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `smartfactory2`;

/*Table structure for table `rel_thing_metric_label` */

DROP TABLE IF EXISTS `rel_thing_metric_label`;

CREATE TABLE `rel_thing_metric_label` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(10) DEFAULT NULL,
  `metric_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `label_path` varchar(64) DEFAULT NULL,
  `enabled` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Table structure for table `rel_thingtype_metric` */

DROP TABLE IF EXISTS `rel_thingtype_metric`;

CREATE TABLE `rel_thingtype_metric` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thingtype_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `metric_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_metric` */

DROP TABLE IF EXISTS `tb_metric`;

CREATE TABLE `tb_metric` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `metric_category_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `metric_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `metric_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `metric_type1_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '维度1的叶子节点',
  `metric_type2_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '维度2的叶子节点',
  `metric_type3_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '维度3的叶子节点',
  `value_type` varchar(5) DEFAULT NULL,
  `value_unit` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_code` (`metric_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_metrictype` */

DROP TABLE IF EXISTS `tb_metrictype`;

CREATE TABLE `tb_metrictype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `metrictype_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `metrictype_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_code` (`metrictype_code`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_thing` */

DROP TABLE IF EXISTS `tb_thing`;

CREATE TABLE `tb_thing` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_thing_id` int(11) DEFAULT NULL,
  `thing_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `thing_category_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `thing_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `thing_type1_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '维度1的叶子节点',
  `thing_type2_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '维度2的叶子节点',
  `thing_type3_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '维度3的叶子节点',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_code` (`thing_code`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_thingtype` */

DROP TABLE IF EXISTS `tb_thingtype`;

CREATE TABLE `tb_thingtype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thingtype_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `thingtype_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `parent_thingtype_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*
Navicat MySQL Data Transfer

Source Server         : 192.168.3.173
Source Server Version : 50623
Source Host           : 192.168.3.173:3306
Source Database       : smartfactory

Target Server Type    : MYSQL
Target Server Version : 50623
File Encoding         : 65001

Date: 2017-09-04 13:39:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for excel_range
-- ----------------------------
DROP TABLE IF EXISTS `excel_range`;
CREATE TABLE `excel_range` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `system` tinyint(4) DEFAULT NULL,
  `start_x` tinyint(4) DEFAULT NULL,
  `start_y` tinyint(4) DEFAULT NULL,
  `row` tinyint(4) DEFAULT NULL,
  `read_mode` tinyint(4) DEFAULT NULL,
  `target_gap` tinyint(4) DEFAULT NULL,
  `time_gap` tinyint(4) DEFAULT NULL,
  `device_code_gap` tinyint(4) DEFAULT NULL,
  `aad_gap` tinyint(4) DEFAULT NULL,
  `mt_gap` tinyint(4) DEFAULT NULL,
  `stad_gap` tinyint(4) DEFAULT NULL,
  `qar_gap` tinyint(4) DEFAULT NULL,
  `state` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of excel_range
-- ----------------------------
INSERT INTO `excel_range` VALUES ('1', 'TCS精矿', '1', '1', '28', '6', '1', null, '3', '0', '5', '7', null, null, '1');
INSERT INTO `excel_range` VALUES ('2', 'TCS精矿', '2', '43', '28', '6', '1', null, '3', '0', '5', '7', null, null, '1');
INSERT INTO `excel_range` VALUES ('3', 'TCS尾矿', '1', '10', '28', '6', '1', null, '3', '0', '5', '7', null, null, '1');
INSERT INTO `excel_range` VALUES ('4', 'TCS尾矿', '2', '52', '28', '6', '1', null, '3', '0', '5', '7', null, null, '1');
INSERT INTO `excel_range` VALUES ('5', '入洗原煤', '1', '5', '4', '9', '1', null, '0', '2', '5', '7', '9', '11', '1');
INSERT INTO `excel_range` VALUES ('6', '入洗原煤', '2', '47', '4', '9', '1', null, '0', '2', '5', '7', '9', '11', '1');
INSERT INTO `excel_range` VALUES ('7', '551生产精煤', '1', '21', '4', '14', '3', null, '0', null, '2', '4', '6', '8', '1');
INSERT INTO `excel_range` VALUES ('8', '551生产精煤', '2', '32', '4', '14', '3', null, '0', null, '2', '4', '6', '8', '1');
INSERT INTO `excel_range` VALUES ('9', '552生产洗混煤', '1', '21', '20', '14', '3', null, '0', null, '2', '4', '6', '8', '1');
INSERT INTO `excel_range` VALUES ('10', '552生产洗混煤', '2', '32', '20', '14', '3', null, '0', null, '2', '4', '6', '8', '1');
INSERT INTO `excel_range` VALUES ('11', '浅槽至末矸区域', '1', '1', '14', '6', '2', '0', '4', '6', '9', '11', '13', '15', '1');
INSERT INTO `excel_range` VALUES ('12', '浅槽至末矸区域', '2', '43', '14', '6', '2', '0', '4', '6', '9', '11', '13', '15', '1');
INSERT INTO `excel_range` VALUES ('13', '块原至末精区域', '1', '1', '21', '5', '2', '0', '4', '6', '9', null, null, null, '1');
INSERT INTO `excel_range` VALUES ('14', '块原至末精区域', '1', '1', '21', '5', '2', '0', '11', '13', '16', null, null, null, '1');
INSERT INTO `excel_range` VALUES ('15', '块原至末精区域', '2', '43', '21', '5', '2', '0', '4', '6', '9', null, null, null, '1');
INSERT INTO `excel_range` VALUES ('16', '块原至末精区域', '2', '43', '21', '5', '2', '0', '11', '13', '16', null, null, null, '1');
-- ----------------------------
-- Records of tb_metric
-- ----------------------------
INSERT INTO `tb_metric` VALUES ('1', 'SIG', '电流0', 'CURRENT0', 'PR', 'CURRENT', NULL, 'FLT', NULL);
INSERT INTO `tb_metric` VALUES ('2', 'SIG', '电流1', 'CURRENT1', 'PR', 'CURRENT', NULL, 'FLT', NULL);
INSERT INTO `tb_metric` VALUES ('3', 'SIG', '集控按钮0', 'LOCAL0', 'SS', 'LOCAL', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('4', 'SIG', '电流2', 'CURRENT2', 'PR', 'CURRENT', NULL, 'FLT', NULL);
INSERT INTO `tb_metric` VALUES ('5', 'SIG', '状态', 'STATE', 'S', 'STATE', NULL, 'INT', NULL);
INSERT INTO `tb_metric` VALUES ('6', 'SIG', '压滤机阶段', 'STAGE', 'S', 'STAGE', NULL, 'INT', NULL);
INSERT INTO `tb_metric` VALUES ('7', 'SIG', '水洗前移', 'WATER_FWD', 'S', 'WATER_FWD', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('8', 'SIG', '水洗后移', 'WATER_BWD', 'S', 'WATER_BWD', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('9', 'SIG', '喷头上移', 'SHOWER_UP', 'S', 'SHOWER_UP', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('10', 'SIG', '喷头下移', 'SHOWER_DWN', 'S', 'SHOWER_DWN', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('11', 'SIG', '暂停', 'PAUSE', 'S', 'PAUSE', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('12', 'SIG', '故障信号', 'FAULT', 'S', 'FAULT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('13', 'SIG', '刮板启动', 'SCRAPER_UP', 'S', 'SCRAPER_UP', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('14', 'SIG', '进料泵启动点', 'RL_PUMP_UP', 'S', 'RL_PUMP_UP', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('15', 'SIG', '松开限位0', 'LOOSE_LMT0', 'S', 'LOOSE_LMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('16', 'SIG', '小车限位0', 'CAR_LMT0', 'S', 'CAR_LMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('17', 'SIG', '手动/自动', 'AUTO', 'S', 'AUTO', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('18', 'SIG', '远程就地按钮', 'LOCAL', 'SS', 'LOCAL', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('19', 'SIG', '压紧上限', 'PRESS_ULMT', 'S', 'PRESS_ULMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('20', 'SIG', '压紧下限', 'PRESS_LLMT', 'S', 'PRESS_LLMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('21', 'SIG', '压紧限位', 'PRESS_LMT', 'S', 'PRESS_LMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('22', 'SIG', '进料泵运行信号', 'JL_PUMP_UP', 'S', 'JL_PUMP_UP', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('23', 'SIG', '输送机运行信号', 'TRS_UP', 'S', 'TRS_UP', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('24', 'SIG', '进料上限', 'JL_ULMT', 'S', 'JL_ULMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('25', 'SIG', '压榨下限', 'PRS_LLMT', 'S', 'PRS_LLMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('140', 'SIG', '松开限位1', 'LOOSE_LMT1', 'S', 'LOOSE_LMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('26', 'SIG', '小车限位1', 'CAR_LMT1', 'S', 'CAR_LMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('27', 'SIG', '压榨上限', 'PRS_ULMT', 'S', 'PRS_ULMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('28', 'SIG', '进料阀0开限位', 'JL_OLMT0', 'S', 'JL_OLMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('29', 'SIG', '进料阀0关限位', 'JL_CLMT0', 'S', 'JL_CLMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('30', 'SIG', '进料阀1开限位', 'JL_OLMT1', 'S', 'JL_OLMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('31', 'SIG', '进料阀1关限位', 'JL_CLMT1', 'S', 'JL_CLMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('32', 'SIG', '压榨阀开限位', 'YZ_OLMT', 'S', 'YZ_OLMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('33', 'SIG', '压榨阀关限位', 'YZ_CLMT', 'S', 'YZ_CLMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('34', 'SIG', '放空阀开限位', 'FK_OLMT', 'S', 'FK_OLMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('35', 'SIG', '放空阀关限位', 'FK_CLMT', 'S', 'FK_CLMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('36', 'SIG', '吹风阀开限位', 'CF_OLMT', 'S', 'CF_OLMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('37', 'SIG', '吹风阀关限位', 'CF_CLMT', 'S', 'CF_CLMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('38', 'SIG', '回流阀开限位', 'HL_OLMT', 'S', 'HL_OLMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('39', 'SIG', '回流阀关限位', 'HL_CLMT', 'S', 'HL_CLMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('40', 'SIG', '过滤/水洗', 'FILTER', 'S', 'FILTER', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('41', 'SIG', '水洗后移限位', 'WT_FW_LMT', 'S', 'WT_FW_LMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('42', 'SIG', '水洗前移限位', 'WT_BW_LMT', 'S', 'WT_BW_LMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('43', 'SIG', '喷头下移限位', 'SW_DWN_LMT', 'S', 'SW_DWN_LMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('44', 'SIG', '喷头上移限位', 'SW_UP_LMT', 'S', 'SW_UP_LMT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('45', 'SIG', '洗布检测点', 'XB_CHK', 'S', 'XB_CHK', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('46', 'SIG', '洗布转换点', 'XB_TRSF', 'S', 'XB_TRSF', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('47', 'SIG', '洗布结束点', 'XB_END', 'S', 'XB_END', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('48', 'SIG', '压榨阀打开超时', 'PRS_OTMOUT', 'F', 'PRS_TMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('49', 'SIG', '压榨阀关闭超时', 'PRS_CTMOUT', 'F', 'PRS_CTMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('50', 'SIG', '排空阀打开超时', 'PK_OTMOUT', 'F', 'PK_OTMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('51', 'SIG', '排空阀关闭超时', 'PK_CTMOUT', 'F', 'PK_CTMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('52', 'SIG', '吹风阀打开超时', 'CF_OTMOUT', 'F', 'CF_OTMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('53', 'SIG', '吹风阀关闭超时', 'CF_CTMOUT', 'F', 'CF_CTMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('54', 'SIG', '回流阀打开超时', 'HL_OTMOUT', 'F', 'HL_OTMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('55', 'SIG', '回流阀关闭超时', 'HL_CTMOUT', 'F', 'HL_CTMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('56', 'SIG', '排空超时', 'PK_TMOUT', 'F', 'PK_TMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('57', 'SIG', '吹风超时', 'CF_TMOUT', 'F', 'CF_TMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('58', 'SIG', '进料阀0打开超时', 'JL_OTMOUT0', 'F', 'JL_OTMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('59', 'SIG', '进料阀0关闭超时', 'JL_CTMOUT0', 'F', 'JL_CTMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('60', 'SIG', '进料阀1打开超时', 'JL_OTMOUT1', 'F', 'JL_OTMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('61', 'SIG', '进料阀1关闭超时', 'JL_CTMOUT1', 'F', 'JL_CTMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('62', 'SIG', '翻板打开超时', 'FB_OTMOUT', 'F', 'FB_OTMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('63', 'SIG', '松开超时', 'SK_TMOUT', 'F', 'SK_TMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('64', 'SIG', '取板超时', 'QB_TMOUT', 'F', 'QB_TMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('65', 'SIG', '拉板超时', 'LB_TMOUT', 'F', 'LB_TMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('66', 'SIG', '翻板关闭超时', 'FB_CTMOUT', 'F', 'FB_CTMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('67', 'SIG', '压紧超时或超限', 'YJ_TMOUT', 'F', 'YJ_TMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('68', 'SIG', '进料超时', 'JL_TMOUT', 'F', 'JL_TMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('69', 'SIG', '压榨超时', 'YZ_TMOUT', 'F', 'YZ_TMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('70', 'SIG', '存在压榨压力，禁止松开', 'SK_FRBD', 'F', 'SK_FRBD', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('71', 'SIG', '存在压榨压力或小车未到位，禁止启动', 'START_FRBD', 'F', 'START_FRBD', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('72', 'SIG', '水洗前移超时', 'WTFW_TMOUT', 'F', 'WTFW_TMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('73', 'SIG', '水洗后移超时', 'WTBW_TMOUT', 'F', 'WTBW_TMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('74', 'SIG', '喷头下移超时', 'SWDW_TMOUT', 'F', 'SWDW_TMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('75', 'SIG', '喷头上移超时', 'SWUP_TMOUT', 'F', 'SWUP_TMOUT', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('76', 'SIG', '一队记录', 'T1_RCD', 'S', 'T1_RCD', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('77', 'SIG', '二队记录', 'T2_RCD', 'S', 'T2_RCD', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('78', 'SIG', '三队纪录', 'T3_RCD', 'S', 'T3_RCD', NULL, 'BOO', '100ms');
INSERT INTO `tb_metric` VALUES ('79', 'SIG', '当前计时', 'CRT_TIMER', 'PR', 'CRT_TIMER', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('80', 'SIG', '过程计时', 'PRC_TIMER', 'PR', 'PRC_TIMER', NULL, 'INT', NULL);
INSERT INTO `tb_metric` VALUES ('81', 'SIG', '一队过滤次数', 'T1_COUNT', 'PR', 'T1_COUNT', NULL, 'INT', NULL);
INSERT INTO `tb_metric` VALUES ('82', 'SIG', '二队过滤次数', 'T2_COUNT', 'PR', 'T2_COUNT', NULL, 'INT', NULL);
INSERT INTO `tb_metric` VALUES ('83', 'SIG', '三队过滤次数', 'T3_COUNT', 'PR', 'T3_COUNT', NULL, 'INT', NULL);
INSERT INTO `tb_metric` VALUES ('84', 'SIG', '总记录', 'TOTL_COUNT', 'PR', 'TOTL_COUNT', NULL, 'INT', NULL);
INSERT INTO `tb_metric` VALUES ('85', 'SIG', '进料延时', 'JL_DELAY', 'PR', 'JL_TIME', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('86', 'SIG', '压榨延时', 'YZ_DELAY', 'PR', 'YZ_TIME', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('87', 'SIG', '压榨延时时间', 'YZ_DL_TIME', 'PR', 'YZ_DL_TIME', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('88', 'SIG', '吹风时间', 'CF_TIME', 'PR', 'CF_TIME', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('89', 'SIG', '首次取板高速时间', '1QB_S_TIME', 'PR', '1QB_S_TIME', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('90', 'SIG', '取板高速时间', 'QB_S_TIME', 'PR', 'QB_S_TIME', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('91', 'SIG', '拉板高速时间', 'LB_S_TIME', 'PR', 'LB_S_TIME', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('92', 'SIG', '拉板高速返回时间', 'LB_SR_TIME', 'PR', 'LB_SR_TIME', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('93', 'SIG', '循环等待时间', 'XHDD_TIME', 'PR', 'XHDD_TIME', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('94', 'SIG', '喷头下方停留时间', 'SW_ST_TIME', 'PR', 'SW_ST_TIME', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('95', 'SIG', '喷头上移喷水时间', 'SW_SW_TIME', 'PR', 'SW_SW_TIME', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('96', 'SIG', '松开超时时间', 'SK_TO_TIME', 'PR', 'SK_TO_TIME', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('97', 'SIG', '取板超时时间', 'QB_TO_TIME', 'PR', 'QB_TO_TIME', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('98', 'SIG', '拉板超时时间', 'LB_TO_TIME', 'PR', 'LB_TO_TIME', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('99', 'SIG', '压紧超时时间', 'YJ_TO_TIME', 'PR', 'YJ_TO_TIME', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('100', 'SIG', '进料超时时间', 'JL_TO_TIME', 'PR', 'JL_TO_TIME', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('101', 'SIG', '压榨超时时间', 'YZ_TO_TIME', 'PR', 'YZ_TO_TIME', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('102', 'SIG', '排空超时时间', 'PK_TO_TIME', 'PR', 'PK_TO_TIME', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('103', 'SIG', '吹风超时时间', 'CF_TO_TIME', 'PR', 'CF_TO_TIME', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('104', 'SIG', '进料阀0动作超时时间', 'JL_TO_TM0', 'PR', 'JL_TO_TM', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('105', 'SIG', '进料阀1动作超时时间', 'JL_TO_TM1', 'PR', 'JL_TO_TM', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('106', 'SIG', '压榨阀动作超时时间', 'YZ_TO_TM', 'PR', 'YZ_TO_TM', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('107', 'SIG', '排空阀动作超时时间', 'PK_TO_TM', 'PR', 'PK_TO_TM', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('108', 'SIG', '吹风阀动作超时时间', 'CF_TO_TM', 'PR', 'CF_TO_TM', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('109', 'SIG', '回流阀动作超时时间', 'HL_TO_TM', 'PR', 'HL_TO_TM', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('110', 'SIG', '水洗架前移超时时间', 'SXF_TO_TM', 'PR', 'SXF_TO_TM', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('111', 'SIG', '水洗架后移超时时间', 'SXB_TO_TM', 'PR', 'SXB_TO_TM', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('112', 'SIG', '喷头下移超时时间', 'SWD_TO_TM', 'PR', 'SWD_TO_TM', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('113', 'SIG', '喷头上移超时时间', 'SWU_TO_TM', 'PR', 'SWU_TO_TM', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('114', 'SIG', '高压卸荷时间', 'GY_TIME', 'PR', 'GY_TIME', NULL, 'INT', '100ms');
INSERT INTO `tb_metric` VALUES ('115', 'SIG', '系统报警选择', 'SYS_ALARM', 'SS', 'SYS_ALARM', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('116', 'SIG', '控制选择', 'CONTROL', 'SS', 'CONTROL', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('117', 'SIG', '阀门报警选择', 'GATE_ALARM', 'SS', 'GATE_ALARM', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('118', 'SIG', '刮板闭锁', 'SCR_BLK', 'SS', 'SCR_BLK', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('119', 'SIG', '总记录清零', 'TOTL_CLR', 'SS', 'TOTL_CLR', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('120', 'SIG', '一队清零', 'T1_CLR', 'SS', 'T1_CLR', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('121', 'SIG', '二队清零', 'T2_CLR', 'SS', 'T2_CLR', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('122', 'SIG', '三队清零', 'T3_CLR', 'SS', 'T3_CLR', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('123', 'SIG', '远程手动松开', 'LOOSE', 'SS', 'LOOSE', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('124', 'SIG', '远程手动取板', 'TAKE', 'SS', 'TAKE', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('125', 'SIG', '远程手动拉板', 'PULL', 'SS', 'PULL', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('126', 'SIG', '远程手动压紧', 'PRESS', 'SS', 'PRESS', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('127', 'SIG', '远程手动进料', 'FEED', 'SS', 'FEED', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('128', 'SIG', '远程手动压榨', 'SQUEEZE', 'SS', 'SQUEEZE', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('129', 'SIG', '远程手动吹风', 'BLOW', 'SS', 'BLOW', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('130', 'SIG', '远程报警复位', 'RESET', 'SS', 'RESET', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('131', 'SIG', '远程启动', 'RUN', 'SS', 'RUN', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('132', 'SIG', '远程暂停', 'S_PAUSE', 'SS', 'S_PAUSE', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('133', 'SIG', '远程停止', 'STOP', 'SS', 'STOP', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('136', 'SIG', '进料结束', 'FEED_OVER', 'SS', 'FEED_OVER', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('137', 'SIG', '一队选择', 'T1_CHOOSE', 'SS', 'T1_CHOOSE', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('138', 'SIG', '二队选择', 'T2_CHOOSE', 'SS', 'T2_CHOOSE', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('139', 'SIG', '三队选择', 'T3_CHOOSE', 'SS', 'T3_CHOOSE', NULL, 'BOO', NULL);
INSERT INTO `tb_metric` VALUES ('141', 'SIG', '远程手动/自动', 'R_AUTO', 'S', 'R_AUTO', NULL, 'BOO', NULL);
-- ----------------------------
-- Records of tb_metrictype
-- ----------------------------
INSERT INTO `tb_metrictype` VALUES ('2', '参数类', 'PR');
INSERT INTO `tb_metrictype` VALUES ('3', '参数设定类', 'PS');
INSERT INTO `tb_metrictype` VALUES ('4', '固有属性', 'ST');
INSERT INTO `tb_metrictype` VALUES ('5', '特殊功能类', 'SF');
INSERT INTO `tb_metrictype` VALUES ('6', '状态类', 'S');
INSERT INTO `tb_metrictype` VALUES ('7', '状态设定类', 'SS');
INSERT INTO `tb_metrictype` VALUES ('8', '故障类', 'F');
INSERT INTO `tb_metrictype` VALUES ('9', '故障旁路类', 'FI');
INSERT INTO `tb_metrictype` VALUES ('10', '电流', 'CURRENT');
INSERT INTO `tb_metrictype` VALUES ('11', '频率', 'FREQUENCY');
INSERT INTO `tb_metrictype` VALUES ('12', '压力', 'PRESSURE');
INSERT INTO `tb_metrictype` VALUES ('13', '就地集控按钮', 'LOCAL');
INSERT INTO `tb_metrictype` VALUES ('18', '状态', 'STATE');
INSERT INTO `tb_metrictype` VALUES ('19', '压滤机阶段', 'STAGE');
INSERT INTO `tb_metrictype` VALUES ('20', '水洗前移', 'WATER_FWD');
INSERT INTO `tb_metrictype` VALUES ('21', '水洗后移', 'WATER_BWD');
INSERT INTO `tb_metrictype` VALUES ('22', '喷头上移', 'SHOWER_UP');
INSERT INTO `tb_metrictype` VALUES ('23', '喷头下移', 'SHOWER_DWN');
INSERT INTO `tb_metrictype` VALUES ('24', '暂停', 'PAUSE');
INSERT INTO `tb_metrictype` VALUES ('25', '故障信号', 'FAULT');
INSERT INTO `tb_metrictype` VALUES ('26', '刮板启动', 'SCRAPER_UP');
INSERT INTO `tb_metrictype` VALUES ('27', '进料泵启动点', 'RL_PUMP_UP');
INSERT INTO `tb_metrictype` VALUES ('28', '松开限位', 'LOOSE_LMT');
INSERT INTO `tb_metrictype` VALUES ('29', '小车限位', 'CAR_LMT');
INSERT INTO `tb_metrictype` VALUES ('30', '手动/自动', 'AUTO');
INSERT INTO `tb_metrictype` VALUES ('32', '压紧上限', 'PRESS_ULMT');
INSERT INTO `tb_metrictype` VALUES ('33', '压紧下限', 'PRESS_LLMT');
INSERT INTO `tb_metrictype` VALUES ('34', '压紧限位', 'PRESS_LMT');
INSERT INTO `tb_metrictype` VALUES ('35', '进料泵运行信号', 'JL_PUMP_UP');
INSERT INTO `tb_metrictype` VALUES ('36', '输送机运行信号', 'TRS_UP');
INSERT INTO `tb_metrictype` VALUES ('37', '进料上限', 'JL_ULMT');
INSERT INTO `tb_metrictype` VALUES ('38', '压榨下限', 'PRS_LLMT');
INSERT INTO `tb_metrictype` VALUES ('40', '压榨上限', 'PRS_ULMT');
INSERT INTO `tb_metrictype` VALUES ('41', '进料阀开限位', 'JL_OLMT');
INSERT INTO `tb_metrictype` VALUES ('42', '进料阀关限位', 'JL_CLMT');
INSERT INTO `tb_metrictype` VALUES ('45', '压榨阀开限位', 'YZ_OLMT');
INSERT INTO `tb_metrictype` VALUES ('46', '压榨阀关限位', 'YZ_CLMT');
INSERT INTO `tb_metrictype` VALUES ('47', '放空阀开限位', 'FK_OLMT');
INSERT INTO `tb_metrictype` VALUES ('48', '放空阀关限位', 'FK_CLMT');
INSERT INTO `tb_metrictype` VALUES ('49', '吹风阀开限位', 'CF_OLMT');
INSERT INTO `tb_metrictype` VALUES ('50', '吹风阀关限位', 'CF_CLMT');
INSERT INTO `tb_metrictype` VALUES ('51', '回流阀开限位', 'HL_OLMT');
INSERT INTO `tb_metrictype` VALUES ('52', '回流阀关限位', 'HL_CLMT');
INSERT INTO `tb_metrictype` VALUES ('53', '过滤/水洗', 'FILTER');
INSERT INTO `tb_metrictype` VALUES ('54', '水洗后移限位', 'WT_FW_LMT');
INSERT INTO `tb_metrictype` VALUES ('55', '水洗前移限位', 'WT_BW_LMT');
INSERT INTO `tb_metrictype` VALUES ('56', '喷头下移限位', 'SW_DWN_LMT');
INSERT INTO `tb_metrictype` VALUES ('57', '喷头上移限位', 'SW_UP_LMT');
INSERT INTO `tb_metrictype` VALUES ('58', '洗布检测点', 'XB_CHK');
INSERT INTO `tb_metrictype` VALUES ('59', '洗布转换点', 'XB_TRSF');
INSERT INTO `tb_metrictype` VALUES ('60', '洗布结束点', 'XB_END');
INSERT INTO `tb_metrictype` VALUES ('61', '压榨阀打开超时', 'PRS_TMOUT');
INSERT INTO `tb_metrictype` VALUES ('62', '压榨阀关闭超时', 'PRS_CTMOUT');
INSERT INTO `tb_metrictype` VALUES ('63', '排空阀打开超时', 'PK_OTMOUT');
INSERT INTO `tb_metrictype` VALUES ('64', '排空阀关闭超时', 'PK_CTMOUT');
INSERT INTO `tb_metrictype` VALUES ('65', '吹风阀打开超时', 'CF_OTMOUT');
INSERT INTO `tb_metrictype` VALUES ('66', '吹风阀关闭超时', 'CF_CTMOUT');
INSERT INTO `tb_metrictype` VALUES ('67', '回流阀打开超时', 'HL_OTMOUT');
INSERT INTO `tb_metrictype` VALUES ('68', '回流阀关闭超时', 'HL_CTMOUT');
INSERT INTO `tb_metrictype` VALUES ('69', '排空超时', 'PK_TMOUT');
INSERT INTO `tb_metrictype` VALUES ('70', '吹风超时', 'CF_TMOUT');
INSERT INTO `tb_metrictype` VALUES ('71', '进料阀打开超时', 'JL_OTMOUT');
INSERT INTO `tb_metrictype` VALUES ('72', '进料阀关闭超时', 'JL_CTMOUT');
INSERT INTO `tb_metrictype` VALUES ('75', '翻板打开超时', 'FB_OTMOUT');
INSERT INTO `tb_metrictype` VALUES ('76', '松开超时', 'SK_TMOUT');
INSERT INTO `tb_metrictype` VALUES ('77', '取板超时', 'QB_TMOUT');
INSERT INTO `tb_metrictype` VALUES ('78', '拉板超时', 'LB_TMOUT');
INSERT INTO `tb_metrictype` VALUES ('79', '翻板关闭超时', 'FB_CTMOUT');
INSERT INTO `tb_metrictype` VALUES ('80', '压紧超时或超限', 'YJ_TMOUT');
INSERT INTO `tb_metrictype` VALUES ('81', '进料超时', 'JL_TMOUT');
INSERT INTO `tb_metrictype` VALUES ('82', '压榨超时', 'YZ_TMOUT');
INSERT INTO `tb_metrictype` VALUES ('83', '存在压榨压力，禁止松开', 'SK_FRBD');
INSERT INTO `tb_metrictype` VALUES ('84', '存在压榨压力或小车未到位，禁止启动', 'START_FRBD');
INSERT INTO `tb_metrictype` VALUES ('85', '水洗前移超时', 'WTFW_TMOUT');
INSERT INTO `tb_metrictype` VALUES ('86', '水洗后移超时', 'WTBW_TMOUT');
INSERT INTO `tb_metrictype` VALUES ('87', '喷头下移超时', 'SWDW_TMOUT');
INSERT INTO `tb_metrictype` VALUES ('88', '喷头上移超时', 'SWUP_TMOUT');
INSERT INTO `tb_metrictype` VALUES ('89', '一队记录', 'T1_RCD');
INSERT INTO `tb_metrictype` VALUES ('90', '二队记录', 'T2_RCD');
INSERT INTO `tb_metrictype` VALUES ('91', '三队记录', 'T3_RCD');
INSERT INTO `tb_metrictype` VALUES ('92', '当前计时', 'CRT_TIMER');
INSERT INTO `tb_metrictype` VALUES ('93', '过程计时', 'PRC_TIMER');
INSERT INTO `tb_metrictype` VALUES ('94', '一队过滤次数', 'T1_COUNT');
INSERT INTO `tb_metrictype` VALUES ('95', '二队过滤次数', 'T2_COUNT');
INSERT INTO `tb_metrictype` VALUES ('96', '三队过滤次数', 'T3_COUNT');
INSERT INTO `tb_metrictype` VALUES ('97', '总记录', 'TOTL_COUNT');
INSERT INTO `tb_metrictype` VALUES ('98', '进料延时', 'JL_TIME');
INSERT INTO `tb_metrictype` VALUES ('99', '压榨延时', 'YZ_TIME');
INSERT INTO `tb_metrictype` VALUES ('100', '压榨延时时间', 'YZ_DL_TIME');
INSERT INTO `tb_metrictype` VALUES ('101', '吹风时间', 'CF_TIME');
INSERT INTO `tb_metrictype` VALUES ('102', '首次取板高速时间', '1QB_S_TIME');
INSERT INTO `tb_metrictype` VALUES ('103', '取板高速时间', 'QB_S_TIME');
INSERT INTO `tb_metrictype` VALUES ('104', '拉板高速时间', 'LB_S_TIME');
INSERT INTO `tb_metrictype` VALUES ('105', '拉板高速返回时间', 'LB_SR_TIME');
INSERT INTO `tb_metrictype` VALUES ('106', '循环等待时间', 'XHDD_TIME');
INSERT INTO `tb_metrictype` VALUES ('107', '喷头下方停留时间', 'SW_ST_TIME');
INSERT INTO `tb_metrictype` VALUES ('108', '喷头上移喷水时间', 'SW_SW_TIME');
INSERT INTO `tb_metrictype` VALUES ('109', '松开超时时间', 'SK_TO_TIME');
INSERT INTO `tb_metrictype` VALUES ('110', '取板超时时间', 'QB_TO_TIME');
INSERT INTO `tb_metrictype` VALUES ('111', '拉板超时时间', 'LB_TO_TIME');
INSERT INTO `tb_metrictype` VALUES ('112', '压紧超时时间', 'YJ_TO_TIME');
INSERT INTO `tb_metrictype` VALUES ('113', '进料超时时间', 'JL_TO_TIME');
INSERT INTO `tb_metrictype` VALUES ('114', '压榨超时时间', 'YZ_TO_TIME');
INSERT INTO `tb_metrictype` VALUES ('115', '排空超时时间', 'PK_TO_TIME');
INSERT INTO `tb_metrictype` VALUES ('116', '吹风超时时间', 'CF_TO_TIME');
INSERT INTO `tb_metrictype` VALUES ('117', '进料阀动作超时时间', 'JL_TO_TM');
INSERT INTO `tb_metrictype` VALUES ('119', '压榨阀动作超时时间', 'YZ_TO_TM');
INSERT INTO `tb_metrictype` VALUES ('120', '排空阀动作超时时间', 'PK_TO_TM');
INSERT INTO `tb_metrictype` VALUES ('121', '吹风阀动作超时时间', 'CF_TO_TM');
INSERT INTO `tb_metrictype` VALUES ('122', '回流阀动作超时时间', 'HL_TO_TM');
INSERT INTO `tb_metrictype` VALUES ('123', '水洗架前移超时时间', 'SXF_TO_TM');
INSERT INTO `tb_metrictype` VALUES ('124', '水洗架后移超时时间', 'SXB_TO_TM');
INSERT INTO `tb_metrictype` VALUES ('125', '喷头下移超时时间', 'SWD_TO_TM');
INSERT INTO `tb_metrictype` VALUES ('126', '喷头上移超时时间', 'SWU_TO_TM');
INSERT INTO `tb_metrictype` VALUES ('127', '高压卸荷时间', 'GY_TIME');
INSERT INTO `tb_metrictype` VALUES ('128', '系统报警选择', 'SYS_ALARM');
INSERT INTO `tb_metrictype` VALUES ('129', '控制选择', 'CONTROL');
INSERT INTO `tb_metrictype` VALUES ('130', '阀门报警选择', 'GATE_ALARM');
INSERT INTO `tb_metrictype` VALUES ('131', '刮板闭锁', 'SCR_BLK');
INSERT INTO `tb_metrictype` VALUES ('132', '总记录清零', 'TOTL_CLR');
INSERT INTO `tb_metrictype` VALUES ('133', '一队清零', 'T1_CLR');
INSERT INTO `tb_metrictype` VALUES ('134', '二队清零', 'T2_CLR');
INSERT INTO `tb_metrictype` VALUES ('135', '三队清零', 'T3_CLR');
INSERT INTO `tb_metrictype` VALUES ('136', '远程手动松开', 'LOOSE');
INSERT INTO `tb_metrictype` VALUES ('137', '远程手动取板', 'TAKE');
INSERT INTO `tb_metrictype` VALUES ('138', '远程手动拉板', 'PULL');
INSERT INTO `tb_metrictype` VALUES ('139', '远程手动压紧', 'PRESS');
INSERT INTO `tb_metrictype` VALUES ('140', '远程手动进料', 'FEED');
INSERT INTO `tb_metrictype` VALUES ('141', '远程手动压榨', 'SQUEEZE');
INSERT INTO `tb_metrictype` VALUES ('142', '远程手动吹风', 'BLOW');
INSERT INTO `tb_metrictype` VALUES ('143', '远程报警复位', 'RESET');
INSERT INTO `tb_metrictype` VALUES ('144', '远程启动', 'RUN');
INSERT INTO `tb_metrictype` VALUES ('145', '远程暂停', 'S_PAUSE');
INSERT INTO `tb_metrictype` VALUES ('146', '远程停止', 'STOP');
INSERT INTO `tb_metrictype` VALUES ('147', '进料结束', 'FEED_OVER');
INSERT INTO `tb_metrictype` VALUES ('148', '一队选择', 'T1_CHOOSE');
INSERT INTO `tb_metrictype` VALUES ('149', '二队选择', 'T2_CHOOSE');
INSERT INTO `tb_metrictype` VALUES ('150', '三队选择', 'T3_CHOOSE');

-- ----------------------------
-- Records of tb_thing
-- ----------------------------
INSERT INTO `tb_thing` VALUES ('6', NULL, '2492', 'DVC', '压滤机', NULL, NULL, NULL);
INSERT INTO `tb_thing` VALUES ('7', NULL, '2493', 'DVC', '压滤机', NULL, NULL, NULL);
INSERT INTO `tb_thing` VALUES ('8', NULL, '2494', 'DVC', '压滤机', NULL, NULL, NULL);
INSERT INTO `tb_thing` VALUES ('9', NULL, '2495', 'DVC', '压滤机', NULL, NULL, NULL);
INSERT INTO `tb_thing` VALUES ('10', NULL, '2496', 'DVC', '压滤机', NULL, NULL, NULL);
INSERT INTO `tb_thing` VALUES ('11', NULL, '2496A', 'DVC', '压滤机', NULL, NULL, NULL);
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'STAGE', 'XG_FP.2492.STAGE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'WATER_FWD', 'XG_FP.2492.WATER_FWD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'WATER_BWD', 'XG_FP.2492.WATER_BWD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'SHOWER_DWN', 'XG_FP.2492.SHOWER_DWN', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'SHOWER_UP', 'XG_FP.2492.SHOWER_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'PAUSE', 'XG_FP.2492.PAUSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'FAULT', 'XG_FP.2492.FAULT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'SCRAPER_UP', 'XG_FP.2492.SCRAPER_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'RL_PUMP_UP', 'XG_FP.2492.RL_PUMP_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'LOOSE_LMT0', 'XG_FP.2492.LOOSE_LMT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'CAR_LMT0', 'XG_FP.2492.CAR_LMT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'PRESS_ULMT', 'XG_FP.2492.PRESS_ULMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'PRESS_LLMT', 'XG_FP.2492.PRESS_LLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'AUTO', 'XG_FP.2492.AUTO', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'PRESS_LMT', 'XG_FP.2492.PRESS_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'JL_PUMP_UP', 'XG_FP.2492.JL_PUMP_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'TRS_UP', 'XG_FP.2492.TRS_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'JL_ULMT', 'XG_FP.2492.JL_ULMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'PRS_LLMT', 'XG_FP.2492.PRS_LLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'CAR_LMT1', 'XG_FP.2492.CAR_LMT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'PRS_ULMT', 'XG_FP.2492.PRS_ULMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'LOOSE_LMT1', 'XG_FP.2492.LOOSE_LMT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'JL_OLMT0', 'XG_FP.2492.JL_OLMT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'JL_CLMT0', 'XG_FP.2492.JL_CLMT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'JL_OLMT1', 'XG_FP.2492.JL_OLMT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'JL_CLMT1', 'XG_FP.2492.JL_CLMT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'YZ_OLMT', 'XG_FP.2492.YZ_OLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'YZ_CLMT', 'XG_FP.2492.YZ_CLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'FK_OLMT', 'XG_FP.2492.FK_OLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'FK_CLMT', 'XG_FP.2492.FK_CLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'CF_OLMT', 'XG_FP.2492.CF_OLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'CF_CLMT', 'XG_FP.2492.CF_CLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'HL_OLMT', 'XG_FP.2492.HL_OLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'HL_CLMT', 'XG_FP.2492.HL_CLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'FILTER', 'XG_FP.2492.FILTER', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'WT_FW_LMT', 'XG_FP.2492.WT_FW_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'WT_BW_LMT', 'XG_FP.2492.WT_BW_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'SW_DWN_LMT', 'XG_FP.2492.SW_DWN_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'SW_UP_LMT', 'XG_FP.2492.SW_UP_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'XB_CHK', 'XG_FP.2492.XB_CHK', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'XB_TRSF', 'XG_FP.2492.XB_TRSF', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'XB_END', 'XG_FP.2492.XB_END', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'LOCAL', 'XG_FP.2492.LOCAL', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'PRS_OTMOUT', 'XG_FP.2492.PRS_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'PRS_CTMOUT', 'XG_FP.2492.PRS_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'PK_OTMOUT', 'XG_FP.2492.PK_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'PK_CTMOUT', 'XG_FP.2492.PK_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'CF_OTMOUT', 'XG_FP.2492.CF_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'CF_CTMOUT', 'XG_FP.2492.CF_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'HL_OTMOUT', 'XG_FP.2492.HL_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'HL_CTMOUT', 'XG_FP.2492.HL_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'PK_TMOUT', 'XG_FP.2492.PK_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'CF_TMOUT', 'XG_FP.2492.CF_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'JL_OTMOUT0', 'XG_FP.2492.JL_OTMOUT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'JL_CTMOUT0', 'XG_FP.2492.JL_CTMOUT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'JL_OTMOUT1', 'XG_FP.2492.JL_OTMOUT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'JL_CTMOUT1', 'XG_FP.2492.JL_CTMOUT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'FB_OTMOUT', 'XG_FP.2492.FB_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'SK_TMOUT', 'XG_FP.2492.SK_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'QB_TMOUT', 'XG_FP.2492.QB_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'LB_TMOUT', 'XG_FP.2492.LB_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'FB_CTMOUT', 'XG_FP.2492.FB_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'YJ_TMOUT', 'XG_FP.2492.YJ_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'JL_TMOUT', 'XG_FP.2492.JL_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'YZ_TMOUT', 'XG_FP.2492.YZ_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'SK_FRBD', 'XG_FP.2492.SK_FRBD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'START_FRBD', 'XG_FP.2492.START_FRBD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'WTFW_TMOUT', 'XG_FP.2492.WTFW_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'WTBW_TMOUT', 'XG_FP.2492.WTBW_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'SWDW_TMOUT', 'XG_FP.2492.SWDW_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'SWUP_TMOUT', 'XG_FP.2492.SWUP_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'T1_RCD', 'XG_FP.2492.T1_RCD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'T2_RCD', 'XG_FP.2492.T2_RCD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'T3_RCD', 'XG_FP.2492.T3_RCD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'CRT_TIMER', 'XG_FP.2492.CRT_TIMER', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'PRC_TIMER', 'XG_FP.2492.PRC_TIMER', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'T1_COUNT', 'XG_FP.2492.T1_COUNT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'T2_COUNT', 'XG_FP.2492.T2_COUNT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'T3_COUNT', 'XG_FP.2492.T3_COUNT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'TOTL_COUNT', 'XG_FP.2492.TOTL_COUNT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'JL_DELAY', 'XG_FP.2492.JL_DELAY', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'YZ_DELAY', 'XG_FP.2492.YZ_DELAY', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'YZ_DL_TIME', 'XG_FP.2492.YZ_DL_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'CF_TIME', 'XG_FP.2492.CF_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', '1QB_S_TIME', 'XG_FP.2492.1QB_S_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'QB_S_TIME', 'XG_FP.2492.QB_S_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'LB_S_TIME', 'XG_FP.2492.LB_S_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'LB_SR_TIME', 'XG_FP.2492.LB_SR_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'XHDD_TIME', 'XG_FP.2492.XHDD_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'SW_ST_TIME', 'XG_FP.2492.SW_ST_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'SW_SW_TIME', 'XG_FP.2492.SW_SW_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'SK_TO_TIME', 'XG_FP.2492.SK_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'QB_TO_TIME', 'XG_FP.2492.QB_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'LB_TO_TIME', 'XG_FP.2492.LB_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'YJ_TO_TIME', 'XG_FP.2492.YJ_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'JL_TO_TIME', 'XG_FP.2492.JL_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'YZ_TO_TIME', 'XG_FP.2492.YZ_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'PK_TO_TIME', 'XG_FP.2492.PK_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'CF_TO_TIME', 'XG_FP.2492.CF_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'JL_TO_TM0', 'XG_FP.2492.JL_TO_TM0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'JL_TO_TM1', 'XG_FP.2492.JL_TO_TM1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'YZ_TO_TM', 'XG_FP.2492.YZ_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'PK_TO_TM', 'XG_FP.2492.PK_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'CF_TO_TM', 'XG_FP.2492.CF_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'HL_TO_TM', 'XG_FP.2492.HL_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'SXF_TO_TM', 'XG_FP.2492.SXF_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'SXB_TO_TM', 'XG_FP.2492.SXB_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'SWD_TO_TM', 'XG_FP.2492.SWD_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'SWU_TO_TM', 'XG_FP.2492.SWU_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'GY_TIME', 'XG_FP.2492.GY_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'SYS_ALARM', 'XG_FP.2492.SYS_ALARM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'CONTROL', 'XG_FP.2492.CONTROL', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'GATE_ALARM', 'XG_FP.2492.GATE_ALARM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'SCR_BLK', 'XG_FP.2492.SCR_BLK', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'TOTL_CLR', 'XG_FP.2492.TOTL_CLR', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'T1_CLR', 'XG_FP.2492.T1_CLR', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'T2_CLR', 'XG_FP.2492.T2_CLR', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'T3_CLR', 'XG_FP.2492.T3_CLR', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'LOOSE', 'XG_FP.2492.LOOSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'TAKE', 'XG_FP.2492.TAKE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'PULL', 'XG_FP.2492.PULL', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'PRESS', 'XG_FP.2492.PRESS', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'FEED', 'XG_FP.2492.FEED', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'SQUEESE', 'XG_FP.2492.SQUEESE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'BLOW', 'XG_FP.2492.BLOW', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'RESET', 'XG_FP.2492.RESET', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'RUN', 'XG_FP.2492.RUN', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'S_PAUSE', 'XG_FP.2492.S_PAUSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'STOP', 'XG_FP.2492.STOP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'R_AUTO', 'XG_FP.2492.R_AUTO', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'FEED_OVER', 'XG_FP.2492.FEED_OVER', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'T1_CHOOSE', 'XG_FP.2492.T1_CHOOSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'T2_CHOOSE', 'XG_FP.2492.T2_CHOOSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2492', 'T3_CHOOSE', 'XG_FP.2492.T3_CHOOSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'STAGE', 'XG_FP.2493.STAGE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'WATER_FWD', 'XG_FP.2493.WATER_FWD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'WATER_BWD', 'XG_FP.2493.WATER_BWD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'SHOWER_DWN', 'XG_FP.2493.SHOWER_DWN', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'SHOWER_UP', 'XG_FP.2493.SHOWER_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'PAUSE', 'XG_FP.2493.PAUSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'FAULT', 'XG_FP.2493.FAULT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'SCRAPER_UP', 'XG_FP.2493.SCRAPER_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'RL_PUMP_UP', 'XG_FP.2493.RL_PUMP_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'LOOSE_LMT0', 'XG_FP.2493.LOOSE_LMT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'CAR_LMT0', 'XG_FP.2493.CAR_LMT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'PRESS_ULMT', 'XG_FP.2493.PRESS_ULMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'PRESS_LLMT', 'XG_FP.2493.PRESS_LLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'AUTO', 'XG_FP.2493.AUTO', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'PRESS_LMT', 'XG_FP.2493.PRESS_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'JL_PUMP_UP', 'XG_FP.2493.JL_PUMP_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'TRS_UP', 'XG_FP.2493.TRS_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'JL_ULMT', 'XG_FP.2493.JL_ULMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'PRS_LLMT', 'XG_FP.2493.PRS_LLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'CAR_LMT1', 'XG_FP.2493.CAR_LMT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'PRS_ULMT', 'XG_FP.2493.PRS_ULMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'LOOSE_LMT1', 'XG_FP.2493.LOOSE_LMT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'JL_OLMT0', 'XG_FP.2493.JL_OLMT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'JL_CLMT0', 'XG_FP.2493.JL_CLMT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'JL_OLMT1', 'XG_FP.2493.JL_OLMT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'JL_CLMT1', 'XG_FP.2493.JL_CLMT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'YZ_OLMT', 'XG_FP.2493.YZ_OLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'YZ_CLMT', 'XG_FP.2493.YZ_CLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'FK_OLMT', 'XG_FP.2493.FK_OLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'FK_CLMT', 'XG_FP.2493.FK_CLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'CF_OLMT', 'XG_FP.2493.CF_OLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'CF_CLMT', 'XG_FP.2493.CF_CLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'HL_OLMT', 'XG_FP.2493.HL_OLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'HL_CLMT', 'XG_FP.2493.HL_CLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'FILTER', 'XG_FP.2493.FILTER', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'WT_FW_LMT', 'XG_FP.2493.WT_FW_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'WT_BW_LMT', 'XG_FP.2493.WT_BW_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'SW_DWN_LMT', 'XG_FP.2493.SW_DWN_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'SW_UP_LMT', 'XG_FP.2493.SW_UP_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'XB_CHK', 'XG_FP.2493.XB_CHK', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'XB_TRSF', 'XG_FP.2493.XB_TRSF', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'XB_END', 'XG_FP.2493.XB_END', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'LOCAL', 'XG_FP.2493.LOCAL', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'PRS_OTMOUT', 'XG_FP.2493.PRS_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'PRS_CTMOUT', 'XG_FP.2493.PRS_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'PK_OTMOUT', 'XG_FP.2493.PK_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'PK_CTMOUT', 'XG_FP.2493.PK_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'CF_OTMOUT', 'XG_FP.2493.CF_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'CF_CTMOUT', 'XG_FP.2493.CF_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'HL_OTMOUT', 'XG_FP.2493.HL_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'HL_CTMOUT', 'XG_FP.2493.HL_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'PK_TMOUT', 'XG_FP.2493.PK_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'CF_TMOUT', 'XG_FP.2493.CF_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'JL_OTMOUT0', 'XG_FP.2493.JL_OTMOUT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'JL_CTMOUT0', 'XG_FP.2493.JL_CTMOUT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'JL_OTMOUT1', 'XG_FP.2493.JL_OTMOUT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'JL_CTMOUT1', 'XG_FP.2493.JL_CTMOUT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'FB_OTMOUT', 'XG_FP.2493.FB_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'SK_TMOUT', 'XG_FP.2493.SK_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'QB_TMOUT', 'XG_FP.2493.QB_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'LB_TMOUT', 'XG_FP.2493.LB_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'FB_CTMOUT', 'XG_FP.2493.FB_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'YJ_TMOUT', 'XG_FP.2493.YJ_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'JL_TMOUT', 'XG_FP.2493.JL_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'YZ_TMOUT', 'XG_FP.2493.YZ_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'SK_FRBD', 'XG_FP.2493.SK_FRBD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'START_FRBD', 'XG_FP.2493.START_FRBD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'WTFW_TMOUT', 'XG_FP.2493.WTFW_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'WTBW_TMOUT', 'XG_FP.2493.WTBW_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'SWDW_TMOUT', 'XG_FP.2493.SWDW_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'SWUP_TMOUT', 'XG_FP.2493.SWUP_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'T1_RCD', 'XG_FP.2493.T1_RCD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'T2_RCD', 'XG_FP.2493.T2_RCD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'T3_RCD', 'XG_FP.2493.T3_RCD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'CRT_TIMER', 'XG_FP.2493.CRT_TIMER', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'PRC_TIMER', 'XG_FP.2493.PRC_TIMER', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'T1_COUNT', 'XG_FP.2493.T1_COUNT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'T2_COUNT', 'XG_FP.2493.T2_COUNT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'T3_COUNT', 'XG_FP.2493.T3_COUNT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'TOTL_COUNT', 'XG_FP.2493.TOTL_COUNT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'JL_DELAY', 'XG_FP.2493.JL_DELAY', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'YZ_DELAY', 'XG_FP.2493.YZ_DELAY', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'YZ_DL_TIME', 'XG_FP.2493.YZ_DL_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'CF_TIME', 'XG_FP.2493.CF_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', '1QB_S_TIME', 'XG_FP.2493.1QB_S_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'QB_S_TIME', 'XG_FP.2493.QB_S_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'LB_S_TIME', 'XG_FP.2493.LB_S_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'LB_SR_TIME', 'XG_FP.2493.LB_SR_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'XHDD_TIME', 'XG_FP.2493.XHDD_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'SW_ST_TIME', 'XG_FP.2493.SW_ST_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'SW_SW_TIME', 'XG_FP.2493.SW_SW_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'SK_TO_TIME', 'XG_FP.2493.SK_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'QB_TO_TIME', 'XG_FP.2493.QB_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'LB_TO_TIME', 'XG_FP.2493.LB_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'YJ_TO_TIME', 'XG_FP.2493.YJ_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'JL_TO_TIME', 'XG_FP.2493.JL_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'YZ_TO_TIME', 'XG_FP.2493.YZ_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'PK_TO_TIME', 'XG_FP.2493.PK_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'CF_TO_TIME', 'XG_FP.2493.CF_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'JL_TO_TM0', 'XG_FP.2493.JL_TO_TM0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'JL_TO_TM1', 'XG_FP.2493.JL_TO_TM1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'YZ_TO_TM', 'XG_FP.2493.YZ_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'PK_TO_TM', 'XG_FP.2493.PK_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'CF_TO_TM', 'XG_FP.2493.CF_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'HL_TO_TM', 'XG_FP.2493.HL_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'SXF_TO_TM', 'XG_FP.2493.SXF_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'SXB_TO_TM', 'XG_FP.2493.SXB_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'SWD_TO_TM', 'XG_FP.2493.SWD_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'SWU_TO_TM', 'XG_FP.2493.SWU_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'GY_TIME', 'XG_FP.2493.GY_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'SYS_ALARM', 'XG_FP.2493.SYS_ALARM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'CONTROL', 'XG_FP.2493.CONTROL', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'GATE_ALARM', 'XG_FP.2493.GATE_ALARM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'SCR_BLK', 'XG_FP.2493.SCR_BLK', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'TOTL_CLR', 'XG_FP.2493.TOTL_CLR', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'T1_CLR', 'XG_FP.2493.T1_CLR', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'T2_CLR', 'XG_FP.2493.T2_CLR', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'T3_CLR', 'XG_FP.2493.T3_CLR', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'LOOSE', 'XG_FP.2493.LOOSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'TAKE', 'XG_FP.2493.TAKE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'PULL', 'XG_FP.2493.PULL', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'PRESS', 'XG_FP.2493.PRESS', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'FEED', 'XG_FP.2493.FEED', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'SQUEESE', 'XG_FP.2493.SQUEESE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'BLOW', 'XG_FP.2493.BLOW', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'RESET', 'XG_FP.2493.RESET', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'RUN', 'XG_FP.2493.RUN', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'S_PAUSE', 'XG_FP.2493.S_PAUSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'STOP', 'XG_FP.2493.STOP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'R_AUTO', 'XG_FP.2493.R_AUTO', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'FEED_OVER', 'XG_FP.2493.FEED_OVER', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'T1_CHOOSE', 'XG_FP.2493.T1_CHOOSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'T2_CHOOSE', 'XG_FP.2493.T2_CHOOSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2493', 'T3_CHOOSE', 'XG_FP.2493.T3_CHOOSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'STAGE', 'XG_FP.2494.STAGE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'WATER_FWD', 'XG_FP.2494.WATER_FWD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'WATER_BWD', 'XG_FP.2494.WATER_BWD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'SHOWER_DWN', 'XG_FP.2494.SHOWER_DWN', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'SHOWER_UP', 'XG_FP.2494.SHOWER_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'PAUSE', 'XG_FP.2494.PAUSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'FAULT', 'XG_FP.2494.FAULT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'SCRAPER_UP', 'XG_FP.2494.SCRAPER_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'RL_PUMP_UP', 'XG_FP.2494.RL_PUMP_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'LOOSE_LMT0', 'XG_FP.2494.LOOSE_LMT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'CAR_LMT0', 'XG_FP.2494.CAR_LMT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'PRESS_ULMT', 'XG_FP.2494.PRESS_ULMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'PRESS_LLMT', 'XG_FP.2494.PRESS_LLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'AUTO', 'XG_FP.2494.AUTO', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'PRESS_LMT', 'XG_FP.2494.PRESS_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'JL_PUMP_UP', 'XG_FP.2494.JL_PUMP_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'TRS_UP', 'XG_FP.2494.TRS_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'JL_ULMT', 'XG_FP.2494.JL_ULMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'PRS_LLMT', 'XG_FP.2494.PRS_LLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'CAR_LMT1', 'XG_FP.2494.CAR_LMT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'PRS_ULMT', 'XG_FP.2494.PRS_ULMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'LOOSE_LMT1', 'XG_FP.2494.LOOSE_LMT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'JL_OLMT0', 'XG_FP.2494.JL_OLMT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'JL_CLMT0', 'XG_FP.2494.JL_CLMT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'JL_OLMT1', 'XG_FP.2494.JL_OLMT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'JL_CLMT1', 'XG_FP.2494.JL_CLMT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'YZ_OLMT', 'XG_FP.2494.YZ_OLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'YZ_CLMT', 'XG_FP.2494.YZ_CLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'FK_OLMT', 'XG_FP.2494.FK_OLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'FK_CLMT', 'XG_FP.2494.FK_CLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'CF_OLMT', 'XG_FP.2494.CF_OLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'CF_CLMT', 'XG_FP.2494.CF_CLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'HL_OLMT', 'XG_FP.2494.HL_OLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'HL_CLMT', 'XG_FP.2494.HL_CLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'FILTER', 'XG_FP.2494.FILTER', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'WT_FW_LMT', 'XG_FP.2494.WT_FW_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'WT_BW_LMT', 'XG_FP.2494.WT_BW_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'SW_DWN_LMT', 'XG_FP.2494.SW_DWN_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'SW_UP_LMT', 'XG_FP.2494.SW_UP_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'XB_CHK', 'XG_FP.2494.XB_CHK', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'XB_TRSF', 'XG_FP.2494.XB_TRSF', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'XB_END', 'XG_FP.2494.XB_END', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'LOCAL', 'XG_FP.2494.LOCAL', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'PRS_OTMOUT', 'XG_FP.2494.PRS_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'PRS_CTMOUT', 'XG_FP.2494.PRS_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'PK_OTMOUT', 'XG_FP.2494.PK_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'PK_CTMOUT', 'XG_FP.2494.PK_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'CF_OTMOUT', 'XG_FP.2494.CF_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'CF_CTMOUT', 'XG_FP.2494.CF_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'HL_OTMOUT', 'XG_FP.2494.HL_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'HL_CTMOUT', 'XG_FP.2494.HL_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'PK_TMOUT', 'XG_FP.2494.PK_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'CF_TMOUT', 'XG_FP.2494.CF_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'JL_OTMOUT0', 'XG_FP.2494.JL_OTMOUT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'JL_CTMOUT0', 'XG_FP.2494.JL_CTMOUT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'JL_OTMOUT1', 'XG_FP.2494.JL_OTMOUT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'JL_CTMOUT1', 'XG_FP.2494.JL_CTMOUT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'FB_OTMOUT', 'XG_FP.2494.FB_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'SK_TMOUT', 'XG_FP.2494.SK_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'QB_TMOUT', 'XG_FP.2494.QB_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'LB_TMOUT', 'XG_FP.2494.LB_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'FB_CTMOUT', 'XG_FP.2494.FB_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'YJ_TMOUT', 'XG_FP.2494.YJ_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'JL_TMOUT', 'XG_FP.2494.JL_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'YZ_TMOUT', 'XG_FP.2494.YZ_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'SK_FRBD', 'XG_FP.2494.SK_FRBD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'START_FRBD', 'XG_FP.2494.START_FRBD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'WTFW_TMOUT', 'XG_FP.2494.WTFW_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'WTBW_TMOUT', 'XG_FP.2494.WTBW_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'SWDW_TMOUT', 'XG_FP.2494.SWDW_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'SWUP_TMOUT', 'XG_FP.2494.SWUP_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'T1_RCD', 'XG_FP.2494.T1_RCD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'T2_RCD', 'XG_FP.2494.T2_RCD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'T3_RCD', 'XG_FP.2494.T3_RCD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'CRT_TIMER', 'XG_FP.2494.CRT_TIMER', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'PRC_TIMER', 'XG_FP.2494.PRC_TIMER', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'T1_COUNT', 'XG_FP.2494.T1_COUNT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'T2_COUNT', 'XG_FP.2494.T2_COUNT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'T3_COUNT', 'XG_FP.2494.T3_COUNT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'TOTL_COUNT', 'XG_FP.2494.TOTL_COUNT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'JL_DELAY', 'XG_FP.2494.JL_DELAY', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'YZ_DELAY', 'XG_FP.2494.YZ_DELAY', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'YZ_DL_TIME', 'XG_FP.2494.YZ_DL_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'CF_TIME', 'XG_FP.2494.CF_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', '1QB_S_TIME', 'XG_FP.2494.1QB_S_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'QB_S_TIME', 'XG_FP.2494.QB_S_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'LB_S_TIME', 'XG_FP.2494.LB_S_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'LB_SR_TIME', 'XG_FP.2494.LB_SR_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'XHDD_TIME', 'XG_FP.2494.XHDD_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'SW_ST_TIME', 'XG_FP.2494.SW_ST_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'SW_SW_TIME', 'XG_FP.2494.SW_SW_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'SK_TO_TIME', 'XG_FP.2494.SK_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'QB_TO_TIME', 'XG_FP.2494.QB_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'LB_TO_TIME', 'XG_FP.2494.LB_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'YJ_TO_TIME', 'XG_FP.2494.YJ_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'JL_TO_TIME', 'XG_FP.2494.JL_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'YZ_TO_TIME', 'XG_FP.2494.YZ_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'PK_TO_TIME', 'XG_FP.2494.PK_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'CF_TO_TIME', 'XG_FP.2494.CF_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'JL_TO_TM0', 'XG_FP.2494.JL_TO_TM0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'JL_TO_TM1', 'XG_FP.2494.JL_TO_TM1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'YZ_TO_TM', 'XG_FP.2494.YZ_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'PK_TO_TM', 'XG_FP.2494.PK_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'CF_TO_TM', 'XG_FP.2494.CF_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'HL_TO_TM', 'XG_FP.2494.HL_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'SXF_TO_TM', 'XG_FP.2494.SXF_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'SXB_TO_TM', 'XG_FP.2494.SXB_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'SWD_TO_TM', 'XG_FP.2494.SWD_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'SWU_TO_TM', 'XG_FP.2494.SWU_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'GY_TIME', 'XG_FP.2494.GY_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'SYS_ALARM', 'XG_FP.2494.SYS_ALARM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'CONTROL', 'XG_FP.2494.CONTROL', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'GATE_ALARM', 'XG_FP.2494.GATE_ALARM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'SCR_BLK', 'XG_FP.2494.SCR_BLK', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'TOTL_CLR', 'XG_FP.2494.TOTL_CLR', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'T1_CLR', 'XG_FP.2494.T1_CLR', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'T2_CLR', 'XG_FP.2494.T2_CLR', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'T3_CLR', 'XG_FP.2494.T3_CLR', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'LOOSE', 'XG_FP.2494.LOOSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'TAKE', 'XG_FP.2494.TAKE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'PULL', 'XG_FP.2494.PULL', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'PRESS', 'XG_FP.2494.PRESS', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'FEED', 'XG_FP.2494.FEED', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'SQUEESE', 'XG_FP.2494.SQUEESE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'BLOW', 'XG_FP.2494.BLOW', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'RESET', 'XG_FP.2494.RESET', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'RUN', 'XG_FP.2494.RUN', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'S_PAUSE', 'XG_FP.2494.S_PAUSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'STOP', 'XG_FP.2494.STOP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'R_AUTO', 'XG_FP.2494.R_AUTO', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'FEED_OVER', 'XG_FP.2494.FEED_OVER', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'T1_CHOOSE', 'XG_FP.2494.T1_CHOOSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'T2_CHOOSE', 'XG_FP.2494.T2_CHOOSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2494', 'T3_CHOOSE', 'XG_FP.2494.T3_CHOOSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'STAGE', 'XG_FP.2495.STAGE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'WATER_FWD', 'XG_FP.2495.WATER_FWD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'WATER_BWD', 'XG_FP.2495.WATER_BWD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'SHOWER_DWN', 'XG_FP.2495.SHOWER_DWN', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'SHOWER_UP', 'XG_FP.2495.SHOWER_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'PAUSE', 'XG_FP.2495.PAUSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'FAULT', 'XG_FP.2495.FAULT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'SCRAPER_UP', 'XG_FP.2495.SCRAPER_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'RL_PUMP_UP', 'XG_FP.2495.RL_PUMP_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'LOOSE_LMT0', 'XG_FP.2495.LOOSE_LMT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'CAR_LMT0', 'XG_FP.2495.CAR_LMT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'PRESS_ULMT', 'XG_FP.2495.PRESS_ULMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'PRESS_LLMT', 'XG_FP.2495.PRESS_LLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'AUTO', 'XG_FP.2495.AUTO', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'PRESS_LMT', 'XG_FP.2495.PRESS_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'JL_PUMP_UP', 'XG_FP.2495.JL_PUMP_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'TRS_UP', 'XG_FP.2495.TRS_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'JL_ULMT', 'XG_FP.2495.JL_ULMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'PRS_LLMT', 'XG_FP.2495.PRS_LLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'CAR_LMT1', 'XG_FP.2495.CAR_LMT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'PRS_ULMT', 'XG_FP.2495.PRS_ULMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'LOOSE_LMT1', 'XG_FP.2495.LOOSE_LMT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'JL_OLMT0', 'XG_FP.2495.JL_OLMT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'JL_CLMT0', 'XG_FP.2495.JL_CLMT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'JL_OLMT1', 'XG_FP.2495.JL_OLMT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'JL_CLMT1', 'XG_FP.2495.JL_CLMT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'YZ_OLMT', 'XG_FP.2495.YZ_OLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'YZ_CLMT', 'XG_FP.2495.YZ_CLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'FK_OLMT', 'XG_FP.2495.FK_OLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'FK_CLMT', 'XG_FP.2495.FK_CLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'CF_OLMT', 'XG_FP.2495.CF_OLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'CF_CLMT', 'XG_FP.2495.CF_CLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'HL_OLMT', 'XG_FP.2495.HL_OLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'HL_CLMT', 'XG_FP.2495.HL_CLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'FILTER', 'XG_FP.2495.FILTER', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'WT_FW_LMT', 'XG_FP.2495.WT_FW_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'WT_BW_LMT', 'XG_FP.2495.WT_BW_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'SW_DWN_LMT', 'XG_FP.2495.SW_DWN_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'SW_UP_LMT', 'XG_FP.2495.SW_UP_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'XB_CHK', 'XG_FP.2495.XB_CHK', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'XB_TRSF', 'XG_FP.2495.XB_TRSF', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'XB_END', 'XG_FP.2495.XB_END', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'LOCAL', 'XG_FP.2495.LOCAL', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'PRS_OTMOUT', 'XG_FP.2495.PRS_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'PRS_CTMOUT', 'XG_FP.2495.PRS_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'PK_OTMOUT', 'XG_FP.2495.PK_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'PK_CTMOUT', 'XG_FP.2495.PK_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'CF_OTMOUT', 'XG_FP.2495.CF_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'CF_CTMOUT', 'XG_FP.2495.CF_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'HL_OTMOUT', 'XG_FP.2495.HL_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'HL_CTMOUT', 'XG_FP.2495.HL_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'PK_TMOUT', 'XG_FP.2495.PK_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'CF_TMOUT', 'XG_FP.2495.CF_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'JL_OTMOUT0', 'XG_FP.2495.JL_OTMOUT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'JL_CTMOUT0', 'XG_FP.2495.JL_CTMOUT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'JL_OTMOUT1', 'XG_FP.2495.JL_OTMOUT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'JL_CTMOUT1', 'XG_FP.2495.JL_CTMOUT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'FB_OTMOUT', 'XG_FP.2495.FB_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'SK_TMOUT', 'XG_FP.2495.SK_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'QB_TMOUT', 'XG_FP.2495.QB_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'LB_TMOUT', 'XG_FP.2495.LB_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'FB_CTMOUT', 'XG_FP.2495.FB_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'YJ_TMOUT', 'XG_FP.2495.YJ_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'JL_TMOUT', 'XG_FP.2495.JL_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'YZ_TMOUT', 'XG_FP.2495.YZ_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'SK_FRBD', 'XG_FP.2495.SK_FRBD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'START_FRBD', 'XG_FP.2495.START_FRBD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'WTFW_TMOUT', 'XG_FP.2495.WTFW_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'WTBW_TMOUT', 'XG_FP.2495.WTBW_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'SWDW_TMOUT', 'XG_FP.2495.SWDW_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'SWUP_TMOUT', 'XG_FP.2495.SWUP_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'T1_RCD', 'XG_FP.2495.T1_RCD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'T2_RCD', 'XG_FP.2495.T2_RCD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'T3_RCD', 'XG_FP.2495.T3_RCD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'CRT_TIMER', 'XG_FP.2495.CRT_TIMER', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'PRC_TIMER', 'XG_FP.2495.PRC_TIMER', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'T1_COUNT', 'XG_FP.2495.T1_COUNT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'T2_COUNT', 'XG_FP.2495.T2_COUNT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'T3_COUNT', 'XG_FP.2495.T3_COUNT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'TOTL_COUNT', 'XG_FP.2495.TOTL_COUNT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'JL_DELAY', 'XG_FP.2495.JL_DELAY', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'YZ_DELAY', 'XG_FP.2495.YZ_DELAY', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'YZ_DL_TIME', 'XG_FP.2495.YZ_DL_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'CF_TIME', 'XG_FP.2495.CF_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', '1QB_S_TIME', 'XG_FP.2495.1QB_S_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'QB_S_TIME', 'XG_FP.2495.QB_S_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'LB_S_TIME', 'XG_FP.2495.LB_S_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'LB_SR_TIME', 'XG_FP.2495.LB_SR_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'XHDD_TIME', 'XG_FP.2495.XHDD_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'SW_ST_TIME', 'XG_FP.2495.SW_ST_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'SW_SW_TIME', 'XG_FP.2495.SW_SW_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'SK_TO_TIME', 'XG_FP.2495.SK_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'QB_TO_TIME', 'XG_FP.2495.QB_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'LB_TO_TIME', 'XG_FP.2495.LB_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'YJ_TO_TIME', 'XG_FP.2495.YJ_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'JL_TO_TIME', 'XG_FP.2495.JL_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'YZ_TO_TIME', 'XG_FP.2495.YZ_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'PK_TO_TIME', 'XG_FP.2495.PK_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'CF_TO_TIME', 'XG_FP.2495.CF_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'JL_TO_TM0', 'XG_FP.2495.JL_TO_TM0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'JL_TO_TM1', 'XG_FP.2495.JL_TO_TM1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'YZ_TO_TM', 'XG_FP.2495.YZ_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'PK_TO_TM', 'XG_FP.2495.PK_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'CF_TO_TM', 'XG_FP.2495.CF_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'HL_TO_TM', 'XG_FP.2495.HL_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'SXF_TO_TM', 'XG_FP.2495.SXF_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'SXB_TO_TM', 'XG_FP.2495.SXB_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'SWD_TO_TM', 'XG_FP.2495.SWD_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'SWU_TO_TM', 'XG_FP.2495.SWU_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'GY_TIME', 'XG_FP.2495.GY_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'SYS_ALARM', 'XG_FP.2495.SYS_ALARM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'CONTROL', 'XG_FP.2495.CONTROL', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'GATE_ALARM', 'XG_FP.2495.GATE_ALARM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'SCR_BLK', 'XG_FP.2495.SCR_BLK', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'TOTL_CLR', 'XG_FP.2495.TOTL_CLR', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'T1_CLR', 'XG_FP.2495.T1_CLR', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'T2_CLR', 'XG_FP.2495.T2_CLR', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'T3_CLR', 'XG_FP.2495.T3_CLR', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'LOOSE', 'XG_FP.2495.LOOSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'TAKE', 'XG_FP.2495.TAKE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'PULL', 'XG_FP.2495.PULL', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'PRESS', 'XG_FP.2495.PRESS', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'FEED', 'XG_FP.2495.FEED', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'SQUEESE', 'XG_FP.2495.SQUEESE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'BLOW', 'XG_FP.2495.BLOW', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'RESET', 'XG_FP.2495.RESET', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'RUN', 'XG_FP.2495.RUN', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'S_PAUSE', 'XG_FP.2495.S_PAUSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'STOP', 'XG_FP.2495.STOP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'R_AUTO', 'XG_FP.2495.R_AUTO', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'FEED_OVER', 'XG_FP.2495.FEED_OVER', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'T1_CHOOSE', 'XG_FP.2495.T1_CHOOSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'T2_CHOOSE', 'XG_FP.2495.T2_CHOOSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2495', 'T3_CHOOSE', 'XG_FP.2495.T3_CHOOSE', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'STAGE', 'XG_FP.2496.STAGE', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'WATER_FWD', 'XG_FP.2496.WATER_FWD', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'WATER_BWD', 'XG_FP.2496.WATER_BWD', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'SHOWER_DWN', 'XG_FP.2496.SHOWER_DWN', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'SHOWER_UP', 'XG_FP.2496.SHOWER_UP', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'PAUSE', 'XG_FP.2496.PAUSE', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'FAULT', 'XG_FP.2496.FAULT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'SCRAPER_UP', 'XG_FP.2496.SCRAPER_UP', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'RL_PUMP_UP', 'XG_FP.2496.RL_PUMP_UP', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'LOOSE_LMT0', 'XG_FP.2496.LOOSE_LMT0', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'CAR_LMT0', 'XG_FP.2496.CAR_LMT0', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'PRESS_ULMT', 'XG_FP.2496.PRESS_ULMT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'PRESS_LLMT', 'XG_FP.2496.PRESS_LLMT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'AUTO', 'XG_FP.2496.AUTO', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'PRESS_LMT', 'XG_FP.2496.PRESS_LMT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'JL_PUMP_UP', 'XG_FP.2496.JL_PUMP_UP', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'TRS_UP', 'XG_FP.2496.TRS_UP', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'JL_ULMT', 'XG_FP.2496.JL_ULMT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'PRS_LLMT', 'XG_FP.2496.PRS_LLMT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'CAR_LMT1', 'XG_FP.2496.CAR_LMT1', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'PRS_ULMT', 'XG_FP.2496.PRS_ULMT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'LOOSE_LMT1', 'XG_FP.2496.LOOSE_LMT1', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'JL_OLMT0', 'XG_FP.2496.JL_OLMT0', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'JL_CLMT0', 'XG_FP.2496.JL_CLMT0', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'JL_OLMT1', 'XG_FP.2496.JL_OLMT1', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'JL_CLMT1', 'XG_FP.2496.JL_CLMT1', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'YZ_OLMT', 'XG_FP.2496.YZ_OLMT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'YZ_CLMT', 'XG_FP.2496.YZ_CLMT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'FK_OLMT', 'XG_FP.2496.FK_OLMT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'FK_CLMT', 'XG_FP.2496.FK_CLMT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'CF_OLMT', 'XG_FP.2496.CF_OLMT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'CF_CLMT', 'XG_FP.2496.CF_CLMT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'HL_OLMT', 'XG_FP.2496.HL_OLMT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'HL_CLMT', 'XG_FP.2496.HL_CLMT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'FILTER', 'XG_FP.2496.FILTER', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'WT_FW_LMT', 'XG_FP.2496.WT_FW_LMT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'WT_BW_LMT', 'XG_FP.2496.WT_BW_LMT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'SW_DWN_LMT', 'XG_FP.2496.SW_DWN_LMT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'SW_UP_LMT', 'XG_FP.2496.SW_UP_LMT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'XB_CHK', 'XG_FP.2496.XB_CHK', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'XB_TRSF', 'XG_FP.2496.XB_TRSF', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'XB_END', 'XG_FP.2496.XB_END', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'LOCAL', 'XG_FP.2496.LOCAL', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'PRS_OTMOUT', 'XG_FP.2496.PRS_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'PRS_CTMOUT', 'XG_FP.2496.PRS_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'PK_OTMOUT', 'XG_FP.2496.PK_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'PK_CTMOUT', 'XG_FP.2496.PK_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'CF_OTMOUT', 'XG_FP.2496.CF_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'CF_CTMOUT', 'XG_FP.2496.CF_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'HL_OTMOUT', 'XG_FP.2496.HL_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'HL_CTMOUT', 'XG_FP.2496.HL_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'PK_TMOUT', 'XG_FP.2496.PK_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'CF_TMOUT', 'XG_FP.2496.CF_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'JL_OTMOUT0', 'XG_FP.2496.JL_OTMOUT0', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'JL_CTMOUT0', 'XG_FP.2496.JL_CTMOUT0', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'JL_OTMOUT1', 'XG_FP.2496.JL_OTMOUT1', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'JL_CTMOUT1', 'XG_FP.2496.JL_CTMOUT1', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'FB_OTMOUT', 'XG_FP.2496.FB_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'SK_TMOUT', 'XG_FP.2496.SK_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'QB_TMOUT', 'XG_FP.2496.QB_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'LB_TMOUT', 'XG_FP.2496.LB_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'FB_CTMOUT', 'XG_FP.2496.FB_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'YJ_TMOUT', 'XG_FP.2496.YJ_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'JL_TMOUT', 'XG_FP.2496.JL_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'YZ_TMOUT', 'XG_FP.2496.YZ_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'SK_FRBD', 'XG_FP.2496.SK_FRBD', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'START_FRBD', 'XG_FP.2496.START_FRBD', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'WTFW_TMOUT', 'XG_FP.2496.WTFW_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'WTBW_TMOUT', 'XG_FP.2496.WTBW_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'SWDW_TMOUT', 'XG_FP.2496.SWDW_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'SWUP_TMOUT', 'XG_FP.2496.SWUP_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'T1_RCD', 'XG_FP.2496.T1_RCD', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'T2_RCD', 'XG_FP.2496.T2_RCD', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'T3_RCD', 'XG_FP.2496.T3_RCD', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'CRT_TIMER', 'XG_FP.2496.CRT_TIMER', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'PRC_TIMER', 'XG_FP.2496.PRC_TIMER', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'T1_COUNT', 'XG_FP.2496.T1_COUNT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'T2_COUNT', 'XG_FP.2496.T2_COUNT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'T3_COUNT', 'XG_FP.2496.T3_COUNT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'TOTL_COUNT', 'XG_FP.2496.TOTL_COUNT', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'JL_DELAY', 'XG_FP.2496.JL_DELAY', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'YZ_DELAY', 'XG_FP.2496.YZ_DELAY', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'YZ_DL_TIME', 'XG_FP.2496.YZ_DL_TIME', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'CF_TIME', 'XG_FP.2496.CF_TIME', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', '1QB_S_TIME', 'XG_FP.2496.1QB_S_TIME', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'QB_S_TIME', 'XG_FP.2496.QB_S_TIME', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'LB_S_TIME', 'XG_FP.2496.LB_S_TIME', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'LB_SR_TIME', 'XG_FP.2496.LB_SR_TIME', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'XHDD_TIME', 'XG_FP.2496.XHDD_TIME', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'SW_ST_TIME', 'XG_FP.2496.SW_ST_TIME', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'SW_SW_TIME', 'XG_FP.2496.SW_SW_TIME', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'SK_TO_TIME', 'XG_FP.2496.SK_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'QB_TO_TIME', 'XG_FP.2496.QB_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'LB_TO_TIME', 'XG_FP.2496.LB_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'YJ_TO_TIME', 'XG_FP.2496.YJ_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'JL_TO_TIME', 'XG_FP.2496.JL_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'YZ_TO_TIME', 'XG_FP.2496.YZ_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'PK_TO_TIME', 'XG_FP.2496.PK_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'CF_TO_TIME', 'XG_FP.2496.CF_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'JL_TO_TM0', 'XG_FP.2496.JL_TO_TM0', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'JL_TO_TM1', 'XG_FP.2496.JL_TO_TM1', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'YZ_TO_TM', 'XG_FP.2496.YZ_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'PK_TO_TM', 'XG_FP.2496.PK_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'CF_TO_TM', 'XG_FP.2496.CF_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'HL_TO_TM', 'XG_FP.2496.HL_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'SXF_TO_TM', 'XG_FP.2496.SXF_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'SXB_TO_TM', 'XG_FP.2496.SXB_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'SWD_TO_TM', 'XG_FP.2496.SWD_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'SWU_TO_TM', 'XG_FP.2496.SWU_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'GY_TIME', 'XG_FP.2496.GY_TIME', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'SYS_ALARM', 'XG_FP.2496.SYS_ALARM', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'CONTROL', 'XG_FP.2496.CONTROL', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'GATE_ALARM', 'XG_FP.2496.GATE_ALARM', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'SCR_BLK', 'XG_FP.2496.SCR_BLK', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'TOTL_CLR', 'XG_FP.2496.TOTL_CLR', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'T1_CLR', 'XG_FP.2496.T1_CLR', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'T2_CLR', 'XG_FP.2496.T2_CLR', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'T3_CLR', 'XG_FP.2496.T3_CLR', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'LOOSE', 'XG_FP.2496.LOOSE', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'TAKE', 'XG_FP.2496.TAKE', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'PULL', 'XG_FP.2496.PULL', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'PRESS', 'XG_FP.2496.PRESS', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'FEED', 'XG_FP.2496.FEED', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'SQUEESE', 'XG_FP.2496.SQUEESE', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'BLOW', 'XG_FP.2496.BLOW', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'RESET', 'XG_FP.2496.RESET', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'RUN', 'XG_FP.2496.RUN', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'S_PAUSE', 'XG_FP.2496.S_PAUSE', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'STOP', 'XG_FP.2496.STOP', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'R_AUTO', 'XG_FP.2496.R_AUTO', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'FEED_OVER', 'XG_FP.2496.FEED_OVER', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'T1_CHOOSE', 'XG_FP.2496.T1_CHOOSE', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'T2_CHOOSE', 'XG_FP.2496.T2_CHOOSE', '1');
INSERT INTO `rel_thing_metric_label` ( `thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496', 'T3_CHOOSE', 'XG_FP.2496.T3_CHOOSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'STAGE', 'XG_FP.2496A.STAGE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'WATER_FWD', 'XG_FP.2496A.WATER_FWD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'WATER_BWD', 'XG_FP.2496A.WATER_BWD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'SHOWER_DWN', 'XG_FP.2496A.SHOWER_DWN', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'SHOWER_UP', 'XG_FP.2496A.SHOWER_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'PAUSE', 'XG_FP.2496A.PAUSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'FAULT', 'XG_FP.2496A.FAULT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'SCRAPER_UP', 'XG_FP.2496A.SCRAPER_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'RL_PUMP_UP', 'XG_FP.2496A.RL_PUMP_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'LOOSE_LMT0', 'XG_FP.2496A.LOOSE_LMT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'CAR_LMT0', 'XG_FP.2496A.CAR_LMT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'PRESS_ULMT', 'XG_FP.2496A.PRESS_ULMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'PRESS_LLMT', 'XG_FP.2496A.PRESS_LLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'AUTO', 'XG_FP.2496A.AUTO', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'PRESS_LMT', 'XG_FP.2496A.PRESS_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'JL_PUMP_UP', 'XG_FP.2496A.JL_PUMP_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'TRS_UP', 'XG_FP.2496A.TRS_UP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'JL_ULMT', 'XG_FP.2496A.JL_ULMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'PRS_LLMT', 'XG_FP.2496A.PRS_LLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'CAR_LMT1', 'XG_FP.2496A.CAR_LMT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'PRS_ULMT', 'XG_FP.2496A.PRS_ULMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'LOOSE_LMT1', 'XG_FP.2496A.LOOSE_LMT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'JL_OLMT0', 'XG_FP.2496A.JL_OLMT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'JL_CLMT0', 'XG_FP.2496A.JL_CLMT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'JL_OLMT1', 'XG_FP.2496A.JL_OLMT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'JL_CLMT1', 'XG_FP.2496A.JL_CLMT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'YZ_OLMT', 'XG_FP.2496A.YZ_OLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'YZ_CLMT', 'XG_FP.2496A.YZ_CLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'FK_OLMT', 'XG_FP.2496A.FK_OLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'FK_CLMT', 'XG_FP.2496A.FK_CLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'CF_OLMT', 'XG_FP.2496A.CF_OLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'CF_CLMT', 'XG_FP.2496A.CF_CLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'HL_OLMT', 'XG_FP.2496A.HL_OLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'HL_CLMT', 'XG_FP.2496A.HL_CLMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'FILTER', 'XG_FP.2496A.FILTER', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'WT_FW_LMT', 'XG_FP.2496A.WT_FW_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'WT_BW_LMT', 'XG_FP.2496A.WT_BW_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'SW_DWN_LMT', 'XG_FP.2496A.SW_DWN_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'SW_UP_LMT', 'XG_FP.2496A.SW_UP_LMT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'XB_CHK', 'XG_FP.2496A.XB_CHK', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'XB_TRSF', 'XG_FP.2496A.XB_TRSF', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'XB_END', 'XG_FP.2496A.XB_END', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'LOCAL', 'XG_FP.2496A.LOCAL', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'PRS_OTMOUT', 'XG_FP.2496A.PRS_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'PRS_CTMOUT', 'XG_FP.2496A.PRS_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'PK_OTMOUT', 'XG_FP.2496A.PK_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'PK_CTMOUT', 'XG_FP.2496A.PK_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'CF_OTMOUT', 'XG_FP.2496A.CF_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'CF_CTMOUT', 'XG_FP.2496A.CF_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'HL_OTMOUT', 'XG_FP.2496A.HL_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'HL_CTMOUT', 'XG_FP.2496A.HL_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'PK_TMOUT', 'XG_FP.2496A.PK_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'CF_TMOUT', 'XG_FP.2496A.CF_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'JL_OTMOUT0', 'XG_FP.2496A.JL_OTMOUT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'JL_CTMOUT0', 'XG_FP.2496A.JL_CTMOUT0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'JL_OTMOUT1', 'XG_FP.2496A.JL_OTMOUT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'JL_CTMOUT1', 'XG_FP.2496A.JL_CTMOUT1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'FB_OTMOUT', 'XG_FP.2496A.FB_OTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'SK_TMOUT', 'XG_FP.2496A.SK_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'QB_TMOUT', 'XG_FP.2496A.QB_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'LB_TMOUT', 'XG_FP.2496A.LB_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'FB_CTMOUT', 'XG_FP.2496A.FB_CTMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'YJ_TMOUT', 'XG_FP.2496A.YJ_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'JL_TMOUT', 'XG_FP.2496A.JL_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'YZ_TMOUT', 'XG_FP.2496A.YZ_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'SK_FRBD', 'XG_FP.2496A.SK_FRBD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'START_FRBD', 'XG_FP.2496A.START_FRBD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'WTFW_TMOUT', 'XG_FP.2496A.WTFW_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'WTBW_TMOUT', 'XG_FP.2496A.WTBW_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'SWDW_TMOUT', 'XG_FP.2496A.SWDW_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'SWUP_TMOUT', 'XG_FP.2496A.SWUP_TMOUT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'T1_RCD', 'XG_FP.2496A.T1_RCD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'T2_RCD', 'XG_FP.2496A.T2_RCD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'T3_RCD', 'XG_FP.2496A.T3_RCD', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'CRT_TIMER', 'XG_FP.2496A.CRT_TIMER', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'PRC_TIMER', 'XG_FP.2496A.PRC_TIMER', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'T1_COUNT', 'XG_FP.2496A.T1_COUNT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'T2_COUNT', 'XG_FP.2496A.T2_COUNT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'T3_COUNT', 'XG_FP.2496A.T3_COUNT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'TOTL_COUNT', 'XG_FP.2496A.TOTL_COUNT', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'JL_DELAY', 'XG_FP.2496A.JL_DELAY', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'YZ_DELAY', 'XG_FP.2496A.YZ_DELAY', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'YZ_DL_TIME', 'XG_FP.2496A.YZ_DL_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'CF_TIME', 'XG_FP.2496A.CF_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', '1QB_S_TIME', 'XG_FP.2496A.1QB_S_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'QB_S_TIME', 'XG_FP.2496A.QB_S_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'LB_S_TIME', 'XG_FP.2496A.LB_S_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'LB_SR_TIME', 'XG_FP.2496A.LB_SR_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'XHDD_TIME', 'XG_FP.2496A.XHDD_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'SW_ST_TIME', 'XG_FP.2496A.SW_ST_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'SW_SW_TIME', 'XG_FP.2496A.SW_SW_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'SK_TO_TIME', 'XG_FP.2496A.SK_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'QB_TO_TIME', 'XG_FP.2496A.QB_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'LB_TO_TIME', 'XG_FP.2496A.LB_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'YJ_TO_TIME', 'XG_FP.2496A.YJ_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'JL_TO_TIME', 'XG_FP.2496A.JL_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'YZ_TO_TIME', 'XG_FP.2496A.YZ_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'PK_TO_TIME', 'XG_FP.2496A.PK_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'CF_TO_TIME', 'XG_FP.2496A.CF_TO_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'JL_TO_TM0', 'XG_FP.2496A.JL_TO_TM0', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'JL_TO_TM1', 'XG_FP.2496A.JL_TO_TM1', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'YZ_TO_TM', 'XG_FP.2496A.YZ_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'PK_TO_TM', 'XG_FP.2496A.PK_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'CF_TO_TM', 'XG_FP.2496A.CF_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'HL_TO_TM', 'XG_FP.2496A.HL_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'SXF_TO_TM', 'XG_FP.2496A.SXF_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'SXB_TO_TM', 'XG_FP.2496A.SXB_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'SWD_TO_TM', 'XG_FP.2496A.SWD_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'SWU_TO_TM', 'XG_FP.2496A.SWU_TO_TM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'GY_TIME', 'XG_FP.2496A.GY_TIME', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'SYS_ALARM', 'XG_FP.2496A.SYS_ALARM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'CONTROL', 'XG_FP.2496A.CONTROL', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'GATE_ALARM', 'XG_FP.2496A.GATE_ALARM', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'SCR_BLK', 'XG_FP.2496A.SCR_BLK', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'TOTL_CLR', 'XG_FP.2496A.TOTL_CLR', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'T1_CLR', 'XG_FP.2496A.T1_CLR', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'T2_CLR', 'XG_FP.2496A.T2_CLR', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'T3_CLR', 'XG_FP.2496A.T3_CLR', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'LOOSE', 'XG_FP.2496A.LOOSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'TAKE', 'XG_FP.2496A.TAKE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'PULL', 'XG_FP.2496A.PULL', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'PRESS', 'XG_FP.2496A.PRESS', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'FEED', 'XG_FP.2496A.FEED', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'SQUEESE', 'XG_FP.2496A.SQUEESE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'BLOW', 'XG_FP.2496A.BLOW', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'RESET', 'XG_FP.2496A.RESET', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'RUN', 'XG_FP.2496A.RUN', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'S_PAUSE', 'XG_FP.2496A.S_PAUSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'STOP', 'XG_FP.2496A.STOP', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'R_AUTO', 'XG_FP.2496A.R_AUTO', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'FEED_OVER', 'XG_FP.2496A.FEED_OVER', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'T1_CHOOSE', 'XG_FP.2496A.T1_CHOOSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'T2_CHOOSE', 'XG_FP.2496A.T2_CHOOSE', '1');
INSERT INTO `rel_thing_metric_label` (`thing_code`, `metric_code`, `label_path`, `enabled`) VALUES ( '2496A', 'T3_CHOOSE', 'XG_FP.2496A.T3_CHOOSE', '1');
/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50710
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50710
File Encoding         : 65001

Date: 2017-09-18 10:54:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_filterpress_feedover_param
-- ----------------------------
DROP TABLE IF EXISTS `tb_filterpress_feedover_param`;
CREATE TABLE `tb_filterpress_feedover_param` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(255) DEFAULT NULL,
  `auto_manu_confirm_state` tinyint(4) DEFAULT NULL,
  `intelligent_manu_state` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_filterpress_feedover_param
-- ----------------------------
INSERT INTO `tb_filterpress_feedover_param` (`thing_code`, `auto_manu_confirm_state`, `intelligent_manu_state`) VALUES ('2492', '1', '1');
INSERT INTO `tb_filterpress_feedover_param` (`thing_code`, `auto_manu_confirm_state`, `intelligent_manu_state`) VALUES ('2493', '1', '0');
INSERT INTO `tb_filterpress_feedover_param` (`thing_code`, `auto_manu_confirm_state`, `intelligent_manu_state`) VALUES ('2494', '1', '0');
INSERT INTO `tb_filterpress_feedover_param` (`thing_code`, `auto_manu_confirm_state`, `intelligent_manu_state`) VALUES ('2495', '1', '0');
INSERT INTO `tb_filterpress_feedover_param` (`thing_code`, `auto_manu_confirm_state`, `intelligent_manu_state`) VALUES ('2496A', '1', '0');
