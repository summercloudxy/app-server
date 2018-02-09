insert into rel_thingtag_thing(thing_code) select thing_code from tb_thing where thing_type1_code='DEVICE';
update rel_thingtag_thing set thing_tag_code='EQUIPMENT';