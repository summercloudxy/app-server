package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.module.metrictag.dao.MetricTagMapper;
import com.zgiot.app.server.module.metrictag.pojo.MetricTag;
import com.zgiot.app.server.service.MetricTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangfan on 2018/1/9.
 */
@Service
public class MetricTagServiceImpl implements MetricTagService {

    private static String CODE_PATH_CUT_OFF_RULE = "/";

    @Autowired
    MetricTagMapper metricTagMapper;

    @Override
    public MetricTag getMetricTag(Integer metricTagId, String metricTagCode){
        MetricTag metricTag = new MetricTag();
        metricTag.setMetricTagGroupId(metricTagId);
        metricTag.setCode(metricTagCode);
        List<MetricTag> metricTags = metricTagMapper.getMetricTag(metricTag);
        if(null == metricTags){
            return null;
        }
        return metricTags.get(0);
    }

    @Override
    public List<MetricTag> findMetricTag(MetricTag metricTag) {
        List<MetricTag> MetricTags = metricTagMapper.getMetricTag(metricTag);
        return MetricTags;
    }

    @Override
    public void addMetricTag(MetricTag MetricTag) {
        MetricTag.setCodePath(getCodePath(MetricTag.getParentId()));
        metricTagMapper.addMetricTag(MetricTag);
    }

    @Override
    public void updateMetricTag(MetricTag MetricTag){

        // 不允许修改code
        MetricTag.setCode(null);
        metricTagMapper.updateMetricTag(MetricTag);
        // 修改整体路径
        if(null != MetricTag.getParentId()){
            updateCodePath(MetricTag);
        }
    }

    @Override
    public void deleteMetricTag(MetricTag MetricTag) {
        metricTagMapper.deleteMetricTag(MetricTag);
    }

    /**
     * 修改整体codePath路径
     * @param metricTag
     */
    private void updateCodePath(MetricTag metricTag){

        // 获取所有待修改codePath的数据
        MetricTag metricTagSelect = new MetricTag();
        metricTagSelect.setMetricTagId(metricTag.getMetricTagId());
        MetricTag MetricTagBefore = metricTagMapper.getMetricTag(metricTagSelect).get(0);
        metricTagSelect = new MetricTag();
        metricTagSelect.setCodePathLike(MetricTagBefore.getCodePath());
        List<MetricTag> updateMetricTagLists = metricTagMapper.getMetricTag(metricTagSelect);

        // 获取替换成codePath
        String codePathReplace = getCodePath(metricTag.getParentId());

        // 批量替换所有codePath
        for(MetricTag updateMetricTag:updateMetricTagLists){
            updateMetricTag.setCodePath(
                    updateMetricTag.getCodePath().replaceAll(MetricTagBefore.getCodePath(),codePathReplace));
            metricTagMapper.updateMetricTag(updateMetricTag);
        }
    }

    /**
     * 获取codePath
     * @param parentId
     * @return
     */
    private String getCodePath(Integer parentId){
        if(null == parentId){
            return CODE_PATH_CUT_OFF_RULE;
        }
        MetricTag tagParent = new MetricTag();
        tagParent.setMetricTagId(parentId);
        tagParent = metricTagMapper.getMetricTag(tagParent).get(0);
        StringBuffer codePath = new StringBuffer();
        codePath.append(tagParent.getCodePath())
                .append(tagParent.getCode())
                .append(CODE_PATH_CUT_OFF_RULE);
        return codePath.toString();
    }
}
