package com.zgiot.app.server.module.bellows.compressor;

import com.zgiot.app.server.module.bellows.compressor.cache.CompressorCache;
import com.zgiot.app.server.module.bellows.dao.BellowsMapper;
import com.zgiot.common.constants.BellowsConstants;
import com.zgiot.common.pojo.DataModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.zgiot.app.server.module.bellows.compressor.Compressor.YES;

/**
 * @author wangwei
 */
@Component
public class CompressorManager {

    private static final Logger logger = LoggerFactory.getLogger(CompressorManager.class);

    @Autowired
    private CompressorCache compressorCache;
    @Autowired
    private BellowsMapper bellowsMapper;


    /**
     * 低压空压机智能
     */
    private volatile boolean intelligent;

    @PostConstruct
    public void init() {
        //初始化空压机缓存
        compressorCache.put("2510", new Compressor("2510", "2510", Compressor.TYPE_LOW, 0, this));
        compressorCache.put("2511", new Compressor("2511", "2511", Compressor.TYPE_LOW, 1, this));
        compressorCache.put("2512", new Compressor("2512", "2512", Compressor.TYPE_LOW, 2, this));
        compressorCache.put("2530", new Compressor("2530", "2530", Compressor.TYPE_HIGH, 0, this));
        compressorCache.put("2531", new Compressor("2531", "2531", Compressor.TYPE_HIGH, 1, this));
        compressorCache.put("2532", new Compressor("2532", "2532", Compressor.TYPE_HIGH, 2, this));

        //初始化空压机智能
        intelligent = (bellowsMapper.selectParamValue(BellowsConstants.SYS, BellowsConstants.CP_INTELLIGENT) == 1);
        logger.info("Low compressor initial intelligent state: {}", intelligent);
    }

    public void onDataSourceChange(DataModel data) {

    }

    /**
     * 设置低压空压机智能模式
     * @param intelligent
     * @param requestId
     */
    public synchronized void changeLowCompressorIntelligent(int intelligent, String requestId) {
        if (this.intelligent == (intelligent==YES)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Low compressor intelligent is already {}. RequestId: {}", this.intelligent, requestId);
            }
        }

        this.intelligent = (intelligent == YES);
        bellowsMapper.updateParamValue(BellowsConstants.SYS, BellowsConstants.CP_INTELLIGENT, (double)intelligent);
        logger.info("Low compressor intelligent is set {}.RequestId: {}", this.intelligent, requestId);
    }
}
