package com.zgiot.app.server.module.sfstop.mapper;

import com.zgiot.app.server.module.sfstop.entity.pojo.StartOperationRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 启车记录Mapper
 */
@Mapper
public interface StartOperationRecordMapper {

    /**
     * 查询最新的一条
     *
     * @param operateState
     * @return
     */
    @Select("select * from tb_start_operation_record where operate_state =#{operateState} ORDER BY created_time DESC LIMIT 1")
    StartOperationRecord getStartOperationRecord(@Param("operateState") Integer operateState);

}
