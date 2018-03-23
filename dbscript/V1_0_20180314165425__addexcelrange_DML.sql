DROP TABLE IF EXISTS `excel_range`;
CREATE TABLE `excel_range` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `system` tinyint(4) DEFAULT NULL,
  `start_x` tinyint(4) DEFAULT NULL,
  `start_y` tinyint(4) DEFAULT NULL,
  `row` tinyint(4) DEFAULT NULL,
  `read_mode` tinyint(4) DEFAULT NULL,
  `target_gap` tinyint(4) DEFAULT NULL,
  `time_gap` tinyint(4) DEFAULT NULL,
  `device_code_gap` tinyint(4) DEFAULT NULL,
  `aad_gap` tinyint(4) DEFAULT NULL,
  `mt_gap` tinyint(4) DEFAULT NULL,
  `stad_gap` tinyint(4) DEFAULT NULL,
  `qar_gap` tinyint(4) DEFAULT NULL,
  `state` tinyint(4) DEFAULT NULL,
  `sequence` int(4) DEFAULT NULL,
  `excel_type` varchar(10) DEFAULT NULL,
  `-1.45_gap` tinyint(4) DEFAULT NULL,
  `1.45-1.8_gap` tinyint(4) DEFAULT NULL,
  `+1.8_gap` tinyint(4) DEFAULT NULL,
  `+1.45_gap` tinyint(4) DEFAULT NULL,
  `-1.8_gap` tinyint(4) DEFAULT NULL,
  `+50mm_gap` tinyint(4) DEFAULT NULL,
  `-50mm_gap` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of excel_range
