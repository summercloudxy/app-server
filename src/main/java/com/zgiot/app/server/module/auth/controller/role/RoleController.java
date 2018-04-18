package com.zgiot.app.server.module.auth.controller.role;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.zgiot.app.server.module.auth.mapper.RelUserRoleMapper;
import com.zgiot.app.server.module.auth.mapper.RoleMapper;
import com.zgiot.app.server.module.auth.pojo.RelRoleAuthorityGroup;
import com.zgiot.app.server.module.auth.pojo.Role;
import com.zgiot.app.server.module.auth.service.RelRoleAuthorityGroupService;
import com.zgiot.app.server.module.auth.service.RoleService;
import com.zgiot.app.server.module.util.validate.ControllerUtil;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private RelRoleAuthorityGroupService relRoleAuthorityGroupService;

    @Autowired
    private RelUserRoleMapper relUserRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @GetMapping("/pageNum/{pageNum}/pageSize/{pageSize}")
    public ResponseEntity<String> getAllRoleInfo(@PathVariable int pageNum, @PathVariable int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<RoleReturn> roleReturns = roleService.getRoleInfo();
        if (roleReturns == null) {
            ServerResponse res = new ServerResponse<>(
                    "roleinfo not exist", SysException.EC_UNKNOWN, 0);
            String resJson = JSON.toJSONString(res);
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(roleReturns),
                HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<String> getAllRole() {
        List<Role> roles = roleMapper.getRoles();
        if (roles == null) {
            ServerResponse res = new ServerResponse<>(
                    "role not exist", SysException.EC_UNKNOWN, 0);
            String resJson = JSON.toJSONString(res);
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(roles),
                HttpStatus.OK);
    }

    @GetMapping("/roleSum")
    public ResponseEntity<String> getRoleSum() {
        int sum = roleService.getRoleSum();
        return new ResponseEntity<>(ServerResponse.buildOkJson(sum),
                HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<String> addOrEditRoleInfo(@RequestBody String bodyStr) {
        ControllerUtil.validateBodyRequired(bodyStr);
        RoleAndAuthorityGroup roleAndAuthorityGroup = JSON.parseObject(bodyStr, RoleAndAuthorityGroup.class);
        Role role = roleAndAuthorityGroup.getRole();
        Map<String, Boolean> addRoleResponse = new HashMap<>();
        boolean isExist = false;
        int rid = 0;
        List<RelRoleAuthorityGroup> relRoleAuthorityGroups = roleAndAuthorityGroup.getRelRoleAuthorityGroups();
        Role existRole = roleService.getRoleByName(role.getName());
        role.setUpdateDate(new Date());
        if (role.getId() != 0) {//edit
            if (existRole != null && (existRole.getId() != role.getId())) {
                isExist = true;
            } else {
                relRoleAuthorityGroupService.deleteRelRoleAuthority(role.getId());
                roleService.modifyRole(role);
            }

        } else {//add
            if (existRole != null) {
                isExist = true;
            } else {
                roleService.addRole(role);
            }
        }
        if (!isExist) {
            for (RelRoleAuthorityGroup relRoleAuthorityGroup : relRoleAuthorityGroups) {
                relRoleAuthorityGroup.setRoleId(role.getId());
                relRoleAuthorityGroupService.addRelRoleAuthority(relRoleAuthorityGroup);
            }
        }

        rid = role.getId();
        addRoleResponse.put(String.valueOf(rid), isExist);

        return new ResponseEntity<>(ServerResponse.buildOkJson(addRoleResponse),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{role-id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteRoleInfo(@PathVariable("role-id") int roleId) {
        int userCount = 0;
        userCount = relUserRoleMapper.getUserCount(roleId);
        if (userCount == 0) {
            relRoleAuthorityGroupService.deleteRelRoleAuthority(roleId);
            roleMapper.deleteRoleAuthority(roleId);
            roleService.deleteRole(roleId);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(userCount),
                HttpStatus.OK);
    }

    @GetMapping(value = "/{role-id}")
    public ResponseEntity<String> getRelRoleAuthority(@PathVariable("role-id") int roleId) {
        List<RelRoleAuthorityGroup> relRoleAuthorityGroups = relRoleAuthorityGroupService.getRelRoleAuthorityGroup(roleId);
        return new ResponseEntity<>(ServerResponse.buildOkJson(relRoleAuthorityGroups),
                HttpStatus.OK);
    }

    @GetMapping(value = "/roleName/{name}")
    public ResponseEntity<String> getRoleInfoByName(@PathVariable("name") String name) {
        RoleReturn roleReturn = roleMapper.getRoleInfoByName(name);
        return new ResponseEntity<>(ServerResponse.buildOkJson(roleReturn),
                HttpStatus.OK);
    }

    @GetMapping("/roleId/{id}")
    public ResponseEntity<String> getRoleInfoById(@PathVariable int id) {
        RoleReturn roleReturn = roleMapper.getRoleInfoById(id);
        return new ResponseEntity<>(ServerResponse.buildOkJson(roleReturn),
                HttpStatus.OK);
    }
}
