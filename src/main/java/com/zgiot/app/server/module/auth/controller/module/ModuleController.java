package com.zgiot.app.server.module.auth.controller.module;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.auth.pojo.Module;
import com.zgiot.app.server.module.auth.service.ModuleService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ModuleController {
    @Autowired
    private ModuleService moduleService;

    @GetMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/modules/platformClient/{platformCilentId}")
    public ResponseEntity<String> getModules(@PathVariable("platformCilentId") int platformCilentId) {
        List<Module> modules = moduleService.getModulesByClientId(platformCilentId);
        if (modules == null || modules.size() == 0) {
            ServerResponse res = new ServerResponse<>(
                    "module data is empty", SysException.EC_UNKNOWN, 0);
            String resJson = JSON.toJSONString(res);
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(modules)
                , HttpStatus.OK);
    }
}
