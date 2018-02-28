SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tb_sfmon_displayzone`
-- ----------------------------
CREATE TABLE `tb_sfmon_displayzone` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `code` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `rel_sfmon_tag_displayzone` (
  `metric_tag_code` varchar(30) NOT NULL,
  `zone_id` int(10) NOT NULL,
  `code` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;