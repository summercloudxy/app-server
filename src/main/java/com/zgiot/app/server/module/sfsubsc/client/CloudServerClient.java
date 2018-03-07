package com.zgiot.app.server.module.sfsubsc.client;

import com.zgiot.app.server.module.sfsubsc.dto.CardData;
import com.zgiot.common.restcontroller.ServerResponse;
import feign.Headers;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author jys
 */
public interface CloudServerClient {
    /**
     * cloudserver客户端
     *
     * @param cardDatas
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @RequestLine("POST cloudServer/v1/subsc/saveAllCardDatas")
    ServerResponse saveAllCardDatas(@RequestBody List<CardData> cardDatas);

}