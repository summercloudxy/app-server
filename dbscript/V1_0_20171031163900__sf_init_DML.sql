UPDATE tb_metric SET  metric_type2_code='ZD' WHERE metric_name='震动';
UPDATE tb_metric SET  metric_type2_code='TORQUE' WHERE metric_name='当前扭矩';
UPDATE rel_thing_metric_label SET enabled = 1;