package com.zgiot.app.server.module.equipments.service;


import com.zgiot.app.server.module.equipments.pojo.RelThingSystem;

public interface RelThingSystemService {

    /**
     * 设备与系统关系-添加
     *
     * @param relThingSystem
     */
    public void addRelThingSystem(RelThingSystem relThingSystem);

    /**
     * 设备与系统关系-删除
     *
     * @param thingCode
     */
    public void deleteRelThingSystemByThingCode(String thingCode);

}
