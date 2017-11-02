
CREATE TABLE `rel_historydata_whitelist` (
  `thing_code` varchar(20) NOT NULL,
  `metric_code` varchar(20) NOT NULL,
  `to_store` tinyint(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8
