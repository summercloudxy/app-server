package com.zgiot.app.server.module.userfavor.controller;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.userfavor.mapper.PadModuleMapper;
import com.zgiot.app.server.module.userfavor.pojo.UserFavor;
import com.zgiot.app.server.module.userfavor.pojo.PadModule;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(GlobalConstants.API + GlobalConstants.API_VERSION + "/padModules")
public class PadModuleController {

    @Autowired
    private PadModuleMapper padModuleMapper;

    @GetMapping("")
    public ResponseEntity<String> getPadModule() {
        List<PadModule> padModuleList = padModuleMapper.getPadModule();
        return new ResponseEntity(ServerResponse.buildOkJson(padModuleList), HttpStatus.OK);
    }

    @GetMapping("/userFavor/user/{userUuid}")
    public ResponseEntity<String> getPadModule(@PathVariable String userUuid) {
        List<UserFavor> userFavorList = padModuleMapper.getUserFavor(userUuid);
        return new ResponseEntity(ServerResponse.buildOkJson(userFavorList), HttpStatus.OK);
    }

    @RequestMapping(value = "/userFavor", method = RequestMethod.POST)
    public ResponseEntity<String> addPadModule(@RequestBody String bodyStr) {
        UserFavorReq userFavorReq = JSON.parseObject(bodyStr, UserFavorReq.class);
        String userUuid = userFavorReq.getUserUuid();
        if (!StringUtils.isBlank(userUuid) && userFavorReq.getModuleIds().size() > 0) {
            Float sort = padModuleMapper.getMaxSort(userUuid);
            List<UserFavor> userFavors = padModuleMapper.getUserFavor(userUuid);
            if (sort == null) {
                sort = 0f;
            }
            padModuleMapper.deleteUserFavor(userUuid);
            addUserFavor(userFavorReq, userUuid, sort, userFavors);
        }

        return new ResponseEntity(ServerResponse.buildOkJson(null), HttpStatus.OK);
    }

    private void addUserFavor(UserFavorReq userFavorReq, String userUuid, Float sort, List<UserFavor> userFavors) {
        for (long padModuleId : userFavorReq.getModuleIds()) {
            UserFavor userFavor = new UserFavor();
            userFavor.setUserUuid(userUuid);
            if (userFavors.size() > 0) {
                for (UserFavor userFavorTemp : userFavors) {
                    if (userFavorTemp.getUserUuid().equals(userUuid) && (userFavorTemp.getPadModuleId() == padModuleId)) {
                        userFavor.setSort(userFavorTemp.getSort());
                        break;
                    }
                }
            }
            if (userFavor.getSort() == 0) {
                userFavor.setSort(++sort);
            }
            userFavor.setPadModuleId(padModuleId);
            UserFavor favor = padModuleMapper.getUserFavorById(userUuid, padModuleId);
            if (favor == null) {
                padModuleMapper.addUserFavor(userFavor);
            }
        }
    }

}
