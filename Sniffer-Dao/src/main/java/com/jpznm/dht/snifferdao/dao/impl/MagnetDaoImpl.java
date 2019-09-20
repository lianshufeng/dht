package com.jpznm.dht.snifferdao.dao.impl;

import com.fast.dev.data.mongo.helper.DBHelper;
import com.jpznm.dht.snifferdao.dao.extend.MagnetDaoExtend;
import com.jpznm.dht.snifferdao.domain.Magnet;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Log
@EnableScheduling
public class MagnetDaoImpl implements MagnetDaoExtend {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;

    //缓存
    private Map<String, String> _putCache = new ConcurrentHashMap<>();


    @Override
    public void put(String hash, String updateHost) {
        //缓存起来
        this._putCache.put(hash, updateHost);
    }


    /**
     * 更新缓存
     */
    @Scheduled(cron = "0/20 * *  * * ?")
    private void putCache() {
        if (this._putCache.size() == 0) {
            return;
        }

        long startTime = System.currentTimeMillis();

        log.info("cache size : " + this._putCache.size());

        BulkOperations bulkOps = this.mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Magnet.class);
        //调度器批量处理
        for (String hash : this._putCache.keySet()) {
            String updateHost = this._putCache.remove(hash);
            Query query = new Query().addCriteria(Criteria.where("hash").is(hash));
            Update update = new Update();
            this.dbHelper.updateTime(update);
            update.setOnInsert("hash", hash);
            update.setOnInsert("getCount", 0);

            update.set("updateTime", dbHelper.getTime());
            update.inc("accessCount", 1);
            update.set("updateHost", updateHost);


            bulkOps.upsert(query, update);
        }

        int size = bulkOps.execute().getUpserts().size();

        log.info("Put Cache to db  : " + size + " , time :" + (System.currentTimeMillis() - startTime));

    }


    @Override
    public List<Magnet> getMagnet(int size) {
        String upSession = UUID.randomUUID().toString();

        Query query = new Query();
        query.with(new Sort(Sort.Direction.ASC, "getCount"));
        query.limit(size);

        Update update = new Update();
        update.set("upSession", upSession);
        update.inc("getCount", 1);
        this.dbHelper.updateTime(update);

        //进行批量修改
        this.mongoTemplate.updateMulti(query, update, Magnet.class);

        //查询修改过的记录
        return this.mongoTemplate.find(query.addCriteria(Criteria.where("upSession").is(upSession)), Magnet.class);
    }


}
