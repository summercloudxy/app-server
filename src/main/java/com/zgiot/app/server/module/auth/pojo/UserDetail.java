package com.zgiot.app.server.module.auth.pojo;

import java.util.Date;

public class UserDetail {
    private long userId;
    private String idNum;
    private String email;
    private String mobile;
    private Date joinDt;
    private String code;
    private String remark;
    private boolean sex;
    private Date birthday;
    private String address;
    private String workAddress;
    private String graduatedFrom;
    private String major;
    private String emergencyContactMobile;
    private String fixedTele;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getJoinDt() {
        return joinDt;
    }

    public void setJoinDt(Date joinDt) {
        this.joinDt = joinDt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isSex() {
        return sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public String getGraduatedFrom() {
        return graduatedFrom;
    }

    public String getMajor() {
        return major;
    }

    public String getEmergencyContactMobile() {
        return emergencyContactMobile;
    }

    public String getFixedTele() {
        return fixedTele;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public void setGraduatedFrom(String graduatedFrom) {
        this.graduatedFrom = graduatedFrom;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setEmergencyContactMobile(String emergencyContactMobile) {
        this.emergencyContactMobile = emergencyContactMobile;
    }

    public void setFixedTele(String fixedTele) {
        this.fixedTele = fixedTele;
    }
}
