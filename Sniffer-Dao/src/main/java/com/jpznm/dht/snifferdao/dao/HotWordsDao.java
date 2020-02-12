package com.jpznm.dht.snifferdao.dao;

import com.fast.dev.data.mongo.dao.MongoDao;
import com.jpznm.dht.snifferdao.dao.extend.HotWordsDaoExtend;
import com.jpznm.dht.snifferdao.domain.HotWords;

public interface HotWordsDao extends MongoDao<HotWords>, HotWordsDaoExtend {


}
