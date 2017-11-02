package com.zgiot.app.server.module.message.mapper;

import com.zgiot.app.server.module.message.pojo.FixMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by xiayun on 2017/9/28.
 */
@Mapper
public interface MessageMapper {
    List<FixMessage> getFixMessage(String module, String type);
}
