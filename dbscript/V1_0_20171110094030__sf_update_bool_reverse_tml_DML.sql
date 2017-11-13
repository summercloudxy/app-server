update rel_thing_metric_label set bool_reverse=1 where metric_code='LOCAL' and thing_code in('2492','2493','2494','2495','2496','2496A');
commit;