-- ----------------------------
INSERT INTO `excel_range` VALUES ('1', 'TCS精矿', '1', '1', '28', '6', '1', null, '3', '0', '5', '7', null, null, '1', '5', '化验班报', null, null, null, null, null, null, null);
INSERT INTO `excel_range` VALUES ('2', 'TCS精矿', '2', '43', '28', '6', '1', null, '3', '0', '5', '7', null, null, '1', '15', '化验班报', null, null, null, null, null, null, null);
INSERT INTO `excel_range` VALUES ('3', 'TCS尾矿', '1', '10', '28', '6', '1', null, '3', '0', '5', '7', null, null, '1', '6', '化验班报', null, null, null, null, null, null, null);
INSERT INTO `excel_range` VALUES ('4', 'TCS尾矿', '2', '52', '28', '6', '1', null, '3', '0', '5', '7', null, null, '1', '16', '化验班报', null, null, null, null, null, null, null);
INSERT INTO `excel_range` VALUES ('5', '入洗原煤', '1', '5', '4', '9', '1', null, '0', '2', '5', '7', '9', '11', '1', '1', '化验班报', null, null, null, null, null, null, null);
INSERT INTO `excel_range` VALUES ('6', '入洗原煤', '2', '47', '4', '9', '1', null, '0', '2', '5', '7', '9', '11', '1', '11', '化验班报', null, null, null, null, null, null, null);
INSERT INTO `excel_range` VALUES ('7', '551生产精煤', '1', '21', '4', '14', '3', null, '0', null, '2', '4', '6', '8', '1', '7', '化验班报', null, null, null, null, null, null, null);
INSERT INTO `excel_range` VALUES ('8', '551生产精煤', '2', '32', '4', '14', '3', null, '0', null, '2', '4', '6', '8', '1', '9', '化验班报', null, null, null, null, null, null, null);
INSERT INTO `excel_range` VALUES ('9', '552生产洗混煤', '1', '21', '20', '14', '3', null, '0', null, '2', '4', '6', '8', '1', '8', '化验班报', null, null, null, null, null, null, null);
INSERT INTO `excel_range` VALUES ('10', '552生产洗混煤', '2', '32', '20', '14', '3', null, '0', null, '2', '4', '6', '8', '1', '10', '化验班报', null, null, null, null, null, null, null);
INSERT INTO `excel_range` VALUES ('11', '浅槽至末矸区域', '1', '1', '14', '6', '2', '0', '4', '6', '9', '11', '13', '15', '1', '2', '化验班报', null, null, null, null, null, null, null);
INSERT INTO `excel_range` VALUES ('12', '浅槽至末矸区域', '2', '43', '14', '6', '2', '0', '4', '6', '9', '11', '13', '15', '1', '12', '化验班报', null, null, null, null, null, null, null);
INSERT INTO `excel_range` VALUES ('13', '块原至末精区域', '1', '1', '21', '5', '2', '0', '4', '6', '9', null, null, null, '1', '3', '化验班报', null, null, null, null, null, null, null);
INSERT INTO `excel_range` VALUES ('14', '块原至末精区域', '1', '1', '21', '5', '2', '0', '11', '13', '16', null, null, null, '1', '4', '化验班报', null, null, null, null, null, null, null);
INSERT INTO `excel_range` VALUES ('15', '块原至末精区域', '2', '43', '21', '5', '2', '0', '4', '6', '9', null, null, null, '1', '13', '化验班报', null, null, null, null, null, null, null);
INSERT INTO `excel_range` VALUES ('16', '块原至末精区域', '2', '43', '21', '5', '2', '0', '11', '13', '16', null, null, null, '1', '14', '化验班报', null, null, null, null, null, null, null);
INSERT INTO `excel_range` VALUES ('17', '末原煤', '1', '1', '4', '1', '1', null, '2', '4', null, null, null, null, '1', '1', '生产检查班报', '6', '8', '10', null, null, null, null);
INSERT INTO `excel_range` VALUES ('18', '末精煤', '1', '1', '6', '3', '1', null, '2', '4', null, null, null, null, '1', '2', '生产检查班报', '6', null, null, '9', null, null, null);
INSERT INTO `excel_range` VALUES ('19', '末中煤', '1', '1', '10', '3', '1', null, '2', '4', null, null, null, null, '1', '3', '生产检查班报', '6', null, null, '9', null, null, null);
INSERT INTO `excel_range` VALUES ('20', '块矸石', '1', '1', '15', '3', '1', null, '2', '4', null, null, null, null, '1', '4', '生产检查班报', null, null, '9', null, '6', null, null);
INSERT INTO `excel_range` VALUES ('21', '末矸石', '1', '1', '18', '9', '1', null, '2', '4', null, null, null, null, '1', '5', '生产检查班报', null, null, '9', null, '6', null, null);
INSERT INTO `excel_range` VALUES ('22', '原煤筛分', '1', '1', '29', '2', '1', null, '2', '4', null, null, null, null, '1', '6', '生产检查班报', null, null, null, null, null, '6', '9');
INSERT INTO `excel_range` VALUES ('23', '末原煤', '2', '1', '4', '1', '1', null, '12', '14', null, null, null, null, '1', '7', '生产检查班报', '16', '18', '20', null, null, null, null);
INSERT INTO `excel_range` VALUES ('24', '末精煤', '2', '1', '6', '3', '1', null, '12', '14', null, null, null, null, '1', '8', '生产检查班报', '16', null, null, '19', null, null, null);
INSERT INTO `excel_range` VALUES ('25', '末中煤', '2', '1', '10', '3', '1', null, '12', '14', null, null, null, null, '1', '9', '生产检查班报', '16', null, null, '19', null, null, null);
INSERT INTO `excel_range` VALUES ('26', '块矸石', '2', '1', '15', '3', '1', null, '12', '14', null, null, null, null, '1', '10', '生产检查班报', null, null, '19', null, '16', null, null);
INSERT INTO `excel_range` VALUES ('27', '末矸石', '2', '1', '18', '9', '1', null, '12', '14', null, null, null, null, '1', '11', '生产检查班报', null, null, '19', null, '16', null, null);
INSERT INTO `excel_range` VALUES ('28', '原煤筛分', '2', '1', '29', '2', '1', null, '12', '14', null, null, null, null, '1', '12', '生产检查班报', null, null, null, null, null, '16', '19');
INSERT INTO `excel_range` VALUES ('29', '551生产精煤平均', '1', '21', '18', '1', '4', null, null, null, '2', '4', '6', null, '1', '17', '化验班报', null, null, null, null, null, null, null);
INSERT INTO `excel_range` VALUES ('30', '551生产精煤平均', '2', '32', '18', '1', '4', null, null, null, '2', '4', '6', null, '1', '18', '化验班报', null, null, null, null, null, null, null);
INSERT INTO `excel_range` VALUES ('31', '552生产洗混煤平均', '1', '21', '34', '1', '4', null, null, null, '2', '4', '6', '8', '1', '19', '化验班报', null, null, null, null, null, null, null);
INSERT INTO `excel_range` VALUES ('32', '552生产洗混煤平均', '2', '32', '34', '1', '4', null, null, null, '2', '4', '6', '8', '1', '20', '化验班报', null, null, null, null, null, null, null);
