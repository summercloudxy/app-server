package com.zgiot.app.server.module.bellows.pojo;

import java.util.List;

/**
 * @author wangwei
 */
public class ValveTeamResult {

    /**
     * 分组列表
     */
    private List<ValveTeam> teams;

    /**
     * 块煤分组数量
     */
    private int lumpTeamCount;

    /**
     * 末煤分组数量
     */
    private int slackTeamCount;

    /**
     * 最大分组id
     */
    private long maxTeamId;

    public List<ValveTeam> getTeams() {
        return teams;
    }

    public void setTeams(List<ValveTeam> teams) {
        this.teams = teams;
    }

    public int getLumpTeamCount() {
        return lumpTeamCount;
    }

    public void setLumpTeamCount(int lumpTeamCount) {
        this.lumpTeamCount = lumpTeamCount;
    }

    public int getSlackTeamCount() {
        return slackTeamCount;
    }

    public void setSlackTeamCount(int slackTeamCount) {
        this.slackTeamCount = slackTeamCount;
    }

    public long getMaxTeamId() {
        return maxTeamId;
    }

    public void setMaxTeamId(long maxTeamId) {
        this.maxTeamId = maxTeamId;
    }
}
