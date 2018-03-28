package com.zgiot.app.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import com.zgiot.app.server.common.QueueManager;
import com.zgiot.app.server.config.prop.MongoDBProperties;
import com.zgiot.app.server.module.historydata.enums.AccuracyEnum;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.app.server.service.impl.mapper.TMLMapper;
import com.zgiot.app.server.service.pojo.HistdataWhitelistModel;
import com.zgiot.common.pojo.DataModel;
import com.zgiot.common.reloader.Reloader;
import com.zgiot.common.reloader.ServerReloadManager;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.BlockingQueue;

import static com.mongodb.client.model.Filters.*;

@Service
public class HistoryDataServiceImpl implements HistoryDataService, Reloader {
    private static final Logger logger = LoggerFactory.getLogger(HistoryDataServiceImpl.class);
    private static final Map<String, Map<String, Object>> WHITE_MAP = new HashMap();

    private static final String COLLECTION_NAME = "metricdata";
    private static final String MIN_COLLECTION_NAME = "metricdata_min";

    private static final String KEY_ARRAY = "array";
    private static final String KEY_SIZE = "size";
    private static final String KEY_TIMESTAMP = "timestamp";


    @Autowired
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private MongoCollection<Document> mincollection;
    @Autowired
    TMLMapper tmlMapper;

    @Autowired
    private MongoDBProperties mongoDBProperties;


    boolean checkEnabled() {
        return mongoDBProperties.getEnable();
    }

    @PostConstruct
    private void initCollection() {
        if (!checkEnabled()) {
            return;
        }
        collection = database.getCollection(COLLECTION_NAME);
        mincollection = database.getCollection(MIN_COLLECTION_NAME);
    }

    @Override
    public List<DataModel> findHistoryData(List<String> thingCodes, List<String> metricCodes, Date endDate) {
        if (!checkEnabled()) {
            return Lists.newArrayList();
        }
        return findHistoryDataList(thingCodes, metricCodes, new Date(0), endDate);
    }

    @Override
    public List<DataModel> findHistoryDataList(List<String> thingCodes, List<String> metricCodes, Date startDate, Date endDate) {
        return findHistoryDataList(thingCodes, metricCodes, startDate, endDate, AccuracyEnum.SECOND);
    }

    private void documentToDataModel(List<DataModel> result, Document document) {
        DataModel model = new DataModel();
        model.setThingCode(document.getString(DataModel.THING_CODE));
        model.setMetricCode(document.getString(DataModel.METRIC_CODE));
        model.setValue(document.getString(DataModel.VALUE));
        model.setDataTimeStamp(new Date(document.getLong(DataModel.DATA_TIMESTAMP)));
        result.add(model);
    }

    @Override
    public List<DataModel> findHistoryData(List<String> thingCodes, List<String> metricCodes, Date startDate, long durationMs) {
        if (!checkEnabled()) {
            return Lists.newArrayList();
        }

        Date endDate = new Date(startDate.getTime() + durationMs);
        return findHistoryDataList(thingCodes, metricCodes, startDate, endDate);
    }

    /**
     * created by wangwei
     */
    @Override
    public Map<String, List<DataModel>> findMultiThingsHistoryDataOfMetricBySegment(List<String> thingCodes, String metricCode, Date startDate, Date endDate, Integer segment, boolean isTimeCorrection) {
        return findMultiThingsHistoryDataOfMetricBySegment(thingCodes, metricCode, startDate, endDate, segment, isTimeCorrection, AccuracyEnum.SECOND);
    }


    /**
     * create by wangwei
     */
    @Override
    public Map<String, List<DataModel>> findMultiThingsHistoryDataOfMetric(List<String> thingCodes, String metricCode, Date startDate, Date endDate) {
        if (!checkEnabled()) {
            return new HashMap<>();
        }

        if (collection == null) {
            logger.warn("mongo disabled");
            return new HashMap<>();
        }

        long startTime = startDate.getTime();
        long endTime = endDate.getTime();

        //param validator
        if (startTime >= endTime) {
            throw new IllegalArgumentException("StartDate must be earlier than endDate");
        }

        //query
        Bson criteria = and(gte(DataModel.DATA_TIMESTAMP, startTime), lt(DataModel.DATA_TIMESTAMP, endTime), eq(DataModel.METRIC_CODE, metricCode), in(DataModel.THING_CODE, thingCodes));
        FindIterable<Document> iterable = collection.find(criteria).sort(Sorts.ascending(DataModel.DATA_TIMESTAMP));

        //generate result map
        Map<String, List<DataModel>> result = new LinkedHashMap<>(thingCodes.size());
        for (String tc : thingCodes) {
            result.put(tc, new ArrayList<>());
        }
        for (Document document : iterable) {
            String tc = document.getString(DataModel.THING_CODE);
            DataModel model = new DataModel();
            model.setValue(document.getString(DataModel.VALUE));
            model.setDataTimeStamp(new Date(document.getLong(DataModel.DATA_TIMESTAMP)));
            result.get(tc).add(model);
        }

        return result;
    }

