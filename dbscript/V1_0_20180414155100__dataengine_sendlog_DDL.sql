CREATE TABLE `tb_dae_send_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_uuid` varchar(50) DEFAULT NULL,
  `send_time` datetime DEFAULT NULL COMMENT '服务器时间',
  `thing_code` varchar(30) DEFAULT NULL,
  `metric_code` varchar(30) DEFAULT NULL,
  `value` varchar(50) DEFAULT NULL COMMENT '数据值',
  `dmtime` datetime DEFAULT NULL COMMENT 'datamodel中的时间戳',
  PRIMARY KEY (`id`),
  KEY `send_time` (`send_time`),
  KEY `thing_code` (`thing_code`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8
