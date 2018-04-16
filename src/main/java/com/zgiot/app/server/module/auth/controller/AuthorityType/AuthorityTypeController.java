package com.zgiot.app.server.module.auth.controller.AuthorityType;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.auth.pojo.AuthorityType;
import com.zgiot.app.server.module.auth.service.AuthorityTypeService;
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
public class AuthorityTypeController {
    @Autowired
    private AuthorityTypeService authorityTypeService;

    @GetMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/authorityTypes/platformClient/{platformClientId}/module/{moduleId}")
    public ResponseEntity<String> getAuthorityTypes(@PathVariable int platformClientId, @PathVariable int moduleId) {
        List<AuthorityType> authorityTypes = authorityTypeService.getAuthorityTypeByClientAndModuleId(platformClientId, moduleId);
        if (authorityTypes == null || authorityTypes.size() == 0) {
            ServerResponse res = new ServerResponse<>(
                    "authorityType data is empty", SysException.EC_UNKNOWN, 0);
            String resJson = JSON.toJSONString(res);
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(authorityTypes)
                , HttpStatus.OK);
    }
}
