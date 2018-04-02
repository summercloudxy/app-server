UPDATE rel_historydata_whitelist set minute_summary =1 where metric_code='COAL_CAP' and thing_code in('1301','1302','2302','2301','701','702','1552','2552','1554','1555','2554','2555');

UPDATE `tb_subsc_card_type` SET `card_name` = '一期智能压滤', `card_code` = 'znyl01', `card_source` = '2', `update_date` = NULL, `update_user` = NULL, `card_type` = 'znyl', `card_param_value` = '1601.DJ.QD-1,1602.DJ.QD-1,1603.DJ.QD-1,2492+2493+2494+2495+2496+2496A', `remark` = NULL, `picurl` = 'ympb02.png' WHERE `card_id` = 15;
UPDATE `tb_subsc_card_type` SET `card_name` = '二期智能压滤', `card_code` = 'znyl02', `card_source` = '2', `update_date` = NULL, `update_user` = NULL, `card_type` = 'znyl', `card_param_value` = '2601.DJ.QD-1,2602.DJ.QD-1,,2492+2493+2494+2495+2496+2496A', `remark` = NULL, `picurl` = 'ssrxl01.png' WHERE `card_id` = 16;

delete from rel_historydata_whitelist WHERE metric_code='CURRENT' and thing_code in('1601','1602','1603','2601','2602');

INSERT INTO `rel_historydata_whitelist` (`thing_code`, `metric_code`, `to_store`) VALUES ('1601.DJ.QD-1', 'CURRENT', '1');
INSERT INTO `rel_historydata_whitelist` (`thing_code`, `metric_code`, `to_store`) VALUES ('1602.DJ.QD-1', 'CURRENT', '1');
INSERT INTO `rel_historydata_whitelist` (`thing_code`, `metric_code`, `to_store`) VALUES ('1603.DJ.QD-1', 'CURRENT', '1');
INSERT INTO `rel_historydata_whitelist` (`thing_code`, `metric_code`, `to_store`) VALUES ('2601.DJ.QD-1', 'CURRENT', '1');
INSERT INTO `rel_historydata_whitelist` (`thing_code`, `metric_code`, `to_store`) VALUES ('2602.DJ.QD-1', 'CURRENT', '1');
