SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE `tb_sfmonitor_signal` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(50) DEFAULT NULL,
  `metric_code` varchar(50) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;