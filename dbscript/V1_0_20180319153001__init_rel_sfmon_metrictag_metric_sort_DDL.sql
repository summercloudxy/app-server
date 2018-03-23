SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE `rel_sfmon_metrictag_metric_sort` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `metric_tag_name` varchar(300) DEFAULT NULL,
  `metric_code` varchar(100) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `direction` int(2) DEFAULT NULL,
  `rule` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='rule描述：\r\n1:不同部件所有点放到一个信号包中\r\n2:只针对远程阀门，信号包中包含不同阀门thingCode和信号点\r\n3:信号保重需要显示设备名称，一个信号包中只包含一个部件和信号点\r\n';