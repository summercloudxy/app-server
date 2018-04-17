package com.zgiot.app.server.module.sfmonitor.pojo;

import java.util.Date;

/**
 * Created by liangyu on 2018/4/16
 */
public class RelSFSysMonTermThing {
    private int id;
    private String thingCode;
    private String thingTagCode;
    private short sort;
    private short termId;
    private Date createDate;
    private Date updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThingCode() {
        return thingCode;
    }

    public void setThingCode(String thingCode) {
        this.thingCode = thingCode;
    }

    public String getThingTagCode() {
        return thingTagCode;
    }

    public void setThingTagCode(String thingTagCode) {
        this.thingTagCode = thingTagCode;
    }

    public short getSort() {
        return sort;
    }

    public void setSort(short sort) {
        this.sort = sort;
    }

    public short getTermId() {
        return termId;
    }

    public void setTermId(short termId) {
        this.termId = termId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
