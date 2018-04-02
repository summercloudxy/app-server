INSERT INTO `tb_card` (`id`, `card_style_id`, `card_param_value_json`) VALUES ('110', '3', '{\"tc\":\"703\",\"mc\":\"ASSAY_DATA\",\"type\":\"sample\",\"showbit\":7}');
INSERT INTO `tb_card` (`id`, `card_style_id`, `card_param_value_json`) VALUES ('111', '3', '{\"tc\":\"703生产洗混煤平均\",\"mc\":\"ASSAY_DATA\",\"type\":\"target\",\"showbit\":7,\"ignoretc\":true,\"isAvg\":true}');

INSERT INTO `rel_qualityandquantity_area_card` (`area_id`, `card_id`, `sequence`, `description`, `area_title`) VALUES ('37', '110', '1', '混煤质量综合化验', '混煤综合数据');
INSERT INTO `rel_qualityandquantity_area_card` (`area_id`, `card_id`, `sequence`, `description`, `area_title`) VALUES ('37', '111', '2', '混煤质量综合化验', '混煤综合数据');

DROP TABLE IF EXISTS `tb_production_inspect`;
CREATE TABLE `tb_production_inspect` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time` datetime DEFAULT NULL COMMENT '化验时间 ',
  `target` varchar(10) DEFAULT NULL COMMENT '化验项目 ',
  `sample` varchar(15) DEFAULT NULL COMMENT '采样地点 ',
  `-1.45` float(10,4) DEFAULT NULL COMMENT '灰分(%), 2位小数 ',
  `1.45-1.8` float(10,4) DEFAULT NULL COMMENT '水份(%), 1位小数 ',
  `+1.8` float(10,4) DEFAULT NULL COMMENT '硫份(%), 2位小数 ',
  `+1.45` float(10,4) DEFAULT NULL COMMENT '发热量, 整数(kal/kg) ',
  `-1.8` float(10,4) DEFAULT NULL COMMENT '系统，1：一期， 2：二期',
  `+50mm` float(10,4) DEFAULT NULL,
  `-50mm` float(10,4) DEFAULT NULL,
  `avg_density` float(10,4) DEFAULT NULL COMMENT '分选密度',
  `system` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10248 DEFAULT CHARSET=utf8;
