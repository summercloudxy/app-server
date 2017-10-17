package com.zgiot.app.server.service.impl;


import com.zgiot.common.pojo.FileModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {

	void saveFile(FileModel attachment);

}
