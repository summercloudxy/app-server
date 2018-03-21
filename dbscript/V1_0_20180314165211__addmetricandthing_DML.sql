ALTER  TABLE  `tb_metric` MODIFY metric_category_code VARCHAR(20);

INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'SIG', '8#煤设备', 'COAL_8_DEVICE', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` (`metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'SIG', '8#煤设备瞬时量', 'COAL_8_CF', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'SIG', '13#煤设备', 'COAL_13_DEVICE', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'SIG', '13#煤设备瞬时量', 'COAL_13_CF', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'SIG', '比例', 'RATIO', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'SIG', '存储量', 'AMOUNT', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'SIG', '平均密度', 'AVG_DENSITY', NULL, NULL, NULL, NULL, NULL);

INSERT INTO `tb_thing` (`parent_thing_id`, `thing_code`, `thing_name`, `thing_type1_code`, `thing_type2_code`, `thing_type3_code`, `thing_shortname`) VALUES ( NULL, 'productioninspect', '生产检查', NULL, NULL, NULL, NULL);
INSERT INTO `tb_thing` (`parent_thing_id`, `thing_code`, `thing_name`, `thing_type1_code`, `thing_type2_code`, `thing_type3_code`, `thing_shortname`) VALUES ( NULL, 'Quit_SYS_1', '一期数质量', NULL, NULL, NULL, NULL);
INSERT INTO `tb_thing` (`parent_thing_id`, `thing_code`, `thing_name`, `thing_type1_code`, `thing_type2_code`, `thing_type3_code`, `thing_shortname`) VALUES ( NULL, 'Quit_SYS_2', '二期数质量', NULL, NULL, NULL, NULL);
INSERT INTO `tb_thing` (`parent_thing_id`, `thing_code`, `thing_name`, `thing_type1_code`, `thing_type2_code`, `thing_type3_code`, `thing_shortname`) VALUES ( NULL, 'Quit_SYS_RAW', '原煤数质量', NULL, NULL, NULL, NULL);


UPDATE `tb_metric` SET  `metric_category_code`='ASSAY' WHERE (`metric_name`='TCS精矿');
UPDATE `tb_metric` SET  `metric_category_code`='ASSAY' WHERE (`metric_name`='TCS尾矿');
UPDATE `tb_metric` SET  `metric_category_code`='ASSAY' WHERE (`metric_name`='入洗原煤');
UPDATE `tb_metric` SET  `metric_category_code`='ASSAY' WHERE (`metric_name`='浅槽溢流煤');
UPDATE `tb_metric` SET  `metric_category_code`='ASSAY' WHERE (`metric_name`='末中煤');
UPDATE `tb_metric` SET  `metric_category_code`='ASSAY' WHERE (`metric_name`='沉降煤泥');
UPDATE `tb_metric` SET  `metric_category_code`='ASSAY' WHERE (`metric_name`='压滤煤泥');
UPDATE `tb_metric` SET  `metric_category_code`='ASSAY' WHERE (`metric_name`='末矸石');
UPDATE `tb_metric` SET  `metric_category_code`='ASSAY' WHERE (`metric_name`='末精煤');
UPDATE `tb_metric` SET  `metric_category_code`='ASSAY' WHERE (`metric_name`='551生产精煤');
UPDATE `tb_metric` SET  `metric_category_code`='ASSAY' WHERE (`metric_name`='552生产洗混煤');
UPDATE `tb_metric` SET  `metric_category_code`='ASSAY' WHERE (`metric_name`='块原煤');
UPDATE `tb_metric` SET  `metric_category_code`='ASSAY' WHERE (`metric_name`='末原煤');
UPDATE `tb_metric` SET  `metric_category_code`='ASSAY' WHERE (`metric_name`='块矸石');

INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('ASSAY', '灰分', 'AAD', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('ASSAY', '水分', 'MT', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('ASSAY', '硫份', 'STAD', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('ASSAY', '发热量', 'QNETAR', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('ASSAY', '煤质化验密度', 'ASSAY_DENSITY', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('ASSAY', '煤质化验流量', 'ASSAY_FLOW', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('ASSAY', '煤质化验设备', 'ASSAY_SAMPLE', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('ASSAY', '煤质化验数据', 'ASSAY_DATA', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('PRODUCTION_INSPECT', '+1.45', 'POSITIVE1_POINT45', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('PRODUCTION_INSPECT', '-1.45', 'NEGATIVE1_POINT45', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('PRODUCTION_INSPECT', '+1.8', 'POSITIVE1_POINT8', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('PRODUCTION_INSPECT', '-1.8', 'NEGATIVE1_POINT8', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('PRODUCTION_INSPECT', '1.45-1.8', 'ONE_POINT45_TO1_POINT8', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('PRODUCTION_INSPECT', '+50mm', 'POSITIVE50MM', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('PRODUCTION_INSPECT', '-50mm', 'NEGATIVE50MM', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('PRODUCTION_INSPECT', '生产检查设备', 'PRODUCT_INSPECT_SAMPLE', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('PRODUCTION_INSPECT', '生产检查数据', 'PRODUCT_INSPECT_DATA', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('PRODUCTION_INSPECT', '生产检查密度', 'PRODUCT_INSPECT_DENSITY', NULL, NULL, NULL, NULL, NULL);

INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'PRODUCTION_INSPECT', '+1.45平均', 'POSITIVE1_POINT45_AVG', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'PRODUCTION_INSPECT', '-1.45平均', 'NEGATIVE1_POINT45_AVG', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'PRODUCTION_INSPECT', '+1.8平均', 'POSITIVE1_POINT8_AVG', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'PRODUCTION_INSPECT', '-1.8平均', 'NEGATIVE1_POINT8_AVG', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'PRODUCTION_INSPECT', '1.45-1.8平均', 'ONE_POINT45_TO1_POINT8_AVG', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'PRODUCTION_INSPECT', '+50mm平均', 'POSITIVE50MM_AVG', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'PRODUCTION_INSPECT', '-50mm平均', 'NEGATIVE50MM_AVG', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'PRODUCTION_INSPECT', '生产检查密度平均', 'PRODUCT_INSPECT_DENSITY_AVG', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'ASSAY', '灰分平均', 'AAD_AVG', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'ASSAY', '水分平均', 'MT_AVG', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'ASSAY', '硫份平均', 'STAD_AVG', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'ASSAY', '发热量平均', 'QNETAR_AVG', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'ASSAY', '煤质化验密度平均', 'ASSAY_DENSITY_AVG', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tb_metric` ( `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'ASSAY', '煤质化验流量平均', 'ASSAY_FLOW_AVG', NULL, NULL, NULL, NULL, NULL);

INSERT INTO `tb_thing` (`parent_thing_id`, `thing_code`, `thing_name`, `thing_type1_code`, `thing_type2_code`, `thing_type3_code`, `thing_shortname`) VALUES ( NULL, '2873-2874', 'TCS尾矿高频筛', 'DEVICE', 'TSS', NULL, '高频');
INSERT INTO `tb_thing` (`parent_thing_id`, `thing_code`, `thing_name`, `thing_type1_code`, `thing_type2_code`, `thing_type3_code`, `thing_shortname`) VALUES ( NULL, '1873-1874', 'TCS尾矿高频筛', 'DEVICE', 'TSS', NULL, '高频');
INSERT INTO `tb_thing` (`parent_thing_id`, `thing_code`, `thing_name`, `thing_type1_code`, `thing_type2_code`, `thing_type3_code`, `thing_shortname`) VALUES ( NULL, '2448-2451', '粗煤泥脱水离心机', 'DEVICE', 'LXJ', NULL, '煤泥');
INSERT INTO `tb_thing` (`parent_thing_id`, `thing_code`, `thing_name`, `thing_type1_code`, `thing_type2_code`, `thing_type3_code`, `thing_shortname`) VALUES ( NULL, '1448-1451', '粗煤泥脱水离心机', 'DEVICE', 'LXJ', NULL, '煤泥');
INSERT INTO `tb_thing` (`parent_thing_id`, `thing_code`, `thing_name`, `thing_type1_code`, `thing_type2_code`, `thing_type3_code`, `thing_shortname`) VALUES ( NULL, '1309-1310', '浅槽溢流脱介筛', 'DEVICE', 'TJS', NULL, '块精');
INSERT INTO `tb_thing` (`parent_thing_id`, `thing_code`, `thing_name`, `thing_type1_code`, `thing_type2_code`, `thing_type3_code`, `thing_shortname`) VALUES ( NULL, '2309-2310', '浅槽溢流脱介筛', 'DEVICE', 'TJS', NULL, '块精');
INSERT INTO `tb_thing` (`parent_thing_id`, `thing_code`, `thing_name`, `thing_type1_code`, `thing_type2_code`, `thing_type3_code`, `thing_shortname`) VALUES ( NULL, '1345-1347', '末矸石脱介筛', 'DEVICE', 'TJS', NULL, '末矸');
INSERT INTO `tb_thing` (`parent_thing_id`, `thing_code`, `thing_name`, `thing_type1_code`, `thing_type2_code`, `thing_type3_code`, `thing_shortname`) VALUES ( NULL, '2345-2347', '末矸石脱介筛', 'DEVICE', 'TJS', NULL, '末矸');
INSERT INTO `tb_thing` (`parent_thing_id`, `thing_code`, `thing_name`, `thing_type1_code`, `thing_type2_code`, `thing_type3_code`, `thing_shortname`) VALUES ( NULL, '2396-2398', '末中煤脱介筛', 'DEVICE', 'TJS', NULL, '末中');
INSERT INTO `tb_thing` (`parent_thing_id`, `thing_code`, `thing_name`, `thing_type1_code`, `thing_type2_code`, `thing_type3_code`, `thing_shortname`) VALUES ( NULL, '1396-1398', '末中煤脱介筛', 'DEVICE', 'TJS', NULL, '末中');
INSERT INTO `tb_thing` (`parent_thing_id`, `thing_code`, `thing_name`, `thing_type1_code`, `thing_type2_code`, `thing_type3_code`, `thing_shortname`) VALUES ( NULL, '2303-2306', '原煤分级脱泥筛', 'DEVICE', 'FJS', NULL, '原煤');
INSERT INTO `tb_thing` (`parent_thing_id`, `thing_code`, `thing_name`, `thing_type1_code`, `thing_type2_code`, `thing_type3_code`, `thing_shortname`) VALUES ( NULL, '1303-1306', '原煤分级脱泥筛', 'DEVICE', 'FJS', NULL, '原煤');


INSERT INTO `tb_metric` (`metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ( 'SIG', '料位', 'LE', 'PR', 'LE', NULL, 'FLT', 'm');

