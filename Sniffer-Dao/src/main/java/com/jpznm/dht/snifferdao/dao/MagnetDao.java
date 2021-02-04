package com.jpznm.dht.snifferdao.dao;

import com.fast.dev.data.mongo.dao.MongoDao;
import com.jpznm.dht.snifferdao.dao.extend.MagnetDaoExtend;
import com.jpznm.dht.snifferdao.domain.Magnet;

public interface MagnetDao extends MongoDao<Magnet>, MagnetDaoExtend {
}
