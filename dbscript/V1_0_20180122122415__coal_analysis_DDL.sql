CREATE TABLE `tb_coal_analysis_density_flow_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `coal_target` varchar(10) DEFAULT NULL COMMENT '化验项目',
  `system` tinyint(2) DEFAULT NULL,
  `thing_code` varchar(32) DEFAULT NULL,
  `density_code` varchar(10) DEFAULT NULL COMMENT '分选密度',
  `flow_code` varchar(10) DEFAULT NULL COMMENT '顶水流量',
  `run_density_threshold` double(5,2) DEFAULT NULL,
  `run_flow_threshold` double(5,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_coal_analysis_density_flow_info
-- ----------------------------
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('1', '浅槽溢流煤', '1', '1307', 'MD', '', '1.40', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('2', '浅槽溢流煤', '1', '1308', 'MD', '', '1.40', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('3', '浅槽溢流煤', '2', '2307', 'MD', '', '1.40', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('4', '浅槽溢流煤', '2', '2308', 'MD', '', '1.40', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('5', '块矸石', '1', '1307', 'MD', '', '1.40', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('6', '块矸石', '1', '1308', 'MD', '', '1.40', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('7', '块矸石', '2', '2307', 'MD', '', '1.40', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('8', '块矸石', '2', '2308', 'MD', '', '1.40', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('9', '末矸石', '1', '1339', 'MD', '', '1.40', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('10', '末矸石', '1', '1340', 'MD', '', '1.40', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('11', '末矸石', '1', '1341', 'MD', '', '1.40', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('12', '末矸石', '2', '2339', 'MD', '', '1.40', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('13', '末矸石', '2', '2340', 'MD', '', '1.40', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('14', '末矸石', '2', '2341', 'MD', '', '1.40', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('15', '末中煤', '1', '1386', 'MD', '', '1.30', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('16', '末中煤', '1', '1387', 'MD', '', '1.30', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('17', '末中煤', '1', '1388', 'MD', '', '1.30', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('18', '末中煤', '2', '2386', 'MD', '', '1.30', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('19', '末中煤', '2', '2387', 'MD', '', '1.30', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('20', '末中煤', '2', '2388', 'MD', '', '1.30', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('21', '末精煤', '1', '1386', 'MD', '', '1.30', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('22', '末精煤', '1', '1387', 'MD', '', '1.30', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('23', '末精煤', '1', '1388', 'MD', '', '1.30', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('24', '末精煤', '2', '2386', 'MD', '', '1.30', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('25', '末精煤', '2', '2387', 'MD', '', '1.30', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('26', '末精煤', '2', '2388', 'MD', '', '1.30', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('27', '551生产精煤', '1', '1386', 'MD', '', '1.30', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('28', '551生产精煤', '1', '1387', 'MD', '', '1.30', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('29', '551生产精煤', '1', '1388', 'MD', '', '1.30', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('30', '551生产精煤', '2', '2386', 'MD', '', '1.30', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('31', '551生产精煤', '2', '2387', 'MD', '', '1.30', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('32', '551生产精煤', '2', '2388', 'MD', '', '1.30', null);
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('33', 'TCS精矿', '1', '1857-1', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('34', 'TCS精矿', '1', '1857-2', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('35', 'TCS精矿', '1', '1857-3', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('36', 'TCS精矿', '1', '1858-1', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('37', 'TCS精矿', '1', '1858-2', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('38', 'TCS精矿', '1', '1858-3', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('39', 'TCS精矿', '1', '1859-1', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('40', 'TCS精矿', '1', '1859-2', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('41', 'TCS精矿', '1', '1859-3', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('42', 'TCS精矿', '2', '2857-1', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('43', 'TCS精矿', '2', '2857-2', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('44', 'TCS精矿', '2', '2857-3', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('45', 'TCS精矿', '2', '2858-1', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('46', 'TCS精矿', '2', '2858-2', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('47', 'TCS精矿', '2', '2858-3', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('48', 'TCS精矿', '2', '2859-1', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('49', 'TCS精矿', '2', '2859-2', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('50', 'TCS精矿', '2', '2859-3', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('51', 'TCS尾矿', '1', '1857-1', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('52', 'TCS尾矿', '1', '1857-2', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('53', 'TCS尾矿', '1', '1857-3', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('54', 'TCS尾矿', '1', '1858-1', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('55', 'TCS尾矿', '1', '1858-2', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('56', 'TCS尾矿', '1', '1858-3', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('57', 'TCS尾矿', '1', '1859-1', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('58', 'TCS尾矿', '1', '1859-2', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('59', 'TCS尾矿', '1', '1859-3', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('60', 'TCS尾矿', '2', '2857-1', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('61', 'TCS尾矿', '2', '2857-2', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('62', 'TCS尾矿', '2', '2857-3', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('63', 'TCS尾矿', '2', '2858-1', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('64', 'TCS尾矿', '2', '2858-2', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('65', 'TCS尾矿', '2', '2858-3', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('66', 'TCS尾矿', '2', '2859-1', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('67', 'TCS尾矿', '2', '2859-2', 'MD1', 'DSFL', null, '10.00');
INSERT INTO `tb_coal_analysis_density_flow_info` VALUES ('68', 'TCS尾矿', '2', '2859-3', 'MD1', 'DSFL', null, '10.00');


CREATE TABLE `tb_coal_analysis` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time` datetime DEFAULT NULL COMMENT '化验时间 ',
  `target` varchar(10) DEFAULT NULL COMMENT '化验项目 ',
  `sample` varchar(15) DEFAULT NULL COMMENT '采样地点 ',
  `aad` float(10,4) DEFAULT NULL COMMENT '灰分(%), 2位小数 ',
  `mt` float(10,4) DEFAULT NULL COMMENT '水份(%), 1位小数 ',
  `stad` float(10,4) DEFAULT NULL COMMENT '硫份(%), 2位小数 ',
  `qnetar` int(11) DEFAULT NULL COMMENT '发热量, 整数(kal/kg) ',
  `system` int(2) DEFAULT NULL COMMENT '系统，1：一期， 2：二期',
  `avg_density` float(10,4) DEFAULT NULL COMMENT '分选密度',
  `avg_flow` float(10,4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_coal_analysis_density_flow_value` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `analysis_id` int(10) DEFAULT NULL,
  `thing_code` varchar(32) DEFAULT NULL,
  `density` float(10,3) DEFAULT NULL COMMENT '分选密度',
  `flow` float(10,3) DEFAULT NULL COMMENT '顶水流量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `rel_historydata_whitelist` VALUES ('1307', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1308', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2307', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2308', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1307', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1308', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2307', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2308', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1339', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1340', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1341', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2339', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2340', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2341', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1386', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1387', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1388', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2386', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2387', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2388', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1386', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1387', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1388', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2386', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2387', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2388', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1386', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1387', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1388', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2386', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2387', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2388', 'MD', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1857-1', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1857-2', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1857-3', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1858-1', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1858-2', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1858-3', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1859-1', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1859-2', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1859-3', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2857-1', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2857-2', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2857-3', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2858-1', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2858-2', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2858-3', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2859-1', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2859-2', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2859-3', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1857-1', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1857-2', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1857-3', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1858-1', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1858-2', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1858-3', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1859-1', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1859-2', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1859-3', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2857-1', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2857-2', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2857-3', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2858-1', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2858-2', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2858-3', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2859-1', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2859-2', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2859-3', 'MD1', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1857-1', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1857-2', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1857-3', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1858-1', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1858-2', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1858-3', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1859-1', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1859-2', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1859-3', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2857-1', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2857-2', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2857-3', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2858-1', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2858-2', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2858-3', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2859-1', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2859-2', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2859-3', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1857-1', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1857-2', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1857-3', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1858-1', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1858-2', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1858-3', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1859-1', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1859-2', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1859-3', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2857-1', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2857-2', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2857-3', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2858-1', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2858-2', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2858-3', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2859-1', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2859-2', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2859-3', 'DSFL', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1301', 'COAL_CAP', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('1302', 'COAL_CAP', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2301', 'COAL_CAP', '1');
INSERT INTO `rel_historydata_whitelist` VALUES ('2302', 'COAL_CAP', '1');
