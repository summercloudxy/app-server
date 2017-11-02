delete from tb_metric where metric_code='FEED_ASUM';
update tb_metric set metric_code='FEED_ASUM' where metric_code='Feeding_E_V';
update rel_thing_metric_label set metric_code='FEED_ASUM' where metric_code='Feeding_E_V';