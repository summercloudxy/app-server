package com.zgiot.app.server.module.auth.controller.auth;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.auth.constants.AppServerConstants;
import com.zgiot.app.server.module.auth.mapper.AuthorityMapper;
import com.zgiot.app.server.module.auth.mapper.PlatformClientMapper;
import com.zgiot.app.server.module.auth.mapper.UserMapper;
import com.zgiot.app.server.module.auth.pojo.PlatformClient;
import com.zgiot.app.server.module.auth.pojo.User;
import com.zgiot.app.server.module.auth.pojo.UserLogin;
import com.zgiot.app.server.module.util.validate.ControllerUtil;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.restcontroller.ServerResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/auth")
public class AuthController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthorityMapper authorityMapper;
    @Autowired
    private MyJwtTokenUtil myJwtTokenUtil;
    @Autowired
    private PlatformClientMapper platformClientMapper;

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @RequestMapping(
            value = "/login/pad",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> login(@RequestBody String bodyStr, HttpServletRequest request) {
        String ip = getIpAddress(request);
        ControllerUtil.validateBodyRequired(bodyStr);

        LoginReq loginReq = JSON.parseObject(bodyStr, LoginReq.class);
        LoginRes loginRes = new LoginRes();
        if (StringUtils.isBlank(loginReq.getLoginName())) {
            return new ResponseEntity<>(ServerResponse.buildJson("Bad request body!", SysException.EC_UNKNOWN, null)
                    , HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.getUsersByLoginName(loginReq.getLoginName());
        if (user == null) {
            return new ResponseEntity<>(ServerResponse.buildJson("user is not exist", SysException.EC_UNKNOWN, null)
                    , HttpStatus.BAD_REQUEST);
        }

        // check password
        if (!ControllerUtil.md5Hex(loginReq.getPassword()).equals(user.getPassword())) {
            return new ResponseEntity<>(ServerResponse.buildJson("Login failed", AppServerConstants.ERR_USER_OR_PASS_WRONG
                    , null)
                    , HttpStatus.OK);
        }

        // logic validation
        ResponseEntity<String> notAllowedRtn = validateUser(loginReq.getPlatformClientId(), user);
        if (notAllowedRtn != null) {
            return notAllowedRtn;
        }

        // generate jwt
        JwtAuthBean from = new JwtAuthBean();
        from.setUserUuid(user.getUuid());
        from.setLoginName(loginReq.getLoginName());
        from.setClientId(loginReq.getPlatformClientId());
        String jwt = myJwtTokenUtil.generateToken(from);

        loginRes.setUserId(user.getId());
        loginRes.setUuid(user.getUuid());
        loginRes.setToken(jwt);

        UserLogin userLogin = new UserLogin();
        userLogin.setUserUuid(user.getUuid());
        userLogin.setPlatClientId(loginReq.getPlatformClientId());
        userLogin.setToken(jwt);
        userLogin.setUpdateDate(new Date());
        userLogin.setIpAddress(ip);
        userLogin.setMacAddress(loginReq.getMacAddr());
        loginRes.setLoginName(user.getLoginName());
        loginRes.setPersonName(user.getPersonName());
        loginRes.setLoginTimestamp(userLogin.getUpdateDate());
        loginRes.setAuthorityList(authorityMapper.findAuthorityViaRoleOrAuthGroupByUserUuid(user.getUuid()));
        userMapper.deleteUserLogin(user.getUuid(), loginReq.getPlatformClientId());
        userMapper.addUserLogin(userLogin);

        return new ResponseEntity<>(
                ServerResponse.buildOkJson(loginRes)
                , HttpStatus.OK); // toadd +refreshToken then

    }

    private ResponseEntity<String> validateUser(int reqPlatformClientId, User userLoaded) {
        // check user enable?
        if (!userLoaded.isEnabled()) {
            return new ResponseEntity<>(ServerResponse.buildJson("User is disabled.", AppServerConstants.ERR_USER_DISABLED
                    , null)
                    , HttpStatus.FORBIDDEN);
        }

        // check can login from the client ?
        ResponseEntity notAllowedForClient = new ResponseEntity<>(ServerResponse.buildJson("Not allowed for this client "
                        + reqPlatformClientId
                , AppServerConstants.ERR_USER_CLIENT_NOT_ALLOWED_TO_LOGIN
                , null)
                , HttpStatus.FORBIDDEN);
        List<PlatformClient> allowedClients = platformClientMapper.findClientsByUser(userLoaded.getId());
        if (allowedClients == null || allowedClients.size() == 0) {
            return notAllowedForClient;
        } else {
            boolean found = false;
            for (PlatformClient o : allowedClients) {
                if (o.getId() == reqPlatformClientId) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return notAllowedForClient;
            }
        }
        return null;
    }

    @RequestMapping(value = "/logout/user/{userUuid}/platformClient/{platformClientId}", method = RequestMethod.GET)
    public ResponseEntity<String> logout(@PathVariable String userUuid, @PathVariable int platformClientId) {
        UserLogin userLogin = userMapper.getUserLogin(userUuid, platformClientId);
        if (userLogin != null) {
            userLogin.setToken("");
            userLogin.setUpdateDate(new Date());
            userMapper.editUserLogin(userLogin);
        }
        return new ResponseEntity<>(
                ServerResponse.buildOkJson(null)
                , HttpStatus.OK);
    }

}

class LoginReq {
    private int platformClientId;
    private String loginName;
    private String password;
    private String macAddr;

    public int getPlatformClientId() {
        return platformClientId;
    }

    public void setPlatformClientId(int platformClientId) {
        this.platformClientId = platformClientId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }
}