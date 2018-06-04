package com.zgiot.app.server.module.sfstop.mapper;


import com.zgiot.app.server.module.sfstop.entity.pojo.StopDeviceArea;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StopDeviceAreaMapper {

    List<StopDeviceArea> getAreaListByRegionId(Long regionId);

    List<StopDeviceArea> getAreaListByNameAndRegionId(StopDeviceArea stopDeviceArea);

    Integer getCountByRegion(StopDeviceArea stopDeviceArea);

    void addStopDeviceArea(StopDeviceArea stopDeviceArea);

    List<StopDeviceArea> getGreaterLevelAreaList(StopDeviceArea stopDeviceArea);

    void updateStopDeviceArea(StopDeviceArea stopDeviceAreaLevel);

    StopDeviceArea getStopDeviceAreaById(Long id);

    /**
     * 查询所有的停车设备区域
     *
     * @return
     */
    @Select("select * from tb_stop_device_area WHERE is_deleted=0  and region_id =#{regionId} and system =#{system} ")
    List<StopDeviceArea> getStopDeviceArea(@Param("regionId") Long regionId, @Param("system") Integer system);


    void deleteStopDeviceArea(Long id);
}
