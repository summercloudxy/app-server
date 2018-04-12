package com.zgiot.app.server.module.equipments.mapper;

import com.zgiot.app.server.module.equipments.pojo.ThingBaseDict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ThingBaseDictMapper {

    /**
     * 根据key获取对象集合
     *
     * @return
     */
    @Select("SELECT * FROM tb_thing_base_dict AS t WHERE t.key = #{key}")
    List<ThingBaseDict> getThingBaseDictListByKey(@Param("key") String key);

}
