package com.zgiot.app.server.module.auth.controller.platformClient;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.auth.pojo.PlatformClient;
import com.zgiot.app.server.module.auth.service.PlatformClientService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PlatformClientController {
    @Autowired
    private PlatformClientService platformClientService;

    @GetMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/platform-clients")
    public ResponseEntity<String> getAllPlateClient() {
        List<PlatformClient> platformClients = platformClientService.getAllPlatformClient();
        if (platformClients == null || platformClients.size() == 0) {
            ServerResponse res = new ServerResponse<>(
                    "platformclient data is empty", SysException.EC_UNKNOWN, 0);
            String resJson = JSON.toJSONString(res);
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(platformClients)
                , HttpStatus.OK);
    }
}
