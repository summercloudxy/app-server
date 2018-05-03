INSERT INTO `tb_metric`(metric_category_code,metric_name,metric_code,metric_type1_code,value_type) VALUES ('SIG', '同时向一期', 'CONFLUENCE_TERM1', 'SS','BOO');
INSERT INTO `tb_metric`(metric_category_code,metric_name,metric_code,metric_type1_code,value_type) VALUES ('SIG', '同时向二期', 'CONFLUENCE_TERM2', 'SS','BOO');
INSERT INTO `tb_metric`(metric_category_code,metric_name,metric_code,metric_type1_code,value_type) VALUES ('SIG', '单独向一期', 'ONE_DIRECTION_TERM1', 'SS','BOO');
INSERT INTO `tb_metric`(metric_category_code,metric_name,metric_code,metric_type1_code,value_type) VALUES ('SIG', '单独向二期', 'ONE_DIRECTION_TERM2', 'SS','BOO');

INSERT INTO `tb_metric`(metric_category_code,metric_name,metric_code,metric_type1_code,value_type) VALUES ('SIG', '正在向一期串料', 'CL_TERM1', 'S','BOO');
INSERT INTO `tb_metric`(metric_category_code,metric_name,metric_code,metric_type1_code,value_type) VALUES ('SIG', '正在向二期串料', 'CL_TERM2', 'S','BOO');
INSERT INTO `tb_metric`(metric_category_code,metric_name,metric_code,metric_type1_code,value_type) VALUES ('SIG', '选择闭锁', 'XZ__BS', 'SS','BOO');
INSERT INTO `tb_metric`(metric_category_code,metric_name,metric_code,metric_type1_code,value_type) VALUES ('SIG', '取消串料', 'CL_OFF', 'SS','BOO');
commit;