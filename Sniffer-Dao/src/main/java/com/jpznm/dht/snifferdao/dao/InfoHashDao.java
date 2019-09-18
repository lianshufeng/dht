package com.jpznm.dht.snifferdao.dao;

import com.fast.dev.data.mongo.dao.MongoDao;
import com.jpznm.dht.snifferdao.dao.extend.InfoHashDaoExtend;
import com.jpznm.dht.snifferdao.domain.InfoHash;

public interface InfoHashDao extends MongoDao<InfoHash>, InfoHashDaoExtend {
}
