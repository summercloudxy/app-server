CREATE TABLE `rel_reportform_feedback_target` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `feedback_info_id` int(11) DEFAULT NULL COMMENT '反馈内容id',
  `term` tinyint(2) DEFAULT NULL COMMENT '期数',
  `target_type_id` int(11) DEFAULT NULL COMMENT '指标id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rel_reportform_feedback_target
-- ----------------------------
INSERT INTO `rel_reportform_feedback_target` VALUES ('1', '9', '1', '1');
INSERT INTO `rel_reportform_feedback_target` VALUES ('2', '12', '2', '1');
INSERT INTO `rel_reportform_feedback_target` VALUES ('3', '10', '1', '2');
INSERT INTO `rel_reportform_feedback_target` VALUES ('4', '13', '2', '2');
INSERT INTO `rel_reportform_feedback_target` VALUES ('5', '11', '1', '3');
INSERT INTO `rel_reportform_feedback_target` VALUES ('6', '12', '2', '3');
INSERT INTO `rel_reportform_feedback_target` VALUES ('7', '15', '1', '5');
INSERT INTO `rel_reportform_feedback_target` VALUES ('8', '16', '2', '5');
INSERT INTO `rel_reportform_feedback_target` VALUES ('9', '17', '1', '6');
INSERT INTO `rel_reportform_feedback_target` VALUES ('10', '18', '2', '6');
INSERT INTO `rel_reportform_feedback_target` VALUES ('11', '59', '0', '7');
INSERT INTO `rel_reportform_feedback_target` VALUES ('12', '60', '0', '8');
INSERT INTO `rel_reportform_feedback_target` VALUES ('13', '61', '0', '9');
INSERT INTO `rel_reportform_feedback_target` VALUES ('14', '62', '0', '10');
INSERT INTO `rel_reportform_feedback_target` VALUES ('15', '63', '0', '11');
INSERT INTO `rel_reportform_feedback_target` VALUES ('16', '19', '1', '12');
INSERT INTO `rel_reportform_feedback_target` VALUES ('17', '20', '2', '12');


