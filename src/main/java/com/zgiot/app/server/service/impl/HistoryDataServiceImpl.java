package com.zgiot.app.server.service.impl;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import com.zgiot.app.server.service.HistoryDataService;
import com.zgiot.common.pojo.DataModel;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

import static com.mongodb.client.model.Filters.*;

@Service
public class HistoryDataServiceImpl implements HistoryDataService {
    private static final Logger logger = LoggerFactory.getLogger(HistoryDataServiceImpl.class);
    private static final String COLLECTION_NAME = "metricdata";
    @Autowired
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @PostConstruct
    private void initCollection() {
        collection = database.getCollection(COLLECTION_NAME);
    }

    @Override
    public List<DataModel> findHistoryData(List<String> thingCodes, List<String> metricCodes, Date endDate) {
        return findHistoryDataList(thingCodes, metricCodes, new Date(0), endDate);
    }

    @Override
    public List<DataModel> findHistoryDataList(List<String> thingCodes, List<String> metricCodes, Date startDate, Date endDate) {
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
        criteria = and(gte("dt", startDateL), lte("dt", endDate.getTime()));
        // for tc
        if (thingCodes != null && thingCodes.size() > 0) {
            criteria = and(criteria, in("tc", thingCodes));
        }
        // for mc
        if (metricCodes != null && metricCodes.size() > 0) {
            criteria = and(criteria, in("mc", metricCodes));
        }

        List<DataModel> result = new LinkedList<>();
        if (collection != null) {
            collection.find(criteria)
                    .sort(Sorts.descending("dt"))
                    .forEach((Block<Document>) document -> {
                        DataModel model = new DataModel();
                        model.setThingCode(document.getString("tc"));
                        model.setMetricDataType(document.getString("mdt"));
                        model.setMetricCode(document.getString("mc"));
                        model.setMetricCategoryCode(document.getString("mcc"));
                        model.setValue(document.getString("v"));
                        model.setDataTimeStamp(new Date(document.getLong("dt")));
                        result.add(model);
                    });
        } else {
            logger.warn("mongo disabled");
        }
        return result;
    }

    @Override
    public List<DataModel> findHistoryData(List<String> thingCodes, List<String> metricCodes, Date startDate, long durationMs) {
        Date endDate = new Date(startDate.getTime() + durationMs);
        return findHistoryDataList(thingCodes, metricCodes, startDate, endDate);
    }

    /**
     * created by wangwei
     */
    @Override
    public List<Map<String, Object>> findHistoryDataMap(String[] thingCodes, String metricCode, Date startDate, Date endDate, Integer segment) {
        if (collection == null) {
            logger.warn("mongo disabled");
            return new ArrayList<>();
        }

        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long interval = (endTime - startTime)/segment;

        //param validator
        if (segment <= 0) {
            throw new IllegalArgumentException("Segment must be greater than 0");
        }
        if (startTime >= endTime) {
            throw new IllegalArgumentException("StartDate must be earlier than endDate");
        }

        //query
        Bson criteria = and(gte("dt", startTime), lte("dt", endTime), eq("mc", metricCode), in("tc", thingCodes));
        FindIterable<Document> iterable = collection.find(criteria).sort(Sorts.descending("dt"));

        Map<String, Map<String, Object>> map = new HashMap<>(thingCodes.length);    //store dataModel array, timestamp and unset size
        for (Document document : iterable) {
            String tc = document.getString("tc");
            Map<String, Object> temp = map.get("tc");
            if (temp == null) {
                //init temp map
                temp = new HashMap<>(3);
                temp.put("array", new DataModel[segment]);  //empty dataModel array
                temp.put("timestamp", endTime); //timestamp for check
                temp.put("size", segment);  //unset size
                map.put(tc, temp);
            }

            checkDocument(document, temp, interval);
        }

        //generate result list
        List<Map<String, Object>> result = new ArrayList<>(thingCodes.length);  //result
        for (String thingCode : thingCodes) {
            Map<String, Object> temp = map.get(thingCode);
            DataModel[] dataModels;
            if (temp == null) {
                dataModels = new DataModel[segment];
                queryForUnsetDataModel(thingCode, metricCode, startTime, dataModels, segment);
            } else {
                dataModels = (DataModel[]) temp.get("array");
                int size = (int)temp.get("size");
                if (size > 0) {
                    queryForUnsetDataModel(thingCode, metricCode, startTime, dataModels, size);
                }
            }

            Map<String, Object> resultMap = new HashMap<>(2);
            resultMap.put("tc", thingCode);
            resultMap.put("values", dataModels);

            result.add(resultMap);
        }

        return result;
    }

    /**
     * created by wangwei
     * check document, save dataModel to tempMap
     * @param document document from mongoDB
     * @param tempMap  store dataModel array, timestamp and unset size
     * @param interval interval by segment
     */
    private void checkDocument(Document document, Map<String, Object> tempMap, long interval) {
        int size = (int)tempMap.get("size");
        if (size == 0) {
            return;
        }

        //check timestamp
        long timestamp = (long) tempMap.get("timestamp");
        long dt = document.getLong("dt");
        if (dt > timestamp) {
            return;
        }

        //store dataModel
        size--;
        DataModel[] dataModels = (DataModel[])tempMap.get("array");
        DataModel model = new DataModel();
        model.setValue(document.getString("v"));
        model.setDataTimeStamp(new Date(dt));
        dataModels[size] = model;

        timestamp -= interval;
        tempMap.put("timestamp", timestamp);
        tempMap.put("size", size);

        //check next timestamp
        checkDocument(document, tempMap, interval);
    }


    /**
     * created by wangwei
     * query from mongoDB again for unset dataModel
     * @param thingCode
     * @param metricCode
     * @param endTime
     * @param dataModels
     * @param size
     */
    private void queryForUnsetDataModel(String thingCode, String metricCode, long endTime, DataModel[] dataModels, int size) {
        Bson criteria = and(lt("dt", endTime), eq("mc", metricCode), eq("tc", thingCode));
        FindIterable<Document> iterable = collection.find(criteria).sort(Sorts.descending("dt")).limit(1);
        for (Document document : iterable) {
            DataModel model = new DataModel();
            model.setValue(document.getString("v"));
            model.setDataTimeStamp(new Date(document.getLong("dt")));
            for (int i=0;i<size;i++) {
                dataModels[i] = model.clone();
            }
        }
    }


    @Override
    public int insertBatch(List<DataModel> modelList) {
        if (collection != null) {
            List<Document> models = new LinkedList<>();
            for (DataModel dataModel : modelList) {
                Document document = new Document()
                        .append("tc", dataModel.getThingCode())
                        .append("mdt", dataModel.getMetricDataType())
                        .append("mc", dataModel.getMetricCode())
                        .append("mcc", dataModel.getMetricCategoryCode())
                        .append("v", dataModel.getValue())
                        .append("dt", dataModel.getDataTimeStamp().getTime());
                models.add(document);
            }
            collection.insertMany(models);
            return models.size();
        } else {
            logger.warn("mongo disabled");
            return 0;
        }
    }

}
