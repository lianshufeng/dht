package com.jpznm.dht.snifferdao.dao.impl;

import com.fast.dev.data.mongo.helper.DBHelper;
import com.fast.dev.data.mongo.util.EntityObjectUtil;
import com.jpznm.dht.snifferdao.dao.extend.TorrentDaoExtend;
import com.jpznm.dht.snifferdao.domain.Torrent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Slf4j
public class TorrentDaoImpl implements TorrentDaoExtend {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;

    @Override
    public void update(Torrent torrent) {
        if (!StringUtils.hasText(torrent.getHash())) {
            return;
        }

        log.info("torrent_dao_update : " + torrent.getName());

        Query query = new Query().addCriteria(Criteria.where("hash").is(torrent.getHash()));

        Update update = new Update();

        //更新到update里
        EntityObjectUtil.entity2Update(torrent, update, new HashSet<String>() {{
            add("id");
        }});

        update.setOnInsert("createTime", this.dbHelper.getTime());
        this.dbHelper.updateTime(update);

        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);


        this.mongoTemplate.findAndModify(query, update, options, Torrent.class);

    }

    @Override
    public List<Torrent> getOnceTorrent(int size) {
        //操作令牌
        String uuid = UUID.randomUUID().toString();
        BulkOperations bulkOperations = this.mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Torrent.class);
        for (int i = 0; i < size; i++) {
            Query query = Query.query(Criteria.where("writeToken").is(null));
            query.with(Sort.by(Sort.Direction.ASC, "createTime"));
            Update update = new Update();
            update.set("writeToken", uuid);
            bulkOperations.updateOne(query, update);
        }
        bulkOperations.execute();
        return this.mongoTemplate.find(Query.query(Criteria.where("writeToken").is(uuid)), Torrent.class);
    }
}
