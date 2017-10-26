package com.zgiot.app.server.service;

import com.zgiot.common.pojo.DataModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface HistoryDataService {
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
     *
     * @param thingCodes    thingCode array
     * @param metricCode
     * @param startDate
     * @param endDate
     * @param segment
     * @return [{"tc":"2492","values":[{"dt":1508833769000,"v":"1.3"},{"dt":1508833769001,"v":"1.4"}]},{"tc":"2493","values":[{"dt":1508833769000,"v":"1.3"},{"dt":1508833769001,"v":"1.4"}]}]
     */
    List<Map<String, Object>> findMultiThingsHistoryDataOfMetric(String[] thingCodes, String metricCode, Date startDate, Date endDate, Integer segment);

    /**
     * @param list MongoData use MongoData directly to avoid another loop to convert obj.
     * @return count of success.
     */
    int insertBatch(List<DataModel> list);

}
