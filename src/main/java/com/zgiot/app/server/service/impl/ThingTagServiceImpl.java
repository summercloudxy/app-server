package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.module.thingtag.dao.ThingTagMapper;
import com.zgiot.app.server.module.thingtag.pojo.ThingTag;
import com.zgiot.app.server.service.ThingTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangfan on 2018/1/9.
 */
@Service
public class ThingTagServiceImpl implements ThingTagService {

    private static final String CODE_PATH_CUT_OFF_RULE = "/";

    @Autowired
    ThingTagMapper thingTagMapper;

    @Override
    public ThingTag getThingTag(Integer thingTagId, String thingTagCode){
        ThingTag thingTag = new ThingTag();
        thingTag.setThingTagGroupId(thingTagId);
        thingTag.setCode(thingTagCode);
        List<ThingTag> thingTags = thingTagMapper.findThingTag(thingTag);
        if(thingTags.isEmpty()){
            return null;
        }
        return thingTags.get(0);
    }

    @Override
    public List<ThingTag> findThingTag(ThingTag thingTag) {
        return thingTagMapper.findThingTag(thingTag);
    }

    @Override
    public void addThingTag(ThingTag thingTag) {
        thingTag.setCodePath(getCodePath(thingTag.getParentId()));
        thingTagMapper.addThingTag(thingTag);
    }

    @Override
    public void updateThingTag(ThingTag thingTag){

        // 不允许修改code
        thingTag.setCode(null);
        thingTagMapper.updateThingTag(thingTag);
        // 修改整体路径
        if(null != thingTag.getParentId()){
            updateCodePath(thingTag);
        }
    }

    @Override
    public void deleteThingTag(ThingTag thingTag) {
        thingTagMapper.deleteThingTag(thingTag);
    }

    /**
     * 修改整体codePath路径
     * @param thingTag
     */
    private void updateCodePath(ThingTag thingTag){

        // 获取所有待修改codePath的数据
        ThingTag thingTagSelect = new ThingTag();
        thingTagSelect.setThingTagId(thingTag.getThingTagId());
        ThingTag thingTagBefore = thingTagMapper.findThingTag(thingTagSelect).get(0);
        thingTagSelect = new ThingTag();
        thingTagSelect.setCodePathLike(thingTagBefore.getCodePath());
        List<ThingTag> updateThingTagLists = thingTagMapper.findThingTag(thingTagSelect);

        // 获取替换成codePath
        String codePathReplace = getCodePath(thingTag.getParentId());

        // 批量替换所有codePath
        for(ThingTag updateThingTag:updateThingTagLists){
            updateThingTag.setCodePath(
                    updateThingTag.getCodePath().replaceAll(thingTagBefore.getCodePath(),codePathReplace));
            thingTagMapper.updateThingTag(updateThingTag);
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
        ThingTag tagParent = new ThingTag();
        tagParent.setThingTagId(parentId);
        tagParent = thingTagMapper.findThingTag(tagParent).get(0);
        StringBuffer codePath = new StringBuffer();
        codePath.append(tagParent.getCodePath())
                .append(tagParent.getCode())
                .append(CODE_PATH_CUT_OFF_RULE);
        return codePath.toString();
    }
}
