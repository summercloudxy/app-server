delete from tb_metric where metric_code='FEED_ASUM' and metric_name='进料结束系统判断';
update tb_metric set metric_code='FEED_ASUM' where metric_code='Feeding_E_V';
COMMIT;
