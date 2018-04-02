package com.zgiot.app.server.module.sfsubsc.controller;


import com.zgiot.app.server.module.sfsubsc.controller.handler.ChemicalTestsDataDetailHandler;
import com.zgiot.app.server.module.sfsubsc.controller.handler.InstantProductDetailHandler;
import com.zgiot.app.server.module.sfsubsc.service.SubscCardTypeService;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.restcontroller.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 卡片详情
 *
 * @author jys
 */
@RestController
@RequestMapping(value = GlobalConstants.API + GlobalConstants.API_VERSION + "/sfsubsc" + "/detail")
public class SubscriptionCardDetailController {
    @Autowired
    private SubscCardTypeService subscCardTypeService;
    @Autowired
    private InstantProductDetailHandler instantProductDetailHandler;
    @Autowired
    private ChemicalTestsDataDetailHandler chemicalTestsDataDetailHandler;


    /**
     * 查询瞬时产品量详情
     *
     * @param cardCode
     * @return
     */
    @RequestMapping(value = "/getInstantProductDetail", method = RequestMethod.GET)
    public ResponseEntity<String> getInstantProductDetail(@RequestParam("cardCode") String cardCode, @RequestParam("dateType") String dateType) {
        return new ResponseEntity<>(ServerResponse.buildOkJson(instantProductDetailHandler.getInstantProductDetail(cardCode, dateType)), HttpStatus.OK);
    }

    /**
     * 化验数据图详情
     *
     * @param cardCode
     * @param dateType
     * @param chartType
     * @return
     */
    @RequestMapping(value = "/getChemicalTestsDataChartDetail", method = RequestMethod.GET)
    public ResponseEntity<String> getChemicalTestsDataChartDetail(@RequestParam("cardCode") String cardCode, @RequestParam("dateType") String dateType, @RequestParam("chartType") String chartType) {
        return chemicalTestsDataDetailHandler.getChemicalTestsDataDetail(cardCode, dateType, chartType);
    }

    /**
     * 化验数据列表详情
     *
     * @param cardCode
     * @param dateType
     * @param chartType
     * @return
     */
    @RequestMapping(value = "/getChemicalTestsDataListDetail", method = RequestMethod.GET)
    public ResponseEntity<String> getChemicalTestsDataListDetail(@RequestParam("cardCode") String cardCode, @RequestParam("dateType") String dateType, @RequestParam("chartType") String chartType) {
        return chemicalTestsDataDetailHandler.getChemicalTestsDataDetail(cardCode, dateType, chartType);
    }


}
