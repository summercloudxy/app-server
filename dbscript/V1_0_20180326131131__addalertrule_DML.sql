

UPDATE `tb_param_range` SET  `upper_limit`='1600.00' WHERE ( `thing_code`='1301'  AND `metric_code`='COAL_CAP');
UPDATE `tb_param_range` SET  `upper_limit`='1600.00' WHERE ( `thing_code`='2301'  AND `metric_code`='COAL_CAP');
UPDATE `tb_param_range` SET  `upper_limit`='1600.00' WHERE ( `thing_code`='1302'  AND `metric_code`='COAL_CAP');
UPDATE `tb_param_range` SET  `upper_limit`='1600.00' WHERE ( `thing_code`='2302'  AND `metric_code`='COAL_CAP');
UPDATE `tb_alert_rule` SET `lower_limit`='1200.00',`upper_limit`='1600.00' WHERE (`thing_code`='1301'AND `metric_code`='COAL_CAP'AND `rule_type`='1'AND `alert_level`='30'AND `lower_limit`='1635.00'AND `upper_limit`='1800.00');
UPDATE `tb_alert_rule` SET `lower_limit`='1200.00',`upper_limit`='1600.00' WHERE (`thing_code`='2301'AND `metric_code`='COAL_CAP'AND `rule_type`='1'AND `alert_level`='30'AND `lower_limit`='1635.00'AND `upper_limit`='1800.00');
UPDATE `tb_alert_rule` SET `lower_limit`='1200.00',`upper_limit`='1600.00' WHERE (`thing_code`='1302'AND `metric_code`='COAL_CAP'AND `rule_type`='1'AND `alert_level`='30'AND `lower_limit`='1635.00'AND `upper_limit`='1800.00');
UPDATE `tb_alert_rule` SET `lower_limit`='1200.00',`upper_limit`='1600.00' WHERE (`thing_code`='2302'AND `metric_code`='COAL_CAP'AND `rule_type`='1'AND `alert_level`='30'AND `lower_limit`='1635.00'AND `upper_limit`='1800.00');
INSERT INTO `tb_alert_rule` ( `thing_code`, `metric_code`, `rule_type`, `alert_level`, `lower_limit`, `upper_limit`, `enable`) VALUES ( '1301', 'COAL_CAP', '1', '30', '0.00', '1000.00', '1');
INSERT INTO `tb_alert_rule` ( `thing_code`, `metric_code`, `rule_type`, `alert_level`, `lower_limit`, `upper_limit`, `enable`) VALUES ( '1302', 'COAL_CAP', '1', '30', '0.00', '1000.00', '1');
INSERT INTO `tb_alert_rule` ( `thing_code`, `metric_code`, `rule_type`, `alert_level`, `lower_limit`, `upper_limit`, `enable`) VALUES ( '2301', 'COAL_CAP', '1', '30', '0.00', '1000.00', '1');
INSERT INTO `tb_alert_rule` ( `thing_code`, `metric_code`, `rule_type`, `alert_level`, `lower_limit`, `upper_limit`, `enable`) VALUES ( '2302', 'COAL_CAP', '1', '30', '0.00', '1000.00', '1');
INSERT INTO `tb_alert_rule` ( `thing_code`, `metric_code`, `rule_type`, `alert_level`, `lower_limit`, `upper_limit`, `enable`) VALUES ( 'Quit_SYS_1', 'COAL_CAP', '1', '30', '0.00', '2000.00', '1');
INSERT INTO `tb_alert_rule` ( `thing_code`, `metric_code`, `rule_type`, `alert_level`, `lower_limit`, `upper_limit`, `enable`) VALUES ( 'Quit_SYS_1', 'COAL_CAP', '1', '30', '2400.00', '3200.00', '1');
INSERT INTO `tb_alert_rule` ( `thing_code`, `metric_code`, `rule_type`, `alert_level`, `lower_limit`, `upper_limit`, `enable`) VALUES ( 'Quit_SYS_2', 'COAL_CAP', '1', '30', '0.00', '2000.00', '1');
INSERT INTO `tb_alert_rule` ( `thing_code`, `metric_code`, `rule_type`, `alert_level`, `lower_limit`, `upper_limit`, `enable`) VALUES ( 'Quit_SYS_2', 'COAL_CAP', '1', '30', '2400.00', '3200.00', '1');
INSERT INTO `tb_param_range` ( `thing_code`, `metric_code`, `lower_limit`, `upper_limit`) VALUES ( 'Quit_SYS_1', 'COAL_CAP', '0.00', '3200.00');
INSERT INTO `tb_param_range` ( `thing_code`, `metric_code`, `lower_limit`, `upper_limit`) VALUES ( 'Quit_SYS_2', 'COAL_CAP', '0.00', '3200.00');
