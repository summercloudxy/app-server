package com.zgiot.app.server.module.auth.service;

import com.zgiot.app.server.module.auth.constants.AppServerConstants;
import com.zgiot.app.server.module.auth.mapper.DepartmentMapper;
import com.zgiot.app.server.module.auth.pojo.Department;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    public List<Department> getSubDepsByDepartmentId(Integer parentId) {
        Integer pid = parentId;
        if (pid == null) {
            pid = AppServerConstants.PARENT_ID;
        }
        List<Department> departments = departmentMapper.getSubDepartment(pid);
        if (departments == null || departments.isEmpty()) {
            departments = Collections.emptyList();
        }
        return departments;
    }

    public Department getDepartmentById(int departmentId) {
        return departmentMapper.getDepartmentById(departmentId);
    }

    public Department getDepartmentByName(String departmentName) {
        if (StringUtils.isBlank(departmentName)) {
            return null;
        }
        return departmentMapper.getDepartmentByName(departmentName);

    }

    public void addDepartment(Department department) {
        if (department == null) {
            return;
        }
        departmentMapper.addDepartment(department);
    }

    public void deleteDepartment(int departmentId) {
        departmentMapper.deleteDepartment(departmentId);
    }

    public void modifyDepartment(Department department) {
        departmentMapper.updateDepartment(department);
    }

    public int getDepartmentUserCount(int departmentId) {
        return departmentMapper.getDepartmentUserCount(departmentId);
    }

    public int getSubDepCount(int departmentId) {
        return departmentMapper.getSubDepCount(departmentId);
    }

    public void getAllSubDepartment(int departmentId, List<Department> departments) {
        List<Department> departmentList = departmentMapper.getSubDepartment(departmentId);
        if (departmentList == null || departmentList.size() == 0) {
            return;
        }
        departments.addAll(departmentList);
        for (Department dep : departmentList) {
            getAllSubDepartment(dep.getId(), departments);
        }
    }

}
