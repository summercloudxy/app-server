package com.zgiot.app.server.module.reportforms.output.dao;

import com.zgiot.app.server.module.reportforms.output.pojo.ProductTransPortCond;
import com.zgiot.app.server.module.reportforms.output.pojo.ProductTransport;
import com.zgiot.app.server.module.reportforms.output.pojo.ProductTransportRemarks;
import com.zgiot.app.server.module.reportforms.output.pojo.TransportSaleStatistics;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface ProductTransportMapper {

    List<ProductTransport> getProductTransport(Date productStartTime);

    void insertProductTransport(ProductTransport value);

    List<ProductTransport> getTransPort(ProductTransPortCond productTransPortCond);

    ProductTransportRemarks getProductTransportMessage(Date productStartTime);

    void updateProductTransportMessage(ProductTransportRemarks productTransportMessage);

    void insertProductTransportMessage(ProductTransportRemarks productTransport);

    List<ProductTransport> getTransPortDayVolume(ProductTransPortCond productTransPortCond);

    List<ProductTransport> getTransPortMonthVolume(ProductTransPortCond productTransPortCond);

    List<ProductTransport> getTransPortYearVolume(ProductTransPortCond productTransPortCond);
}
