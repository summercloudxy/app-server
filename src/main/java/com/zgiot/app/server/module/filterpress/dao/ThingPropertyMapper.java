package com.zgiot.app.server.module.filterpress.dao;

import com.zgiot.app.server.module.filterpress.pojo.BaseProperty;
import com.zgiot.app.server.module.filterpress.pojo.PropOrDisProp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ThingPropertyMapper {
    //根据thingCode查找thing基本信息
    public BaseProperty getBasePropertiy(@Param("thingCode") String thingCode);

    //根据thingCode查找thing属性信息和属性的显示信息
    public List<PropOrDisProp> getPropOrDisPropPropertiy(@Param("thingCode") String thingCode, @Param("prop_type") String pro_type);

    //查找所有设备信息
    public List<BaseProperty> getThings();
}
