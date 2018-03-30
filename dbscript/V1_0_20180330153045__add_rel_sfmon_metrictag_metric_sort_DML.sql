delete from tb_sfmon_signal_wrapper_rule where signal_wrapper_name='轴承温度';
insert into tb_sfmon_signal_wrapper_rule values(23,'部件控制2','auxiliary_area',1);
insert into tb_sfmon_signal_wrapper_rule values(24,'部件控制3','auxiliary_area',1);
insert into tb_sfmon_signal_wrapper_rule values(25,'参数设定','auxiliary_area',1);
insert into tb_sfmon_style values (10,'kz_2','部件控制2',null,'kz_2.png');
insert into tb_sfmon_style values (11,'kz_3','部件控制3',null,'kz_3.png');
insert into tb_sfmon_style values (12,'sd_1','参数设定',null,'sd_1.png');
delete from rel_sfmon_metrictag_metric_sort
where sort in (7,9) and metric_tag_name='状态控制';
update rel_sfmon_metrictag_metric_sort set sort=7
where metric_tag_name='状态控制' and sort=8;
update rel_sfmon_metrictag_metric_sort set sort=8
where metric_tag_name='状态控制' and sort=10;
update rel_sfmon_metrictag_metric_sort set sort=9
where metric_tag_name='状态控制' and sort=11;

update rel_sfmon_metrictag_metric_sort set sort=5
where metric_tag_name='设备保护' and metric_code='PK' and direction=1 ;

update rel_sfmon_metrictag_metric_sort set sort=8
where metric_tag_name='状态控制' and metric_code='LDS' and direction=1;

update rel_sfmon_metrictag_metric_sort set sort=18
where metric_tag_name='设备保护' and metric_code='LDF' and direction=2;

update rel_sfmon_metrictag_metric_sort set sort=14
where metric_tag_name='状态控制' and metric_code='USP1' and direction=2;

update rel_sfmon_metrictag_metric_sort set sort=15
where metric_tag_name='状态控制' and metric_code='USP2' and direction=2;

update rel_sfmon_metrictag_metric_sort set sort=16
where metric_tag_name='状态控制' and metric_code='US1' and direction=2;

update rel_sfmon_metrictag_metric_sort set sort=17
where metric_tag_name='状态控制' and metric_code='US2' and direction=2;

update rel_sfmon_metrictag_metric_sort set sort=10
where metric_tag_name='状态控制' and metric_code='PKH1' and direction=2;

update rel_sfmon_metrictag_metric_sort set sort=11
where metric_tag_name='状态控制' and metric_code='PKH2' and direction=2;

update rel_sfmon_metrictag_metric_sort set sort=12
where metric_tag_name='状态控制' and metric_code='PKH3' and direction=2;

update rel_sfmon_metrictag_metric_sort set sort=13
where metric_tag_name='状态控制' and metric_code='PKH4' and direction=2;

update rel_sfmon_metrictag_metric_sort set sort=6
where metric_tag_name='状态控制' and metric_code='PKL1' and direction=2;

update rel_sfmon_metrictag_metric_sort set sort=7
where metric_tag_name='状态控制' and metric_code='PKL2' and direction=2;

update rel_sfmon_metrictag_metric_sort set sort=8
where metric_tag_name='状态控制' and metric_code='PKL3' and direction=2;

update rel_sfmon_metrictag_metric_sort set sort=9
where metric_tag_name='状态控制' and metric_code='PKL4' and direction=2;

update rel_sfmon_metrictag_metric_sort set sort=5,metric_tag_name='温度'
where metric_tag_name='电机温度' and metric_code='ETEMP1';

update rel_sfmon_metrictag_metric_sort set sort=6,metric_tag_name='温度'
where metric_tag_name='电机温度' and metric_code='ETEMP2';

update rel_sfmon_metrictag_metric_sort set sort=7,metric_tag_name='温度'
where metric_tag_name='电机温度' and metric_code='ETEMP3';

update rel_sfmon_metrictag_metric_sort set sort=8,metric_tag_name='温度'
where metric_tag_name='电机温度' and metric_code='ETEMP4';

