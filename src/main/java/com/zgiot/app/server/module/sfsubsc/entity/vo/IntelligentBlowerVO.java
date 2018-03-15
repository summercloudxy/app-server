package com.zgiot.app.server.module.sfsubsc.entity.vo;

import java.util.List;

/**
 * 智能鼓风
 *
 * @author jys
 */
public class IntelligentBlowerVO {


    /**
     * 卡片标题
     */
    private String cardTitle;
    /**
     * 空压机运行状态
     */
    private List<MompressorMetric> mompressorMetrics;
    /**
     * 管道压力值
     */
    private List<String> pressCurs;

    /**
     * 空压机运行状态
     */
    public class MompressorMetric {

        private String thingCode;

        private String runState;

        private String runStateName;

        public String getThingCode() {
            return thingCode;
        }

        public void setThingCode(String thingCode) {
            this.thingCode = thingCode;
        }

        public String getRunState() {
            return runState;
        }

        public void setRunState(String runState) {
            this.runState = runState;
        }

        public String getRunStateName() {
            return runStateName;
        }

        public void setRunStateName(String runStateName) {
            this.runStateName = runStateName;
        }
    }


    public List<MompressorMetric> getMompressorMetrics() {
        return mompressorMetrics;
    }

    public void setMompressorMetrics(List<MompressorMetric> mompressorMetrics) {
        this.mompressorMetrics = mompressorMetrics;
    }


    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public List<String> getPressCurs() {
        return pressCurs;
    }

    public void setPressCurs(List<String> pressCurs) {
        this.pressCurs = pressCurs;
    }
}
