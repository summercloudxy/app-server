
ALTER  TABLE  `tb_alert_rule`  ADD  INDEX alert_id (  `thing_code`,`metric_code`,`alert_level`  );
ALTER TABLE  `rel_thing_metric_label` ADD INDEX thing_code(`thing_code`,`metric_code`);

-- ----------------------------
-- Table structure for tb_alert_param_configuration_list
-- ----------------------------
CREATE TABLE `tb_alert_param_configuration_list` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(30) DEFAULT NULL,
  `metric_code` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `thing_code` (`thing_code`,`metric_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for tb_density_control_thing_list
-- ----------------------------
DROP TABLE IF EXISTS `tb_density_control_thing_list`;
CREATE TABLE `tb_density_control_thing_list` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(30) DEFAULT NULL,
  `module` varchar(20) DEFAULT NULL COMMENT '所属模块  re 再洗  main 主选  sg 浅槽',
  `parent_thing_code` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_density_control_thing_list
-- ----------------------------
INSERT INTO `tb_density_control_thing_list` VALUES ('1', '2386', 're', '2412');
INSERT INTO `tb_density_control_thing_list` VALUES ('2', '2387', 're', '2413');
INSERT INTO `tb_density_control_thing_list` VALUES ('3', '2388', 're', '2414');
INSERT INTO `tb_density_control_thing_list` VALUES ('4', '2412', 're', null);
INSERT INTO `tb_density_control_thing_list` VALUES ('5', '2413', 're', null);
INSERT INTO `tb_density_control_thing_list` VALUES ('6', '2414', 're', null);


-- ----------------------------
-- Table structure for tb_density_control_notify_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_density_control_notify_info`;
CREATE TABLE `tb_density_control_notify_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `notify_thing_code` varchar(30) DEFAULT NULL,
  `valve_opening_thing_code` varchar(30) DEFAULT NULL,
  `density_thing_code` varchar(30) DEFAULT NULL,
  `level_thing_code` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_density_control_notify_info
-- ----------------------------
INSERT INTO `tb_density_control_notify_info` VALUES ('1', '2412', '2386.BS-1', '2412.FL-1', '2412');
INSERT INTO `tb_density_control_notify_info` VALUES ('2', '2413', '2387.BS-1', '2413.FL-1', '2413');
INSERT INTO `tb_density_control_notify_info` VALUES ('3', '2414', '2388.BS-1', '2414.FL-1', '2414');
INSERT INTO `tb_density_control_notify_info` VALUES ('4', '2321', '2321', '2321', '2321');
INSERT INTO `tb_density_control_notify_info` VALUES ('5', '2358', '2339.BS-1', '2339', '2358');
INSERT INTO `tb_density_control_notify_info` VALUES ('6', '2359', '2340.BS-1', '2340', '2359');
INSERT INTO `tb_density_control_notify_info` VALUES ('7', '2360', '2341.BS-1', '2341', '2360');
-- ----------------------------
-- Table structure for tb_density_control_interval
-- ----------------------------
DROP TABLE IF EXISTS `tb_density_control_interval`;
CREATE TABLE `tb_density_control_interval` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(30) DEFAULT NULL,
  `notify_type` varchar(20) DEFAULT NULL,
  `interval` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_density_control_interval
-- ----------------------------
INSERT INTO `tb_density_control_interval` VALUES ('1', '2412', 'ADDING_MEDIUM', '1');
INSERT INTO `tb_density_control_interval` VALUES ('2', '2413', 'ADDING_MEDIUM', '1');
INSERT INTO `tb_density_control_interval` VALUES ('3', '2414', 'ADDING_MEDIUM', '1');
INSERT INTO `tb_density_control_interval` VALUES ('4', '2412', 'BACK_FLOW', '1');
INSERT INTO `tb_density_control_interval` VALUES ('5', '2413', 'BACK_FLOW', '1');
INSERT INTO `tb_density_control_interval` VALUES ('6', '2414', 'BACK_FLOW', '1');
INSERT INTO `tb_density_control_interval` VALUES ('7', '2358', 'ADDING_MEDIUM', '1');
INSERT INTO `tb_density_control_interval` VALUES ('8', '2359', 'ADDING_MEDIUM', '1');
INSERT INTO `tb_density_control_interval` VALUES ('9', '2360', 'ADDING_MEDIUM', '1');
INSERT INTO `tb_density_control_interval` VALUES ('10', '2358', 'NOTIFY_ALERT', '1');
INSERT INTO `tb_density_control_interval` VALUES ('11', '2359', 'NOTIFY_ALERT', '1');
INSERT INTO `tb_density_control_interval` VALUES ('12', '2360', 'NOTIFY_ALERT', '1');
INSERT INTO `tb_density_control_interval` VALUES ('13', '2321', 'ADDING_MEDIUM', '1');
INSERT INTO `tb_density_control_interval` VALUES ('14', '2321', 'NOTIFY_ALERT', '1');

INSERT INTO `tb_metric` (`metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('SIG', '密度波动值', 'DENSITY_FLU', 'PS', NULL, NULL, 'FLT', NULL);
INSERT INTO `tb_metric` (`metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('SIG', '停车前高液位设定', 'PRE_STOP_DENSITY_HIGH_SET', 'PS', NULL, NULL, 'FLT', NULL);
INSERT INTO `tb_metric` (`metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('SIG', '低开度设定', 'VALVE_LOW_SET', 'PS', NULL, NULL, 'FLT', NULL);
INSERT INTO `tb_metric` (`metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('SIG', '高开度设定', 'VALVE_HIGH_SET', 'PS', NULL, NULL, 'FLT', NULL);
INSERT INTO `tb_metric` (`metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('SIG', '停车前开度设定', 'PRE_STOP_VALVE_SET', 'PS', NULL, NULL, 'FLT', NULL);
INSERT INTO `tb_metric` (`metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('SIG', '提示加介', 'DENSITY_CONTROL_ADDMEDIUM', 'S', NULL, NULL, 'BOO', NULL);
INSERT INTO `tb_metric` (`metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('SIG', '提示高密度高液位报警', 'DENSITY_CONTROL_ALERT', 'S', NULL, NULL, 'BOO', NULL);
INSERT INTO `tb_metric` (`metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('SIG', '提示回流', 'DENSTIY_CONTROL_BACKFLOW', 'S', NULL, NULL, 'BOO', NULL);
INSERT INTO `tb_metric` (`metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('SIG', '模式选择', 'MODE_CHOOSE', 'SS', NULL, NULL, 'BOO', NULL);