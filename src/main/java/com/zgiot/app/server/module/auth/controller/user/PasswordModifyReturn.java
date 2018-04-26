package com.zgiot.app.server.module.auth.controller.user;

public class PasswordModifyReturn {
    private boolean isExsitUser;
    private boolean isRightOldPassword;
    private boolean isConsistentPassWord;
    private boolean isSuccess;

    public boolean isExsitUser() {
        return isExsitUser;
    }

    public boolean isRightOldPassword() {
        return isRightOldPassword;
    }

    public boolean isConsistentPassWord() {
        return isConsistentPassWord;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setRightOldPassword(boolean rightOldPassword) {
        isRightOldPassword = rightOldPassword;
    }

    public void setConsistentPassWord(boolean consistentPassWord) {
        isConsistentPassWord = consistentPassWord;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public void setExsitUser(boolean exsitUser) {
        isExsitUser = exsitUser;
    }
}
