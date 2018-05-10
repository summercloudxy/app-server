package com.zgiot.app.server.module.auth.service;

import com.zgiot.app.server.module.auth.controller.role.RoleReturn;
import com.zgiot.app.server.module.auth.mapper.RoleMapper;
import com.zgiot.app.server.module.auth.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    public List<RoleReturn> getRoleInfo() {
        return roleMapper.getAllRoleInfo();
    }

    public void addRole(Role role) {
        roleMapper.insertRole(role);
    }

    public Role getRoleByName(String name) {
        return roleMapper.getRoleByRoleName(name);
    }

    public void deleteRole(int id) {
        roleMapper.deleteRole(id);
    }

    public void modifyRole(Role role) {
        roleMapper.updateRole(role);
    }

    public int getRoleSum() {
        return roleMapper.getRoleSum();
    }
}
