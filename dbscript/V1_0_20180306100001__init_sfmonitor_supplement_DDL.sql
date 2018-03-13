SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tb_sfmon`
-- ----------------------------
CREATE TABLE `tb_sfmon_style` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `comments` varchar(100) DEFAULT NULL,
  `preview_image_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `rel_sfmon_metrictag_style` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `metric_tag_code` varchar(30) NOT NULL,
  `style_code` varchar(30) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `comment` varchar(100) DEFAULT NULL,
  `editor` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_sfmon_equipmonitor_config` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(20) DEFAULT NULL,
  `metric_tag_name` varchar(30) DEFAULT NULL,
  `key` varchar(20) DEFAULT NULL,
  `value` varchar(30) DEFAULT NULL,
  `selected` bit(1) DEFAULT NULL,
  `model` int(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='model:0为查看，\r\nmodel:1为操作';


CREATE TABLE `tb_sfmon_equipmonitor_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(20) DEFAULT NULL,
  `thing_name` varchar(30) DEFAULT NULL,
  `config_progress` varchar(10) DEFAULT NULL,
  `editor` varchar(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `comment` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `tb_sfmon_signal_wrapper_rule` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `signal_wrapper_name` varchar(30) DEFAULT NULL,
  `zone_code` varchar(30) DEFAULT NULL,
  `is_allmatch` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

