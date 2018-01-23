package com.zgiot.app.server.module.metrictag.pojo;

import com.zgiot.app.server.module.util.validate.AddValidate;
import com.zgiot.app.server.module.util.validate.DeleteValidate;
import com.zgiot.app.server.module.util.validate.UpdateValidate;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by wangfan on 2018/1/8.
 */
public class MetricTagGroup {

    /**
     * 主键
     */
    @NotNull(message = "主键(metricTagGroupId)不能为空", groups = {UpdateValidate.class, DeleteValidate.class})
    private Integer metricTagGroupId;

    /**
     * code编号
     */
    @NotNull(message = "code编号(code)不能为空", groups = {AddValidate.class})
    private String code;

    /**
     * 标签组名称
     */
    @NotNull(message = "标签组名称(tagGroupName)不能为空", groups = {AddValidate.class})
    private String tagGroupName;

    /**
     * 创建时间
     */
    private Date createDate;

    public Integer getMetricTagGroupId() {
        return metricTagGroupId;
    }

    public void setMetricTagGroupId(Integer metricTagGroupId) {
        this.metricTagGroupId = metricTagGroupId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTagGroupName() {
        return tagGroupName;
    }

    public void setTagGroupName(String tagGroupName) {
        this.tagGroupName = tagGroupName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
