package com.zgiot.app.server.module.bellows;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.bellows.compressor.Compressor;
import com.zgiot.app.server.module.bellows.compressor.CompressorManager;
import com.zgiot.app.server.module.bellows.compressor.pojo.CompressorLog;
import com.zgiot.app.server.module.bellows.enumeration.EnumCompressorOperation;
import com.zgiot.common.constants.BellowsConstants;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<String> setLowCompressorIntelligent(Integer state, HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        //param validate
        if (state == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("RequestId: {} send a bland request", requestId);
            }

            ServerResponse res = new ServerResponse("Blank request.", SysException.EC_UNKNOWN, 0);
            String resJSON = JSON.toJSONString(res);
            return new ResponseEntity<>(resJSON, HttpStatus.BAD_REQUEST);
        }

        if (state != Compressor.YES && state != Compressor.NO) {
            if (logger.isDebugEnabled()) {
                logger.debug("RequestId: {} send a wrong state: {}", requestId, state);
            }
            ServerResponse res = new ServerResponse("State must be 1 or 0.Got state :" + state + ".", SysException.EC_UNKNOWN, 0);
            String resJSON = JSON.toJSONString(res);
            return new ResponseEntity<>(resJSON, HttpStatus.BAD_REQUEST);
        }

        logger.info("RequestId: {} set low press compressor intelligent state: {}", requestId, state);
        compressorManager.changeLowCompressorIntelligent(state, requestId);

        return new ResponseEntity<>(ServerResponse.buildOkJson(null),
                HttpStatus.OK);
    }

    /**
     * 空压机手动启动/停止
     * @param thingCode 空压机设备号
     * @param operation 1：运行，0：停止
     * @return
     */
    @PostMapping(value = "api/bellows/compressor/operation")
    public ResponseEntity<String> operateCompressor(String thingCode, Integer operation, HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);

        //param validate
        if (StringUtils.isEmpty(thingCode) || operation == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("RequestId: {} send a wrong request.ThingCode:{}, operation: {}.", requestId, thingCode, operation);
            }

            ServerResponse res = new ServerResponse("Wrong request.Got thingCode:" + thingCode + ",operation:" + operation + ".", SysException.EC_UNKNOWN, 0);
            String resJSON = JSON.toJSONString(res);
            return new ResponseEntity<>(resJSON, HttpStatus.BAD_REQUEST);
        }
        if (operation != Compressor.YES && operation != Compressor.NO) {
            if (logger.isDebugEnabled()) {
                logger.debug("RequestId: {} send a wrong operation: {}", requestId, operation);
            }
            ServerResponse res = new ServerResponse("Operation must be 1 or 0.Got operation :" + operation + ".", SysException.EC_UNKNOWN, 0);
            String resJSON = JSON.toJSONString(res);
            return new ResponseEntity<>(resJSON, HttpStatus.BAD_REQUEST);
        }

        logger.info("RequestId: {} operate compressor: {} to running: {}", requestId, thingCode, operation);

        EnumCompressorOperation opt;
        if (Compressor.YES == operation) {
            opt = EnumCompressorOperation.START;
        } else {
            opt = EnumCompressorOperation.STOP;
        }

        int count = compressorManager.operateCompressor(thingCode, opt, BellowsConstants.TYPE_MANUAL, requestId);

        return new ResponseEntity<>(ServerResponse.buildOkJson(count),
                HttpStatus.OK);
    }


    /**
     * 获取空压机日志
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param page  页数（从0开始）
     * @param count 每页个数
     * @return
     */
    @GetMapping(value = "api/bellows/compressor/log")
    public ResponseEntity<String> getCompressorLog(@RequestParam Date startTime, @RequestParam Date endTime, @RequestParam(required = false) Integer page,
                                                   @RequestParam(required = false) Integer count, HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        logger.info("RequestId {} query compressor log, startTime: {}, endTime: {}, page: {}, count: {}.", requestId, startTime, endTime, page, count);

        List<CompressorLog> result = compressorManager.getCompressorLog(startTime, endTime, page, count, requestId);
        return new ResponseEntity<String>(ServerResponse.buildOkJson(result), HttpStatus.OK);
    }

    /**
     * 获取空压机状态统计
     * @param startTime     开始时间
     * @param endTime   结束时间
     * @param request
     * @return
     */
    @GetMapping(value = "api/bellows/compressor/state")
    public ResponseEntity<String> analyseCompressorState(@RequestParam Date startTime, @RequestParam Date endTime, HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        logger.info("RequestId {} query compressor state analysis, startTime: {}, endTime: {}.", requestId, startTime, endTime);

        Map<String, Map<String, Long>> result = compressorManager.analyseCompressorState(startTime, endTime, requestId);
        String json = ServerResponse.buildOkJson(result);
        return new ResponseEntity<String>(json, HttpStatus.OK);
    }
}
