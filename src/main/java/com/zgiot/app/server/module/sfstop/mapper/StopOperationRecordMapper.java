package com.zgiot.app.server.module.sfstop.mapper;


import com.zgiot.app.server.module.sfstop.entity.pojo.StopOperationRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StopOperationRecordMapper {

    /**
     * 查询未完成的有效启车任务
     *
     * @param operateState 查询状态开始
     * @param finishState  查询状态结尾
     * @return
     */
    List<StopOperationRecord> selectOperateRecordWithoutOperateState(@Param("system") Integer system, @Param("operateState") Integer operateState, @Param("finishState") Integer finishState);


    /**
     * 保存新的停车记录
     *
     * @param stopOperationRecord
     */
    void saveStopOperationRecord(StopOperationRecord stopOperationRecord);

    /**
     * 根据操作状态查询停车记录
     *
     * @param operateState
     * @return
     */
    @Select("select * from tb_stop_operation_record where is_delete=0 and operate_state =#{operateState}")
    StopOperationRecord getStopOperationRecordByOperateState(@Param("operateState") int operateState);
}
