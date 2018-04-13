UPDATE `rel_historydata_whitelist` SET  `metric_code` = 'COAL_8_DEVICE' WHERE `thing_code` = 'Quit_SYS_1';
UPDATE `rel_historydata_whitelist` SET  `metric_code` = 'COAL_13_DEVICE' WHERE `thing_code` = 'Quit_SYS_2';

UPDATE `smartfactory2`.`tb_subsc_card_type` SET `card_name` = '一期智能鼓风', `card_code` = 'zngf01', `card_source` = '2', `update_date` = NULL, `update_user` = NULL, `card_type` = 'zngf', `card_param_value` = '1510,1511,1512,1530,1531,1532,1333,1334', `remark` = NULL, `picurl` = 'ssrxl02.png' WHERE `card_code` = 'zngf01';
UPDATE `smartfactory2`.`tb_subsc_card_type` SET `card_name` = '二期产品产率', `card_code` = 'cpcl02', `card_source` = '1', `update_date` = NULL, `update_user` = NULL, `card_type` = 'cpcl', `card_param_value` = '2301+2302,701,2552,2553', `remark` = NULL, `picurl` = 'cpcl02.png' WHERE `card_id` = 'cpcl02';
