package com.zgiot.app.server.module.auth.pojo;

import java.util.List;

public class UserPack {
    private User user;
    private UserDetail userDetail;
    private List<Role> roles;
    private List<PlatformClient> clients;
    private List<UserDepartmentStation> userDepartmentStations;
    private List<String> stationNames;

    public List<String> getStationNames() {
        return stationNames;
    }

    public void setStationNames(List<String> stationNames) {
        this.stationNames = stationNames;
    }

    public List<UserDepartmentStation> getUserDepartmentStations() {
        return userDepartmentStations;
    }

    public void setUserDepartmentStations(List<UserDepartmentStation> userDepartmentStations) {
        this.userDepartmentStations = userDepartmentStations;
    }

    public List<PlatformClient> getClients() {
        return clients;
    }

    public void setClients(List<PlatformClient> clients) {
        this.clients = clients;
    }

    public User getUser() {
        return user;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
