SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tb_sfmon`
-- ----------------------------
CREATE TABLE `tb_sfmon` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `sfmon_name` varchar(30) DEFAULT NULL,
  `user_id` int(10) DEFAULT NULL,
  `sort` float(20,10) DEFAULT NULL,
  `user_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `rel_sfmon_item` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `sfmon_id` int(10) DEFAULT NULL,
  `sort` float(20,0) DEFAULT NULL,
  `view_type_id` int(3) DEFAULT NULL,
  `thing_code` varchar(20) DEFAULT NULL,
  `metric_code` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sfmod_id` (`sfmon_id`) USING BTREE,
  KEY `thing_code` (`thing_code`) USING BTREE,
  KEY `metric_code` (`metric_code`) USING BTREE,
  KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;