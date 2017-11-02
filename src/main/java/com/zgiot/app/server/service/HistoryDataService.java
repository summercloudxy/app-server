package com.zgiot.app.server.service;

import com.zgiot.app.server.service.impl.HistoryDataServiceImpl;
import com.zgiot.common.pojo.DataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface HistoryDataService {
    Logger fulldataLogger = LoggerFactory.getLogger(
            HistoryDataServiceImpl.class.getPackage().getName() + ".FullHistData");

    /**
     * @see
     */
    List<DataModel> findHistoryData(List<String> thingCodes, List<String> metricCodes, Date endDate);

    /**
     * @param thingCodes  null means no limit
     * @param metricCodes null means no limit
     * @param startDate
     * @param endDate
     * @return key is 'thingcode-metriccode' ; sort desc on timestatmp;  if no data, return map size 0 .
     * returned DataModel value is all type of 'String'.
     * @see #
     */
    List<DataModel> findHistoryDataList(List<String> thingCodes, List<String> metricCodes
            , Date startDate, Date endDate);

    /**
     * * @see #findHistoryData(List, List, Date, Date)
     *
     * @param thingCodes
     * @param metricCodes
     * @param startDate
     * @param durationMs
     * @return
     */
    List<DataModel> findHistoryData(List<String> thingCodes, List<String> metricCodes
            , Date startDate, long durationMs);


    /**
     * find history data of one metricCode of multi thingCodes in a time range by segment
     * @param thingCodes    thingCode list
     * @param metricCode
     * @param startDate
     * @param endDate
     * @param segment
     * @return {"2492":[{"dt":1508833769000,"v":"1.3"},{"dt":1508833769001,"v":"1.4"}],"2493":[{"dt":1508833769000,"v":"1.3"},{"dt":1508833769001,"v":"1.4"}]}
     */
    Map<String, List<DataModel>> findMultiThingsHistoryDataOfMetricBySegment(List<String> thingCodes, String metricCode, Date startDate, Date endDate, Integer segment);


    /**
     * find all history data of one metricCode of multi thingCodes in a time range
     * @param thingCodes    thingCode list
     * @param metricCode
     * @param startDate
     * @param endDate
     * @return {"2492":[{"dt":1508833769000,"v":"1.3"},{"dt":1508833769001,"v":"1.4"}],"2493":[{"dt":1508833769000,"v":"1.3"},{"dt":1508833769001,"v":"1.4"}]}
     */
    Map<String, List<DataModel>> findMultiThingsHistoryDataOfMetric(List<String> thingCodes, String metricCode, Date startDate, Date endDate);

    /**
     * @param list MongoData use MongoData directly to avoid another loop to convert obj.
     * @return count of success.
     */
    int insertBatch(List<DataModel> list);

    /**
     * Check whitelist and determine how to add to batch insert queue.
     * @param dm
     */
    void asyncSmartAddData(DataModel dm);

}
