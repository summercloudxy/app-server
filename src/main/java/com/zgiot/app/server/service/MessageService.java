package com.zgiot.app.server.service;

import com.zgiot.app.server.module.message.pojo.FixMessage;

import java.util.List;

/**
 * Created by xiayun on 2017/9/28.
 */
public interface MessageService {
    List<FixMessage> getFixMessage(String module, String type);
}
