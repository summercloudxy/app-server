package com.zgiot.app.server.module.sfsubsc.mapper;

import com.zgiot.app.server.module.sfsubsc.entity.pojo.SubscCardStore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SubCardStoreMapper {

    @Select("select * from tb_subsc_card_store")
    List<SubscCardStore> getAllSubscCardStore();

}
