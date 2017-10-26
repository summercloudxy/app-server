package com.zgiot.app.server.module.message.controller;

import com.zgiot.app.server.service.impl.MessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xiayun on 2017/9/28.
 */
@RestController
public class MessageController {
    @Autowired
    private MessageServiceImpl messageService;

    public void getFixMessage(String module, String type){

    }

}
