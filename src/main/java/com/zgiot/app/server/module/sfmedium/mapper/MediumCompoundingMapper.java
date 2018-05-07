package com.zgiot.app.server.module.sfmedium.mapper;

import com.zgiot.app.server.module.sfmedium.entity.po.MediumCompoundingConfigDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MediumCompoundingMapper {
    /**
     * 根据介质池查询配介液位和最低液位
     *
     * @param mediumPoolCode
     * @return
     */
    @Select("select * from tb_medium_compounding_config where medium_pool_code =#{mediumPoolCode}")
    MediumCompoundingConfigDO getMediumCompoundingConfigByPoolCode(@Param("mediumPoolCode") String mediumPoolCode);
}
