alter TABLE excel_range ADD excel_type VARCHAR(10);
UPDATE excel_range set excel_type = "化验班报";

ALTER TABLE excel_range ADD `-1.45_gap` TINYINT(4);
ALTER TABLE excel_range ADD `1.45-1.8_gap` TINYINT(4);
ALTER TABLE excel_range ADD `+1.8_gap` TINYINT(4);
ALTER TABLE excel_range ADD `+1.45_gap` TINYINT(4);
ALTER TABLE excel_range ADD `-1.8_gap` TINYINT(4);
ALTER TABLE excel_range ADD `+50mm_gap` TINYINT(4);
ALTER TABLE excel_range ADD `-50mm_gap` TINYINT(4);
