package com.zgiot.app.server.module.sfstop.mapper;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopDeviceBag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import java.util.List;

/**
 * 停车包mapper
 */
@Mapper
public interface StopDeviceBagMapper {

    List<StopDeviceBag> getStopDeviceBagByAreaId(Long areaId);

    List<StopDeviceBag> getStopDeviceBagByLineId(Long stopLineId);

    List<StopDeviceBag> checkAreaHaveBagName(StopDeviceBag stopDeviceBag);

    void addStopDeviceBag(StopDeviceBag stopDeviceBag);

    void updateStopDeviceBag(StopDeviceBag stopDeviceBag);

    void deleteStopDeviceBag(StopDeviceBag stopDeviceBag);
    /**
     * 根据停车线查询停车包
     *
     * @param stopLineId
     * @return
     */
    @Select("SELECT * from tb_stop_device_bag where is_deleted =0 and stop_line_id =#{stopLineId}")
    List<StopDeviceBag> getStopDeviceBagByStartLineId(@Param("stopLineId") Long stopLineId);

    @Select("SELECT * FROM `tb_stop_device_bag` where id =#{bagId} and is_deleted=0 limit 1")
    StopDeviceBag getStopDeviceBagById(@Param("bagId") Long bagId);
}
                                                  