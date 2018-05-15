update tb_metric set metric_name='连锁' where metric_name='一期压滤连锁';
delete from tb_metric where metric_name='二期压滤连锁';
insert into tb_thing(thing_code,thing_name) values('SYS_1_YL','一期压滤机');
insert into tb_thing(thing_code,thing_name) values('SYS_2_YL','二期压滤机');
commit;