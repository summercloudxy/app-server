SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE `tb_filterpress_plate_statistics` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `is_day_shift` bit(1) DEFAULT NULL,
  `term` int(2) DEFAULT NULL,
  `total_plate_count` int(8) DEFAULT NULL,
  `team` int(2) DEFAULT NULL,
  `datetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;