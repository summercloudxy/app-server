package com.zgiot.app.server.module.sfsubsc.job;

import com.zgiot.app.server.module.sfsubsc.service.SubscCardTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jys
 */
@Component
public class CardDataManager {


    @Autowired
    private SubscCardTypeService subscCardTypeService;


    public void getAllCardDatas() {
        subscCardTypeService.getAllCardDatas();
    }

}
