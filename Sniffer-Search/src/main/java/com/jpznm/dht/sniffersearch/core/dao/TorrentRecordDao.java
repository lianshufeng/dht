package com.jpznm.dht.sniffersearch.core.dao;

import com.fast.dev.data.jpa.es.dao.ElasticSearchDao;
import com.jpznm.dht.sniffersearch.core.dao.extend.TorrentRecordDaoExtend;
import com.jpznm.dht.sniffersearch.core.domain.TorrentRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TorrentRecordDao extends ElasticSearchDao<TorrentRecord>, TorrentRecordDaoExtend {


    Page<TorrentRecord> findByTitle(String title, Pageable pageable);


    long count();

}
