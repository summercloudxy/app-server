package com.zgiot.app.server.module.thingtag.pojo;

import com.zgiot.app.server.module.util.validate.AddValidate;
import com.zgiot.app.server.module.util.validate.DeleteValidate;
import com.zgiot.app.server.module.util.validate.UpdateValidate;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by wangfan on 2018/1/8.
 */
public class ThingTag {

    /**
     * 主键
     */
    @NotNull(message = "主键(thingTagId)不能为空", groups = {UpdateValidate.class, DeleteValidate.class})
    private Integer thingTagId;

    /**
     * code编号
     */
    @NotNull(message = "code编号(code)不能为空", groups = {AddValidate.class})
    private String code;

    /**
     * 标签名称
     */
    @NotNull(message = "标签名称(tagName)不能为空", groups = {AddValidate.class})
    private String tagName;

    /**
     * 所属父类标签id
     */
    private Integer parentId;

    /**
     * 层级路径
     */
    private String codePath;

    /**
     * 层级路径模糊查询
     */
    private String codePathLike;

    /**
     * 所属标签大类
     */
    private Integer thingTagGroupId;

    /**
     * 根据标签组code
     */
    private String thingTagGroupCode;

    /**
     * 创建时间
     */
    private Date createDate;

    public Integer getThingTagId() {
        return thingTagId;
    }

    public void setThingTagId(Integer thingTagId) {
        this.thingTagId = thingTagId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getCodePath() {
        return codePath;
    }

    public void setCodePath(String codePath) {
        this.codePath = codePath;
    }

    public String getCodePathLike() {
        return codePathLike;
    }

    public void setCodePathLike(String codePathLike) {
        this.codePathLike = codePathLike;
    }

    public Integer getThingTagGroupId() {
        return thingTagGroupId;
    }

    public void setThingTagGroupId(Integer thingTagGroupId) {
        this.thingTagGroupId = thingTagGroupId;
    }

    public String getThingTagGroupCode() {
        return thingTagGroupCode;
    }

    public void setThingTagGroupCode(String thingTagGroupCode) {
        this.thingTagGroupCode = thingTagGroupCode;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
