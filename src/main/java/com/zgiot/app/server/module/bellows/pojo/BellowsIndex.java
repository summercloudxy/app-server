package com.zgiot.app.server.module.bellows.pojo;

import com.zgiot.app.server.module.bellows.compressor.CompressorGroup;

/**
 * @author wangwei
 */
public class BellowsIndex {

    private CompressorGroup low;

    private CompressorGroup high;

    private Boolean intelligent;

    public CompressorGroup getLow() {
        return low;
    }

    public void setLow(CompressorGroup low) {
        this.low = low;
    }

    public CompressorGroup getHigh() {
        return high;
    }

    public void setHigh(CompressorGroup high) {
        this.high = high;
    }

    public Boolean getIntelligent() {
        return intelligent;
    }

    public void setIntelligent(Boolean intelligent) {
        this.intelligent = intelligent;
    }
}
