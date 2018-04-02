package com.zgiot.app.server.module.sfsubsc.service.feign;

import com.zgiot.app.server.module.sfsubsc.entity.dto.CardDataDTO;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author jys
 */
@FeignClient(name = "cloudserver", url = "${cloud.service.url}${cloud.service.path}")
public interface CloudServerFeignClient {
    /**
     * cloudserver客户端
     *
     * @param cardDataDTOS
     * @return
     */
    @RequestMapping(value = "/sfsubsc/usersubsc/saveAllCardDatas", method = RequestMethod.POST)
    ServerResponse saveAllCardDatas(@RequestBody List<CardDataDTO> cardDataDTOS, @RequestHeader("Authorization") String authorization);

}