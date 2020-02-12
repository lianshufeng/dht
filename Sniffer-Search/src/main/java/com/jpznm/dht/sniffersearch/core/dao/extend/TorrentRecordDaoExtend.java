package com.jpznm.dht.sniffersearch.core.dao.extend;

import com.jpznm.dht.sniffersearch.core.domain.TorrentRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TorrentRecordDaoExtend {

    /**
     * 查询
     *
     * @param keyWord
     * @param pageable
     * @return
     */
    Page<TorrentRecord> query(String keyWord, String preTag, String postTag, Pageable pageable);


    /**
     * 分页查询
     *
     * @param keyWord
     * @param pageable
     * @return
     */
    Page<TorrentRecord> query(String keyWord, Pageable pageable);

}
