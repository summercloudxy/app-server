package com.zgiot.app.server.module.auth.controller.user;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.zgiot.app.server.module.auth.constants.AppServerConstants;
import com.zgiot.app.server.module.auth.constants.AuthConstants;
import com.zgiot.app.server.module.auth.controller.auth.MyJwtTokenUtil;
import com.zgiot.app.server.module.auth.mapper.DepartmentMapper;
import com.zgiot.app.server.module.auth.mapper.UserMapper;
import com.zgiot.app.server.module.auth.pojo.*;
import com.zgiot.app.server.module.auth.service.UserService;
import com.zgiot.app.server.module.util.validate.ControllerUtil;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.restcontroller.ServerResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/users")
public class UserController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    UserService userService;
    @Autowired
    MyJwtTokenUtil jwtTokenUtil;

    @RequestMapping(
            value = "/pageNum/{pageNum}/pageSize/{pageSize}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> findAllUsers(@PathVariable int pageNum, @PathVariable int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userMapper.findAllUsers();
        int userSum = userMapper.getUserSum();
        List<UserPack> rtnList = new ArrayList<>(users.size());
        for (User user : users) {
            rtnList.add(userService.getUserPack(user.getId(), null));
        }
        UserReturn userReturn = new UserReturn();
        userReturn.setUserPacks(rtnList);
        userReturn.setUserSum(userSum);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(userReturn)
                , HttpStatus.OK);
    }

    @RequestMapping(
            value = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> createUser(@RequestBody String bodyStr) {
        ControllerUtil.validateBodyRequired(bodyStr);

        User user = JSON.parseObject(bodyStr, User.class);
        if (user.getLoginName().trim().length() == 0) {
            throw new SysException("Bad body found, pls check!", SysException.EC_UNKNOWN);
        }

        // convert to md5
        if (!StringUtils.isBlank(user.getPassword())) {
            String md5Pass = DigestUtils.md5Hex(user.getPassword()).toUpperCase();
            user.setPassword(md5Pass);
        }

        userMapper.insertUser(user);

        return new ResponseEntity<>(
                ServerResponse.buildOkJson(user.getId())
                , HttpStatus.OK);
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> getUser(@PathVariable("id") int userId) {
        System.out.println(userId);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(userMapper.getUser(userId))
                , HttpStatus.OK);
    }

    @RequestMapping(
            value = "/getUserByUuid",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> getUserByUuid(@RequestParam("userUuid") String userUuid) {
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(userMapper.getUserByUuid(userUuid))
                , HttpStatus.OK);
    }

    @RequestMapping(
            value = "/userDetail/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> getUserDetail(@PathVariable("id") int userId) {
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(userMapper.getUserDetail(userId))
                , HttpStatus.OK);
    }

    @RequestMapping(value = "/userDetail", method = RequestMethod.POST)
    public ResponseEntity<String> editUserInfo(@RequestBody String bodyStr) {
        ControllerUtil.validateBodyRequired(bodyStr);
        UserDetail userDetail = JSON.parseObject(bodyStr, UserDetail.class);
        userMapper.updateUserDetail(userDetail);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(null)
                , HttpStatus.OK);
    }

    @RequestMapping(
            value = "/{userId}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> deleteUser(@PathVariable int userId) {
        userService.deleteUserCascaded(userId);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(null)
                , HttpStatus.OK);
    }

    @GetMapping("/personName/{personName}")
    public ResponseEntity<String> getUser(@PathVariable String personName) {
        UserPack userPack = userService.getUserPack(0, personName);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(userPack)
                , HttpStatus.OK);
    }

    @RequestMapping(
            value = "/gen-loginname/{user-name} ",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> genLoginName(@RequestBody String bodyStr) {
// todo
        return null;
    }

    @RequestMapping(
            value = "/pack",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> editUserPack(@RequestBody String bodyStr) {
        ControllerUtil.validateBodyRequired(bodyStr);
        UserPack userPack = JSON.parseObject(bodyStr, UserPack.class);
        if (StringUtils.isBlank(userPack.getUser().getLoginName())) {
            throw new SysException("Bad body found, pls check!", SysException.EC_UNKNOWN);
        }

        userService.editUserPack(userPack);

        return new ResponseEntity<>(
                ServerResponse.buildOkJson("OK")
                , HttpStatus.OK);
    }

    @RequestMapping(
            value = "/pack/{userId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> getUserPack(@PathVariable long userId) {
        UserPack userPack = userService.getUserPack(userId, null);
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(userPack)
                , HttpStatus.OK);
    }

    @RequestMapping(
            value = "/pack/departments/{departmentId}/pageNum/{pageNum}/pageSize/{pageSize}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> findUserPackByDepartmentId(@PathVariable int departmentId, @PathVariable int pageNum, @PathVariable int pageSize) {
        List<Department> departmentList = new ArrayList<>();
        getAllSubDepartment(departmentId, departmentList);
        List<User> users = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        int userSum = 0;
        for (Department dep : departmentList) {
            users.addAll(userMapper.findUsersByDepartment(dep.getId()));
            userSum += userMapper.getUserCountByDepartment(dep.getId());
        }
        List<User> userList = new ArrayList<>();
        if (users.size() != 0 && users.size() >= pageSize) {
            for (int i = (pageNum - 1) * pageSize; i < pageNum * pageSize; i++) {
                userList.add(users.get(i));
            }
        } else {
            userList.addAll(users);
        }
        List<UserPack> rtnList = new ArrayList<>(userList.size());
        for (User user : userList) {
            rtnList.add(userService.getUserPack(user.getId(), null));
        }
        UserReturn userReturn = new UserReturn();
        userReturn.setUserPacks(rtnList);
        userReturn.setUserSum(userSum);

        return new ResponseEntity<>(
                ServerResponse.buildOkJson(userReturn)
                , HttpStatus.OK);
    }

    @GetMapping("/departmentTreeAndStation/{userId}")
    public ResponseEntity<String> getdepartmentTreeAndStationRelateUser(@PathVariable int userId) {
        Map<Integer, List<Department>> departmentTreeAndStation = new HashMap<>();
        List<Department> departments = null;
        List<UserDepartmentStation> userDepartmentStations = userMapper.getUDSByUserId(userId);
        if (userDepartmentStations != null && userDepartmentStations.size() > 0) {
            for (UserDepartmentStation userDepartmentStation : userDepartmentStations) {
                departments = new ArrayList<>();
                int depId = userDepartmentStation.getDepartmentId();
                getDepartmentTreePathNodes(depId, departments);
                departmentTreeAndStation.put(userDepartmentStation.getStationId(), departments);
            }
        }

        return new ResponseEntity<>(
                ServerResponse.buildOkJson(departmentTreeAndStation)
                , HttpStatus.OK);
    }

    @RequestMapping(value = "/modifyPassWord", method = RequestMethod.POST)
    public ResponseEntity<String> modifyPassword(@RequestBody String bodyStr) {
        ControllerUtil.validateBodyRequired(bodyStr);
        PasswordModifyReturn passwordModifyReturn = new PasswordModifyReturn();
        passwordModifyReturn.setRightOldPassword(true);
        passwordModifyReturn.setExsitUser(true);
        passwordModifyReturn.setConsistentPassWord(true);
        passwordModifyReturn.setSuccess(true);
        PasswordModify passwordModify = JSON.parseObject(bodyStr, PasswordModify.class);
        User user = userMapper.getUser(passwordModify.getUserId());
        if (user == null) {
            passwordModifyReturn.setExsitUser(false);
            passwordModifyReturn.setSuccess(false);
        } else {
            if (!user.getPassword().equals(ControllerUtil.md5Hex(passwordModify.getOldPassword()))) {
                passwordModifyReturn.setRightOldPassword(false);
                passwordModifyReturn.setSuccess(false);
            } else if (!ControllerUtil.md5Hex(passwordModify.getNewPassword()).equals(ControllerUtil.md5Hex(passwordModify.getConfirmPassword()))) {
                passwordModifyReturn.setConsistentPassWord(false);
                passwordModifyReturn.setSuccess(false);
            } else {
                user.setPassword(ControllerUtil.md5Hex(passwordModify.getNewPassword()));
                userMapper.updateUser(user);
            }

        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(passwordModifyReturn)
                , HttpStatus.OK);
    }

    private void getDepartmentTreePathNodes(int depId, List<Department> departments) {
        Department department = departmentMapper.getDepartmentById(depId);
        departments.add(department);
        if (department != null) {
            if (department.getParentId() == AppServerConstants.PARENT_ID) {
                return;
            }
            int pid = department.getParentId();
            getDepartmentTreePathNodes(pid, departments);
        }
    }

    private void getAllSubDepartment(int depId, List<Department> departments) {
        Department department = departmentMapper.getDepartmentById(depId);
        departments.add(department);
        List<Department> departmentList = departmentMapper.getSubDepartment(depId);
        if (departmentList == null || departmentList.size() == 0) {
            return;
        }
        for (Department dep : departmentList) {
            int pid = dep.getId();
            getAllSubDepartment(pid, departments);
        }
    }
}
