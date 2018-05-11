package com.zgiot.app.server.module.auth.controller.department;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.auth.constants.DepartmentConstant;
import com.zgiot.app.server.module.auth.pojo.Department;
import com.zgiot.app.server.module.auth.service.DepartmentService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.restcontroller.ServerResponse;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/departments")
public class DepartmentController {

    private static final String ERRORMESSAGE = "modify department is not exist,departmentId: `";
    @Autowired
    private DepartmentService departmentService;

    @ApiOperation("获取顶级部门")
    @GetMapping("")
    public ResponseEntity<String> getFirstLevelDepartmnets() {
        List<Department> departments = departmentService.getSubDepsByDepartmentId(null);
        return new ResponseEntity<>(ServerResponse.buildOkJson(departments),
                HttpStatus.OK);
    }

    @ApiOperation("根据部门id获取第一级子部门")
    @GetMapping(value = "/sub/{departmentId}")
    public ResponseEntity<String> getSubDepByDepartmentId(@PathVariable int departmentId) {
        List<Department> departments = departmentService.getSubDepsByDepartmentId(departmentId);
        if (departments == null || departments.isEmpty()) {
            departments = Collections.emptyList();
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(departments),
                HttpStatus.OK);
    }

    @ApiOperation("根据部门id查询部门信息")
    @GetMapping("/{departmentId}")
    public ResponseEntity<String> getDepartmentById(@PathVariable int departmentId) {
        Department department = departmentService.getDepartmentById(departmentId);
        if (department == null) {
            ServerResponse res = new ServerResponse<>(
                    "department is not exist: `" + departmentId + "`", SysException.EC_UNKNOWN, 0);
            String resJson = JSON.toJSONString(res);
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(department),
                HttpStatus.OK);
    }

