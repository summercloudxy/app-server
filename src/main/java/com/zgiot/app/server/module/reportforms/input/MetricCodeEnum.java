package com.zgiot.app.server.module.reportforms.input;

import com.zgiot.common.constants.MetricCodes;
import com.zgiot.common.pojo.MetricModel;

public enum MetricCodeEnum {
    COAL_ANALYSIS(MetricModel.CATEGORY_ASSAY, MetricCodes.ASSAY_DATA,MetricCodes.ASSAY_SAMPLE),
    PRODUCTION_INSPECT(MetricModel.CATEGORY_PRODUCTION_INSPECT,MetricCodes.PRODUCT_INSPECT_DATA,MetricCodes.PRODUCT_INSPECT_SAMPLE);

    private String metricType;
    private String dataCode;
    private String sampleCode;

    MetricCodeEnum(String metricType, String dataCode, String sampleCode) {
        this.metricType = metricType;
        this.dataCode = dataCode;
        this.sampleCode = sampleCode;
    }

    public String getMetricType() {
        return metricType;
    }

    public String getDataCode() {
        return dataCode;
    }

    public String getSampleCode() {
        return sampleCode;
    }
}
