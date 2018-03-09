package com.zgiot.app.server.module.sfsubsc.mapper;

import com.zgiot.app.server.module.sfsubsc.entity.pojo.SubscCardTypeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author jys
 * 卡片配置
 */
@Mapper
public interface SubscCardTypeMapper {
    /**
     * 查询所有的订阅开篇配置
     *
     * @return
     */
    @Select("select * from tb_subsc_card_type")
    List<SubscCardTypeDO> getAllSubscCardTypes();
}
