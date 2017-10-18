package com.zgiot.app.server.service.impl;

import com.mongodb.Block;
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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
