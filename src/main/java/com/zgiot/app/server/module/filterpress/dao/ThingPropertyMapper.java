package com.zgiot.app.server.module.filterpress.dao;

import com.zgiot.common.pojo.ThingPropertyModel;
import com.zgiot.common.pojo.ThingModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ThingPropertyMapper {
    /*
    * 根据thingCode查找thing基本信息
    * */
    public ThingModel getBasePropertiy(@Param("thingCode") String thingCode);

    /*
    * 根据thingCode查找thing属性信息和属性的显示信息
    * */
    public List<ThingPropertyModel> getPropOrDisPropPropertiy(@Param("thingCode") String thingCode, @Param("propType") String proType);

    /*
    * 查找所有设备信息
    * */
    public List<ThingModel> getThings();
}
