/*
Navicat MySQL Data Transfer

Source Server         : 5.34
Source Server Version : 50635
Source Host           : 192.168.5.34:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50635
File Encoding         : 65001

Date: 2018-05-09 17:36:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_platform_client
-- ----------------------------
CREATE TABLE `tb_platform_client` (
  `id` int(11) NOT NULL,
  `client_name` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
