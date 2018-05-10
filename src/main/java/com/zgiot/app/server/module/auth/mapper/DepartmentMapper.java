package com.zgiot.app.server.module.auth.mapper;

import com.zgiot.app.server.module.auth.pojo.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    List<Department> getSubDepartment(@Param("departmentId") int departmentId);

    Department getDepartmentById(@Param("departmentId") int departmentId);

    Department getDepartmentByName(@Param("departmentName") String departmentName);

    void addDepartment(Department department);

    void deleteDepartment(@Param("departmentId") int departmentId);

    void updateDepartment(Department department);

    int getDepartmentUserCount(@Param(value = "departmentId") int departmentId);

    int getSubDepCount(@Param(value = "departmentId") int departmentId);

}
