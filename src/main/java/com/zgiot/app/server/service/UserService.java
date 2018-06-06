package com.zgiot.app.server.service;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<String> getThingCodesInWorkshopPostByUserId(Long userId);
}
                                                  