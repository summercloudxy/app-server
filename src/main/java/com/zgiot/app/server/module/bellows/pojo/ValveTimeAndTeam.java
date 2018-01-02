package com.zgiot.app.server.module.bellows.pojo;


import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Date;

/**
 * @author wangwei
 */
public class ValveTimeAndTeam {

    @JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue)
    private Long nextBlowTime;

    private int lumpCount;

    private int slackCount;

    public ValveTimeAndTeam(Long nextBlowTime, int lumpCount, int slackCount) {
        if (nextBlowTime != null) {
            Long countDown = nextBlowTime - new Date().getTime();
            this.nextBlowTime = countDown < 0 ? 0 : countDown;
        } else {
            this.nextBlowTime = null;
        }
        this.lumpCount = lumpCount;
        this.slackCount = slackCount;
    }

    public Long getNextBlowTime() {
        return nextBlowTime;
    }

    public int getLumpCount() {
        return lumpCount;
    }

    public int getSlackCount() {
        return slackCount;
    }
}
