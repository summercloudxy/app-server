package com.zgiot.app.server.module.reportforms.output.dao;

import com.zgiot.app.server.module.reportforms.output.pojo.DutyLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface DutyLogMapper{

    @Select("SELECT\n" +
            "\td.id,\n" +
            "\td.team_id,\n" +
            "\td.is_day,\n" +
            "\td.start_time,\n" +
            "\td.end_time\n" +
            "FROM\n" +
            "\ttb_duty_log d\n" +
            "ORDER BY\n" +
            "\td.end_time DESC")
    List<DutyLog> getLastDutyLog();

    @Select("SELECT\n" +
            "\td.*\n" +
            "FROM\n" +
            "\ttb_duty_log d\n" +
            "WHERE\n" +
            "\t d.start_time <= date_format(#{workTime},'%Y-%m-%d %H:%i:%s')\n" +
            " AND d.end_time > date_format(#{workTime},'%Y-%m-%d %H:%i:%s')\n" +
            " AND d.is_delete = 0")
    DutyLog getWhichTeam(@Param("workTime") Date workTime);
}