    @ApiOperation("增加部门")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<String> addDepartment(@RequestBody String department) {
        Department dep = JSON.parseObject(department, Department.class);
        if (StringUtils.isBlank(department) || dep == null || dep.getName() == null) {
            ServerResponse res = new ServerResponse<>(
                    "Not valid request data. The incoming req body is: `" + department + "`", SysException.EC_UNKNOWN, 0);
            String resJson = JSON.toJSONString(res);
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST);
        }
        Boolean isExist = null;
        Department existDep = departmentService.getDepartmentByName(dep.getName());
        if (existDep == null) {
            isExist = false;
            int parentId = dep.getParentId();
            int depSort = DepartmentConstant.DEPARTMENT_START_INDEX;
            List<Department> departments = departmentService.getSubDepsByDepartmentId(parentId);
            if (departments.size() > 0) {
                depSort = departments.get(departments.size() - 1).getDepSort();
                ++depSort;
            }
            dep.setDepSort(depSort);
            departmentService.addDepartment(dep);
        } else {
            isExist = true;
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(isExist),
                HttpStatus.OK);
    }

    @ApiOperation("删除部门，如部门下有子部门或员工，不能删除")
    @RequestMapping(value = "/{departmentId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteDepartment(@PathVariable int departmentId) {
        DeleteDepartmentResponse deleteDepartmentResponse = new DeleteDepartmentResponse();
        int userCount = 0;
        int subDepCount = 0;

        List<Department> departmentList = new ArrayList<>();
        departmentService.getAllSubDepartment(departmentId, departmentList);
        userCount = departmentService.getDepartmentUserCount(departmentId);
        if (userCount == 0) {
            for (Department dep : departmentList) {
                userCount = departmentService.getDepartmentUserCount(dep.getId());
                if (userCount > 0) {
                    break;
                }
            }
        }

        subDepCount = departmentService.getSubDepCount(departmentId);
        if (userCount == 0) {
            for (Department dep : departmentList) {
                departmentService.deleteDepartment(dep.getId());
            }
            departmentService.deleteDepartment(departmentId);
        }

        deleteDepartmentResponse.setUserCount(userCount);
        deleteDepartmentResponse.setSubDepCount(subDepCount);
        return new ResponseEntity<>(ServerResponse.buildOkJson(deleteDepartmentResponse),
                HttpStatus.OK);
    }

    @ApiOperation("修改部门")
    @RequestMapping(value = "/{departmentId}", method = RequestMethod.PUT)
    public ResponseEntity<String> editDepartment(@PathVariable int departmentId, @RequestBody String department) {
        Department dep = JSON.parseObject(department, Department.class);
        if (StringUtils.isBlank(department) || dep == null || dep.getName() == null) {
            ServerResponse res = new ServerResponse<>(
                    "Not valid request data. The incoming req body is: `" + department + "`", SysException.EC_UNKNOWN, 0);
            String resJson = JSON.toJSONString(res);
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST);
        }

        Department modDepartment = departmentService.getDepartmentById(departmentId);
        boolean isExist = false;
        if (modDepartment == null) {
            ServerResponse res = new ServerResponse<>(
                    ERRORMESSAGE + departmentId + "`", SysException.EC_UNKNOWN, 0);
            String resJson = JSON.toJSONString(res);
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST);
        }

        Department existDep = departmentService.getDepartmentByName(dep.getName());
        if ((existDep != null) && (existDep.getId() != departmentId)) {
            isExist = true;
        } else {
            modDepartment.setId(departmentId);
            modDepartment.setParentId(dep.getParentId());
            modDepartment.setName(dep.getName());
            modDepartment.setParentName(dep.getParentName());
            modDepartment.setDepartmentManagerName(dep.getDepartmentManagerName());
            departmentService.modifyDepartment(modDepartment);
        }

        return new ResponseEntity<>(ServerResponse.buildOkJson(isExist),
                HttpStatus.OK);
    }

    @ApiOperation("修改部门的父部门")
    @RequestMapping(value = "/{departmentId}/{parentDepartmentId}", method = RequestMethod.PUT)
    public ResponseEntity<String> modifyParentDepartment(@PathVariable int departmentId, @PathVariable int parentDepartmentId) {
        Department department = departmentService.getDepartmentById(departmentId);
        if (department == null) {
            ServerResponse res = new ServerResponse<>(
                    ERRORMESSAGE + departmentId + "`", SysException.EC_UNKNOWN, 0);
            String resJson = JSON.toJSONString(res);
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST);
        }

        Department parentDepartment = departmentService.getDepartmentById(parentDepartmentId);
        if (parentDepartment == null) {
            ServerResponse res = new ServerResponse<>(
                    ERRORMESSAGE + parentDepartmentId + "`", SysException.EC_UNKNOWN, 0);
            String resJson = JSON.toJSONString(res);
            return new ResponseEntity<>(resJson, HttpStatus.BAD_REQUEST);
        }


        department.setParentId(parentDepartment.getId());
        department.setParentName(parentDepartment.getParentName());
        department.setParentId(parentDepartmentId);
        departmentService.modifyDepartment(department);

        return new ResponseEntity<>(ServerResponse.buildOkJson(null),
                HttpStatus.OK);
    }


    @ApiOperation("返回当前部门的直属员工数")
    @GetMapping(value = "/{departmentId}/users")
    public ResponseEntity<String> getDepartmentUserCount(@PathVariable int departmentId) {
        int userCount = departmentService.getDepartmentUserCount(departmentId);
        return new ResponseEntity<>(ServerResponse.buildOkJson(userCount),
                HttpStatus.OK);
    }

    @ApiOperation("上下移动部门")
    @RequestMapping(value = "upOrDownShift/{upOrDownTag}/departmet/{departmentId}", method = RequestMethod.POST)
    public ResponseEntity<String> departmentUpOrDownShift(@PathVariable int departmentId, @PathVariable int upOrDownTag) {
        boolean isSuccessfulShift = true;
        Department shiftDepFrom = departmentService.getDepartmentById(departmentId);
        List<Department> deps = departmentService.getSubDepsByDepartmentId(shiftDepFrom.getParentId());
        int minSort = deps.get(0).getDepSort();
        int maxSort = deps.get(deps.size() - 1).getDepSort();
        if (upOrDownTag == DepartmentConstant.UP_SHIFT && shiftDepFrom.getDepSort() != minSort) {
            shiftDeaprtments(upOrDownTag, deps, shiftDepFrom);
        } else if (upOrDownTag == DepartmentConstant.DOWN_SHIFT && shiftDepFrom.getDepSort() != maxSort) {
            shiftDeaprtments(upOrDownTag, deps, shiftDepFrom);
        } else {
            isSuccessfulShift = false;
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(isSuccessfulShift),
                HttpStatus.OK);
    }

    private synchronized void shiftDeaprtments(int upOrDownTag, List<Department> departments, Department department) {
        Department priorOrNexttDep = null;
        for (Department dep : departments) {
            if (upOrDownTag == DepartmentConstant.UP_SHIFT && dep.getDepSort() == department.getDepSort()) {
                break;
            } else if (upOrDownTag == DepartmentConstant.UP_SHIFT) {
                priorOrNexttDep = dep;
            }
            if (upOrDownTag == DepartmentConstant.DOWN_SHIFT && dep.getDepSort() > department.getDepSort()) {
                priorOrNexttDep = dep;
                break;
            }
        }
        int depSortTo = 0;
        int depSortFrom = 0;
        if (priorOrNexttDep != null) {
            if (upOrDownTag == 1) {
                depSortTo = priorOrNexttDep.getDepSort();
            }
            if ((upOrDownTag == 0) && (priorOrNexttDep != null)) {
                depSortTo = priorOrNexttDep.getDepSort();
            }
            depSortFrom = department.getDepSort();
            department.setDepSort(depSortTo);
            priorOrNexttDep.setDepSort(depSortFrom);
            departmentService.modifyDepartment(department);
            departmentService.modifyDepartment(priorOrNexttDep);
        }

    }
}
