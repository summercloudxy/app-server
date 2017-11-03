package com.zgiot.app.server.service.impl;

import com.zgiot.app.server.service.FileService;
import com.zgiot.common.pojo.FileModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FileServiceImpl implements FileService{



    @Value("${uploadFile.dir}")
    private String basePath;

    @Autowired
    private FileMapper fileMapper;

    /**
     * @Description: 上传文件
     * @param file
     * @param fileName 文件名
     * @param moduleName 所属模块
     * @param fileType 文件类型 1：图片、2：语音、3：视频、4：其他
     * @param userId userId
     * @return
     * @throws Exception Attachment
     */
    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    public FileModel uploadFile(MultipartFile file, String fileName, String moduleName, Integer fileType, String userId) throws Exception {
        Date date = new Date();
        String currentDay = new SimpleDateFormat("yyyyMMdd").format(date);
        String fileTypePath ;
        String[] originalFileName = file.getOriginalFilename().split("\\.");
        String suffix ="." + originalFileName[originalFileName.length -1];
        if(fileType == IMAGE){
            fileTypePath = "IMAGE";
        }else if(fileType == VOICE){
            fileTypePath = "VOICE";
        }else if(fileType == VIDEO){
            fileTypePath = "VIDEO";
        }else{
            fileTypePath = "TEMP";
        }

        // 相对路径
        String relatePath = currentDay + "/" + moduleName + "/" + fileTypePath;
        relatePath = relatePath + "/" + fileName;
        String pcPath = basePath+ "/"  + relatePath;

        File file2 = new File(pcPath);
        if (!file2.getParentFile().exists()) {
            file2.getParentFile().mkdirs();
        }
        file.transferTo(file2);

        // 保存到数据库
        FileModel attachment = new FileModel();
        attachment.setFileName(fileName);
        attachment.setFilePath(relatePath);
        attachment.setAbsolutePath(relatePath);
        attachment.setFile(file2);
        attachment.setContentType(file.getContentType());
        attachment.setCreatUserId(userId);
        attachment.setCreatDate(date);
        fileMapper.saveFile(attachment);
        return attachment;
    }



}
