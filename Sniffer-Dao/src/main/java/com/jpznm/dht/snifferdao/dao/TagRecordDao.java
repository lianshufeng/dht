package com.jpznm.dht.snifferdao.dao;

import com.fast.dev.data.mongo.dao.MongoDao;
import com.jpznm.dht.snifferdao.dao.extend.TagRecordDaoExtend;
import com.jpznm.dht.snifferdao.domain.TagRecord;

import java.util.Collection;
import java.util.List;

public interface TagRecordDao extends MongoDao<TagRecord>, TagRecordDaoExtend {


    /**
     * 批量查询字段
     *
     * @param names
     * @return
     */
    List<TagRecord> findByNameIn(Collection names);


}
