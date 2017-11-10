alter table `tb_thing_position` drop index unq_code ;
ALTER  TABLE  `tb_thing_position`  ADD  INDEX thing_code (  `thing_code`,  `building_id`,  `floor`  );
ALTER  TABLE  `rel_thing_system`  ADD  INDEX thing_code (  `thing_code`,  `system_id` );
alter table `tb_alert_data` drop index alert_type ;
ALTER  TABLE  `tb_alert_data`  ADD  INDEX thing_code (  `thing_code`,  `alert_type` ,`alert_stage` ,`alert_level` ,`alert_datetime` );