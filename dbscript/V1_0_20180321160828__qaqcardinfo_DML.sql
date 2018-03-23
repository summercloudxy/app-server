CREATE TABLE `tb_card_style` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `preview_image` varchar(30) DEFAULT NULL,
  `parser_name` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_card_style
-- ----------------------------
INSERT INTO `tb_card_style` VALUES ('1', '仪表盘显示', null, 'DashboardCardParser');
INSERT INTO `tb_card_style` VALUES ('2', '配煤饼图', null, 'ProportionPieChartParser');
INSERT INTO `tb_card_style` VALUES ('3', '煤质化验', null, 'CoalAnalysisCardParser');
INSERT INTO `tb_card_style` VALUES ('4', '双仓位图', null, 'DoubleBunkerCardParser');
INSERT INTO `tb_card_style` VALUES ('5', '单仓位图', null, 'SingleBunkerCardParser');
INSERT INTO `tb_card_style` VALUES ('6', '浮沉饼图两部分', null, 'ProductionInspectCardParser');
INSERT INTO `tb_card_style` VALUES ('7', '浮沉饼图三部分', null, 'ProductionInspectCardParser');
INSERT INTO `tb_card_style` VALUES ('8', '仪表盘控制', null, 'DashboardCardParser');

DROP TABLE IF EXISTS `tb_card`;
CREATE TABLE `tb_card` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `card_style_id` int(10) DEFAULT NULL,
  `card_param_value_json` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_card
