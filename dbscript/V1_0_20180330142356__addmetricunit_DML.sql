UPDATE `tb_metric` SET   `value_unit`='g/cm^3' WHERE (`metric_code`='AVG_DENSITY');
UPDATE `tb_metric` SET   `value_unit`='g/cm^3' WHERE (`metric_code`='ASSAY_DENSITY');
UPDATE `tb_metric` SET   `value_unit`='g/cm^3' WHERE (`metric_code`='PRODUCT_INSPECT_DENSITY');
UPDATE `tb_metric` SET   `value_unit`='g/cm^3' WHERE (`metric_code`='PRODUCT_INSPECT_DENSITY_AVG');
UPDATE `tb_metric` SET   `value_unit`='g/cm^3' WHERE (`metric_code`='ASSAY_DENSITY_AVG');

UPDATE `tb_metric` SET  `metric_category_code`='PRODUCTION_INSPECT', `metric_name`='+1.45平均', `metric_code`='POSITIVE1_POINT45_AVG', `metric_type1_code`=NULL, `metric_type2_code`=NULL, `metric_type3_code`=NULL, `value_type`=NULL, `value_unit`='kg/L' WHERE (`metric_code`='POSITIVE1_POINT45_AVG');
UPDATE `tb_metric` SET  `metric_category_code`='PRODUCTION_INSPECT', `metric_name`='-1.45平均', `metric_code`='NEGATIVE1_POINT45_AVG', `metric_type1_code`=NULL, `metric_type2_code`=NULL, `metric_type3_code`=NULL, `value_type`=NULL, `value_unit`='kg/L' WHERE (`metric_code`='NEGATIVE1_POINT45_AVG');
UPDATE `tb_metric` SET  `metric_category_code`='PRODUCTION_INSPECT', `metric_name`='+1.8平均', `metric_code`='POSITIVE1_POINT8_AVG', `metric_type1_code`=NULL, `metric_type2_code`=NULL, `metric_type3_code`=NULL, `value_type`=NULL, `value_unit`='kg/L' WHERE (`metric_code`='POSITIVE1_POINT8_AVG');
UPDATE `tb_metric` SET  `metric_category_code`='PRODUCTION_INSPECT', `metric_name`='-1.8平均', `metric_code`='NEGATIVE1_POINT8_AVG', `metric_type1_code`=NULL, `metric_type2_code`=NULL, `metric_type3_code`=NULL, `value_type`=NULL, `value_unit`='kg/L' WHERE (`metric_code`='NEGATIVE1_POINT8_AVG');
UPDATE `tb_metric` SET  `metric_category_code`='PRODUCTION_INSPECT', `metric_name`='1.45-1.8平均', `metric_code`='ONE_POINT45_TO1_POINT8_AVG', `metric_type1_code`=NULL, `metric_type2_code`=NULL, `metric_type3_code`=NULL, `value_type`=NULL, `value_unit`='kg/L' WHERE (`metric_code`='ONE_POINT45_TO1_POINT8_AVG');
UPDATE `tb_metric` SET  `metric_category_code`='PRODUCTION_INSPECT', `metric_name`='+50mm平均', `metric_code`='POSITIVE50MM_AVG', `metric_type1_code`=NULL, `metric_type2_code`=NULL, `metric_type3_code`=NULL, `value_type`=NULL, `value_unit`='mm' WHERE (`metric_code`='POSITIVE50MM_AVG');
UPDATE `tb_metric` SET  `metric_category_code`='PRODUCTION_INSPECT', `metric_name`='-50mm平均', `metric_code`='NEGATIVE50MM_AVG', `metric_type1_code`=NULL, `metric_type2_code`=NULL, `metric_type3_code`=NULL, `value_type`=NULL, `value_unit`='mm' WHERE (`metric_code`='NEGATIVE50MM_AVG');

UPDATE `tb_metric` SET  `metric_category_code`='ASSAY', `metric_name`='灰分平均', `metric_code`='AAD_AVG', `metric_type1_code`=NULL, `metric_type2_code`=NULL, `metric_type3_code`=NULL, `value_type`=NULL, `value_unit`='%' WHERE (`metric_code`='AAD_AVG');
UPDATE `tb_metric` SET  `metric_category_code`='ASSAY', `metric_name`='水分平均', `metric_code`='MT_AVG', `metric_type1_code`=NULL, `metric_type2_code`=NULL, `metric_type3_code`=NULL, `value_type`=NULL, `value_unit`='%' WHERE (`metric_code`='MT_AVG');
UPDATE `tb_metric` SET  `metric_category_code`='ASSAY', `metric_name`='硫份平均', `metric_code`='STAD_AVG', `metric_type1_code`=NULL, `metric_type2_code`=NULL, `metric_type3_code`=NULL, `value_type`=NULL, `value_unit`='%' WHERE (`metric_code`='STAD_AVG');

UPDATE `tb_metric` SET  `metric_category_code`='ASSAY', `metric_name`='发热量', `metric_code`='QNETAR', `metric_type1_code`=NULL, `metric_type2_code`=NULL, `metric_type3_code`=NULL, `value_type`=NULL, `value_unit`='kg' WHERE (`metric_code`='QNETAR');
UPDATE `tb_metric` SET  `metric_category_code`='ASSAY', `metric_name`='发热量平均', `metric_code`='QNETAR_AVG', `metric_type1_code`=NULL, `metric_type2_code`=NULL, `metric_type3_code`=NULL, `value_type`=NULL, `value_unit`='kg' WHERE (`metric_code`='QNETAR_AVG');

UPDATE `tb_metric` SET  `metric_category_code`='ASSAY', `metric_name`='煤质化验流量', `metric_code`='ASSAY_FLOW', `metric_type1_code`=NULL, `metric_type2_code`=NULL, `metric_type3_code`=NULL, `value_type`=NULL, `value_unit`='m^3/h' WHERE (`metric_code`='ASSAY_FLOW');
UPDATE `tb_metric` SET  `metric_category_code`='ASSAY', `metric_name`='煤质化验流量平均', `metric_code`='ASSAY_FLOW_AVG', `metric_type1_code`=NULL, `metric_type2_code`=NULL, `metric_type3_code`=NULL, `value_type`=NULL, `value_unit`='m^3/h' WHERE (`metric_code`='ASSAY_FLOW_AVG');




