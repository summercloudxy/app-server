package com.zgiot.app.server.service;

import com.zgiot.common.pojo.ThingModel;
import org.springframework.stereotype.Service;

@Service
public interface ThingService {
    ThingModel getThing(String thingCode);
}

