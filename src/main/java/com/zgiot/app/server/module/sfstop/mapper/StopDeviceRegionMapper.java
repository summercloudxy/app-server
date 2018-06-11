package com.zgiot.app.server.module.sfstop.mapper;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopDeviceRegion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import java.util.List;

/**
 * 设备大区
 */
@Mapper
public interface StopDeviceRegionMapper {
    /**
     * 查询停车设备大区
     *
     * @return
     */
    @Select("select * from tb_stop_device_region where is_deleted =0 ORDER BY level ")
    List<StopDeviceRegion> getStopDeviceRegion();


    List<StopDeviceRegion> selectAllStopDeviceRegion();

    List<StopDeviceRegion> selectAllStopDeviceRegionOrder();

    List<StopDeviceRegion> validateStopDeviceRegionName(StopDeviceRegion stopDeviceRegion);

    Integer getRegionCount();

    void addStopDeviceRegion(StopDeviceRegion stopDeviceRegion);

    List<StopDeviceRegion> selectAllRegionByLevel(Integer level);

    void updateStopDeviceRegion(StopDeviceRegion stopDeviceRegion);

    StopDeviceRegion selectStopDeviceRegionById(Long id);

    void deleteStopDeviceRegion(Long id);
}
                                                  