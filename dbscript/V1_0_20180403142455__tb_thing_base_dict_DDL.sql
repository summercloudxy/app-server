
CREATE TABLE `tb_thing_base_dict` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `parent_id` int(10) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `key` varchar(30) DEFAULT NULL,
  `value` varchar(50) DEFAULT NULL,
  `short_name` varchar(50) DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=385 DEFAULT CHARSET=utf8;
