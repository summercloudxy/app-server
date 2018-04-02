package com.zgiot.app.server.module.equipments.service;


import com.zgiot.app.server.module.equipments.pojo.RelThingSystem;

public interface RelThingSystemService {

    public void addRelThingSystem(RelThingSystem relThingSystem);

    public void deleteRelThingSystemByThingCode(String thing_code);
}
