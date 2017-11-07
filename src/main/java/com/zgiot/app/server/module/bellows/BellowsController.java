package com.zgiot.app.server.module.bellows;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.bellows.compressor.Compressor;
import com.zgiot.app.server.module.bellows.compressor.CompressorManager;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.restcontroller.ServerResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangwei
 */
@Controller
public class BellowsController {

    private static final Logger logger = LoggerFactory.getLogger(BellowsController.class);

    @Autowired
    private CompressorManager compressorManager;

    /**
     * 设置低压空压机智能模式
     * @param state 0：手动，1：智能
     * @param request
     * @return
     */
    @PostMapping(value = "api/bellows/compressor/intelligent")
    public ResponseEntity<String> setLowCompressorIntelligent(String state, HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        //param validate
        if (StringUtils.isEmpty(state)) {
            if (logger.isDebugEnabled()) {
                logger.debug("RequestId: {} send a bland request", requestId);
            }

            ServerResponse res = new ServerResponse("Blank request.", SysException.EC_UNKNOWN, 0);
            String resJSON = JSON.toJSONString(res);
            return new ResponseEntity<>(resJSON, HttpStatus.BAD_REQUEST);
        }

        if (!NumberUtils.isDigits(state)) {
            if (logger.isDebugEnabled()) {
                logger.debug("RequestId: {} send a wrong state: {}", requestId, state);
            }
            ServerResponse res = new ServerResponse("State cannot be parse to number.State :" + state, SysException.EC_UNKNOWN, 0);
            String resJSON = JSON.toJSONString(res);
            return new ResponseEntity<>(resJSON, HttpStatus.BAD_REQUEST);
        }

        int intelligent = Integer.parseInt(state);
        if (intelligent != Compressor.YES && intelligent != Compressor.NO) {
            if (logger.isDebugEnabled()) {
                logger.debug("RequestId: {} send a wrong state: {}", requestId, state);
            }
            ServerResponse res = new ServerResponse("State must be 1 or 0.State :" + state, SysException.EC_UNKNOWN, 0);
            String resJSON = JSON.toJSONString(res);
            return new ResponseEntity<>(resJSON, HttpStatus.BAD_REQUEST);
        }

        logger.info("RequestId: {} set low press compressor intelligent state: {}", requestId, state);
        compressorManager.changeLowCompressorIntelligent(intelligent, requestId);

        return new ResponseEntity<>(ServerResponse.buildOkJson(null),
                HttpStatus.OK);
    }
}
