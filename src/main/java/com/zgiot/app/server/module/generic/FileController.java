package com.zgiot.app.server.module.generic;

import com.zgiot.app.server.service.FileService;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.pojo.FileModel;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileController {
    @Autowired
    private FileService fileService;

    @RequestMapping(name = "file/upload", method = RequestMethod.POST)
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file, @RequestParam String module,
            @RequestParam int type, @RequestParam String userId) throws Exception {
        String uri = null;
        try {
            FileModel fileModel = fileService.uploadFile(file, file.getOriginalFilename(), module, type, userId);
            uri = fileModel.getAbsolutePath();
        } catch (Exception e) {
            throw new SysException("file upload fail", SysException.EC_UNKNOWN);
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(uri), HttpStatus.OK);
    }


}
