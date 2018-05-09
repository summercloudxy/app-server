package com.zgiot.app.server.module.filterpress.pojo;

import java.util.Date;

public class FilterPressPlateStatistic {
    private int id;
    private boolean isDayShift;
    private int term;
    private int totalPlateCount;
    private int team;
    private Date dateTime;

    public int getId() {
        return id;
    }

    public boolean getIsDayShift() {
        return isDayShift;
    }

    public int getTerm() {
        return term;
    }

    public int getTotalPlateCount() {
        return totalPlateCount;
    }

    public int getTeam() {
        return team;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIsDayShift(boolean dayShift) {
        isDayShift = dayShift;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public void setTotalPlateCount(int totalPlateCount) {
        this.totalPlateCount = totalPlateCount;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
