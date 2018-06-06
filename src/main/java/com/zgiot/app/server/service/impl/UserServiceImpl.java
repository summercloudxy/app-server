package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.service.UserService;
import com.zgiot.app.server.service.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<String> getThingCodesInWorkshopPostByUserId(Long userId) {
        return userMapper.getThingCodesInWorkshopPostByUserId(userId);
    }
}
