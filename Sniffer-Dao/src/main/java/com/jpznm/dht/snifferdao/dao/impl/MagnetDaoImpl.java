package com.jpznm.dht.snifferdao.dao.impl;

import com.fast.dev.data.mongo.helper.DBHelper;
import com.jpznm.dht.snifferdao.dao.extend.MagnetDaoExtend;
import com.jpznm.dht.snifferdao.domain.Magnet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class MagnetDaoImpl implements MagnetDaoExtend {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;

    @Override
    public void updateHash(String hash, String updateHost) {
        Query query = new Query().addCriteria(Criteria.where("hash").is(hash));

        Update update = new Update();
        update.setOnInsert("hash", hash);
        update.setOnInsert("createTime", dbHelper.getTime());
        update.set("updateTime", dbHelper.getTime());
        update.inc("accessCount", 1);
        update.set("updateHost", updateHost);

        this.mongoTemplate.upsert(query, update, Magnet.class);
    }


}
