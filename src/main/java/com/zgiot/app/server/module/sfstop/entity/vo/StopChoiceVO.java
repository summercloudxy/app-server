package com.zgiot.app.server.module.sfstop.entity.vo;

import java.util.List;

/**
 * 停车方案选择
 */
public class StopChoiceVO {

    private List<ChiceVO> rawStopSets;

    private List<ChiceVO> tcsStopSet;

    private List<ChiceVO> filterpressStopSet;

    private List<ChiceVO> beltRoute;


    public List<ChiceVO> getRawStopSets() {
        return rawStopSets;
    }

    public void setRawStopSets(List<ChiceVO> rawStopSets) {
        this.rawStopSets = rawStopSets;
    }

    public List<ChiceVO> getTcsStopSet() {
        return tcsStopSet;
    }

    public void setTcsStopSet(List<ChiceVO> tcsStopSet) {
        this.tcsStopSet = tcsStopSet;
    }

    public List<ChiceVO> getFilterpressStopSet() {
        return filterpressStopSet;
    }

    public void setFilterpressStopSet(List<ChiceVO> filterpressStopSet) {
        this.filterpressStopSet = filterpressStopSet;
    }

    public List<ChiceVO> getBeltRoute() {
        return beltRoute;
    }

    public void setBeltRoute(List<ChiceVO> beltRoute) {
        this.beltRoute = beltRoute;
    }

    public class ChiceVO {

        private String id;

        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
