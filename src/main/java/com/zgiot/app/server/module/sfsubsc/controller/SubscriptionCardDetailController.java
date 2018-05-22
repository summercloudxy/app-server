package com.zgiot.app.server.module.sfsubsc.controller;


import com.zgiot.app.server.module.sfsubsc.controller.handler.CoalAnalysisDataDetailHandler;
import com.zgiot.app.server.module.sfsubsc.controller.handler.InstantProductDetailHandler;
import com.zgiot.app.server.module.sfsubsc.entity.vo.CoalQualityDetailVO;
import com.zgiot.app.server.module.sfsubsc.entity.vo.ProductionDetailVO;
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
    private CoalAnalysisDataDetailHandler coalAnalysisDataDetailHandler;


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
    @RequestMapping(value = "/getCoalAnalysisDataChartDetail", method = RequestMethod.GET)
    public ResponseEntity<String> getCoalAnalysisDataChartDetail(@RequestParam("cardCode") String cardCode, @RequestParam("dateType") String dateType, @RequestParam("chartType") String chartType) {
        return coalAnalysisDataDetailHandler.getCoalAnalysisDataChartDetail(cardCode, dateType, chartType);
    }

    /**
     * 化验数据列表详情
     *
     * @param cardCode
     * @param dateType
     * @param chartType
     * @return
     */
    @RequestMapping(value = "/getCoalAnalysisDataListDetail", method = RequestMethod.GET)
    public ResponseEntity<String> getCoalAnalysisDataListDetail(@RequestParam("cardCode") String cardCode, @RequestParam("dateType") String dateType, @RequestParam("chartType") String chartType) {
        return coalAnalysisDataDetailHandler.getCoalAnalysisDataChartDetail(cardCode, dateType, chartType);
    }

    /**
     * 班累计煤质统计详情
     *
     * @param date  日期
     * @param shift 班次
     * @return
     */
    @RequestMapping(value = "/getCoalQualityDetail", method = RequestMethod.GET)
    public ResponseEntity<String> getCoalQualityDetail(@RequestParam("date") String date, @RequestParam("shift") String shift) {
        CoalQualityDetailVO coalQualityDetail = coalAnalysisDataDetailHandler.getCoalQualityDetail(date, shift);
        if (null == coalQualityDetail) {
            coalQualityDetail = new CoalQualityDetailVO();
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(coalQualityDetail), HttpStatus.OK);
    }

    /**
     * 班累计生产统计详情
     *
     * @param date  日期
     * @param shift 班次
     * @return
     */
    @RequestMapping(value = "/getProductionDetail", method = RequestMethod.GET)
    public ResponseEntity<String> getProductionDetail(@RequestParam("date") String date, @RequestParam("shift") String shift) {
        ProductionDetailVO productionDetail = coalAnalysisDataDetailHandler.getProductionDetail(date, shift);
        if (null == productionDetail) {
            productionDetail = new ProductionDetailVO();
        }
        return new ResponseEntity<>(ServerResponse.buildOkJson(productionDetail), HttpStatus.OK);
    }


}
