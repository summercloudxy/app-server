/*
系统监控thingtag排序数据
*/

ALTER TABLE `tb_thing_tag`  ADD COLUMN `sort` float(11,1) DEFAULT 0 COMMENT '排序序号';
update tb_thing_tag set sort =1 where code ='SAVECOAL';
update tb_thing_tag set sort =2 where code ='READYCOAL';
update tb_thing_tag set sort =1 where code ='LUMPCOAL';
update tb_thing_tag set sort =2 where code ='FINAL-COAL-MAIN';
update tb_thing_tag set sort =3 where code ='FINAL-COAL-AGAIN';
update tb_thing_tag set sort =4 where code ='TCS-WASH';
update tb_thing_tag set sort =5 where code ='FILTERPRESS';
update tb_thing_tag set sort =6 where code ='BELLOWS';
update tb_thing_tag set sort =7 where code ='CONCENTRATION';
update tb_thing_tag set sort =8 where code ='SEDIMENTATION';
update tb_thing_tag set sort =9 where code ='PRODUCTION';
update tb_thing_tag set sort =10 where code ='WASHINGPUBLIC';
update tb_thing_tag set sort =11 where code ='COALBYPASS';
update tb_thing_tag set sort =1 where code ='FASTLOADING';
update tb_thing_tag set sort =2 where code ='TRANSPORTATION';

update rel_sfsysmon_term_thing set thing_code ='Quit_SYS_RAW' where thing_code='Quit_SYS';