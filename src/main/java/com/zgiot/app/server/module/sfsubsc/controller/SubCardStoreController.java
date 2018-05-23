package com.zgiot.app.server.module.sfsubsc.controller;

import com.zgiot.app.server.module.sfsubsc.entity.pojo.SubscCardStore;
import com.zgiot.app.server.module.sfsubsc.service.SubCardStoreService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/sfsubsc/store")
public class SubCardStoreController {

    @Autowired
    private SubCardStoreService subCardStoreService;

    /**
     * 获取所有卡片类型
     * @return
     */
    @GetMapping(value = "/getAllSubscCardStore")
    public ResponseEntity<String> getAllSubscCardStore() {
        List<SubscCardStore> subscCardStoreList = subCardStoreService.getAllSubscCardStore();
        return new ResponseEntity<>(ServerResponse.buildOkJson(subscCardStoreList), HttpStatus.OK);
    }

}
