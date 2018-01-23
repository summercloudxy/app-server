package com.zgiot.app.server.module.thingtag.pojo;

import com.zgiot.app.server.module.util.validate.AddValidate;
import com.zgiot.app.server.module.util.validate.DeleteValidate;
import com.zgiot.app.server.module.util.validate.UpdateValidate;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by wangfan on 2018/1/8.
 */
public class ThingTagRelation {

    /**
     * 主键
     */
    @NotNull(message = "主键(thingTagRelationId)不能为空", groups = {UpdateValidate.class, DeleteValidate.class})
    private Integer thingTagRelationId;

    private String thingCode;

    private String thingTagCode;

    private Date createDate;

    public Integer getThingTagRelationId() {
        return thingTagRelationId;
    }

    public void setThingTagRelationId(Integer thingTagRelationId) {
        this.thingTagRelationId = thingTagRelationId;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
