update tb_metric_tag set tag_name='远控阀门' where tag_name='远程阀门';
update rel_sfmon_metrictag_metric_sort set metric_tag_name='远控阀门'
where metric_tag_name='远程阀门';
update tb_sfmonitor_signal set metric_code='LEVELPROPORTION'
where thing_code in('1857.DY-1','1858.DY-1','1859.DY-1',
'2857.DY-1','2858.DY-1','2859.DY-1') and metric_code='LEVEL';
insert into
rel_sfmon_metrictag_metric_sort(metric_tag_name,metric_code,sort,direction,rule) values('液位连锁','LE_H_SET',2,0,3);
insert into rel_sfmon_metrictag_metric_sort(metric_tag_name,metric_code,sort,direction,rule) values('液位连锁','LE_L_SET',3,0,3);
delete from tb_sfmon_signal_wrapper_rule where signal_wrapper_name='电机温度';
commit;