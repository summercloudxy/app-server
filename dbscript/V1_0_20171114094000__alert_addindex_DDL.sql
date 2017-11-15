
ALTER  TABLE  `tb_alert_message`  ADD  INDEX alert_id (  `alert_id`  );

alter table `tb_alert_data` drop index thing_code ;
ALTER  TABLE  `tb_alert_data`  ADD  INDEX thing_code ( `id`, `thing_code`,  `alert_type` ,`alert_stage` ,`alert_level` ,`alert_datetime` );

-- ----------------------------
-- Table structure for tb_alert_data_backup
-- ----------------------------
CREATE TABLE `tb_alert_data_backup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thing_code` varchar(30) DEFAULT NULL,
  `metric_code` varchar(10) DEFAULT NULL,
  `alert_source` tinyint(2) DEFAULT NULL COMMENT '报警触发方式：0.系统自动触发,1.人工生成',
  `alert_type` tinyint(2) DEFAULT NULL COMMENT '报警类型：0.故障 1.参数 2.保护 3.人工',
  `alert_level` tinyint(4) DEFAULT NULL COMMENT '报警等级（10,20,30）',
  `alert_datetime` datetime DEFAULT NULL,
  `alert_info` varchar(30) DEFAULT NULL COMMENT '报警信息',
  `reporter` varchar(20) DEFAULT NULL COMMENT '报警发起人 ',
  `alert_stage` varchar(20) DEFAULT NULL COMMENT '报警阶段："NOT_VERIFY"; 未核实、已发起  "VERIFIED";  已核实、已评级    "UNTREATED"; 未处理"REQUEST_REPAIR"; 申请维修    "REPAIRING";维修中   "REPAIRED";维修结束    "RELEASE"; 报警解除',
  `is_repair` tinyint(1) DEFAULT NULL COMMENT '发起维修 0.否 1.是',
  `repair_confirm_user` varchar(20) DEFAULT NULL COMMENT '维修确认人',
  `repair_start_time` datetime DEFAULT NULL COMMENT '维修开始时间',
  `repair_end_time` datetime DEFAULT NULL COMMENT '维修结束时间',
  `is_manual_intervention` tinyint(1) DEFAULT NULL COMMENT '是否人工干预：0.否 1.是',
  `feedback_image` varchar(500) DEFAULT NULL COMMENT '反馈图片',
  `feedback_video` varchar(255) DEFAULT NULL COMMENT '反馈视频',
  `scene_confirm_user` varchar(20) DEFAULT NULL COMMENT '现场确认人',
  `scene_confirm_time` datetime DEFAULT NULL COMMENT '现场确认时间',
  `scene_confirm_state` tinyint(2) DEFAULT NULL COMMENT '现场确认结果 0.未解除 1.已解除',
  `is_recovery` tinyint(1) DEFAULT NULL COMMENT '恢复到正常范围 0.未恢复 1.已恢复',
  `param_value` float(11,2) DEFAULT NULL,
  `param_lower` float(11,2) DEFAULT NULL,
  `param_upper` float(11,2) DEFAULT NULL,
  `last_update_time` datetime DEFAULT NULL COMMENT '参数类报警上次查询更新时间',
  `verify_time` datetime DEFAULT NULL COMMENT '已核实时间',
  `post_worker` varchar(20) DEFAULT NULL COMMENT '最后操作的岗位工',
  `dispatcher` varchar(20) CHARACTER SET utf32 DEFAULT NULL COMMENT '最后操作的调度员',
  `release_time` datetime DEFAULT NULL COMMENT '报警解除时间',
  PRIMARY KEY (`id`),
  KEY `thing_code` (`thing_code`,`alert_type`,`alert_stage`,`alert_level`,`alert_datetime`)
) ENGINE=MyISAM AUTO_INCREMENT=72471 DEFAULT CHARSET=utf8;
