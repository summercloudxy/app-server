package com.zgiot.app.server.module.auth.service;

import com.zgiot.app.server.module.auth.controller.workshopPost.WorkshopPost;
import com.zgiot.app.server.module.auth.mapper.WorkshopPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class WorkshopPostService {
    @Autowired
    private WorkshopPostMapper workshopPostMapper;
    private static final String SPLIT_CODE = ";";

    public List<WorkshopPost> getWorkshopPostList(Integer departmentId) {
        List<WorkshopPost> workshopPostList = workshopPostMapper.getWorkshopPostList(departmentId);
        workshopPostList.stream().forEach(t -> {
            String thingCodes = t.getThingCodes();
            String[] thingCodeArray = thingCodes.split(SPLIT_CODE);
            List thingCodeList = CollectionUtils.arrayToList(thingCodeArray);
            t.setThingCodeList(thingCodeList);
        });
        return workshopPostList;
    }

    public WorkshopPost getWorkshopPostById(Integer workshopPostId){
        return workshopPostMapper.getWorkshopPostById(workshopPostId);
    }

    public List<WorkshopPost> getWorkshopPostByUserId(Long userId){
        return workshopPostMapper.getWorkshopPostByUserId(userId);
    }


}