-- ----------------------------
INSERT INTO `tb_card` VALUES ('1', '1', '{\"tc\":\"1144\",\"mcs\":[\"COAL_CAP\",\"CT_C\"]}');
INSERT INTO `tb_card` VALUES ('2', '1', '{\"tc\":\"1143\",\"mcs\":[\"COAL_CAP\",\"CT_C\"]}');
INSERT INTO `tb_card` VALUES ('3', '1', '{\"tc\":\"2143\",\"mcs\":[\"COAL_CAP\",\"CT_C\"]}');
INSERT INTO `tb_card` VALUES ('4', '1', '{\"tc\":\"2144\",\"mcs\":[\"COAL_CAP\",\"CT_C\"]}');
INSERT INTO `tb_card` VALUES ('5', '3', '{\"tc\":\"1301\",\"mc\":\"ASSAY_DATA\",\"type\":\"sample\",\"showbit\":7}');
INSERT INTO `tb_card` VALUES ('6', '1', '{\"tc\":\"1201\",\"mcs\":[\"COAL_CAP\",\"CT_C\"]}');
INSERT INTO `tb_card` VALUES ('7', '2', '{\"title\":\"一期配比\",\"tc\":\"Quit_SYS_1\",\"tmc1\":\"COAL_8_DEVICE\",\"tmc2\":\"COAL_13_DEVICE\",\"cmc1\":\"COAL_8_CF\",\"cmc2\":\"COAL_13_CF\",\"rmc\":\"RATIO\"}');
INSERT INTO `tb_card` VALUES ('8', '1', '{\"tc\":\"2201\",\"mcs\":[\"COAL_CAP\",\"CT_C\"]}');
INSERT INTO `tb_card` VALUES ('9', '2', '{\"title\":\"二期配比\",\"tc\":\"Quit_SYS_2\",\"tmc1\":\"COAL_8_DEVICE\",\"tmc2\":\"COAL_13_DEVICE\",\"cmc1\":\"COAL_8_CF\",\"cmc2\":\"COAL_13_CF\",\"rmc\":\"RATIO\"}');
INSERT INTO `tb_card` VALUES ('10', '1', '{\"title\":\"总入洗量\",\"tc\":\"Quit_SYS_1\",\"mcs\":[\"COAL_CAP\",\"CT_C\"],\"ignoretc\":true}');
INSERT INTO `tb_card` VALUES ('11', '1', '{\"tc\":\"1302\",\"mcs\":[\"COAL_CAP\",\"CT_C\"]}');
INSERT INTO `tb_card` VALUES ('12', '1', '{\"tc\":\"1301\",\"mcs\":[\"COAL_CAP\",\"CT_C\"]}');
INSERT INTO `tb_card` VALUES ('13', '1', '{\"tc\":\"2301\",\"mcs\":[\"COAL_CAP\",\"CT_C\"]}');
INSERT INTO `tb_card` VALUES ('14', '1', '{\"tc\":\"2302\",\"mcs\":[\"COAL_CAP\",\"CT_C\"]}');
INSERT INTO `tb_card` VALUES ('15', '1', '{\"title\":\"总入洗量\",\"tc\":\"Quit_SYS_2\",\"mcs\":[\"COAL_CAP\",\"CT_C\"],\"ignoretc\":true}');
INSERT INTO `tb_card` VALUES ('16', '1', '{\"tc\":\"702\",\"mcs\":[\"COAL_CAP\",\"CT_C\"]}');
INSERT INTO `tb_card` VALUES ('17', '1', '{\"tc\":\"1552\",\"mcs\":[\"COAL_CAP\",\"CT_C\"]}');
INSERT INTO `tb_card` VALUES ('18', '1', '{\"tc\":\"1553\",\"mcs\":[\"COAL_CAP\",\"CT_C\"]}');
INSERT INTO `tb_card` VALUES ('19', '1', '{\"tc\":\"1554\",\"mcs\":[\"COAL_CAP\",\"CT_C\"]}');
INSERT INTO `tb_card` VALUES ('20', '1', '{\"tc\":\"701\",\"mcs\":[\"COAL_CAP\",\"CT_C\"]}');
INSERT INTO `tb_card` VALUES ('21', '1', '{\"tc\":\"2552\",\"mcs\":[\"COAL_CAP\",\"CT_C\"]}');
INSERT INTO `tb_card` VALUES ('22', '1', '{\"tc\":\"2553\",\"mcs\":[\"COAL_CAP\",\"CT_C\"]}');
INSERT INTO `tb_card` VALUES ('23', '1', '{\"tc\":\"2554\",\"mcs\":[\"COAL_CAP\",\"CT_C\"]}');
INSERT INTO `tb_card` VALUES ('24', '1', '{\"tc\":\"703\",\"mcs\":[\"COAL_CAP\",\"CT_C\"]}');
INSERT INTO `tb_card` VALUES ('25', '1', '{\"tc\":\"901\",\"mcs\":[\"COAL_CAP\",\"CT_C\"]}');
INSERT INTO `tb_card` VALUES ('26', '4', '{\"title\":\"精煤仓A\",\"tc\":\"700A\",\"mc1\":\"LE1\",\"mc2\":\"LE2\",\"mc3\":\"AMONT\"}');
INSERT INTO `tb_card` VALUES ('27', '4', '{\"title\":\"精煤仓B\",\"tc\":\"700B\",\"mc1\":\"LE1\",\"mc2\":\"LE2\",\"mc3\":\"AMONT\"}');
INSERT INTO `tb_card` VALUES ('28', '4', '{\"title\":\"混煤仓C\",\"tc\":\"700C\",\"mc1\":\"LE1\",\"mc2\":\"LE2\",\"mc3\":\"AMONT\"}');
INSERT INTO `tb_card` VALUES ('29', '4', '{\"title\":\"混煤仓D\",\"tc\":\"700D\",\"mc1\":\"LE1\",\"mc2\":\"LE2\",\"mc3\":\"AMONT\"}');
INSERT INTO `tb_card` VALUES ('30', '5', '{\"title\":\"一期原煤缓冲仓\",\"tc\":\"1200A\",\"mc\":\"LE1\"}');
INSERT INTO `tb_card` VALUES ('31', '5', '{\"title\":\"二期原煤缓冲仓\",\"tc\":\"2200A\",\"mc\":\"LE1\"}');
INSERT INTO `tb_card` VALUES ('32', '5', '{\"title\":\"矸石缓冲仓\",\"tc\":\"900A\",\"mc\":\"LE1\"}');
INSERT INTO `tb_card` VALUES ('33', '3', '{\"tc\":\"1302\",\"mc\":\"ASSAY_DATA\",\"type\":\"sample\",\"showbit\":7}');
INSERT INTO `tb_card` VALUES ('34', '3', '{\"tc\":\"2301\",\"mc\":\"ASSAY_DATA\",\"type\":\"sample\",\"showbit\":7}');
INSERT INTO `tb_card` VALUES ('35', '3', '{\"tc\":\"2302\",\"mc\":\"ASSAY_DATA\",\"type\":\"sample\",\"showbit\":7}');
INSERT INTO `tb_card` VALUES ('36', '6', '{\"tc\":\"1324\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":16}');
INSERT INTO `tb_card` VALUES ('37', '6', '{\"tc\":\"2324\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":16}');
INSERT INTO `tb_card` VALUES ('38', '6', '{\"tc\":\"1301\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":8}');
INSERT INTO `tb_card` VALUES ('39', '6', '{\"tc\":\"1302\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":8}');
INSERT INTO `tb_card` VALUES ('40', '6', '{\"tc\":\"2301\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":8}');
INSERT INTO `tb_card` VALUES ('41', '6', '{\"tc\":\"2302\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":8}');
INSERT INTO `tb_card` VALUES ('42', '6', '{\"tc\":\"1389\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":3}');
INSERT INTO `tb_card` VALUES ('43', '6', '{\"tc\":\"1390\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":3}');
INSERT INTO `tb_card` VALUES ('44', '6', '{\"tc\":\"1391\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":3}');
INSERT INTO `tb_card` VALUES ('45', '6', '{\"tc\":\"2389\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":3}');
INSERT INTO `tb_card` VALUES ('46', '6', '{\"tc\":\"2390\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":3}');
INSERT INTO `tb_card` VALUES ('47', '6', '{\"tc\":\"2391\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":3}');
INSERT INTO `tb_card` VALUES ('48', '3', '{\"tc\":\"1552\",\"mc\":\"ASSAY_DATA\",\"type\":\"sample\",\"showbit\":7}');
INSERT INTO `tb_card` VALUES ('49', '3', '{\"tc\":\"一期浅槽溢流煤\",\"mc\":\"ASSAY_DATA\",\"type\":\"target\",\"showbit\":7}');
INSERT INTO `tb_card` VALUES ('50', '3', '{\"tc\":\"一期552生产洗混煤平均\",\"mc\":\"ASSAY_DATA\",\"type\":\"target\",\"showbit\":7,\"ignoretc\":true,\"isAvg\":true}');
INSERT INTO `tb_card` VALUES ('51', '3', '{\"tc\":\"一期末中煤\",\"mc\":\"ASSAY_DATA\",\"type\":\"target\",\"showbit\":7}');
INSERT INTO `tb_card` VALUES ('52', '3', '{\"tc\":\"1470\",\"mc\":\"ASSAY_DATA\",\"type\":\"sample\",\"showbit\":7}');
INSERT INTO `tb_card` VALUES ('53', '3', '{\"tc\":\"1553\",\"mc\":\"ASSAY_DATA\",\"type\":\"sample\",\"showbit\":7}');
INSERT INTO `tb_card` VALUES ('54', '3', '{\"tc\":\"一期TCS精矿\",\"mc\":\"ASSAY_DATA\",\"type\":\"target\",\"showbit\":3}');
INSERT INTO `tb_card` VALUES ('55', '3', '{\"tc\":\"2552\",\"mc\":\"ASSAY_DATA\",\"type\":\"sample\",\"showbit\":7}');
INSERT INTO `tb_card` VALUES ('56', '3', '{\"tc\":\"二期552生产洗混煤平均\",\"mc\":\"ASSAY_DATA\",\"type\":\"target\",\"showbit\":7,\"ignoretc\":true,\"isAvg\":true}');
INSERT INTO `tb_card` VALUES ('57', '3', '{\"tc\":\"二期浅槽溢流煤\",\"mc\":\"ASSAY_DATA\",\"type\":\"target\",\"showbit\":7}');
INSERT INTO `tb_card` VALUES ('58', '3', '{\"tc\":\"二期末中煤\",\"mc\":\"ASSAY_DATA\",\"type\":\"target\",\"showbit\":7}');
INSERT INTO `tb_card` VALUES ('59', '3', '{\"tc\":\"2470\",\"mc\":\"ASSAY_DATA\",\"type\":\"sample\",\"showbit\":7}');
INSERT INTO `tb_card` VALUES ('60', '3', '{\"tc\":\"2553\",\"mc\":\"ASSAY_DATA\",\"type\":\"sample\",\"showbit\":7}');
INSERT INTO `tb_card` VALUES ('61', '3', '{\"tc\":\"二期TCS精矿\",\"mc\":\"ASSAY_DATA\",\"type\":\"target\",\"showbit\":3}');
INSERT INTO `tb_card` VALUES ('62', '3', '{\"tc\":\"一期TCS尾矿\",\"mc\":\"ASSAY_DATA\",\"type\":\"target\",\"showbit\":3}');
INSERT INTO `tb_card` VALUES ('63', '3', '{\"tc\":\"一期末矸石\",\"mc\":\"ASSAY_DATA\",\"type\":\"target\",\"showbit\":7}');
INSERT INTO `tb_card` VALUES ('64', '3', '{\"tc\":\"二期TCS尾矿\",\"mc\":\"ASSAY_DATA\",\"type\":\"target\",\"showbit\":3}');
INSERT INTO `tb_card` VALUES ('65', '3', '{\"tc\":\"二期末矸石\",\"mc\":\"ASSAY_DATA\",\"type\":\"target\",\"showbit\":7}');
INSERT INTO `tb_card` VALUES ('66', '6', '{\"tc\":\"1345\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":5}');
INSERT INTO `tb_card` VALUES ('67', '6', '{\"tc\":\"1346\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":5}');
INSERT INTO `tb_card` VALUES ('68', '6', '{\"tc\":\"1347\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":5}');
INSERT INTO `tb_card` VALUES ('69', '6', '{\"tc\":\"2345\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":5}');
INSERT INTO `tb_card` VALUES ('70', '6', '{\"tc\":\"2346\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":5}');
INSERT INTO `tb_card` VALUES ('71', '6', '{\"tc\":\"2347\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":5}');
INSERT INTO `tb_card` VALUES ('72', '8', '{\"tc\":\"1307\",\"mcs\":[\"MD\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('73', '8', '{\"tc\":\"1308\",\"mcs\":[\"MD\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('74', '8', '{\"tc\":\"2307\",\"mcs\":[\"MD\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('75', '8', '{\"tc\":\"2308\",\"mcs\":[\"MD\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('76', '8', '{\"tc\":\"1339\",\"mcs\":[\"MD\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('77', '8', '{\"tc\":\"1340\",\"mcs\":[\"MD\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('78', '8', '{\"tc\":\"1341\",\"mcs\":[\"MD\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('79', '8', '{\"tc\":\"2339\",\"mcs\":[\"MD\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('80', '8', '{\"tc\":\"2340\",\"mcs\":[\"MD\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('81', '8', '{\"tc\":\"2341\",\"mcs\":[\"MD\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('82', '8', '{\"tc\":\"1386\",\"mcs\":[\"MD\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('83', '8', '{\"tc\":\"1387\",\"mcs\":[\"MD\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('84', '8', '{\"tc\":\"1388\",\"mcs\":[\"MD\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('85', '8', '{\"tc\":\"2386\",\"mcs\":[\"MD\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('86', '8', '{\"tc\":\"2387\",\"mcs\":[\"MD\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('87', '8', '{\"tc\":\"2388\",\"mcs\":[\"MD\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('88', '8', '{\"tc\":\"1857\",\"mcs\":[\"AVG_DENSITY\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('89', '8', '{\"tc\":\"1858\",\"mcs\":[\"AVG_DENSITY\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('90', '8', '{\"tc\":\"1859\",\"mcs\":[\"AVG_DENSITY\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('91', '8', '{\"tc\":\"2857\",\"mcs\":[\"AVG_DENSITY\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('92', '8', '{\"tc\":\"2858\",\"mcs\":[\"AVG_DENSITY\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('93', '8', '{\"tc\":\"2859\",\"mcs\":[\"AVG_DENSITY\",\"PID_AUTO_MD\"]}');
INSERT INTO `tb_card` VALUES ('94', '3', '{\"tc\":\"1551\",\"mc\":\"ASSAY_DATA\",\"type\":\"sample\",\"showbit\":7}');
INSERT INTO `tb_card` VALUES ('95', '3', '{\"tc\":\"一期551生产精煤平均\",\"mc\":\"ASSAY_DATA\",\"type\":\"target\",\"showbit\":7,\"ignoretc\":true,\"isAvg\":true}');
INSERT INTO `tb_card` VALUES ('96', '3', '{\"tc\":\"2551\",\"mc\":\"ASSAY_DATA\",\"type\":\"sample\",\"showbit\":7}');
INSERT INTO `tb_card` VALUES ('97', '3', '{\"tc\":\"二期551生产精煤平均\",\"mc\":\"ASSAY_DATA\",\"type\":\"target\",\"showbit\":7,\"ignoretc\":true,\"isAvg\":true}');
INSERT INTO `tb_card` VALUES ('98', '3', '{\"tc\":\"1389\",\"mc\":\"ASSAY_DATA\",\"type\":\"sample\",\"showbit\":9}');
INSERT INTO `tb_card` VALUES ('99', '3', '{\"tc\":\"1390\",\"mc\":\"ASSAY_DATA\",\"type\":\"sample\",\"showbit\":9}');
INSERT INTO `tb_card` VALUES ('100', '3', '{\"tc\":\"1391\",\"mc\":\"ASSAY_DATA\",\"type\":\"sample\",\"showbit\":9}');
INSERT INTO `tb_card` VALUES ('101', '3', '{\"tc\":\"2389\",\"mc\":\"ASSAY_DATA\",\"type\":\"sample\",\"showbit\":9}');
INSERT INTO `tb_card` VALUES ('102', '3', '{\"tc\":\"2390\",\"mc\":\"ASSAY_DATA\",\"type\":\"sample\",\"showbit\":9}');
INSERT INTO `tb_card` VALUES ('103', '3', '{\"tc\":\"2391\",\"mc\":\"ASSAY_DATA\",\"type\":\"sample\",\"showbit\":9}');
INSERT INTO `tb_card` VALUES ('104', '6', '{\"tc\":\"1396\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":3}');
INSERT INTO `tb_card` VALUES ('105', '6', '{\"tc\":\"1397\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":3}');
INSERT INTO `tb_card` VALUES ('106', '6', '{\"tc\":\"1398\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":3}');
INSERT INTO `tb_card` VALUES ('107', '6', '{\"tc\":\"2396\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":3}');
INSERT INTO `tb_card` VALUES ('108', '6', '{\"tc\":\"2397\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":3}');
INSERT INTO `tb_card` VALUES ('109', '6', '{\"tc\":\"2398\",\"mc\":\"PRODUCT_INSPECT_DATA\",\"type\":\"sample\",\"showbit\":3}');


CREATE TABLE `rel_qualityandquantity_area_card` (
  `id` int(11) DEFAULT NULL,
  `area_id` int(11) DEFAULT NULL,
  `card_id` int(11) DEFAULT NULL,
  `sequence` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `area_title` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rel_qualityandquantity_area_card
-- ----------------------------
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '1', '10', '1', '概况产品及质量数据一期', '产品和质量数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '1', '16', '2', '概况产品及质量数据一期', '产品和质量数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '1', '94', '3', '概况产品及质量数据一期', '产品和质量数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '1', '48', '4', '概况产品及质量数据一期', '产品和质量数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '2', '82', '1', '概况分选指标一期', '分选指标');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '2', '83', '2', '概况分选指标一期', '分选指标');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '2', '84', '3', '概况分选指标一期', '分选指标');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '3', '26', '1', '概况仓位信息一期', '仓位信息');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '3', '27', '2', '概况仓位信息一期', '仓位信息');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '3', '28', '3', '概况仓位信息一期', '仓位信息');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '3', '29', '4', '概况仓位信息一期', '仓位信息');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '7', '2', '1', '原煤准备', '原煤准备');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '7', '1', '2', '原煤准备', '原煤准备');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '7', '3', '3', '原煤准备', '原煤准备');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '7', '4', '4', '原煤准备', '原煤准备');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '7', '6', '5', '原煤准备', '原煤准备');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '7', '7', '6', '原煤准备', '原煤准备');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '7', '8', '7', '原煤准备', '原煤准备');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '7', '9', '8', '原煤准备', '原煤准备');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '8', '5', '1', '原煤入洗一期', '一期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '8', '11', '2', '原煤入洗一期', '一期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '8', '10', '3', '原煤入洗一期', '一期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '8', '7', '4', '原煤入洗一期', '一期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '9', '13', '1', '原煤入洗二期', '二期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '9', '14', '2', '原煤入洗二期', '二期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '9', '15', '3', '原煤入洗二期', '二期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '9', '9', '4', '原煤入洗二期', '二期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '10', '16', '1', '产品产量一期', '一期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '10', '17', '2', '产品产量一期', '一期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '10', '18', '3', '产品产量一期', '一期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '10', '19', '4', '产品产量一期', '一期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '11', '20', '1', '产品产量二期', '二期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '11', '21', '2', '产品产量二期', '二期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '11', '22', '3', '产品产量二期', '二期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '11', '23', '4', '产品产量二期', '二期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '12', '24', '1', '产品产量综合', '综合');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '12', '25', '2', '产品产量综合', '综合');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '13', '26', '1', '仓位产品', '产品');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '13', '27', '2', '仓位产品', '产品');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '13', '28', '3', '仓位产品', '产品');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '13', '29', '4', '仓位产品', '产品');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '14', '30', '1', '仓位原煤及矸石', '原煤及矸石');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '14', '31', '2', '仓位原煤及矸石', '原煤及矸石');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '14', '32', '3', '仓位原煤及矸石', '原煤及矸石');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '15', '5', '1', '原煤质量化验', '化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '15', '33', '2', '原煤质量化验', '化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '15', '34', '3', '原煤质量化验', '化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '15', '35', '4', '原煤质量化验', '化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '16', '36', '1', '原煤质量浮沉', '浮沉数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '16', '37', '2', '原煤质量浮沉', '浮沉数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '17', '38', '1', '原煤质量筛分', '筛分数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '17', '39', '2', '原煤质量筛分', '筛分数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '17', '40', '3', '原煤质量筛分', '筛分数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '17', '41', '4', '原煤质量筛分', '筛分数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '18', '94', '1', '精煤质量化验总览', '化验数据概况');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '18', '95', '2', '精煤质量化验总览', '化验数据概况');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '18', '96', '3', '精煤质量化验总览', '化验数据概况');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '18', '97', '4', '精煤质量化验总览', '化验数据概况');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '19', '98', '1', '精煤质量一期化验', '一期化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '19', '99', '2', '精煤质量一期化验', '一期化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '19', '100', '3', '精煤质量一期化验', '一期化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '20', '101', '1', '精煤质量二期化验', '二期化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '20', '102', '2', '精煤质量二期化验', '二期化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '20', '103', '3', '精煤质量二期化验', '二期化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '21', '42', '1', '精煤质量一期浮沉', '一期浮沉数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '21', '43', '2', '精煤质量一期浮沉', '一期浮沉数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '21', '44', '3', '精煤质量一期浮沉', '一期浮沉数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '22', '45', '1', '精煤质量二期浮沉', '二期浮沉数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '22', '46', '2', '精煤质量二期浮沉', '二期浮沉数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '22', '47', '3', '精煤质量二期浮沉', '二期浮沉数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '23', '48', '1', '混煤质量一期化验', '一期化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '23', '50', '2', '混煤质量一期化验', '一期化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '23', '49', '3', '混煤质量一期化验', '一期化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '23', '51', '4', '混煤质量一期化验', '一期化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '23', '52', '5', '混煤质量一期化验', '一期化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '23', '53', '6', '混煤质量一期化验', '一期化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '23', '54', '7', '混煤质量一期化验', '一期化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '24', '55', '1', '混煤质量二期化验', '二期化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '24', '56', '2', '混煤质量二期化验', '二期化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '24', '57', '3', '混煤质量二期化验', '二期化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '24', '58', '4', '混煤质量二期化验', '二期化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '24', '59', '5', '混煤质量二期化验', '二期化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '24', '60', '6', '混煤质量二期化验', '二期化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '24', '61', '7', '混煤质量二期化验', '二期化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '27', '62', '1', '矸石质量化验', '化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '27', '63', '2', '矸石质量化验', '化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '27', '64', '3', '矸石质量化验', '化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '27', '65', '4', '矸石质量化验', '化验数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '28', '66', '1', '矸石质量一期浮沉', '一期浮沉数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '28', '67', '2', '矸石质量一期浮沉', '一期浮沉数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '28', '68', '3', '矸石质量一期浮沉', '一期浮沉数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '29', '69', '1', '矸石质量二期浮沉', '二期浮沉数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '29', '70', '2', '矸石质量二期浮沉', '二期浮沉数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '29', '71', '3', '矸石质量二期浮沉', '二期浮沉数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '30', '72', '1', '分选浅槽', '浅槽');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '30', '73', '2', '分选浅槽', '浅槽');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '30', '74', '3', '分选浅槽', '浅槽');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '30', '75', '4', '分选浅槽', '浅槽');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '31', '76', '1', '分选旋流器主洗一期', '一期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '31', '77', '2', '分选旋流器主洗一期', '一期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '31', '78', '3', '分选旋流器主洗一期', '一期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '32', '79', '1', '分选旋流器主洗二期', '二期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '32', '80', '2', '分选旋流器主洗二期', '二期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '32', '81', '3', '分选旋流器主洗二期', '二期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '33', '82', '1', '分选旋流器再洗一期', '一期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '33', '83', '2', '分选旋流器再洗一期', '一期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '33', '84', '3', '分选旋流器再洗一期', '一期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '34', '85', '1', '分选旋流器再洗二期', '二期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '34', '86', '2', '分选旋流器再洗二期', '二期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '34', '87', '3', '分选旋流器再洗二期', '二期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '35', '88', '1', '分选TCS一期', '一期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '35', '89', '2', '分选TCS一期', '一期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '35', '90', '3', '分选TCS一期', '一期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '36', '91', '1', '分选TCS二期', '二期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '36', '92', '2', '分选TCS二期', '二期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '36', '93', '3', '分选TCS二期', '二期');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '4', '10', '1', '概况产品及质量数据二期', '产品和质量数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '4', '20', '2', '概况产品及质量数据二期', '产品和质量数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '4', '96', '3', '概况产品及质量数据二期', '产品和质量数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '4', '55', '4', '概况产品及质量数据二期', '产品和质量数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '5', '85', '1', '概况分选指标二期', '分选指标');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '5', '86', '2', '概况分选指标二期', '分选指标');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '5', '87', '3', '概况分选指标二期', '分选指标');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '6', '26', '1', '概况仓位信息二期', '仓位信息');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '6', '27', '2', '概况仓位信息二期', '仓位信息');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '6', '28', '3', '概况仓位信息二期', '仓位信息');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '6', '29', '4', '概况仓位信息二期', '仓位信息');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '25', '104', '1', '混煤质量一期浮沉', '一期浮沉数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '25', '105', '2', '混煤质量一期浮沉', '一期浮沉数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '25', '106', '3', '混煤质量一期浮沉', '一期浮沉数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '26', '107', '1', '混煤质量二期浮沉', '二期浮沉数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '26', '107', '2', '混煤质量二期浮沉', '二期浮沉数据');
INSERT INTO `rel_qualityandquantity_area_card` VALUES (null, '26', '107', '3', '混煤质量二期浮沉', '二期浮沉数据');

