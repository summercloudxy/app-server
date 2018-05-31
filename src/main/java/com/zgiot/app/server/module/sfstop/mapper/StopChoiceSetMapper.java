package com.zgiot.app.server.module.sfstop.mapper;


import com.zgiot.app.server.module.sfstop.entity.pojo.StopChoiceSet;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StopChoiceSetMapper {

    /**
     * 保存停车方案设置
     *
     * @param stopChoiceSet
     */
    @Insert("INSERT INTO tb_stop_choice_set (raw_stop_set,tcs_stop_set,filterpress_stop_set,belt_route,create_user,create_time,update_user,update_time) VALUES(#{rawStopSet},#{tcsStopSet},#{filterpressStopSet},#{beltRoute}，#{createUser}，#{createTime},#{updateUser},#{updateTime} )")
    void saveStopChoiceSet(StopChoiceSet stopChoiceSet);
}
