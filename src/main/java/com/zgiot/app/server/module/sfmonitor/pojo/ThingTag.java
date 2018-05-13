package com.zgiot.app.server.module.sfmonitor.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
     * 页面路由
     */
    private String webPath;
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

    /**
     * 用于菜单
     */
    private List nodeList = new ArrayList();

    /**
     * 运行设备数量
     */
    private Integer thingRunCount;

    /**
     * 故障设备数量
     */
    private Integer thingFaultCount;

    /**
     * 待机设备数量
     */
    private Integer thingStopCount;

    /**
     * 系统图标
     */
    private String imageName;

    /**
     * 排序序号
     */
    private Float sort;

    public Float getSort() {
        return sort;
    }

    public void setSort(Float sort) {
        this.sort = sort;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Integer getThingRunCount() {
        return thingRunCount;
    }

    public void setThingRunCount(Integer thingRunCount) {
        this.thingRunCount = thingRunCount;
    }

    public Integer getThingFaultCount() {
        return thingFaultCount;
    }

    public void setThingFaultCount(Integer thingFaultCount) {
        this.thingFaultCount = thingFaultCount;
    }

    public Integer getThingStopCount() {
        return thingStopCount;
    }

    public void setThingStopCount(Integer thingStopCount) {
        this.thingStopCount = thingStopCount;
    }

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

    public String getWebPath() {
        return webPath;
    }

    public void setWebPath(String webPath) {
        this.webPath = webPath;
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

    public List getNodeList() {
        return nodeList;
    }

    public void setNodeList(List nodeList) {
        this.nodeList = nodeList;
    }
}
