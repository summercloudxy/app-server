package com.zgiot.app.server.module.sfstop.mapper;

import com.zgiot.app.server.module.sfstop.entity.pojo.StopLine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import java.util.List;

/**
 * 停车线Mapper
 */
@Mapper
public interface StopLineMapper {

    /**
     * 按照区域查询停车线
     *
     * @param areaId
     * @return
     */
    List<StopLine> getStopLineByAreaId(@Param("areaId") Long areaId);



    List<StopLine> validateStopLineName(StopLine stopLine);

    void addStopLine(StopLine stopLine);

    void updateStopLine(StopLine stopLine);

    void deleteStopLine(Long id);
}
                                                  