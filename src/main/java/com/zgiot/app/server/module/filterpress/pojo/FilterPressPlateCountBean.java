package com.zgiot.app.server.module.filterpress.pojo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FilterPressPlateCountBean {
        private String thingCode;
        /**
         * 理论压板开始时间，压滤机开始工作时所处的不同状态到松开的固定时间
         */
        private Date startTime;
        /**
         *     //时间轴(第几板，开始时间)
         */
        private Map<String,Date> timeLineMap = new HashMap<>();

        public String getThingCode() {
            return thingCode;
        }

        public Date getStartTime() {
            return startTime;
        }

        public Map<String, Date> getTimeLineMap() {
            return timeLineMap;
        }

        public void setThingCode(String thingCode) {
            this.thingCode = thingCode;
        }

        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }

        public void setTimeLineMap(Map<String, Date> timeLineMap) {
            this.timeLineMap = timeLineMap;
        }
}

