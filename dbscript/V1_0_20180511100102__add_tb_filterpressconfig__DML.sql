insert into tb_filterpress_config(thing_code,param_name,param_value) values('SYS_R_BAN_RUN','YL_LS_TERM1','1502');
insert into tb_filterpress_config(thing_code,param_name,param_value) values('SYS_R_BAN_RUN','YL_LS_TERM2','2502');
INSERT INTO `tb_metric`(metric_category_code,metric_name,metric_code,metric_type1_code,value_type) VALUES ('SIG', '远程压板控制', 'R_BAN_RUN', 'SS','SHT');
commit;