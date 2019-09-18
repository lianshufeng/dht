package com.jpznm.dht.sniffercore.core.dao;

import com.fast.dev.data.mongo.dao.MongoDao;
import com.jpznm.dht.sniffercore.core.dao.extend.InfoHashDaoExtend;
import com.jpznm.dht.sniffercore.core.domain.InfoHash;

public interface InfoHashDao extends MongoDao<InfoHash>, InfoHashDaoExtend {
}
