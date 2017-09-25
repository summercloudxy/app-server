update tb_thing_properties set prop_type='PROP'
where thing_code in('2502','2553','2529','2492','2485','2497','2487','2493','2498','2488','2494','2486','2499','2489','2495','2500','2490','2496','2501','2491','2496A')
and prop_key!='IMAGE_NAME';

update tb_thing_properties set prop_type='DISP_PROP'
where thing_code in('2502','2553','2529','2492','2485','2497','2487','2493','2498','2488','2494','2486','2499','2489','2495','2500','2490','2496','2501','2491','2496A')
and prop_key='IMAGE_NAME';





