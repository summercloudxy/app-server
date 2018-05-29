package com.zgiot.app.server.module.reportforms.output.service;

import com.zgiot.app.server.module.reportforms.output.pojo.TransBean;
import com.zgiot.app.server.module.reportforms.output.pojo.Transport;
import com.zgiot.app.server.module.reportforms.output.pojo.TransportSaleStatistics;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Transactional
public interface TransPortService {

    void updateTransPort(List<Transport> transportList);


    void updateSaleStatistics(List<TransportSaleStatistics> transportSaleStatisticsList);

    TransBean getTransPortCacheBean(Date date);

    Map<Integer,List<Transport>> getTransportInDuration(Date startTime, Date endTime);
}
