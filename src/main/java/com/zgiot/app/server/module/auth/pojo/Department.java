package com.zgiot.app.server.module.auth.pojo;

public class Department {
    private int id;
    private String level;
    private String code;
    private String name;
    private int parentId;
    private String parentName;
    private int depSort;
    private String remark;
    private int companyId;
    private String departmentManagerName;

    public int getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }

    public String getCode() {
        return code;
    }

    public int getParentId() {
        return parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public int getDepSort() {
        return depSort;
    }

    public String getRemark() {
        return remark;
    }

    public int getCompanyId() {
        return companyId;
    }

    public String getDepartmentManagerName() {
        return departmentManagerName;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setDepSort(int depSort) {
        this.depSort = depSort;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public void setDepartmentManagerName(String departmentManagerName) {
        this.departmentManagerName = departmentManagerName;
    }

    public void setName(String name) {
        this.name = name;
    }
}