    /**
     * created by wangwei
     * check document, save dataModel to tempMap
     *
     * @param document document from mongoDB
     * @param tempMap  store dataModel array, timestamp and unset size
     * @param interval interval by segment
     */
    private void checkDocument(Document document, Map<String, Object> tempMap, long interval, boolean isTimeCorrection) {
        while (true) {
            int size = (int) tempMap.get(KEY_SIZE);
            if (size == 0) {
                return;
            }

            //check timestamp
            long timestamp = (long) tempMap.get(KEY_TIMESTAMP);
            long dt = document.getLong(DataModel.DATA_TIMESTAMP);
            if (dt > timestamp) {
                return;
            }

            //store dataModel
            size--;
            DataModel[] dataModels = (DataModel[]) tempMap.get(KEY_ARRAY);
            DataModel model = new DataModel();
            model.setValue(document.getString(DataModel.VALUE));
            model.setDataTimeStamp(new Date(dt));
            if (isTimeCorrection) {
                model.setDataTimeStamp(new Date(timestamp));
            }
            dataModels[size] = model;

            timestamp -= interval;
            tempMap.put(KEY_TIMESTAMP, timestamp);
            tempMap.put(KEY_SIZE, size);
        }
    }


    /**
     * created by wangwei
     * query from mongoDB again for unset dataModel
     *
     * @param thingCode
     * @param metricCode
     * @param endTime
     * @param dataModels
     * @param size
     */
    private void queryForUnsetDataModel(String thingCode, String metricCode, long endTime, DataModel[] dataModels, int size, long interval, boolean isTimeCorrection, AccuracyEnum accuracy) {
        MongoCollection<Document> mongoCollection = null;
        if (AccuracyEnum.SECOND.equals(accuracy)) {
            mongoCollection = collection;
        } else if (AccuracyEnum.MINUTE.equals(accuracy)) {
            mongoCollection = mincollection;
        }
        Bson criteria = and(lte(DataModel.DATA_TIMESTAMP, endTime), eq(DataModel.METRIC_CODE, metricCode), eq(DataModel.THING_CODE, thingCode));
        FindIterable<Document> iterable = mongoCollection.find(criteria).sort(Sorts.descending(DataModel.DATA_TIMESTAMP)).limit(1);
        for (Document document : iterable) {
            DataModel model = new DataModel();
            model.setValue(document.getString(DataModel.VALUE));
            model.setDataTimeStamp(new Date(document.getLong(DataModel.DATA_TIMESTAMP)));
            for (int i = 0; i < size; i++) {
                DataModel clone = model.clone();
                if (isTimeCorrection) {
                    clone.setDataTimeStamp(new Date(endTime + i * interval));
                }
                dataModels[i] = clone;
            }
        }
    }


