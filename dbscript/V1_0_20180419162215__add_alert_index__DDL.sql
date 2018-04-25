ALTER  TABLE  `tb_alert_data`  ADD  INDEX thing_level_stage ( `thing_code`, `alert_level`  ,`alert_stage`  );
ALTER  TABLE  `tb_alert_data`  ADD  INDEX level_stage (`alert_level`  ,`alert_stage`  );
