package com.zgiot.app.server.module.auth.controller.AuthorityGroup;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.zgiot.app.server.module.auth.mapper.AuthorityGroupMapper;
import com.zgiot.app.server.module.auth.mapper.RelAuthorityGroupAuthorityMapper;
import com.zgiot.app.server.module.auth.pojo.AuthorityGroup;
import com.zgiot.app.server.module.auth.pojo.RelAuthorityGroupAuthority;
import com.zgiot.app.server.module.auth.service.AuthorityGroupService;
import com.zgiot.app.server.module.auth.service.RelRoleAuthorityGroupService;
import com.zgiot.app.server.module.util.validate.ControllerUtil;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.restcontroller.ServerResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/authority-groups")
public class AuthorityGroupController {
    @Autowired
    private AuthorityGroupService authorityGroupService;

    @Autowired
    private RelRoleAuthorityGroupService relRoleAuthorityGroupService;

    @Autowired
    private RelAuthorityGroupAuthorityMapper relAuthorityGroupAuthorityMapper;

    @Autowired
    private AuthorityGroupMapper authorityGroupMapper;

    @GetMapping("/platform-clients/{platformClientId}/modules/{moduleId}/pageNum/{pageNum}/pageSize/{pageSize}")
    public ResponseEntity<String> getAuthorityGroup(@PathVariable int platformClientId, @PathVariable int moduleId, @PathVariable int pageNum, @PathVariable int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<AuthorityGroup> authorityGroups = authorityGroupService.getAuthorityByModuleAndClientId(platformClientId, moduleId);
        int sum = authorityGroupService.getAuthGroupCountByModuleAndClientId(platformClientId, moduleId);
        if (authorityGroups == null || authorityGroups.size() == 0) {
            ServerResponse res = new ServerResponse<>(
                    "authorityGroups data is empty", SysException.EC_UNKNOWN, 0);
            String resJson = JSON.toJSONString(res);
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST);
        }
        AuthorityGroupReturn authorityGroupReturn = new AuthorityGroupReturn();
        authorityGroupReturn.setAuthorityGroups(authorityGroups);
        authorityGroupReturn.setSum(sum);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(authorityGroupReturn)
                , HttpStatus.OK);
    }

    @GetMapping("/platform-clients/{platformClientId}/modules/{moduleId}")
    public ResponseEntity<String> getAllAuthorityGroup(@PathVariable int platformClientId, @PathVariable int moduleId) {
        List<AuthorityGroup> authorityGroups = authorityGroupService.getAuthorityByModuleAndClientId(platformClientId, moduleId);
        if (authorityGroups == null || authorityGroups.size() == 0) {
            ServerResponse res = new ServerResponse<>(
                    "authorityGroups data is empty", SysException.EC_UNKNOWN, 0);
            String resJson = JSON.toJSONString(res);
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(authorityGroups)
                , HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<String> addAuthGroup(@RequestBody String bodyStr) {
        ControllerUtil.validateBodyRequired(bodyStr);
        AuthorityAndAuthorityGroup authorityAndAuthorityGroup = JSON.parseObject(bodyStr, AuthorityAndAuthorityGroup.class);
        String authGroupName = authorityAndAuthorityGroup.getAuthorityGroup().getName();
        boolean isExist = false;
        if (!StringUtils.isBlank(authGroupName)) {
            isExist = authorityGroupService.addOrEditAuthGroup(authorityAndAuthorityGroup);
        }

        return new ResponseEntity<>(
                ServerResponse.buildOkJson(isExist)
                , HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAuthGroup(@PathVariable int id) {
        int roleCount = 0;
        roleCount = relRoleAuthorityGroupService.getRoleCountbyAuthGroupId(id);
        if (roleCount == 0) {
            relAuthorityGroupAuthorityMapper.deleteRelAuthGroupAuthByAuthGroupId(id);
            authorityGroupService.deleteAuthGroupById(id);
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(roleCount)
                , HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getAuthGroup(@PathVariable int id) {
        List<RelAuthorityGroupAuthority> relAuthorityGroupAuthorities = relAuthorityGroupAuthorityMapper.getRelAuthorityGroupAuthority(id);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(relAuthorityGroupAuthorities)
                , HttpStatus.OK);
    }

    @GetMapping("/platformAndModule/{authGroupId}")
    public ResponseEntity<String> getPlatClientAndModuleInfo(@PathVariable int authGroupId) {
        PlatformClientAndModule platformClientAndModule = authorityGroupService.getClientAndModuleByAuthGroupId(authGroupId);
        if (platformClientAndModule == null) {
            ServerResponse res = new ServerResponse<>(
                    "department is not exist", SysException.EC_UNKNOWN, 0);
            String resJson = JSON.toJSONString(res);
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(platformClientAndModule)
                , HttpStatus.OK);
    }

    @GetMapping(value = "/{authGroupId}/roleCount")
    public ResponseEntity<String> getRoleCount(@PathVariable int authGroupId) {
        int roleCount = relRoleAuthorityGroupService.getRoleCountbyAuthGroupId(authGroupId);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(roleCount)
                , HttpStatus.OK);
    }

    @GetMapping(value = "/userName/{personName}")
    public ResponseEntity<String> getAuthGroupByUserName(@PathVariable String personName) {
        List<AuthorityGroup> authorityGroups = authorityGroupMapper.selectAuthGroupByUserName(personName);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(authorityGroups)
                , HttpStatus.OK);
    }
}
