update rel_sfmon_metrictag_metric_sort set sort=1
where metric_tag_name='设备保护' and metric_code='SPT';
update rel_sfmon_metrictag_metric_sort set sort=2
where metric_tag_name='设备保护' and metric_code='CB';
update rel_sfmon_metrictag_metric_sort set sort=3
where metric_tag_name='设备保护' and metric_code='DM';
update rel_sfmon_metrictag_metric_sort set sort=4
where metric_tag_name='设备保护' and metric_code='FG';
update rel_sfmon_metrictag_metric_sort set sort=5
where metric_tag_name='设备保护' and metric_code='PK';
update rel_sfmon_metrictag_metric_sort set sort=6
where metric_tag_name='设备保护' and metric_code='US';
update rel_sfmon_metrictag_metric_sort set sort=7
where metric_tag_name='设备保护' and metric_code='US2';
update rel_sfmon_metrictag_metric_sort set sort=8
where metric_tag_name='设备保护' and metric_code='LDS';

update rel_sfmon_metrictag_metric_sort set sort=1
where metric_tag_name='设备保护' and metric_code='SPTF';
update rel_sfmon_metrictag_metric_sort set sort=2
where metric_tag_name='设备保护' and metric_code='CBF';
update rel_sfmon_metrictag_metric_sort set sort=3
where metric_tag_name='设备保护' and metric_code='DMP';
update rel_sfmon_metrictag_metric_sort set sort=4
where metric_tag_name='设备保护' and metric_code='DMF';
update rel_sfmon_metrictag_metric_sort set sort=5
where metric_tag_name='设备保护' and metric_code='FGF';
update rel_sfmon_metrictag_metric_sort set sort=6
where metric_tag_name='设备保护' and metric_code='PKL1';
update rel_sfmon_metrictag_metric_sort set sort=7
where metric_tag_name='设备保护' and metric_code='PKL2';
update rel_sfmon_metrictag_metric_sort set sort=8
where metric_tag_name='设备保护' and metric_code='PKL3';
update rel_sfmon_metrictag_metric_sort set sort=9
where metric_tag_name='设备保护' and metric_code='PKL4';
update rel_sfmon_metrictag_metric_sort set sort=10
where metric_tag_name='设备保护' and metric_code='PKH1';
update rel_sfmon_metrictag_metric_sort set sort=11
where metric_tag_name='设备保护' and metric_code='PKH2';
update rel_sfmon_metrictag_metric_sort set sort=12
where metric_tag_name='设备保护' and metric_code='PKH3';
update rel_sfmon_metrictag_metric_sort set sort=13
where metric_tag_name='设备保护' and metric_code='PKH4';
update rel_sfmon_metrictag_metric_sort set sort=14
where metric_tag_name='设备保护' and metric_code='USP1';
update rel_sfmon_metrictag_metric_sort set sort=15
where metric_tag_name='设备保护' and metric_code='USP2';
update rel_sfmon_metrictag_metric_sort set sort=16
where metric_tag_name='设备保护' and metric_code='US1';
update rel_sfmon_metrictag_metric_sort set sort=17
where metric_tag_name='设备保护' and metric_code='USF2';
update rel_sfmon_metrictag_metric_sort set sort=18
where metric_tag_name='设备保护' and metric_code='LDF';

update rel_sfmon_metrictag_metric_sort set sort=1,metric_code='START_ALL'
where metric_tag_name='操作按钮' and metric_code='SPEED';
update rel_sfmon_metrictag_metric_sort set sort=2,metric_code='STOP_ALL'
where metric_tag_name='操作按钮' and metric_code='SPEED_SET';
commit;