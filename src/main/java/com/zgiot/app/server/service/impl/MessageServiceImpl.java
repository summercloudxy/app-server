package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.module.message.mapper.MessageMapper;
import com.zgiot.app.server.module.message.pojo.FixMessage;
import com.zgiot.app.server.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xiayun on 2017/9/28.
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<FixMessage> getFixMessage(String module, String type) {
        return messageMapper.getFixMessage(module, type);
    }
}
