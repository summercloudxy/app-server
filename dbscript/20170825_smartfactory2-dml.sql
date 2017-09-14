/*
SQLyog Ultimate v12.08 (64 bit)
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

/*Data for the table `rel_thing_metric_label` */

insert  into `rel_thing_metric_label`(`id`,`thing_code`,`metric_code`,`label_path`) values (4,'1111','LC0','CW_1.CW_1.1111/B/LOCAL/0');

/*Data for the table `rel_thingtype_metric` */

insert  into `rel_thingtype_metric`(`id`,`thingtype_code`,`metric_code`) values (4,'DD','CR0'),(5,'DD','CR1'),(6,'DD','CR2'),(7,'D','LC0');

/*Data for the table `tb_metric` */

insert  into `tb_metric`(`id`,`metric_category_code`,`metric_name`,`metric_code`,`metric_type1_code`,`metric_type2_code`,`metric_type3_code`,`value_type`,`value_unit`) values (1,'SIG','电流0','CR0','PR','CUR',NULL,NULL,NULL),(2,'SIG','电流1','CR1','PR','CUR',NULL,NULL,NULL),(3,'SIG','集控按钮0','LC0','SS','LOC',NULL,NULL,NULL),(4,'SIG','电流2','CR2','PR','CUR',NULL,NULL,NULL);

/*Data for the table `tb_metrictype` */

insert  into `tb_metrictype`(`id`,`metrictype_name`,`metrictype_code`) values (2,'参数类','PR'),(3,'参数设定类','PS'),(4,'固有属性','ST'),(5,'特殊功能类','SF'),(6,'状态类','S'),(7,'状态设定类','SS'),(8,'故障类','F'),(9,'故障旁路类','FI'),(10,'电流','CUR'),(11,'频率','FRQ'),(12,'压力','PRS'),(13,'就地集控按钮','LOC');

/*Data for the table `tb_thing` */

insert  into `tb_thing`(`id`,`parent_thing_id`,`thing_code`,`thing_category_code`,`thing_name`,`thing_type1_code`,`thing_type2_code`,`thing_type3_code`) values (1,NULL,'701','DVC','煤带输送机','TRD',NULL,NULL),(2,1,'701.YB.PD-1','DVC','皮带秤','PDC',NULL,NULL),(3,NULL,'1301','DVC','2#皮带机','PSJ',NULL,NULL),(4,NULL,'XG.XG.1303','DVC','测试设备','D',NULL,NULL),(5,NULL,'CW_1.CW_1.1111','DVC','asdfasdf','D',NULL,NULL);

/*Data for the table `tb_thingtype` */

insert  into `tb_thingtype`(`id`,`thingtype_name`,`thingtype_code`,`parent_thingtype_id`) values (2,'皮带机','PD',4),(3,'设备','D',NULL),(4,'输送设备','TRD',3),(5,'部件','PRT',NULL),(6,'皮带秤','PDC',5),(7,'破碎设备','PSD',3),(8,'破碎机','PSJ',7);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
