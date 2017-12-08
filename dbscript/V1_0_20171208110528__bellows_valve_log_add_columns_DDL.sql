ALTER TABLE `tb_bellows_valve_log`
ADD COLUMN `team_id`  bigint NULL COMMENT '分组号' AFTER `memo`,
ADD COLUMN `name`  varchar(30) NULL COMMENT '设备名称' AFTER `team_id`;