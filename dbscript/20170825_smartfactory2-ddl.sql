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
  `thing_code` varchar(8) DEFAULT NULL,
  `metric_code` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
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
  `metric_type1_code` varchar(3) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '维度1的叶子节点',
  `metric_type2_code` varchar(3) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '维度2的叶子节点',
  `metric_type3_code` varchar(3) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '维度3的叶子节点',
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
