package com.zgiot.app.server.module.reportforms.output.dao;

import com.zgiot.app.server.module.reportforms.output.pojo.Transport;
import com.zgiot.app.server.module.reportforms.output.pojo.TransportSaleStatistics;
import com.zgiot.app.server.module.reportforms.output.pojo.TransportVolume;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface TransPortMapper {

    void createTransPort(Transport transport);

    Transport getMostNewTransPortByType(Integer coalType);

    void editTransPort(Transport transport);

    void editTransportSaleStatistics(TransportSaleStatistics transportSaleStatistics);

    void createSaleStatistics(TransportSaleStatistics transportSaleStatistics);

    List<TransportSaleStatistics> getMostNewSaleStatisticsOut(Date nowDutyStartTime);

    List<TransportSaleStatistics> getSaleStatisticsByDutyStartTimeOut(Date nowDutyStartTime);

    List<TransportVolume> getTransportVolumeByDate(Date nowDutyStartTime);

    TransportSaleStatistics getMostNewSaleStatisticsLocality(TransportSaleStatistics sale);

    List<Transport> getTransPortByDate(Date nowDutyStartTime);

    List<TransportSaleStatistics> getSaleByDateLocality(Date nowDutyStartTime);

    List<TransportSaleStatistics> getMostNewSaleByDate(Date nowDutyStartTime);
}
