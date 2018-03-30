package com.zgiot.app.server.module.equipments.service;

import com.zgiot.app.server.module.equipments.pojo.CoalStorageDepot;

import java.util.List;

public interface CoalStorageDepotService {

    public void addCoalStorageDepot(CoalStorageDepot coalStorageDepot);

    public CoalStorageDepot getCoalStorageDepotById(Long id);

    public void editCoalStorageDepot(CoalStorageDepot coalStorageDepot);

    public void deleteCoalStorageDepot(Long id);

    public List<CoalStorageDepot> getCoalStorageDepotAll(int pageNum, int pageSize);
}
