package com.jpznm.dht.snifferdao.dao.impl;

import com.fast.dev.data.base.util.PageEntityUtil;
import com.fast.dev.data.mongo.helper.DBHelper;
import com.jpznm.dht.snifferdao.dao.extend.HotWordsDaoExtend;
import com.jpznm.dht.snifferdao.domain.HotWords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class HotWordsDaoImpl implements HotWordsDaoExtend {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;

    @Override
    public void addHotWords(String... words) {
        if (words == null || words.length == 0) {
            return;
        }

        BulkOperations bulkOperations = this.mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, HotWords.class);
        for (String word : words) {
            Query query = new Query();
            query.addCriteria(Criteria.where("name").is(word));

            Update update = new Update();
            update.inc("count", 1);
            this.dbHelper.updateTime(update);
            bulkOperations.upsert(query, update);
        }
        bulkOperations.execute();
    }

    @Override
    public Page<HotWords> listHotWords(Pageable pageable) {
        return this.dbHelper.pages(new Query().addCriteria(Criteria.where("name").ne("").and("count").gt(0)), pageable, HotWords.class);
    }

    @Override
    public void allSub() {
        Query query = new Query();
        query.addCriteria(Criteria.where("count").gt(0));


        Update update = new Update();
        update.inc("count", -1);
        this.dbHelper.updateTime(update);
        this.mongoTemplate.updateMulti(query, update, HotWords.class);
    }


}
