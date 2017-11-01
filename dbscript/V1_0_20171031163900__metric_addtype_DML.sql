UPDATE tb_metric SET  metric_type2_code='ZD' WHERE metric_name='震动';
UPDATE tb_metric SET  metric_type2_code='TORQUE' WHERE metric_name='当前扭矩';
UPDATE rel_thing_metric_label SET enabled = 1;

DELETE FROM rel_metric_alert_type WHERE metric_code = 'DMF' and alert_type = 2;
INSERT INTO rel_metric_alert_type (`id`, `metric_code`, `alert_type`) VALUES ('98', 'DMP', '2');
INSERT INTO tb_metric (`id`, `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('2735', 'SIG', '撕裂保护', 'DMP', 'P', NULL, NULL, 'BOO', NULL);
UPDATE rel_thing_metric_label SET metric_code = 'DMP' WHERE label_path = 'XG1_3.XG1_3.1554/F/DM/0';
UPDATE rel_thing_metric_label SET metric_code = 'DMP' WHERE label_path = 'XG2_3.XG2_3.2554/F/DM/0';

DELETE FROM rel_metric_alert_type WHERE metric_code = 'INV_ALM' and alert_type = 2;
INSERT INTO rel_metric_alert_type (`id`, `metric_code`, `alert_type`) VALUES ('99', 'INV_ALMP', '2');
INSERT INTO tb_metric (`id`, `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('2736', 'SIG', '变频报警保护', 'INV_ALMP', 'P', NULL, NULL, 'BOO', NULL);
UPDATE rel_thing_metric_label SET metric_code = 'INV_ALMP' WHERE label_path = 'CW_1.CW_1.703/F/INV_ALM/0';

DELETE FROM rel_metric_alert_type WHERE metric_code = 'INV_PR' and alert_type = 2;
INSERT INTO rel_metric_alert_type (`id`, `metric_code`, `alert_type`) VALUES ('100', 'INV_PRP', '2');
INSERT INTO tb_metric (`id`, `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('2737', 'SIG', '变频就绪保护', 'INV_PRP', 'P', NULL, NULL, 'BOO', NULL);
UPDATE rel_thing_metric_label SET metric_code = 'INV_PRP' WHERE label_path = 'CW_1.CW_1.703/F/INV_PR/0';

DELETE FROM rel_metric_alert_type WHERE metric_code = 'LD' and alert_type = 0;
UPDATE tb_metric SET metric_name = '漏电保护' WHERE metric_code = 'LD';
INSERT INTO rel_metric_alert_type (`id`, `metric_code`, `alert_type`) VALUES ('101', 'LDF', '0');
INSERT INTO tb_metric (`id`, `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('2738', 'SIG', '漏电故障', 'LDF', 'F', NULL, NULL, 'BOO', NULL);
UPDATE rel_thing_metric_label SET  metric_code = 'LDF' WHERE label_path = 'TCS1_1.TCS1_1.1873/F/LD/0';
UPDATE rel_thing_metric_label SET  metric_code = 'LDF' WHERE label_path = 'TCS1_1.TCS1_1.1874/F/LD/0';
UPDATE rel_thing_metric_label SET  metric_code = 'LDF' WHERE label_path = 'TCS1_1.TCS1_1.2873/F/LD/0';
UPDATE rel_thing_metric_label SET  metric_code = 'LDF' WHERE label_path = 'TCS1_1.TCS1_1.2874/F/LD/0';

DELETE FROM rel_metric_alert_type WHERE metric_code = 'TRIP' and alert_type = 2;
INSERT INTO rel_metric_alert_type (`id`, `metric_code`, `alert_type`) VALUES ('102', 'TRIPP', '2');
INSERT INTO tb_metric (`id`, `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('2739', 'SIG', '跳闸保护', 'TRIPP', 'P', NULL, NULL, 'BOO', NULL);
UPDATE rel_thing_metric_label SET metric_code = 'TRIPP' WHERE label_path = 'CW_1.CW_1.702*DJ*YB_1/F/TRIP/0';

DELETE FROM rel_metric_alert_type WHERE metric_code = 'US1' and alert_type = 2;
INSERT INTO rel_metric_alert_type (`id`, `metric_code`, `alert_type`) VALUES ('103', 'USP1', '2');
INSERT INTO tb_metric (`id`, `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('2740', 'SIG', '欠速保护1', 'USP1', 'P', NULL, NULL, 'BOO', NULL);
UPDATE rel_thing_metric_label SET metric_code = 'USP1' WHERE label_path = 'XG1_3.XG1_3.1497/F/US/0';
UPDATE rel_thing_metric_label SET metric_code = 'USP1' WHERE label_path = 'XG1_3.XG1_3.1498/F/US/0';
UPDATE rel_thing_metric_label SET metric_code = 'USP1' WHERE label_path = 'XG1_3.XG1_3.1499/F/US/0';
UPDATE rel_thing_metric_label SET metric_code = 'USP1' WHERE label_path = 'XG1_3.XG1_3.1500/F/US/0';
UPDATE rel_thing_metric_label SET metric_code = 'USP1' WHERE label_path = 'XG1_3.XG1_3.1501/F/US/0';
UPDATE rel_thing_metric_label SET metric_code = 'USP1' WHERE label_path = 'XG1_3.XG1_3.1501A/F/US/0';
UPDATE rel_thing_metric_label SET metric_code = 'USP1' WHERE label_path = 'XG2_3.XG2_3.2497/F/US/0';
UPDATE rel_thing_metric_label SET metric_code = 'USP1' WHERE label_path = 'XG2_3.XG2_3.2498/F/US/0';
UPDATE rel_thing_metric_label SET metric_code = 'USP1' WHERE label_path = 'XG2_3.XG2_3.2499/F/US/0';
UPDATE rel_thing_metric_label SET metric_code = 'USP1' WHERE label_path = 'XG2_3.XG2_3.2500/F/US/0';
UPDATE rel_thing_metric_label SET metric_code = 'USP1' WHERE label_path = 'XG2_3.XG2_3.2501/F/US/0';
UPDATE rel_thing_metric_label SET metric_code = 'USP1' WHERE label_path = 'XG2_3.XG2_3.2501A/F/US/0';

DELETE FROM rel_metric_alert_type WHERE metric_code = 'USF2' and alert_type = 2;
INSERT INTO rel_metric_alert_type (`id`, `metric_code`, `alert_type`) VALUES ('104', 'USP2', '2');
INSERT INTO tb_metric (`id`, `metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`) VALUES ('2741', 'SIG', '欠速保护2', 'USP2', 'P', NULL, NULL, 'BOO', NULL);
UPDATE rel_thing_metric_label SET metric_code = 'USP2' WHERE label_path = 'XG1_3.XG1_3.1497/F/US/1';
UPDATE rel_thing_metric_label SET metric_code = 'USP2' WHERE label_path = 'XG1_3.XG1_3.1498/F/US/1';
UPDATE rel_thing_metric_label SET metric_code = 'USP2' WHERE label_path = 'XG1_3.XG1_3.1499/F/US/1';
UPDATE rel_thing_metric_label SET metric_code = 'USP2' WHERE label_path = 'XG1_3.XG1_3.1500/F/US/1';
UPDATE rel_thing_metric_label SET metric_code = 'USP2' WHERE label_path = 'XG1_3.XG1_3.1501/F/US/1';
UPDATE rel_thing_metric_label SET metric_code = 'USP2' WHERE label_path = 'XG1_3.XG1_3.1501A/F/US/1';
UPDATE rel_thing_metric_label SET metric_code = 'USP2' WHERE label_path = 'XG2_3.XG2_3.2497/F/US/1';
UPDATE rel_thing_metric_label SET metric_code = 'USP2' WHERE label_path = 'XG2_3.XG2_3.2498/F/US/1';
UPDATE rel_thing_metric_label SET metric_code = 'USP2' WHERE label_path = 'XG2_3.XG2_3.2499/F/US/1';
UPDATE rel_thing_metric_label SET metric_code = 'USP2' WHERE label_path = 'XG2_3.XG2_3.2500/F/US/1';
UPDATE rel_thing_metric_label SET metric_code = 'USP2' WHERE label_path = 'XG2_3.XG2_3.2501/F/US/1';
UPDATE rel_thing_metric_label SET metric_code = 'USP2' WHERE label_path = 'XG2_3.XG2_3.2501A/F/US/1';






