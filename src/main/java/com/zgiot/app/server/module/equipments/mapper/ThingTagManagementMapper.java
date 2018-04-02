package com.zgiot.app.server.module.equipments.mapper;

import com.zgiot.app.server.module.equipments.pojo.ThingTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ThingTagManagementMapper {

    /**
     * 获取所有设备类型
     * @return
     */
    @Select("SELECT * FROM tb_thing_tag " +
            "WHERE parent_id in ( " +
            "SELECT id FROM tb_thing_tag " +
            "WHERE parent_id in ( " +
            "SELECT id FROM tb_thing_tag " +
            "WHERE parent_id in ( " +
            "SELECT id FROM tb_thing_tag " +
            "WHERE parent_id = 0)))")
    List<ThingTag> getAllEquipmentType();

    /**
     * 根据parentId获取一级子节点
     * @param id
     * @return
     */
    @Select("SELECT * FROM tb_thing_tag WHERE parent_id = #{id}")
    List<ThingTag> getThingTagByParentId(@Param("id") Long id);

}
