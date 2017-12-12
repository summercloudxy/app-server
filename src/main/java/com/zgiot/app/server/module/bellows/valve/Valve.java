package com.zgiot.app.server.module.bellows.valve;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zgiot.app.server.module.bellows.dao.BellowsMapper;
import com.zgiot.app.server.module.bellows.enumeration.EnumValveState;
import com.zgiot.app.server.module.bellows.util.BellowsUtil;
import com.zgiot.app.server.service.DataService;
import com.zgiot.common.constants.BellowsConstants;
import com.zgiot.common.constants.ValveMetricConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author wangwei
 */
public class Valve {

    private static final Logger logger = LoggerFactory.getLogger(Valve.class);


    private final String thingCode;

    /**
     * 显示名称
     */
    private final String name;

    /**
     * 排序
     */
    @JSONField(serialize = false)
    private final int sort;

    /**
     * 分类
     */
    private final String type;

    /**
     * 介质桶thingCode
     */
    @JSONField(serialize = false)
    private final String bucketThingCode;

    /**
     * 泵thingCode
     */
    @JSONField(serialize = false)
    private final String pumpThingCode;

    /**
     * 是否智能操作
     */
    private volatile boolean intelligent;

    /**
     * 分组
     */
    @JSONField(serialize = false)
    private volatile Long teamId;

    /**
     * 关到位
     */
    @JSONField(serialize = false)
    private volatile boolean closed;

    /**
     * 开到位
     */
    @JSONField(serialize = false)
    private volatile boolean open;

    /**
     * 状态
     */
    private volatile String state;

    /**
     * 智能鼓风状态
     */
    private volatile String stage;

    /**
     * 本组执行时间
     */
    @JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue)
    private volatile Long execTime;




    public Valve(String thingCode, String name, String type, int sort, String bucketThingCode, String pumpThingCode) {
        this.thingCode = thingCode;
        this.name = name;
        this.type = type;
        this.sort = sort;
        this.bucketThingCode = bucketThingCode;
        this.pumpThingCode = pumpThingCode;
    }

    /**
     * 初始化
     * @param mapper
     * @return
     */
    public Valve init(BellowsMapper mapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Valve {} start to init.", thingCode);
        }

        teamId = mapper.selectParamValue(thingCode, BellowsConstants.VALVE_TEAM);


        Long intelligent = mapper.selectParamValue(thingCode, BellowsConstants.VALVE_INTELLIGENT);
        if (intelligent != null) {
            this.intelligent = (intelligent.intValue() == BellowsConstants.YES);
        }

        stage = BellowsConstants.BLOW_STAGE_NONE;
        execTime = null;

        return this;
    }


    /**
     * 修改智能状态
     * @param intelligent
     * @param bellowsMapper
     * @param requestId
     * @return true为修改成功，false为不需要修改
     */
    public boolean setIntelligent(boolean intelligent, BellowsMapper bellowsMapper, String requestId) {
        if (intelligent == this.intelligent) {
            if (logger.isDebugEnabled()) {
                logger.debug("Valve {} intelligent is already {}. RequestId: {}.", thingCode, intelligent, requestId);
            }
            return false;
        }
        this.intelligent = intelligent;

        long value = (long)BellowsConstants.NO;
        if (intelligent) {
            value = (long)BellowsConstants.YES;
        }
        bellowsMapper.updateParamValue(thingCode, BellowsConstants.VALVE_INTELLIGENT, value);

        if (logger.isDebugEnabled()) {
            logger.debug("Valve {} intelligent is set {}. RequestId: {}.", thingCode, intelligent, requestId);
        }

        //设置teamId为null
        setTeamId(null, bellowsMapper, requestId);

        stage = BellowsConstants.BLOW_STAGE_NONE;
        execTime = null;

        return true;
    }

    /**
     * 更新
     * @param teamId
     * @param bellowsMapper
     * @param requestId
     * @return true为修改成功，false为不需要修改
     */
    public boolean setTeamId(Long teamId, BellowsMapper bellowsMapper, String requestId) {
        Long oldTeamId = this.teamId;
        //需要更新
        boolean needUpdate = true;

        if (teamId == null) {
            if (oldTeamId == null) {
                needUpdate = false;
            }
        } else {
            if (teamId.equals(oldTeamId)) {
                needUpdate = false;
            }
        }

        if (!needUpdate) {
            if (logger.isDebugEnabled()) {
                logger.debug("Valve {} team is already {}. RequestId: {}.", thingCode, teamId, requestId);
            }
            return false;
        }

        this.teamId = teamId;

        bellowsMapper.updateParamValue(thingCode, BellowsConstants.VALVE_TEAM, teamId);
        if (logger.isDebugEnabled()) {
            logger.debug("Valve {} team is set null. RequestId: {}.", thingCode, requestId);
        }

        return true;
    }

    /**
     * 刷新阀门参数
     * @param dataService
     * @return
     */
    public synchronized Valve refresh(DataService dataService) {
        if (logger.isDebugEnabled()) {
            logger.debug("Valve {} refresh.", thingCode);
        }
        setOpen(Boolean.parseBoolean(BellowsUtil.getDataModelValue(dataService, thingCode, ValveMetricConstants.STATE_OPEN).orElse(BellowsConstants.FALSE)));
        setClosed(Boolean.parseBoolean(BellowsUtil.getDataModelValue(dataService, thingCode, ValveMetricConstants.STATE_CLOSE).orElse(BellowsConstants.FALSE)));

        setState(open, closed);
        return this;
    }

    /**
     * 更新状态
     * @param open
     * @param closed
     */
    private void setState(boolean open, boolean closed) {
        if (open && !closed) {
            this.state = EnumValveState.OPEN.getState();
        } else if (!open && closed) {
            this.state = EnumValveState.CLOSE.getState();
        } else {
            this.state = EnumValveState.UNKNOWN.getState();
        }
    }


    public String getThingCode() {
        return thingCode;
    }

    public String getName() {
        return name;
    }

    public int getSort() {
        return sort;
    }

    public String getType() {
        return type;
    }

    public boolean isIntelligent() {
        return intelligent;
    }

    public Long getTeamId() {
        return teamId;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getState() {
        return state;
    }

    public String getBucketThingCode() {
        return bucketThingCode;
    }

    public String getPumpThingCode() {
        return pumpThingCode;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public Long getExecTime() {
        if (execTime == null) {
            return null;
        }
        //返回前端倒计时毫秒数
        long countDown = execTime - new Date().getTime();
        return countDown < 0 ? 0 : countDown;
    }

    public void setExecTime(Long execTime) {
        this.execTime = execTime;
    }
}
