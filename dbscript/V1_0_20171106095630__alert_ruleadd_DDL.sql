INSERT INTO `rel_metric_alert_type` VALUES ('105', 'COAL_CAP', '1');
UPDATE `tb_metric` SET `id`='2568', `metric_category_code`='SIG', `metric_name`='实际检测密度P1', `metric_code`='MD1', `metric_type1_code`='PR', `metric_type2_code`='DENSITY', `metric_type3_code`=NULL, `value_type`='FLT', `value_unit`='g/cm^3' WHERE (`id`='2568');
ALTER TABLE tb_alert_data MODIFY param_value FLOAT(11,2);
ALTER TABLE tb_alert_data MODIFY param_upper FLOAT(11,2);
ALTER TABLE tb_alert_data MODIFY param_lower FLOAT(11,2);
