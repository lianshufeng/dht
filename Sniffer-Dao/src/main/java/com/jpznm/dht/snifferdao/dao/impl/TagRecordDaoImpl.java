package com.jpznm.dht.snifferdao.dao.impl;

import com.fast.dev.data.mongo.helper.DBHelper;
import com.jpznm.dht.snifferdao.dao.extend.TagRecordDaoExtend;
import com.jpznm.dht.snifferdao.domain.TagRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Collection;
import java.util.Map;

@Slf4j
public class TagRecordDaoImpl implements TagRecordDaoExtend {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;


    @Override
    public void inc(Collection<String> names) {

        BulkOperations bulkOperations = this.mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, TagRecord.class);
        for (String name : names) {
            Query query = new Query();
            query.addCriteria(Criteria.where("name").is(name));

            Update update = new Update();
            update.inc("count", 1);
            this.dbHelper.updateTime(update);
            bulkOperations.upsert(query, update);
        }

        if (names.size() > 0) {
            bulkOperations.execute();
        }

    }

    @Override
    public void inc(Map<String, Integer> items) {
        long total = 0;
        BulkOperations bulkOperations = this.mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, TagRecord.class);

        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            total++;
            Query query = new Query();
            query.addCriteria(Criteria.where("name").is(entry.getKey()));

            Update update = new Update();
            update.inc("count", entry.getValue());
            bulkOperations.upsert(query, update);
        }

        if (total > 0) {
            bulkOperations.execute();
        }
    }
}