update rel_sfmon_metrictag_metric_sort set sort=9,metric_tag_name='温度'
where metric_tag_name='电机温度' and metric_code='ETEMP5';

update rel_sfmon_metrictag_metric_sort set sort=10,metric_tag_name='温度'
where metric_tag_name='轴承温度' and metric_code='TEMP_BE1';

update rel_sfmon_metrictag_metric_sort set sort=11,metric_tag_name='温度'
where metric_tag_name='轴承温度' and metric_code='TEMP_BE2';

update rel_sfmon_metrictag_metric_sort set sort=12,metric_tag_name='温度'
where metric_tag_name='轴承温度' and metric_code='TEMP_BE3';

update rel_sfmon_metrictag_metric_sort set sort=13,metric_tag_name='温度'
where metric_tag_name='轴承温度' and metric_code='TEMP_BE4';

update rel_sfmon_metrictag_metric_sort set sort=15
where metric_tag_name='温度' and metric_code='TEMP_C';

update rel_sfmon_metrictag_metric_sort set sort=14
where metric_tag_name='温度' and metric_code='TEMP_GE';

update rel_sfmon_metrictag_metric_sort set sort=16
where metric_tag_name='温度' and metric_code='TEMP_MD';

update rel_sfmon_metrictag_metric_sort set sort=17
where metric_tag_name='温度' and metric_code='TEMP_MGE';

update rel_sfmon_metrictag_metric_sort set sort=3
where metric_tag_name='部件控制' and metric_code='STOP_CMD';

update rel_sfmon_metrictag_metric_sort set sort=4
where metric_tag_name='部件控制' and metric_code='START_CMD';

update rel_sfmon_metrictag_metric_sort set sort=6
where metric_tag_name='电机连锁' and metric_code='STOP_CMD';

update rel_sfmon_metrictag_metric_sort set sort=7
where metric_tag_name='电机连锁' and metric_code='START_CMD';

update rel_sfmon_metrictag_metric_sort set sort=6
where metric_tag_name='提耙连锁' and metric_code='DO';
update rel_sfmon_metrictag_metric_sort set sort=7
where metric_tag_name='提耙连锁' and metric_code='UO';
update rel_sfmon_metrictag_metric_sort set sort=4
where metric_tag_name='操作按钮' and metric_code='LJ_STOP';
update rel_sfmon_metrictag_metric_sort set sort=3
where metric_tag_name='操作按钮' and metric_code='LJ_START';
update rel_sfmon_metrictag_metric_sort set sort=6
where metric_tag_name='操作按钮' and metric_code='ODL';
update rel_sfmon_metrictag_metric_sort set sort=5
where metric_tag_name='操作按钮' and metric_code='OUL';

insert into rel_sfmon_metrictag_metric_sort(metric_tag_name,metric_code,sort,direction,rule) values('部件控制2','STATE',1,0,2);
insert into rel_sfmon_metrictag_metric_sort(metric_tag_name,metric_code,sort,direction,rule) values('部件控制2','LOCAL',2,0,2);
insert into rel_sfmon_metrictag_metric_sort(metric_tag_name,metric_code,sort,direction,rule) values('部件控制2','STOP_CMD',3,0,2);
insert into rel_sfmon_metrictag_metric_sort(metric_tag_name,metric_code,sort,direction,rule) values('部件控制2','START_CMD',4,0,2);
insert into rel_sfmon_metrictag_metric_sort(metric_tag_name,metric_code,sort,direction,rule) values('部件控制3','STATE',1,0,2);
insert into rel_sfmon_metrictag_metric_sort(metric_tag_name,metric_code,sort,direction,rule) values('部件控制3','STOP_CMD',2,0,2);
insert into rel_sfmon_metrictag_metric_sort(metric_tag_name,metric_code,sort,direction,rule) values('部件控制3','START_CMD',3,0,2);
insert into rel_sfmon_metrictag_metric_sort(metric_tag_name,metric_code,sort,direction,rule) values('参数设定','SPEED',1,0,null);
insert into rel_sfmon_metrictag_metric_sort(metric_tag_name,metric_code,sort,direction,rule) values('参数设定','SPEED_SET',2,0,null);