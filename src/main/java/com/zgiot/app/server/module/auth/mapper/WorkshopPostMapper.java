package com.zgiot.app.server.module.auth.mapper;

import com.zgiot.app.server.module.auth.controller.workshopPost.WorkshopPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WorkshopPostMapper {
    List<WorkshopPost> getWorkshopPostList(@Param("departmentId") Integer departmentId);

    WorkshopPost getWorkshopPostById(@Param("workshopPostId") Integer workshopPostId);

    void insertWorkshopPost(WorkshopPost workshopPost);

    List<WorkshopPost> getWorkshopPostByUserId(@Param("userId") Long userId);

}