    @Override
    public DataModel findClosestHistoryDataInDuration(List<String> thingCodes, List<String> metricCodes, Date queryTime, String queryType) {
        if (!checkEnabled()) {
            return null;
        }
        Bson criteria;
        Bson sortBson;
        // for end date
        if (queryTime == null) {
            throw new IllegalArgumentException("query time required.");
        }
        if (QUERY_TIME_TYPE_BEFORE.equals(queryType)) {
            criteria = and(lte(DataModel.DATA_TIMESTAMP, queryTime.getTime()));
            sortBson = Sorts.descending(DataModel.DATA_TIMESTAMP);
        } else {
            criteria = and(gte(DataModel.DATA_TIMESTAMP, queryTime.getTime()));
            sortBson = Sorts.ascending(DataModel.DATA_TIMESTAMP);
        }
        // for tc
        if (thingCodes != null && thingCodes.size() > 0) {
            criteria = and(criteria, in(DataModel.THING_CODE, thingCodes));
        }
        // for mc
        if (metricCodes != null && metricCodes.size() > 0) {
            criteria = and(criteria, in(DataModel.METRIC_CODE, metricCodes));
        }
        List<DataModel> result = new LinkedList<>();
        if (collection != null) {
            collection.find(criteria)
                    .sort(sortBson)
                    .limit(1).forEach((Block<Document>) document -> {
                documentToDataModel(result, document);
            });
        } else {
            logger.warn("mongo disabled");
        }
        if (result.size() != 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public DataModel findClosestHistoryData(List<String> thingCodes, List<String> metricCodes, Date queryTime) {
        DataModel closestHistoryDataBeforeTime = findClosestHistoryDataInDuration(thingCodes, metricCodes, queryTime, QUERY_TIME_TYPE_BEFORE);
        DataModel closestHistoryDataAfterTime = findClosestHistoryDataInDuration(thingCodes, metricCodes, queryTime, QUERY_TIME_TYPE_AFTER);
        if (closestHistoryDataAfterTime != null && closestHistoryDataBeforeTime != null) {
            if ((closestHistoryDataAfterTime.getDataTimeStamp().getTime() - queryTime.getTime()) > (queryTime.getTime() - closestHistoryDataBeforeTime.getDataTimeStamp().getTime())) {
                return closestHistoryDataBeforeTime;
            } else {
                return closestHistoryDataAfterTime;
            }
        } else if (closestHistoryDataAfterTime != null) {
            return closestHistoryDataAfterTime;
        } else if (closestHistoryDataBeforeTime != null) {
            return closestHistoryDataBeforeTime;
        } else {
            return null;
        }
    }

    @Override
    public DataModel findMaxValueDataInDuration(String thingCode, String metricCode, Date startTime, Date endTime) {
        List<DataModel> result = getDataListInDuration(thingCode, metricCode, startTime, endTime, AccuracyEnum.SECOND);
        if (!result.isEmpty()) {
            Optional<DataModel> max = result.stream().max((o1, o2) -> {
                String valueStr1 = o1.getValue();
                Double value1 = Double.valueOf(valueStr1);
                String valueStr2 = o2.getValue();
                Double value2 = Double.valueOf(valueStr2);
                return value1.compareTo(value2);
            });
            if (max.isPresent()) {
                return max.get();
            }
        }
        return null;
    }

    @Override
    public Double findAvgValueDataInDuration(String thingCode, String metricCode, Date startTime, Date endTime) {
        List<DataModel> resultList = getDataListInDuration(thingCode, metricCode, startTime, endTime, AccuracyEnum.SECOND);
        if (CollectionUtils.isEmpty(resultList)) {
            return null;
        }
        return getAvgValue(resultList);
    }


    private double getAvgValue(List<DataModel> resultList) {
        int count = 0;
        double sumValue = 0.0;
        double avgValue = 0.0;
        for (DataModel dataModel : resultList) {
            sumValue += Double.parseDouble(dataModel.getValue());
            count++;
        }
        if (count != 0) {
            avgValue = sumValue / count;
        }
        return avgValue;
    }

    private List<DataModel> getDataListInDuration(String thingCode, String metricCode, Date startTime, Date endTime, AccuracyEnum accuracy) {

        MongoCollection<Document> mongoCollection = null;
        if (AccuracyEnum.SECOND.equals(accuracy)) {
            mongoCollection = collection;
        } else if (AccuracyEnum.MINUTE.equals(accuracy)) {
            mongoCollection = mincollection;
        }
        if (!checkEnabled()) {
            return Collections.emptyList();
        }
        Bson criteria;
        // for end date
        if (startTime == null) {
            throw new IllegalArgumentException("start time required.");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("end time required.");
        }
        if (startTime.after(endTime)) {
            throw new IllegalArgumentException("StartDate must be earlier than endDate");
        }
        criteria = and(gte(DataModel.DATA_TIMESTAMP, startTime.getTime()), lt(DataModel.DATA_TIMESTAMP, endTime.getTime()), eq(DataModel.METRIC_CODE, metricCode), eq(DataModel.THING_CODE, thingCode));
        List<DataModel> result = new LinkedList<>();
        if (mongoCollection != null) {
            mongoCollection.find(criteria)
                    .forEach((Block<Document>) document -> {
                        documentToDataModel(result, document);
                    });
        } else {
            logger.warn("mongo disabled");
        }
        return result;
    }

    @Override
    public int insertBatch(List<DataModel> modelList) {
        if (!checkEnabled()) {
            return 0;
        }
        return saveDatasByCollection(modelList, collection);
    }

    @Override
    public void asyncSmartAddData(DataModel dm) {

        if (fulldataLogger.isDebugEnabled()) {
            fulldataLogger.debug(JSON.toJSONString(dm));
        }

        if (!checkEnabled()) {
            return;
        }

        synchronized (inited) {
            if (!inited) {
                logger.info("Start to init HistoryDataListener ... ");
                init();
            }
        }

        boolean toStore = false;
        if (WHITE_MAP.containsKey(dm.getThingCode())) {
            Map metricMap = WHITE_MAP.get(dm.getThingCode());

            if (metricMap != null && metricMap.containsKey(dm.getMetricCode())) {
                toStore = true;
            }
        }

        if (toStore) {
            BlockingQueue q = (BlockingQueue) QueueManager.getQueue(QueueManager.HIST_BUFFER);
            q.add(dm);
            if (logger.isDebugEnabled()) {
                logger.debug("Added to hist data queue (data=`{}`).", dm.toString());
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Not added to hist data queue (data=`{}`).", dm.toString());
            }
        }

    }

    void init() {
        synchronized (inited) {
            if (inited) {
                return;
            }

            ServerReloadManager.addReloader(this);
            reload();
        }

    }

    private static Boolean inited = false;

    @Override
    public void reload() {
        synchronized (inited) {
            inited = false;

            WHITE_MAP.clear();
            List<HistdataWhitelistModel> list = tmlMapper.findAllHistdataWhitelist();
            for (HistdataWhitelistModel tm : list) {
                if (tm.getToStore() != 1) {
                    continue;
                }

                Map metricMap = WHITE_MAP.get(tm.getThingCode());
                if (metricMap == null) {
                    metricMap = new HashMap();
                    WHITE_MAP.put(tm.getThingCode(), metricMap);
                }
                metricMap.put(tm.getMetricCode(), null);
            }

            inited = true;
        }
    }

    @Override
    public Map<String, List<DataModel>> findMultiThingsHistoryDataOfMetricBySegment(List<String> thingCodes, String metricCode, Date startDate, Date endDate, Integer segment, boolean isTimeCorrection, AccuracyEnum accuracy) {

        MongoCollection<Document> mongoCollection = null;
        if (AccuracyEnum.SECOND.equals(accuracy)) {
            mongoCollection = collection;
        } else if (AccuracyEnum.MINUTE.equals(accuracy)) {
            mongoCollection = mincollection;
        }
        if (!checkEnabled()) {
            return new HashMap<>();
        }
        if (mongoCollection == null) {
            logger.warn("mongo disabled");
            return new HashMap<>();
        }

        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        //param validator
        if (segment <= 0) {
            throw new IllegalArgumentException("Segment must be greater than 0");
        }
        if (startTime >= endTime) {
            throw new IllegalArgumentException("StartDate must be earlier than endDate");
        }

        long interval;
        FindIterable<Document> iterable = null; //query result

        if (segment == 1) {
            //when segment eq 1, take startTime as the standard
            interval = 0;
        } else {
            interval = (endTime - startTime) / (segment - 1);
            //query
            Bson criteria = and(gte(DataModel.DATA_TIMESTAMP, startTime), lt(DataModel.DATA_TIMESTAMP, endTime), eq(DataModel.METRIC_CODE, metricCode), in(DataModel.THING_CODE, thingCodes));
            iterable = mongoCollection.find(criteria).sort(Sorts.descending(DataModel.DATA_TIMESTAMP));
        }


        Map<String, Map<String, Object>> map = new HashMap<>(thingCodes.size());    //store dataModel array, timestamp and unset size
        if (iterable != null) {
            for (Document document : iterable) {
                String tc = document.getString(DataModel.THING_CODE);
                Map<String, Object> temp = map.get(tc);
                if (temp == null) {
                    //init temp map
                    temp = new HashMap<>(3);
                    temp.put(KEY_ARRAY, new DataModel[segment]);  //empty dataModel array
                    temp.put(KEY_TIMESTAMP, endTime); //timestamp for check
                    temp.put(KEY_SIZE, segment);  //unset size
                    map.put(tc, temp);
                }

                checkDocument(document, temp, interval, isTimeCorrection);
            }
        }


        //generate result map
        Map<String, List<DataModel>> result = new LinkedHashMap<>(thingCodes.size());
        for (String thingCode : thingCodes) {
            Map<String, Object> temp = map.get(thingCode);
            DataModel[] dataModels;
            if (temp == null) {
                dataModels = new DataModel[segment];
                queryForUnsetDataModel(thingCode, metricCode, startTime, dataModels, segment, interval, isTimeCorrection, accuracy);
            } else {
                dataModels = (DataModel[]) temp.get(KEY_ARRAY);
                int size = (int) temp.get(KEY_SIZE);
                if (size > 0) {
                    queryForUnsetDataModel(thingCode, metricCode, startTime, dataModels, size, interval, isTimeCorrection, accuracy);
                }
            }

            List<DataModel> dataModelList = Arrays.asList(dataModels);
            result.put(thingCode, dataModelList);
        }

        return result;
    }

    @Override
    public int insertMinDataBatch(List<DataModel> list) {
        if (!checkEnabled()) {
            return 0;
        }

        return saveDatasByCollection(list, mincollection);
    }


    private int saveDatasByCollection(List<DataModel> list, MongoCollection<Document> collection) {
        if (collection != null) {
            List<Document> models = new LinkedList<>();
            for (DataModel dataModel : list) {
                Document document = new Document()
                        .append(DataModel.THING_CODE, dataModel.getThingCode())
                        .append(DataModel.METRIC_CODE, dataModel.getMetricCode())
                        .append(DataModel.VALUE, dataModel.getValue())
                        .append(DataModel.DATA_TIMESTAMP, dataModel.getDataTimeStamp().getTime());
                models.add(document);
            }
            collection.insertMany(models);
            return models.size();
        } else {
            logger.warn("mongo disabled");
            return 0;
        }
    }


    @Override
    public Long getDataBatchCount(Date startTime, Date endTime, String thingCode, String metricCode, AccuracyEnum accuracy) {
        MongoCollection<Document> mongoCollection = null;
        if (AccuracyEnum.SECOND.equals(accuracy)) {
            mongoCollection = collection;
        } else if (AccuracyEnum.MINUTE.equals(accuracy)) {
            mongoCollection = mincollection;
        }

        Bson criteria = and(gte(DataModel.DATA_TIMESTAMP, startTime.getTime()), lt(DataModel.DATA_TIMESTAMP, endTime.getTime()), eq(DataModel.METRIC_CODE, metricCode), in(DataModel.THING_CODE, thingCode));
        return mongoCollection.count(criteria);
    }

    @Override
    public List<DataModel> findHistoryDataList(List<String> thingCodes, List<String> metricCodes, Date startDate, Date endDate, AccuracyEnum accuracy) {

        MongoCollection<Document> mongoCollection = null;
        if (AccuracyEnum.SECOND.equals(accuracy)) {
            mongoCollection = collection;
        } else if (AccuracyEnum.MINUTE.equals(accuracy)) {
            mongoCollection = mincollection;
        }
        if (!checkEnabled()) {
            return Lists.newArrayList();
        }

        Bson criteria;
        // for end date
        if (endDate == null) {
            throw new IllegalArgumentException("end date required.");
        }
        // for start date
        long startDateL = 0;
        if (startDate != null) {
            startDateL = startDate.getTime();
        }
        criteria = and(gte(DataModel.DATA_TIMESTAMP, startDateL), lte(DataModel.DATA_TIMESTAMP, endDate.getTime()));
        // for tc
        if (thingCodes != null && thingCodes.size() > 0) {
            criteria = and(criteria, in(DataModel.THING_CODE, thingCodes));
        }
        // for mc
        if (metricCodes != null && metricCodes.size() > 0) {
            criteria = and(criteria, in(DataModel.METRIC_CODE, metricCodes));
        }

        List<DataModel> result = new LinkedList<>();
        if (mongoCollection != null) {
            mongoCollection.find(criteria)
                    .sort(Sorts.descending(DataModel.DATA_TIMESTAMP))
                    .forEach((Block<Document>) document -> {
                        documentToDataModel(result, document);
                    });
        } else {
            logger.warn("mongo disabled");
        }
        return result;
    }

    @Override
    public Double findAvgValueDataInDuration(String thingCode, String metricCode, Date startDate, Date endDate, AccuracyEnum accuracyEnum) {
        List<DataModel> resultList = getDataListInDuration(thingCode, metricCode, startDate, endDate, accuracyEnum);
        if (CollectionUtils.isEmpty(resultList)) {
            return null;
        }
        return getAvgValue(resultList);
    }

}
