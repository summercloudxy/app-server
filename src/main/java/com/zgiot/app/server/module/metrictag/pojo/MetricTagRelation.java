package com.zgiot.app.server.module.metrictag.pojo;

import com.zgiot.app.server.module.util.validate.AddValidate;
import com.zgiot.app.server.module.util.validate.DeleteValidate;
import com.zgiot.app.server.module.util.validate.UpdateValidate;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by wangfan on 2018/1/8.
 */
public class MetricTagRelation {

    /**
     * 主键
     */
    @NotNull(message = "主键(metricTagRelationId)不能为空", groups = {UpdateValidate.class, DeleteValidate.class})
    private Integer metricTagRelationId;

    @NotBlank(message = "信号类型(metricCode)不能为空", groups = {AddValidate.class})
    private String metricCode;

    @NotBlank(message = "信号标签(metricTagCode)不能为空", groups = {AddValidate.class})
    private String metricTagCode;

    private Date createDate;

    public Integer getMetricTagRelationId() {
        return metricTagRelationId;
    }

    public void setMetricTagRelationId(Integer metricTagRelationId) {
        this.metricTagRelationId = metricTagRelationId;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }

    public String getMetricTagCode() {
        return metricTagCode;
    }

    public void setMetricTagCode(String metricTagCode) {
        this.metricTagCode = metricTagCode;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
