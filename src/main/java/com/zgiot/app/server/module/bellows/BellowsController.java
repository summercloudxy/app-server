package com.zgiot.app.server.module.bellows;

import com.alibaba.fastjson.JSON;
import com.zgiot.app.server.module.bellows.compressor.CompressorGroup;
import com.zgiot.app.server.module.bellows.compressor.CompressorManager;
import com.zgiot.app.server.module.bellows.enumeration.EnumValveOperation;
import com.zgiot.app.server.module.bellows.pojo.CompressorLog;
import com.zgiot.app.server.module.bellows.enumeration.EnumCompressorOperation;
import com.zgiot.app.server.module.bellows.pojo.ValveLog;
import com.zgiot.app.server.module.bellows.pojo.ValveParam;
import com.zgiot.app.server.module.bellows.pojo.ValveTimeAndTeam;
import com.zgiot.app.server.module.bellows.valve.Valve;
import com.zgiot.app.server.module.bellows.valve.ValveManager;
import com.zgiot.common.constants.BellowsConstants;
import com.zgiot.common.constants.GlobalConstants;
import com.zgiot.common.exceptions.SysException;
import com.zgiot.common.restcontroller.ServerResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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

    @Autowired
    private ValveManager valveManager;


    private static final String STATE = "state";
    private static final String OPERATION = "operation";

    /**
     * 获取空压机压力
     * @param request
     * @return
     */
    @GetMapping(value = "api/bellows/pressure")
    public ResponseEntity<String> getPressure(HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);

        logger.info("RequestId: {} query compressor pressure.", requestId);
        Map<String, Double> res = compressorManager.getPressure(requestId);

        return new ResponseEntity<String>(ServerResponse.buildOkJson(res), HttpStatus.OK);
    }


    /**
     * 获取空压机组详情
     * @param type
     * @param request
     * @return
     */
    @GetMapping(value = "api/bellows/compressor/{type}")
    public ResponseEntity<String> getCompressors(@PathVariable String type, HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);

        if (!BellowsConstants.CP_TYPE_HIGH.equals(type) && !BellowsConstants.CP_TYPE_LOW.equals(type)) {
            if (logger.isDebugEnabled()) {
                logger.debug("RequestId: {} send a wrong type {}.", requestId, type);
            }
            String resJSON = JSON.toJSONString(new ServerResponse("Type must be high or low.Got type: " + type, SysException.EC_UNKNOWN, 0));
            return new ResponseEntity<>(resJSON, HttpStatus.BAD_REQUEST);
        }

        logger.info("RequestId: {} query detail of compressor {}.", requestId, type);

        CompressorGroup group = compressorManager.refreshGroup(type, requestId);
        return new ResponseEntity<String>(ServerResponse.buildOkJson(group), HttpStatus.OK);
    }


    /**
     * 设置低压空压机智能模式
     * @param type  low/high
     * @param intelligent true智能 false手动
     * @param request
     * @return
     */
    @PostMapping(value = "api/bellows/compressor/{type}/intelligent")
    public ResponseEntity<String> setLowCompressorIntelligent(@PathVariable("type") String type, Boolean intelligent, HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);

        //param validate
        if (!BellowsConstants.CP_TYPE_LOW.equals(type)) {
            if (logger.isDebugEnabled()) {
                logger.debug("RequestId: {} send a wrong type {}.", requestId, type);
            }

            String resJSON = JSON.toJSONString(new ServerResponse("Type must be low.Got type: " + type, SysException.EC_UNKNOWN, 0));
            return new ResponseEntity<>(resJSON, HttpStatus.BAD_REQUEST);
        }


        if (intelligent == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("RequestId: {} send a blank intelligent.", requestId);
            }
            String resJSON = JSON.toJSONString(new ServerResponse("Intelligent cannot be empty.", SysException.EC_UNKNOWN, 0));
            return new ResponseEntity<>(resJSON, HttpStatus.BAD_REQUEST);
        }

        logger.info("RequestId: {} set {} press compressor intelligent: {}", requestId, type, intelligent);
        compressorManager.changeGroupIntelligent(type, intelligent, requestId);

        return new ResponseEntity<>(ServerResponse.buildOkJson(null),
                HttpStatus.OK);
    }

    /**
     * 空压机手动启动/停止
     * @param thingCode 空压机设备号
     * @param operation true运行 false停止
     * @return
     */
    @PostMapping(value = "api/bellows/compressor/{thingCode}/open")
    public ResponseEntity<String> operateCompressor(@PathVariable("thingCode")String thingCode, Boolean operation, HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);

        //param validate
        String resJSON = checkOperation(operation, requestId);
        if (resJSON != null) {
            return new ResponseEntity<>(resJSON, HttpStatus.BAD_REQUEST);
        }

        logger.info("RequestId: {} operate compressor: {} to running: {}", requestId, thingCode, operation);

        EnumCompressorOperation opt;
        if (operation) {
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


    /**
     * 查询阀门列表
     * @param request
     * @return
     */
    @GetMapping(value = "api/bellows/valve")
    public ResponseEntity<String> getValveList(HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);

        logger.info("RequestId: {} query valve list.", requestId);
        List<Valve> list = valveManager.refreshValves();

        return new ResponseEntity<>(ServerResponse.buildOkJson(list),
                HttpStatus.OK);
    }


    /**
     * 查询阀门下次鼓风时间和分组数量
     * @param request
     * @return
     */
    @GetMapping(value = "api/bellows/valve/time")
    public ResponseEntity<String> getValveTeamCountAndBlowTime(HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);

        logger.info("RequestId: {} query valve nextBlowTime and teamCount.", requestId);
        ValveTimeAndTeam valveTimeAndTeam = new ValveTimeAndTeam(valveManager.getNextBlowTime(), valveManager.getLumpTeamCount(), valveManager.getSlackTeamCount());
        return new ResponseEntity<String>(ServerResponse.buildOkJson(valveTimeAndTeam),
                HttpStatus.OK);
    }


    /**
     * 设置阀门智能模式
     * @param requestParam    智能阀门thingCode列表
     * @param request
     * @return
     */
    @PostMapping(value = "api/bellows/valve/intelligent")
    public ResponseEntity<String> setValveIntelligent(@RequestBody  String requestParam, HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);

        List<String> thingCodes;
        if (StringUtils.isEmpty(requestId)) {
            thingCodes = new ArrayList<>();
        } else {
            thingCodes = JSON.parseArray(requestParam, String.class);
        }

        logger.info("RequestId: {} set valve {} intelligent.", requestId, thingCodes);
        valveManager.setValveIntelligentBatch(thingCodes, requestId);

        return new ResponseEntity<>(ServerResponse.buildOkJson(null),
                HttpStatus.OK);
    }

    /**
     * 请求阀门智能参数
     * @param request
     * @return
     */
    @GetMapping(value = "api/bellows/valve/param")
    public ResponseEntity<String> getValveParam(HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);

        logger.info("RequestId: {} query valve param.", requestId);

        ValveParam valveParam = new ValveParam(valveManager.getMaxCount(), valveManager.getRunTime(), valveManager.getWaitTime());

        return new ResponseEntity<>(ServerResponse.buildOkJson(valveParam),
                HttpStatus.OK);
    }


    /**
     * 设置阀门智能参数
     * @param requestData
     * @param request
     * @return
     */
    @PostMapping(value = "api/bellows/valve/param")
    public ResponseEntity<String> setValveParam(@RequestBody String requestData, HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);

        if (StringUtils.isEmpty(requestData)) {
            if (logger.isDebugEnabled()) {
                logger.debug("RequestId: {} send a bland request", requestId);
            }
            ServerResponse res = new ServerResponse("Blank request", SysException.EC_UNKNOWN, 0);
            return new ResponseEntity<>(JSON.toJSONString(res), HttpStatus.BAD_REQUEST);
        }
        ValveParam valveParam = JSON.parseObject(requestData, ValveParam.class);
        if (valveParam.getMaxCount() == null || valveParam.getRunTime() == null || valveParam.getWaitTime() == null) {
            ServerResponse res = new ServerResponse("Invalid request data.The incoming req body is: `" + requestData + "`", SysException.EC_UNKNOWN, 0);
            return new ResponseEntity<String>(JSON.toJSONString(res), HttpStatus.BAD_REQUEST);
        }

        if(valveParam.getMaxCount() < 0) {
            ServerResponse res = new ServerResponse("Valve max count must be greater than 0.", SysException.EC_UNKNOWN, 0);
            String resJSON = JSON.toJSONString(res);
            return new ResponseEntity<>(resJSON, HttpStatus.BAD_REQUEST);
        }

        if(valveParam.getRunTime() < 0) {
            ServerResponse res = new ServerResponse("Valve run time must be greater than 0.", SysException.EC_UNKNOWN, 0);
            String resJSON = JSON.toJSONString(res);
            return new ResponseEntity<>(resJSON, HttpStatus.BAD_REQUEST);
        }

        if(valveParam.getWaitTime() < 0) {
            ServerResponse res = new ServerResponse("Valve wait time must be greater than 0.", SysException.EC_UNKNOWN, 0);
            String resJSON = JSON.toJSONString(res);
            return new ResponseEntity<>(resJSON, HttpStatus.BAD_REQUEST);
        }

        logger.info("RequestId: {} set valve maxCount {}, runTime {}, waitTime {}.", requestId, valveParam.getMaxCount(), valveParam.getRunTime(), valveParam.getWaitTime());

        valveManager.setValveParam(valveParam.getMaxCount(), valveParam.getRunTime(), valveParam.getWaitTime(), requestId);

        return new ResponseEntity<>(ServerResponse.buildOkJson(null),
                HttpStatus.OK);
    }


    /**
     * 阀门手动开/关
     * @param thingCode 阀门设备号
     * @param operation true开 false关
     * @return
     */
    @PostMapping(value = "api/bellows/valve/{thingCode}/open")
    public ResponseEntity<String> operationValve(@PathVariable("thingCode")String thingCode, Boolean operation, HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);

        //param validate
        String resJSON = checkOperation(operation, requestId);
        if (resJSON != null) {
            return new ResponseEntity<>(resJSON, HttpStatus.BAD_REQUEST);
        }

        logger.info("RequestId: {} operate valve: {} to running: {}", requestId, thingCode, operation);

        EnumValveOperation opt;
        if (operation) {
            opt = EnumValveOperation.OPEN;
        } else {
            opt = EnumValveOperation.CLOSE;
        }

        int count = valveManager.operateValve(thingCode, opt, BellowsConstants.TYPE_MANUAL, requestId);

        return new ResponseEntity<>(ServerResponse.buildOkJson(count),
                HttpStatus.OK);
    }


    /**
     * 阀门手动批量开/关
     * @param operation true开 false关
     * @return
     */
    @PostMapping(value = "api/bellows/valve/open/all")
    public ResponseEntity<String> operationAllValve(Boolean operation, HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);

        //param validate
        String resJSON = checkOperation(operation, requestId);
        if (resJSON != null) {
            return new ResponseEntity<>(resJSON, HttpStatus.BAD_REQUEST);
        }

        logger.info("RequestId: {} operate valve: {} to running: {}", requestId, operation);

        EnumValveOperation opt;
        if (operation) {
            opt = EnumValveOperation.OPEN;
        } else {
            opt = EnumValveOperation.CLOSE;
        }

        int count = valveManager.operateValveAll(opt, BellowsConstants.TYPE_MANUAL, requestId);

        return new ResponseEntity<>(ServerResponse.buildOkJson(count),
                HttpStatus.OK);
    }


    /**
     * 获取阀门日志
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param page  页数（从0开始）
     * @param count 每页个数
     * @return
     */
    @GetMapping(value = "api/bellows/valve/log")
    public ResponseEntity<String> getValveLog(@RequestParam Date startTime, @RequestParam Date endTime, @RequestParam(required = false) Integer page,
                                                   @RequestParam(required = false) Integer count, HttpServletRequest request) {
        String requestId = request.getHeader(GlobalConstants.REQUEST_ID_HEADER_KEY);
        logger.info("RequestId {} query valve log, startTime: {}, endTime: {}, page: {}, count: {}.", requestId, startTime, endTime, page, count);

        List<ValveLog> result = valveManager.getValveLog(startTime, endTime, page, count, requestId);
        return new ResponseEntity<String>(ServerResponse.buildOkJson(result), HttpStatus.OK);
    }


    /**
     * 判断operation为空
     * @param operation
     * @param requestId
     * @return 返回错误信息，Null为正确
     */
    private String checkOperation(Boolean operation, String requestId) {
        if (operation == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("RequestId: {} send a blank operation.", requestId);
            }
            String resJSON = JSON.toJSONString(new ServerResponse("Operation cannot be empty.", SysException.EC_UNKNOWN, 0));
            return resJSON;
        }
        return null;
    }
}
