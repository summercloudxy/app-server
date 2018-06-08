package com.zgiot.app.server.module.auth.service;

import com.zgiot.app.server.module.auth.controller.workshopPost.RelThingCodeUserInWorkshop;
import com.zgiot.app.server.module.auth.mapper.PlatformClientMapper;
import com.zgiot.app.server.module.auth.mapper.RoleUserMapper;
import com.zgiot.app.server.module.auth.mapper.StationMapper;
import com.zgiot.app.server.module.auth.mapper.UserMapper;
import com.zgiot.app.server.module.auth.pojo.*;
import com.zgiot.app.server.module.auth.service.UserService;
import com.zgiot.app.server.module.util.validate.ControllerUtil;
import com.zgiot.common.exceptions.SysException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleUserMapper roleUserMapper;
    @Autowired
    PlatformClientMapper platformClientMapper;
    @Autowired
    StationMapper stationMapper;

    @Transactional
    public void editUserPack(UserPack userPack) {
        User user = userPack.getUser();

        Date now = new Date();
        if (user.getId() == 0) {
            // check login name
            User userViaLoginName = userMapper.getUsersByLoginName(user.getLoginName());
            if (userViaLoginName != null) {
                throw new SysException("Dup loginName `" + user.getLoginName()
                        + "` found, pls check!", SysException.EC_UNKNOWN);
            }

            user.setEnabled(true);
            user.setRegistDate(now);
            user.setUpdateDate(now);
            user.setVersion(1);

            user.setPassword(ControllerUtil.md5Hex(user.getLoginName()));
            // gen uuid
            user.setUuid(ControllerUtil.genUUID());
            userMapper.insertUser(user);

            userPack.getUserDetail().setUserId(user.getId());
            userMapper.insertUserDetail(userPack.getUserDetail());

        } else { // edit
            User editingUser = userMapper.getUser(user.getId());
            if (editingUser == null) {
                throw new SysException("edit user is null", SysException.EC_UNKNOWN);
            }
            editingUser.setUpdateDate(now);
            editingUser.setPersonName(user.getPersonName());
            editingUser.setVersion(user.getVersion() + 1);
            userMapper.updateUser(editingUser);

            UserDetail ud = userMapper.getUserDetail(user.getId());
            ud.setIdNum(userPack.getUserDetail().getIdNum());
            ud.setEmail(userPack.getUserDetail().getEmail());
            ud.setMobile(userPack.getUserDetail().getMobile());
            userMapper.updateUserDetail(ud);
        }

        // insert role-user
        roleUserMapper.deleteByUser(user.getId());
        addRoles(userPack.getRoles(), user.getId());

        // insert user-depart-station
        userMapper.deleteUdsByUser(user.getId());
        List<UserDepartmentStation> udsList = userPack.getUserDepartmentStations();
        if (udsList != null && udsList.size() > 0) {
            for (UserDepartmentStation uds : udsList) {
                // set user id
                uds.setUserId(user.getId());
                // insert
                userMapper.insertUds(uds);
            }
        }

        // insert user clients
        platformClientMapper.deleteClientByUser(user.getId());
        if (userPack.getClients() != null && userPack.getClients().size() > 0) {
            for (PlatformClient obj : userPack.getClients()) {
                UserPlatformClient item = new UserPlatformClient();
                item.setUserId(user.getId());
                item.setPlatformClientId(obj.getId());
                platformClientMapper.insertUserClient(item);
            }
        }

    }

    private void addRoles(List<Role> roles, long userId) {
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                RoleUser roleUser = new RoleUser();
                roleUser.setRoleId(role.getId());
                roleUser.setUserId(userId);
                roleUserMapper.insertRoleUser(roleUser);
            }
        }
    }

    public UserPack getUserPack(long userId, String personName) {
        UserPack rtn = new UserPack();

        // get user base
        User user = null;
        if (StringUtils.isBlank(personName)) {
            user = userMapper.getUser(userId);
        } else {
            user = userMapper.getUsersByPersonName(personName);
        }

        if (user == null) {
            throw new SysException("User `" + userId + "` not found. ", SysException.EC_UNKNOWN);
        }

        rtn.setUser(user);

        // get user detail
        UserDetail ud = userMapper.getUserDetail(userId);
        rtn.setUserDetail(ud);

        // get roles
        List<Role> roles = roleUserMapper.findRolesByUser(user.getId());
        rtn.setRoles(roles);

        // get clients
        List<PlatformClient> clients = platformClientMapper.findClientsByUser(user.getId());
        rtn.setClients(clients);

        // get stations
        List<Station> stations = stationMapper.getStationByUserId(user.getId());
        List<String> stationNames = new ArrayList<>();
        if (stations.size() > 0) {
            for (Station station : stations) {
                stationNames.add(station.getName());
            }
        }
        rtn.setStationNames(stationNames);
        List<UserDepartmentStation> userDepartmentStations = userMapper.getUDSByUserId(user.getId());
        if (userDepartmentStations.size() > 0) {
            rtn.setUserDepartmentStations(userDepartmentStations);
        }

        return rtn;
    }

    @Transactional
    public void deleteUserCascaded(long userId) {
        // delete user depart station
        userMapper.deleteUdsByUser(userId);
        // delete user platformclient
        platformClientMapper.deleteClientByUser(userId);
        // delete role user
        roleUserMapper.deleteByUser(userId);
        // delete user detail
        userMapper.deleteUserDetail(userId);
        // delete user
        userMapper.deleteUser(userId);
    }

    /**
     * @param userId
     * @return
     */
    public User getUser(long userId) {
        return userMapper.getUser(userId);
    }

    public User getUserByUuid(String userUuid) {
        return userMapper.getUserByUuid(userUuid);
    }


    public List<String> getThingCodesInWorkshopPostByUserId(Long userId) {
        return userMapper.getThingCodesInWorkshopPostByUserId(userId);
    }


    public List<RelThingCodeUserInWorkshop> getThingCodesInWorkshopPost() {
        return userMapper.getThingCodesInWorkshopPost();
    }


}
