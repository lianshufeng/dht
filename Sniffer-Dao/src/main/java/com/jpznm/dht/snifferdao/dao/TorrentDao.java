package com.jpznm.dht.snifferdao.dao;

import com.fast.dev.data.mongo.dao.MongoDao;
import com.jpznm.dht.snifferdao.dao.extend.TorrentDaoExtend;
import com.jpznm.dht.snifferdao.domain.Torrent;

public interface TorrentDao extends MongoDao<Torrent>, TorrentDaoExtend {
}
