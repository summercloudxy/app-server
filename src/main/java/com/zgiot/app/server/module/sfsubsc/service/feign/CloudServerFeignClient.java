package com.zgiot.app.server.module.sfsubsc.service.feign;

import com.zgiot.app.server.module.sfsubsc.entity.dto.CardDataDTO;
import com.zgiot.common.restcontroller.ServerResponse;
import feign.Headers;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author jys
 */
public interface CloudServerFeignClient {
    /**
     * cloudserver客户端
     *
     * @param cardDataDTOS
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @RequestLine("POST /sfsubsc/usersubsc/saveAllCardDatas")
    ServerResponse saveAllCardDatas(@RequestBody List<CardDataDTO> cardDataDTOS);

}