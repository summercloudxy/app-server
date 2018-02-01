package com.zgiot.app.server.service;

import com.zgiot.common.pojo.FileModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FileService {
    int TEST = 1;
    int IN = 2;
    int OUT = 3;

    int IMAGE = 1;
    int VOICE = 2;
    int VIDEO = 3;

    FileModel uploadFile(MultipartFile file, String fileName, String moduleName, Integer fileType, String userId) throws IOException;
}