CREATE TABLE `tb_report_form_influence_time` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `duty_start_time` datetime DEFAULT NULL COMMENT '班次开始时间',
  `influence_type` int(10) DEFAULT NULL COMMENT '影响时间类型',
  `class_duration` int(11) DEFAULT NULL COMMENT '当班时长',
  `month_duration` int(20) DEFAULT NULL COMMENT '月累积时长',
  `year_duration` int(30) DEFAULT NULL COMMENT '年累积时长',
  `term` tinyint(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;


CREATE TABLE `tb_report_form_influence_time_remarks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `duty_start_time` datetime DEFAULT NULL COMMENT '当班开始时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `factory_duty_leader` varchar(255) DEFAULT NULL COMMENT '厂值班领导',
  `checker` varchar(255) DEFAULT NULL COMMENT '审核人员',
  `dispatcher` varchar(255) DEFAULT NULL COMMENT '调度员',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;


CREATE TABLE `tb_report_form_influence_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `influence_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_report_form_influence_type
-- ----------------------------
INSERT INTO `tb_report_form_influence_type` VALUES ('1', '生产时间');
INSERT INTO `tb_report_form_influence_type` VALUES ('2', '检修时间');
INSERT INTO `tb_report_form_influence_type` VALUES ('3', '欠煤时间');
INSERT INTO `tb_report_form_influence_type` VALUES ('4', '欠车时间');
INSERT INTO `tb_report_form_influence_type` VALUES ('5', '欠水时间');
INSERT INTO `tb_report_form_influence_type` VALUES ('6', '限电时间');
INSERT INTO `tb_report_form_influence_type` VALUES ('7', '准备车间');
INSERT INTO `tb_report_form_influence_type` VALUES ('8', '洗煤车间');
INSERT INTO `tb_report_form_influence_type` VALUES ('9', '运销车间');
INSERT INTO `tb_report_form_influence_type` VALUES ('10', '煤质车间');
INSERT INTO `tb_report_form_influence_type` VALUES ('11', '矸运车间');
INSERT INTO `tb_report_form_influence_type` VALUES ('12', '芬雷公司');
INSERT INTO `tb_report_form_influence_type` VALUES ('13', '美腾公司');
INSERT INTO `tb_report_form_influence_type` VALUES ('14', '大矸影响');
INSERT INTO `tb_report_form_influence_type` VALUES ('15', '其他');


CREATE TABLE `tb_report_form_module_state_threshold` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(30) DEFAULT NULL COMMENT '设备号',
  `metric_code` varchar(255) DEFAULT NULL,
  `threshold_value` double(20,2) DEFAULT NULL COMMENT '启动阈值',
  `module` varchar(20) DEFAULT NULL COMMENT '系统  1.浅槽 2.主洗 3.再洗',
  `term` tinyint(2) DEFAULT NULL COMMENT '系统期数',
  `index` tinyint(4) DEFAULT NULL COMMENT '顺序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_report_form_module_state_threshold
-- ----------------------------
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('1', '1857-1.FT-2', 'FL', '10.00', 'tcs_857', '1', '1');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('2', '1857-2.FT-2', 'FL', '10.00', 'tcs_857', '1', '2');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('3', '1857-3.FT-2', 'FL', '10.00', 'tcs_857', '1', '3');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('4', '1858-1.FT-2', 'FL', '10.00', 'tcs_858', '1', '1');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('5', '1858-2.FT-2', 'FL', '10.00', 'tcs_858', '1', '2');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('6', '1858-3.FT-2', 'FL', '10.00', 'tcs_858', '1', '3');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('7', '1859-1.FT-2', 'FL', '10.00', 'tcs_859', '1', '1');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('8', '1859-2.FT-2', 'FL', '10.00', 'tcs_859', '1', '2');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('9', '1859-3.FT-2', 'FL', '10.00', 'tcs_859', '1', '3');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('10', '2857-1.FT-2', 'FL', '10.00', 'tcs_857', '2', '1');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('11', '2857-2.FT-2', 'FL', '10.00', 'tcs_857', '2', '2');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('12', '2857-3.FT-2', 'FL', '10.00', 'tcs_857', '2', '3');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('13', '2858-1.FT-2', 'FL', '10.00', 'tcs_858', '2', '1');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('14', '2858-2.FT-2', 'FL', '10.00', 'tcs_858', '2', '2');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('15', '2858-3.FT-2', 'FL', '10.00', 'tcs_858', '2', '3');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('16', '2859-1.FT-2', 'FL', '10.00', 'tcs_859', '2', '1');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('17', '2859-2.FT-2', 'FL', '10.00', 'tcs_859', '2', '2');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('18', '2859-3.FT-2', 'FL', '10.00', 'tcs_859', '2', '3');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('19', '1211', 'STATE', '2.00', 'coal_feeder', '1', '1');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('20', '1212', 'STATE', '2.00', 'coal_feeder', '1', '2');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('21', '1213', 'STATE', '2.00', 'coal_feeder', '1', '3');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('22', '1214', 'STATE', '2.00', 'coal_feeder', '1', '4');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('23', '2211', 'STATE', '2.00', 'coal_feeder', '2', '1');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('24', '2212', 'STATE', '2.00', 'coal_feeder', '2', '2');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('25', '2213', 'STATE', '2.00', 'coal_feeder', '2', '3');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('26', '2214', 'STATE', '2.00', 'coal_feeder', '2', '4');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('27', '1307', 'MD', '1.60', 'shallow_groove', '1', '1');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('28', '1308', 'MD', '1.60', 'shallow_groove', '1', '2');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('29', '2307', 'MD', '1.60', 'shallow_groove', '2', '1');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('30', '2308', 'MD', '1.60', 'shallow_groove', '2', '2');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('31', '1339', 'MD', '1.50', 'cyclone_main', '1', '1');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('32', '1340', 'MD', '1.50', 'cyclone_main', '1', '2');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('33', '1341', 'MD', '1.50', 'cyclone_main', '1', '3');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('34', '2339', 'MD', '1.50', 'cyclone_main', '2', '1');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('35', '2340', 'MD', '1.50', 'cyclone_main', '2', '2');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('36', '2341', 'MD', '1.50', 'cyclone_main', '2', '3');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('37', '1386', 'MD', '1.30', 'cyclone_re', '1', '1');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('38', '1387', 'MD', '1.30', 'cyclone_re', '1', '2');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('39', '1388', 'MD', '1.30', 'cyclone_re', '1', '3');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('40', '2386', 'MD', '1.30', 'cyclone_re', '2', '1');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('41', '2387', 'MD', '1.30', 'cyclone_re', '2', '2');
INSERT INTO `tb_report_form_module_state_threshold` VALUES ('42', '2388', 'MD', '1.30', 'cyclone_re', '2', '3');

CREATE TABLE `tb_report_form_output_store` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `duty_start_time` datetime DEFAULT NULL COMMENT '当班开始时间',
  `clened_coal` double(20,0) DEFAULT NULL COMMENT '气精煤',
  `washed_coal` double(20,0) DEFAULT NULL COMMENT '洗混煤',
  `slime` double(20,0) DEFAULT NULL COMMENT '煤泥',
  `washery_rejects` double(20,0) DEFAULT NULL COMMENT '洗矸',
  `raw_coal` double(20,0) DEFAULT NULL COMMENT '入洗原煤',
  `raw_coal_8` double(20,0) DEFAULT NULL COMMENT '8#原煤',
  `raw_coal_13` double(20,0) DEFAULT NULL COMMENT '13#原煤',
  `type` tinyint(2) DEFAULT NULL COMMENT '类型：1.生产  2.库存',
  `term` tinyint(2) DEFAULT NULL COMMENT '期数',
  `local_washed_coal` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `tb_report_form_system_start_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `duty_start_time` datetime DEFAULT NULL COMMENT '当班开始时间',
  `start_time` datetime DEFAULT NULL COMMENT '状态开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '状态结束时间',
  `coal_8_thing_code` varchar(30) DEFAULT NULL COMMENT '8#煤设备',
  `coal_13_thing_code` varchar(30) DEFAULT NULL COMMENT '13#煤设备',
  `blending_washing_type` varchar(20) DEFAULT NULL COMMENT '配洗类型',
  `production_description` tinyint(2) DEFAULT NULL COMMENT '生产描述 1.生产、2.检修、3.欠煤、4.欠车、5.欠水',
  `duration` int(30) DEFAULT NULL COMMENT '时长，分钟为单位',
  `heavy_medium_lump` varchar(10) DEFAULT NULL COMMENT '重介洗煤系统 块A/B',
  `heavy_medium_slack` varchar(10) DEFAULT NULL COMMENT '重介洗煤系统 末1/2/3',
  `tcs_857` varchar(10) DEFAULT NULL COMMENT '857运行情况：运行显示x857，不运行显示/',
  `tcs_858` varchar(10) DEFAULT NULL COMMENT '858运行情况：运行显示x858，不运行显示/',
  `tcs_859` varchar(10) DEFAULT NULL COMMENT '859运行情况：运行显示x859，不运行显示/',
  `reason` varchar(10) DEFAULT NULL COMMENT '原因',
  `term` tinyint(2) DEFAULT NULL COMMENT '一期/二期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_report_form_target` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `duty_start_time` datetime DEFAULT NULL,
  `target_type` int(10) DEFAULT NULL,
  `class_value` double(20,2) DEFAULT NULL,
  `month_value` double(20,2) DEFAULT NULL,
  `year_value` double(20,2) DEFAULT NULL,
  `term` tinyint(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_report_form_target_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `target_name` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_report_form_target_type
-- ----------------------------
INSERT INTO `tb_report_form_target_type` VALUES ('1', '介质消耗铲数-块煤');
INSERT INTO `tb_report_form_target_type` VALUES ('2', '介质消耗铲数-主洗');
INSERT INTO `tb_report_form_target_type` VALUES ('3', '介质消耗铲数-再洗');
INSERT INTO `tb_report_form_target_type` VALUES ('5', '压滤煤泥板数');
INSERT INTO `tb_report_form_target_type` VALUES ('6', '絮凝剂消耗袋数');
INSERT INTO `tb_report_form_target_type` VALUES ('7', '介质入库-车数');
INSERT INTO `tb_report_form_target_type` VALUES ('8', '介质入库-吨数');
INSERT INTO `tb_report_form_target_type` VALUES ('9', '大矸拉运-车数');
INSERT INTO `tb_report_form_target_type` VALUES ('10', '大矸拉运-吨数');
INSERT INTO `tb_report_form_target_type` VALUES ('11', '自用煤-吨数');
INSERT INTO `tb_report_form_target_type` VALUES ('12', '水表读数');
INSERT INTO `tb_report_form_target_type` VALUES ('13', '中水');

CREATE TABLE `tb_report_form_transport` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `coal_type` int(4) DEFAULT NULL COMMENT '品种 1.洗混煤 2.气精煤 3.煤泥',
  `transport_start_time` datetime DEFAULT NULL COMMENT '开装时间',
  `transport_end_time` datetime DEFAULT NULL COMMENT '装完时间',
  `destination` varchar(20) DEFAULT NULL COMMENT '到站',
  `batch` int(11) DEFAULT NULL COMMENT '批次',
  `carriage_number` int(11) DEFAULT NULL COMMENT '车厢数',
  `transport_volume` double(20,2) DEFAULT NULL COMMENT '运输量（吨）',
  `ratio` varchar(20) DEFAULT NULL COMMENT '装车配比',
  `station` varchar(20) DEFAULT NULL COMMENT '装车站点',
  `duration` int(11) DEFAULT NULL COMMENT '装车用时 分钟数',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `duty_start_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_report_form_transport_sale_statistics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `duty_start_time` datetime DEFAULT NULL COMMENT '当班开始时间',
  `coal_type` int(4) DEFAULT NULL COMMENT '煤的种类  1.洗混煤 2.气精煤 3.煤泥',
  `train_number` int(10) DEFAULT NULL COMMENT '车数',
  `coal_volunm` double(20,2) DEFAULT NULL COMMENT '吨数',
  `month_train_number` int(10) DEFAULT NULL COMMENT '月累积列数',
  `month_coal_volunm` double(20,2) DEFAULT NULL COMMENT '月累积吨数',
  `year_train_number` int(20) DEFAULT NULL COMMENT '年累积列数',
  `year_coal_volunm` double(30,2) DEFAULT NULL COMMENT '年累积吨数',
  `stastistics_type` int(4) DEFAULT NULL COMMENT '统计类型： 1.外运  2.地销',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
