/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-05-09 17:24:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_user_login
-- ----------------------------
CREATE TABLE `tb_user_login` (
  `user_uuid` varchar(50) NOT NULL,
  `platclient_id` int(11) DEFAULT NULL COMMENT '终端:1-web;2-pad;3-手机',
  `token` varchar(300) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `ip_address` varchar(16) DEFAULT NULL COMMENT 'pad端ip地址',
  `mac_address` varchar(32) DEFAULT NULL COMMENT 'mac地址'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
