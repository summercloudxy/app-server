package com.zgiot.app.server.module.auth.controller.workshopPost;

import com.zgiot.app.server.module.auth.service.WorkshopPostService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(GlobalConstants.API + GlobalConstants.API_VERSION + "/workshopPost")
public class WorkshopPostController {
    @Autowired
    private WorkshopPostService workshopPostService;

    @ApiOperation("获取指定部门下的车间岗位")
    @GetMapping()
    public ResponseEntity<String> getAllStations(@RequestParam Integer departmentId) {
        List<WorkshopPost> workshopPostList = workshopPostService.getWorkshopPostList(departmentId);
        return new ResponseEntity<>(ServerResponse.buildOkJson(workshopPostList),
                HttpStatus.OK);
    }
}
