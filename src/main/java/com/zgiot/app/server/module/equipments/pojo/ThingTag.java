package com.zgiot.app.server.module.equipments.pojo;

import java.util.Date;

public class ThingTag {
    /**
     * 主键
     */
    private Long id;
    /**
     * 编号
     */
    private String code;
    /**
     * 名称
     */
    private String tagName;
    /**
     * 父节点主键
     */
    private Long parentId;
    /**
     *
     */
    private String codePath;
    /**
     * 分组主键
     */
    private Long thingTagGroupId;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新时间
     */
    private Date updateDate;
    /**
     *
     */
    private String comments;
    /**
     *
     */
    private String operator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCodePath() {
        return codePath;
    }

    public void setCodePath(String codePath) {
        this.codePath = codePath;
    }

    public Long getThingTagGroupId() {
        return thingTagGroupId;
    }

    public void setThingTagGroupId(Long thingTagGroupId) {
        this.thingTagGroupId = thingTagGroupId;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
