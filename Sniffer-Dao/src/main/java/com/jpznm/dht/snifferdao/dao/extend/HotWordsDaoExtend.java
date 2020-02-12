package com.jpznm.dht.snifferdao.dao.extend;

import com.jpznm.dht.snifferdao.domain.HotWords;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HotWordsDaoExtend {


    /**
     * 增加热词
     *
     * @param words
     */
    void addHotWords(String... words);


    /**
     * 查询热词
     *
     * @param pageable
     * @return
     */
    Page<HotWords> listHotWords(Pageable pageable);


    /**
     * 所有数据都inc
     *
     */
    void allSub();


}
