  alter table tb_filterpress_log drop column statistic_log_plate_count;
	alter table tb_filterpress_log drop column statistic_log_total_plate_count;
	alter table tb_filterpress_log modify feed_start_time datetime DEFAULT NULL;
	alter table tb_filterpress_log modify feed_duration bigint(50) DEFAULT NULL;
	alter table tb_filterpress_log modify feed_current double(20,0) DEFAULT NULL;
	alter table tb_filterpress_log modify unload_start_time datetime DEFAULT NULL;
	alter table tb_filterpress_log modify unload_duration bigint(50) DEFAULT NULL;
	alter table tb_filterpress_log modify waiting_time bigint(50) DEFAULT NULL;
	alter table tb_filterpress_log modify  proceeding_time bigint(50) DEFAULT NULL;
	alter table tb_filterpress_log modify  proceeding_time bigint(50) DEFAULT NULL;
	commit;