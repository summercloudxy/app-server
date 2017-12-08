ALTER  TABLE  `rel_metric_alert_type` MODIFY metric_code VARCHAR(30);
ALTER  TABLE  `tb_alert_rule` MODIFY metric_code VARCHAR(30);
ALTER  TABLE  `tb_param_range` MODIFY metric_code VARCHAR(30);

UPDATE tb_metric
SET metric_name = '当前液位（m）'
WHERE metric_code = 'LEVEL';
INSERT INTO tb_metric (`metric_category_code`, `metric_name`, `metric_code`, `metric_type1_code`, `metric_type2_code`, `metric_type3_code`, `value_type`, `value_unit`)
VALUES ('SIG', '当前液位（%）', 'LEVELPROPORTION', 'PR', 'LEVEL', NULL, 'FLT', '%');

INSERT INTO rel_metric_alert_type (`metric_code`, `alert_type`) VALUES ('LEVELPROPORTION', '1');

UPDATE tb_alert_rule
SET metric_code = 'LEVELPROPORTION'
WHERE metric_code = 'LEVEL' AND
      thing_code IN ('1857.DY-1', '1858.DY-1', '1859.DY-1', '2857.DY-1', '2858.DY-1', '2859.DY-1');

UPDATE tb_param_range
SET metric_code = 'LEVELPROPORTION'
WHERE metric_code = 'LEVEL' AND
      thing_code IN ('1857.DY-1', '1858.DY-1', '1859.DY-1', '2857.DY-1', '2858.DY-1', '2859.DY-1');

UPDATE `tb_alert_rule`
SET `lower_limit` = '130.00'
WHERE `thing_code` = '1436' AND `metric_code` = 'PRESS_CUR' AND `lower_limit` = '150.00' AND `upper_limit` = '150.00';
UPDATE `tb_alert_rule`
SET `lower_limit` = '130.00'
WHERE `thing_code` = '1437' AND `metric_code` = 'PRESS_CUR' AND `lower_limit` = '150.00' AND `upper_limit` = '150.00';
UPDATE `tb_alert_rule`
SET `lower_limit` = '130.00'
WHERE `thing_code` = '1438' AND `metric_code` = 'PRESS_CUR' AND `lower_limit` = '150.00' AND `upper_limit` = '150.00';
UPDATE `tb_alert_rule`
SET `lower_limit` = '130.00'
WHERE `thing_code` = '2436' AND `metric_code` = 'PRESS_CUR' AND `lower_limit` = '150.00' AND `upper_limit` = '150.00';
UPDATE `tb_alert_rule`
SET `lower_limit` = '130.00'
WHERE `thing_code` = '2437' AND `metric_code` = 'PRESS_CUR' AND `lower_limit` = '150.00' AND `upper_limit` = '150.00';
UPDATE `tb_alert_rule`
SET `lower_limit` = '130.00'
WHERE `thing_code` = '2438' AND `metric_code` = 'PRESS_CUR' AND `lower_limit` = '150.00' AND `upper_limit` = '150.00';
UPDATE `tb_alert_rule`
SET `lower_limit` = '145.00'
WHERE `thing_code`='2604' AND `metric_code`='CURRENT'AND `lower_limit`='251.00'AND `upper_limit`='161.00';
UPDATE `tb_alert_rule`
SET `lower_limit` = '145.00'
WHERE `thing_code`='2605' AND `metric_code`='CURRENT'AND `lower_limit`='251.00'AND `upper_limit`='161.00';
UPDATE `tb_alert_rule`
SET `lower_limit` = '256.00'
WHERE `thing_code`='2606' AND `metric_code`='CURRENT'AND `lower_limit`='456.00'AND `upper_limit`='292.00';


