package com.jpznm.dht.sniffercore.core.dao.impl;

import com.fast.dev.data.mongo.helper.DBHelper;
import com.jpznm.dht.sniffercore.core.dao.extend.InfoHashDaoExtend;
import com.jpznm.dht.sniffercore.core.domain.InfoHash;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class InfoHashDaoImpl implements InfoHashDaoExtend {

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

        this.mongoTemplate.upsert(query, update, InfoHash.class);
    }




}
