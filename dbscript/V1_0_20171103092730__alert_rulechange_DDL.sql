UPDATE `tb_alert_rule` set metric_code = 'DMP' WHERE metric_code = 'DMF';
UPDATE `tb_alert_rule` set metric_code = 'INV_ALMP' WHERE metric_code = 'INV_ALM';
UPDATE `tb_alert_rule` set metric_code = 'INV_PRP' WHERE metric_code = 'INV_PR';
UPDATE `tb_alert_rule` set metric_code = 'TRIPP' WHERE metric_code = 'TRIP';
UPDATE `tb_alert_rule` set metric_code = 'USP1' WHERE metric_code = 'US1';
UPDATE `tb_alert_rule` set metric_code = 'USP2' WHERE metric_code = 'USF2';