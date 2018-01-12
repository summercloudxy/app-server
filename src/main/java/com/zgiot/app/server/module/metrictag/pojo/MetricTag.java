package com.zgiot.app.server.module.metrictag.pojo;

import com.zgiot.app.server.module.util.validate.AddValidate;
import com.zgiot.app.server.module.util.validate.DeleteValidate;
import com.zgiot.app.server.module.util.validate.UpdateValidate;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by wangfan on 2018/1/8.
 */
public class MetricTag {

    /**
     * 主键
     */
    @NotNull(message = "主键(thingTagId)不能为空", groups = {UpdateValidate.class, DeleteValidate.class})
    private Integer metricTagId;

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
    private Integer metricTagGroupId;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 逻辑删除
     */
    private Integer isDelete;

    public Integer getMetricTagId() {
        return metricTagId;
    }

    public void setMetricTagId(Integer metricTagId) {
        this.metricTagId = metricTagId;
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

    public Integer getMetricTagGroupId() {
        return metricTagGroupId;
    }

    public void setMetricTagGroupId(Integer metricTagGroupId) {
        this.metricTagGroupId = metricTagGroupId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}
