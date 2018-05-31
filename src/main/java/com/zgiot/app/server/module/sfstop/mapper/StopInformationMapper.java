package com.zgiot.app.server.module.sfstop.mapper;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopInformation;
import org.apache.ibatis.annotations.*;

import java.util.List;

import java.util.List;

/**
 * 停车设备
 */
@Mapper
public interface StopInformationMapper {

    /**
     * 查询停车包下的停车设备
     *
     * @param bagId
     * @return
     */
    @Select("select * from tb_stop_information where is_delete=0 and bag_id =#{bagId}")
    List<StopInformation> getStopInformationByBagId(@Param("bagId") Long bagId);

    @Select("select * from tb_stop_information where is_delete=0 and bag_id IS NULL")
    List<StopInformation> getStopInformationByNoBagId();

    void updateStopInformation(StopInformation stopInformation);

    @Select("SELECT * FROM `tb_stop_information` where is_delete=0 and thing_code=#{thingCode} LIMIT 1")
    StopInformation getStopInformationByTC(StopInformation stopInformation);

    @Select("select * from tb_stop_information where is_delete=0")
    List<StopInformation> getStopInformationList();

    @Update("update tb_stop_information SET bag_id=null where thing_code=#{thingCode} AND is_delete=0")
    void relieveStopInformationAndBag(@Param("thingCode") String thingCode);

    @Select("select * from tb_stop_information where bag_id is null and (thing_code like CONCAT('%',#{string},'%') OR thing_name like CONCAT('%',#{string},'%'))")
    List<StopInformation> getInformationByTCAndNameNoBagId(@Param("string") String string);

    @Select("select * from tb_stop_information where thing_code like CONCAT('%',#{string},'%') OR thing_name like CONCAT('%',#{string},'%')")
    List<StopInformation> getInformationByTCAndName(@Param("string") String string);
}
                                                  