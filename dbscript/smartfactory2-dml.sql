-- ----------------------------
-- Records of rel_label_metric
-- ----------------------------
INSERT INTO `rel_label_metric` VALUES ('1', 'CR0', '/PR/CURRENT/0');
INSERT INTO `rel_label_metric` VALUES ('2', 'CR1', '/PR/CURRENT/1');
INSERT INTO `rel_label_metric` VALUES ('3', 'LC0', '/SS/LOCAL/0');


-- ----------------------------
-- Records of rel_thingtype_metric
-- ----------------------------
INSERT INTO `rel_thingtype_metric` VALUES ('1', 'PSJ', 'CR0');
INSERT INTO `rel_thingtype_metric` VALUES ('2', 'PSJ', 'CR1');
INSERT INTO `rel_thingtype_metric` VALUES ('3', 'PSJ', 'LC0');

-- ----------------------------
-- Records of tb_metric
-- ----------------------------
INSERT INTO `tb_metric` VALUES ('1', 'SIG', '电流0', 'CR0', 'PR', 'CUR', null, null, null);
INSERT INTO `tb_metric` VALUES ('2', 'SIG', '电流1', 'CR1', 'PR', 'CUR', null, null, null);
INSERT INTO `tb_metric` VALUES ('3', 'SIG', '集控按钮0', 'LC0', 'SS', 'LOC', null, null, null);

-- ----------------------------
-- Records of tb_metrictype
-- ----------------------------
INSERT INTO `tb_metrictype` VALUES ('2', '参数类', 'PR');
INSERT INTO `tb_metrictype` VALUES ('3', '参数设定类', 'PS');
INSERT INTO `tb_metrictype` VALUES ('4', '固有属性', 'ST');
INSERT INTO `tb_metrictype` VALUES ('5', '特殊功能类', 'SF');
INSERT INTO `tb_metrictype` VALUES ('6', '状态类', 'S');
INSERT INTO `tb_metrictype` VALUES ('7', '状态设定类', 'SS');
INSERT INTO `tb_metrictype` VALUES ('8', '故障类', 'F');
INSERT INTO `tb_metrictype` VALUES ('9', '故障旁路类', 'FI');
INSERT INTO `tb_metrictype` VALUES ('10', '电流', 'CUR');
INSERT INTO `tb_metrictype` VALUES ('11', '频率', 'FRQ');
INSERT INTO `tb_metrictype` VALUES ('12', '压力', 'PRS');
INSERT INTO `tb_metrictype` VALUES ('13', '就地集控按钮', 'LOC');

-- ----------------------------
-- Records of tb_thing
-- ----------------------------
INSERT INTO `tb_thing` VALUES ('1', null, '701', 'DVC', '煤带输送机', 'TRD', null, null);
INSERT INTO `tb_thing` VALUES ('2', '1', '701.YB.PD-1', 'DVC', '皮带秤', 'PDC', null, null);
INSERT INTO `tb_thing` VALUES ('3', null, '1301', 'DVC', '2#皮带机', 'PSJ', null, null);

-- ----------------------------
-- Records of tb_thingtype
-- ----------------------------
INSERT INTO `tb_thingtype` VALUES ('2', '皮带机', 'PD', '4');
INSERT INTO `tb_thingtype` VALUES ('3', '设备', 'D', null);
INSERT INTO `tb_thingtype` VALUES ('4', '输送设备', 'TRD', '3');
INSERT INTO `tb_thingtype` VALUES ('5', '部件', 'PRT', null);
INSERT INTO `tb_thingtype` VALUES ('6', '皮带秤', 'PDC', '5');
INSERT INTO `tb_thingtype` VALUES ('7', '破碎设备', 'PSD', '3');
INSERT INTO `tb_thingtype` VALUES ('8', '破碎机', 'PSJ', '7');
