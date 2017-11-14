
ALTER  TABLE  `tb_alert_message`  ADD  INDEX alert_id (  `alert_id`  );

alter table `tb_alert_data` drop index thing_code ;
ALTER  TABLE  `tb_alert_data`  ADD  INDEX thing_code ( `id`, `thing_code`,  `alert_type` ,`alert_stage` ,`alert_level` ,`alert_datetime` );