package com.jpznm.dht.sniffercore.core.dao;

import com.fast.dev.data.mongo.dao.MongoDao;
import com.jpznm.dht.sniffercore.core.dao.extend.TorrentDaoExtend;
import com.jpznm.dht.sniffercore.core.domain.Torrent;

public interface TorrentDao extends MongoDao<Torrent>, TorrentDaoExtend {
}
