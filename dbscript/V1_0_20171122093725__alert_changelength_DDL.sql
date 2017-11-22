ALTER  TABLE  `tb_alert_message` MODIFY user_id VARCHAR(30);
ALTER  TABLE  `tb_alert_data` MODIFY reporter VARCHAR(30);

ALTER  TABLE  `tb_alert_data` MODIFY post_worker VARCHAR(30);
ALTER  TABLE  `tb_alert_data` MODIFY dispatcher VARCHAR(30);

ALTER  TABLE  `tb_alert_data` MODIFY repair_confirm_user VARCHAR(30);
ALTER  TABLE  `tb_alert_data` MODIFY scene_confirm_user VARCHAR(30);