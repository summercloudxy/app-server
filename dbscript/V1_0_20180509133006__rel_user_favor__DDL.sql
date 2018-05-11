/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : smartfactory2

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-05-09 13:48:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for rel_user_favor
-- ----------------------------
CREATE TABLE `rel_user_favor` (
  `user_uuid` varchar(50) DEFAULT NULL,
  `pad_module_id` int(20) DEFAULT NULL,
  `sort` float(20,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
