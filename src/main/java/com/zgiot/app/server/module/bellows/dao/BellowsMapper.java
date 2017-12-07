package com.zgiot.app.server.module.bellows.dao;

import com.zgiot.app.server.module.bellows.pojo.CompressorLog;
import com.zgiot.app.server.module.bellows.pojo.CompressorState;
import com.zgiot.app.server.module.bellows.pojo.ValveLog;
import com.zgiot.app.server.module.bellows.pojo.ValveTeam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author wangwei
 */
@Mapper
public interface BellowsMapper {

    /**
     * 查询bellows配置
     * @param thingCode
     * @param paramName
     * @return
     */
    Long selectParamValue(@Param("thingCode") String thingCode, @Param("paramName") String paramName);

    /**
     * 修改bellows配置
     * @param thingCode
     * @param paramName
     * @param paramValue
     */
    void updateParamValue(@Param("thingCode") String thingCode, @Param("paramName") String paramName,
                                @Param("paramValue") Long paramValue);


    /**
     * 保存空压机日志
     * @param compressorLog
     */
    void saveCompressorLog(@Param("log")CompressorLog compressorLog);

    /**
     * 更新空压机日志，确认状态
     * @param compressorLog
     */
    void updateCompressorLog(@Param("log") CompressorLog compressorLog);

    /**
     * 查询空压机操作日志
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param lastId    上次查询的最后一个id（null为首次查询）
     * @param count 个数
     * @return
     */
    List<CompressorLog> getCompressorLog(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("lastId") Long lastId, @Param("count") Integer count);


    /**
     * 保存空压机状态
     * @param state
     */
    void saveCompressorState(@Param("state")CompressorState state);

    /**
     * 查询空压机状态
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param thingCodes    查询thingCode列表
     * @param isAsc 是否顺序排序
     * @param offset    偏移量
     * @param count 个数
     * @return
     */
    List<CompressorState> getCompressorState(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("thingCodes") List<String> thingCodes, @Param("isAsc") Boolean isAsc,
                                             @Param("offset") Integer offset, @Param("count") Integer count);

    /**
     * 批量插入阀门分组
     * @param teams
     */
    void insertBatchValveTeam(@Param("teams") List<ValveTeam> teams);

    /**
     * 更新分组exec_time和status
     * @param team
     */
    void updateValveTeam(@Param("team") ValveTeam team);

    /**
     * 更新某状态下的分组状态
     * @param newStatus 新状态
     * @param oldStatus 旧状态
     */
    void updateValveTeamByStatus(@Param("newStatus") String newStatus, @Param("oldStatus") String oldStatus);

    /**
     * 删除某状态的分组
     * @param status
     */
    void deleteValveTeamByStatus(@Param("status") String status);


    /**
     * 获取状态下分组列表
     * @param status
     * @return
     */
    List<ValveTeam> getValveTeamByStatus(@Param("status") String status);

    /**
     * 获取将要执行的分组
     * @param time
     * @return
     */
    ValveTeam getValveTeamToExec(@Param("time") Date time);

    /**
     * 根据id获取阀门分组
     * @param id
     * @return
     */
    ValveTeam getValveTeamById(@Param("id") Long id);

    /**
     * 获取最大分组id
     * @return
     */
    Long getMaxTeamId();

    /**
     * 根据分组类型计算组数
     * @param type
     * @return
     */
    Long countValveTeamByType(@Param("type") String type);


    /**
     * 保存阀门日志
     * @param valveLog
     */
    void saveValveLog(@Param("log")ValveLog valveLog);

    /**
     * 更新阀门日志，确认状态
     * @param valveLog
     */
    void updateValveLog(@Param("log") ValveLog valveLog);

    /**
     * 查询阀门操作日志
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param lastId    上次查询的最后一个id（null为首次查询）
     * @param count 个数
     * @return
     */
    List<ValveLog> getValveLog(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("lastId") Long lastId, @Param("count") Integer count);
}
