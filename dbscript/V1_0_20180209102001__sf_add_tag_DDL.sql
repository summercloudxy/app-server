SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tb_thing_tag`
-- ----------------------------
CREATE TABLE `tb_thing_tag` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `code` varchar(32) DEFAULT '',
  `tag_name` varchar(32) DEFAULT NULL,
  `parent_id` int(16) DEFAULT NULL,
  `code_path` varchar(64) DEFAULT NULL COMMENT '父类路径/id/id/id',
  `thing_tag_group_id` int(32) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `comments` varchar(100) DEFAULT NULL,
  `operator` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_metric_tag` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `code` varchar(32) DEFAULT '',
  `tag_name` varchar(32) DEFAULT NULL,
  `parent_id` int(16) DEFAULT NULL,
  `code_path` varchar(64) DEFAULT NULL COMMENT '父类路径/id/id/id',
  `metric_tag_group_id` varchar(32) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `comments` varchar(100) DEFAULT NULL,
  `operator` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_thing_tag_group` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(32) DEFAULT '',
  `tag_group_name` varchar(60) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_metric_tag_group` (
  `id` int(16) NOT NULL AUTO_INCREMENT,
  `code` varchar(32) DEFAULT '',
  `tag_group_name` varchar(32) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `rel_thingtag_thing` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(30) DEFAULT NULL,
  `thing_tag_code` varchar(32) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `rel_metrictag_metric` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `metric_code` varchar(30) DEFAULT NULL,
  `metric_tag_code` varchar(32) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
