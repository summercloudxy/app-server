package com.zgiot.app.server.module.auth.controller.user;

import com.zgiot.app.server.module.auth.pojo.UserPack;

import java.util.List;

public class UserReturn {
    private List<UserPack> userPacks;
    private int userSum;

    public List<UserPack> getUserPacks() {
        return userPacks;
    }

    public int getUserSum() {
        return userSum;
    }

    public void setUserPacks(List<UserPack> userPacks) {
        this.userPacks = userPacks;
    }

    public void setUserSum(int userSum) {
        this.userSum = userSum;
    }
}
