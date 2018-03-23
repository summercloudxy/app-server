package com.zgiot.app.server.module.qualityandquantity.mapper;

import com.zgiot.app.server.module.qualityandquantity.pojo.AreaInfo;
import com.zgiot.app.server.module.qualityandquantity.pojo.CardInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QualityAndQuantityMapper {
    List<CardInfo> getCardInfosInArea(int areaId);
    AreaInfo getAreaInfo(int areaId);
}
