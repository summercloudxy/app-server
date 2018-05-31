package com.zgiot.app.server.module.sfstop.mapper;


import com.zgiot.app.server.module.sfstop.entity.pojo.StopOperationRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    List<StopOperationRecord> selectOperateRecordWithoutOperateState(@Param("operateState") Integer operateState, @Param("finishState") Integer finishState);


    /**
     * 保存新的停车记录
     *
     * @param stopOperationRecord
     */
    void saveStopOperationRecord(StopOperationRecord stopOperationRecord);
}
